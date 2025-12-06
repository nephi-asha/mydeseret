package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/2fa")
@Tag(name = "Two-Factor Authentication", description = "Setup and Verify 2FA")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Setup 2FA", description = "Generates a secret key for Google Authenticator.")
    @PostMapping("/setup")
    public ResponseEntity<Map<String, String>> setup2FA() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isMfaEnabled()) {
            return ResponseEntity.badRequest().body(Map.of("error", "2FA is already enabled"));
        }

        // Generates a valid Base32 secret using Aerogear
        String secret = org.jboss.aerogear.security.otp.api.Base32.random();
        user.setMfaSecret(secret);
        userRepository.save(user);

        // Generate the OTP Auth URL for QR Code
        // format: otpauth://totp/Issuer:Email?secret=Secret&issuer=Issuer
        String qrUrl = String.format("otpauth://totp/MyDeseret:%s?secret=%s&issuer=MyDeseret", email, secret);

        return ResponseEntity.ok(Map.of(
                "secret", secret,
                "qrUrl", qrUrl,
                "message",
                "Enter this secret in your Authenticator App or scan the QR code (if generated on frontend)."));
    }

    @Operation(summary = "Verify 2FA Code (Enable)", description = "Verifies the code and enables 2FA if correct.")
    @PostMapping("/verify")
    public ResponseEntity<String> verify2FA(@RequestBody Map<String, String> request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String code = request.get("code");
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Code is required");
        }

        org.jboss.aerogear.security.otp.Totp totp = new org.jboss.aerogear.security.otp.Totp(user.getMfaSecret());
        // Verify the code
        if (!totp.verify(code)) {
            return ResponseEntity.badRequest().body("Invalid verification code.");
        }

        user.setMfaEnabled(true);
        userRepository.save(user);

        return ResponseEntity.ok("2FA Enabled Successfully.");
    }
}
