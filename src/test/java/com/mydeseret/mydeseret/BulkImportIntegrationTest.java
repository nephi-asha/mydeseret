package com.mydeseret.mydeseret;

import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.UserRepository;
import com.mydeseret.mydeseret.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BulkImportIntegrationTest extends AbstractIntegrationTest {

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
        // Create User
        User user = new User();
        user.setEmail("importer@example.com");
        user.setPasswordHash("password");
        user.setFirstName("Import");
        user.setLastName("User");
        user.setBusinessName("Import Corp");
        user.setActive(true);
        userRepository.save(user);

        // Generate Token
        var userDetails = userDetailsService.loadUserByUsername("importer@example.com");
        token = "Bearer " + jwtUtil.generateToken(userDetails);
    }

    @Test
    void testBulkImport() throws Exception {
        String csvContent = "name,description,sku,costPrice,sellingPrice,quantityOnHand\n" +
                "Test Item,Description,TEST-SKU-001,10.00,20.00,100";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "items.csv",
                "text/csv",
                csvContent.getBytes());

        mockMvc.perform(multipart("/api/v1/import/items")
                .file(file)
                .header("Authorization", token))
                .andExpect(status().isOk());
    }
}
