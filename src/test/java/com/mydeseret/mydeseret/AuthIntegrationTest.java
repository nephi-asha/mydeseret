package com.mydeseret.mydeseret;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydeseret.mydeseret.dto.LoginRequestDto;
import com.mydeseret.mydeseret.dto.UserRequestDto;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterAndLogin() throws Exception {
        // Register
        UserRequestDto registerRequest = new UserRequestDto();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setBusinessName("Johns Business");

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        // Activate User (Simulate Admin Approval)
        User user = userRepository.findByEmail("john@example.com").orElseThrow();
        user.setActive(true);
        userRepository.save(user);

        // Login
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void test2FALoginFlow() throws Exception {
        // Setup User
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane@example.com");
        user.setPasswordHash(passwordEncoder.encode("password123"));
        user.setBusinessName("Janes Business");
        user.setActive(true);

        // Enable 2FA
        String secret = org.jboss.aerogear.security.otp.api.Base32.random();
        user.setMfaSecret(secret);
        user.setMfaEnabled(true);
        userRepository.save(user);

        // Login (Should require MFA)
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("jane@example.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(jsonPath("$.status").value("MFA_REQUIRED"));

        // Verify 2FA
        org.jboss.aerogear.security.otp.Totp totp = new org.jboss.aerogear.security.otp.Totp(secret);
        String code = totp.now();

        mockMvc.perform(post("/api/v1/users/login/verify-2fa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("email", "jane@example.com", "code", code))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }
}
