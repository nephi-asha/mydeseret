package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.UserResponseDto;
import com.mydeseret.mydeseret.mapper.UserMapper;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.UserRepository;
import com.mydeseret.mydeseret.service.EmailService;
import com.mydeseret.mydeseret.service.SchemaProvisioningService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SchemaProvisioningService schemaProvisioningService;

    // GET /api/v1/admin/pending-owners
    @GetMapping("/pending-owners")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<UserResponseDto>> getPendingOwners() {
        List<User> pendingOwners = userRepository.findAll().stream()
            .filter(user -> !user.is_active()) 
            .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getRoleName().equals("OWNER")))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            pendingOwners.stream().map(UserMapper::toResponseDto).collect(Collectors.toList())
        );
    }

    /**
     * Gets a list of all INACTIVE users (Pending Businesses)
     */
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<UserResponseDto>> getPendingUsers() {
        List<User> pendingUsers = userRepository.findAll().stream()
                .filter(user -> !user.is_active())
                .collect(Collectors.toList());

        List<UserResponseDto> response = pendingUsers.stream()
                .map(UserMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/owners")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<UserResponseDto>> getAllOwners() {
            List<User> allUsers = userRepository.findAll().stream()
                .collect(Collectors.toList());

        List<UserResponseDto> response = allUsers.stream()
                .map(UserMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Approve a specific user/business
     */
    @PutMapping("/approve/{user_id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<String> approveOwner(@PathVariable UUID user_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.is_active()) {
            return ResponseEntity.badRequest().body("User is already active.");
        }

        String schema_name = user.getTenant().getSchemaName();
        try {
            schemaProvisioningService.createTenantSchema(schema_name);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to create business database: " + e.getMessage());
        }

        user.setActive(true);
        userRepository.save(user);

        String subject = "Account Approved - MyDeseret Technologies";
        String body = "Congratulations " + user.getFirstName() + "!\n\n" +
                      "Your business '" + user.getBusinessName() + "' has been APPROVED.\n" +
                      "You can now log in and start managing your business.\n\n" +
                      "Login here: http://localhost:8080/login\n\n" +
                      "Welcome aboard,\nMyDeseret Technologies Team";

        emailService.sendEmail(user.getEmail(), subject, body);

        return ResponseEntity.ok("User approved and email sent.");
    }
}