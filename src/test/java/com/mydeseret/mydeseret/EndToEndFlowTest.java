package com.mydeseret.mydeseret;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydeseret.mydeseret.dto.*;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class EndToEndFlowTest extends AbstractIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    @Test
    void fullBusinessLifecycleTest() throws Exception {
        // Register a New Business
        UserRequestDto registerDto = new UserRequestDto();
        registerDto.setUserName("test_owner");
        registerDto.setFirstName("Test");
        registerDto.setLastName("Owner");
        registerDto.setEmail("owner@test.com");
        registerDto.setPassword("pass123");
        registerDto.setBusinessName("Test Corp");
        registerDto.setTimeZone("UTC");

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated());

        // Login as Super Admin
        LoginRequestDto adminLogin = new LoginRequestDto();
        adminLogin.setEmail("nephiasha2017@gmail.com");
        adminLogin.setPassword("@Deseret2025");

        MvcResult adminResult = mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn();

        String adminToken = extractToken(adminResult);

        // Approve the New Business
        User pendingUser = userRepository.findByEmail("owner@test.com").orElseThrow();
        
        mockMvc.perform(put("/api/v1/admin/approve/" + pendingUser.getUserId())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        // Login as New Owner
        LoginRequestDto ownerLogin = new LoginRequestDto();
        ownerLogin.setEmail("owner@test.com");
        ownerLogin.setPassword("pass123");

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ownerLogin)))
                .andExpect(status().isOk()); 
    }

    private String extractToken(MvcResult result) throws Exception {
        String json = result.getResponse().getContentAsString();
        LoginResponseDto response = objectMapper.readValue(json, LoginResponseDto.class);
        return response.getToken();
    }
}