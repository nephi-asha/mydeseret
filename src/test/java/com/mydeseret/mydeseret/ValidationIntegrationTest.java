package com.mydeseret.mydeseret;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydeseret.mydeseret.dto.CustomerRequestDto;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.UserRepository;
import com.mydeseret.mydeseret.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ValidationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() {
        // Create User
        User user = new User();
        user.setEmail("validator@example.com");
        user.setPasswordHash("password");
        user.setFirstName("Valid");
        user.setLastName("User");
        user.setBusinessName("Valid Corp");
        user.setActive(true);
        userRepository.save(user);

        // Generate Token
        var userDetails = userDetailsService.loadUserByUsername("validator@example.com");
        token = "Bearer " + jwtUtil.generateToken(userDetails);
    }

    @Test
    void testCreateCustomer_InvalidEmail() throws Exception {
        CustomerRequestDto request = new CustomerRequestDto();
        request.setName("Bad Email Customer");
        request.setEmail("not-an-email"); // Invalid Email

        mockMvc.perform(post("/api/v1/customers")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message.email").exists());
    }

    @Test
    void testCreateCustomer_MissingName() throws Exception {
        CustomerRequestDto request = new CustomerRequestDto();
        request.setEmail("valid@example.com");
        // Name is null

        mockMvc.perform(post("/api/v1/customers")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message.name").exists());
    }
}
