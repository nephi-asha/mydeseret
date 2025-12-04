package com.mydeseret.mydeseret.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydeseret.mydeseret.model.Tenant;
import com.mydeseret.mydeseret.model.enums.SubscriptionStatus;
import com.mydeseret.mydeseret.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Formatter;

@RestController
@RequestMapping("/api/v1/webhooks")
public class PaystackWebhookController {

    @Value("${paystack.secret-key}")
    private String secretKey;

    @Autowired private TenantRepository tenantRepository;
    @Autowired private ObjectMapper objectMapper;

    @PostMapping("/paystack")
    public ResponseEntity<String> handlePaystackWebhook(
            @RequestBody String payload,
            @RequestHeader("x-paystack-signature") String signature) {

        if (!verifySignature(payload, signature)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Signature");
        }

        try {
            JsonNode root = objectMapper.readTree(payload);
            String event = root.path("event").asText();

            if ("charge.success".equals(event)) {
                JsonNode data = root.path("data");
                JsonNode metadata = data.path("metadata");
                
                // Get Tenant ID that was sent earlier
                if (metadata.has("tenant_id")) {
                    Long tenantId = metadata.get("tenant_id").asLong();
                    fulfillOrder(tenantId, data.path("customer").path("customer_code").asText());
                }
            }

            return ResponseEntity.ok("Received");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing webhook");
        }
    }

    private void fulfillOrder(Long tenantId, String customerCode) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow();
        
        tenant.setStripeCustomerId(customerCode); 
        tenant.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        tenant.setCurrentPeriodEnd(LocalDate.now().plusDays(30));
        
        tenantRepository.save(tenant);
        System.out.println("Paystack Payment Successful for Tenant: " + tenant.getTenantName());
    }

    // Helper to verify HMAC SHA512
    private boolean verifySignature(String payload, String signature) {
        try {
            Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
            sha512_HMAC.init(secret_key);
            byte[] hash = sha512_HMAC.doFinal(payload.getBytes());
            return toHexString(hash).equals(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return false;
        }
    }

    private String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}