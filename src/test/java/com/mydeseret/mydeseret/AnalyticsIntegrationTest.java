package com.mydeseret.mydeseret;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.UserRepository;
import com.mydeseret.mydeseret.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AnalyticsIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private String token;

    @BeforeEach
    void setUp() {
        // Create User with Permissions
        User user = new User();
        user.setEmail("analyst@example.com");
        user.setPasswordHash("password");
        user.setFirstName("Analyst");
        user.setLastName("User");
        user.setBusinessName("Analytics Corp");
        user.setActive(true);
        // Add Role/Permission logic here if needed (e.g. FINANCIAL_REPORT_READ)
        // For simplicity, assuming default roles or adding manually
        userRepository.save(user);

        // Generate Token
        var userDetails = userDetailsService.loadUserByUsername("analyst@example.com");
        token = "Bearer " + jwtUtil.generateToken(userDetails);
    }

    @Test
    void testGetSalesAnalytics() throws Exception {
        mockMvc.perform(get("/api/v1/analytics/sales")
                .header("Authorization", token))
                .andExpect(status().isOk()) // Might be 403 if permissions are strict
                .andExpect(jsonPath("$.totalRevenue").exists());
    }

    @Test
    void testAsyncReportGeneration() throws Exception {
        mockMvc.perform(post("/api/v1/analytics/reports/generate")
                .header("Authorization", token))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.reportId").exists());
    }
}
