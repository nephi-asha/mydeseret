// package com.mydeseret.mydeseret.service;

// import com.mydeseret.mydeseret.model.Tenant;
// import com.mydeseret.mydeseret.model.User;
// import com.mydeseret.mydeseret.repository.TenantRepository;
// import com.mydeseret.mydeseret.repository.UserRepository;
// import com.stripe.exception.StripeException;
// import com.stripe.model.checkout.Session;
// import com.stripe.param.checkout.SessionCreateParams;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Service;

// @Service
// public class SubscriptionService {

//     @Autowired private UserRepository userRepository;
//     @Autowired private TenantRepository tenantRepository;

//     @Value("${app.frontend.url}")
//     private String frontendUrl;

//     private User getAuthenticatedUser() {
//         String email = SecurityContextHolder.getContext().getAuthentication().getName();
//         return userRepository.findByEmail(email).orElseThrow();
//     }

//     public String createCheckoutSession(String priceId) throws StripeException {
//         User user = getAuthenticatedUser();
//         Tenant tenant = user.getTenant();

//         // Create Stripe Checkout Session
//         SessionCreateParams params = SessionCreateParams.builder()
//                 .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
//                 .setSuccessUrl(frontendUrl + "/dashboard?payment=success")
//                 .setCancelUrl(frontendUrl + "/dashboard?payment=cancelled")
//                 .setCustomerEmail(user.getEmail())
//                 // Store Tenant ID in metadata so we know who paid later
//                 .putMetadata("tenant_id", String.valueOf(tenant.getTenantId())) 
//                 .addLineItem(
//                         SessionCreateParams.LineItem.builder()
//                                 .setQuantity(1L)
//                                 .setPrice(priceId) 
//                                 .build())
//                 .build();

//         Session session = Session.create(params);
//         return session.getUrl();
//     }
// }
package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.paystack.PaystackInitRequest;
import com.mydeseret.mydeseret.dto.paystack.PaystackInitResponse;
import com.mydeseret.mydeseret.model.Tenant;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.TenantRepository;
import com.mydeseret.mydeseret.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class SubscriptionService {

    @Autowired private UserRepository userRepository;
    @Autowired private TenantRepository tenantRepository;

    @Value("${paystack.secret-key}")
    private String secretKey;
    
    @Value("${paystack.base-url}")
    private String baseUrl;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    public String initializePayment(BigDecimal amount) {
        User user = getAuthenticatedUser();
        Tenant tenant = user.getTenant();

        PaystackInitRequest request = new PaystackInitRequest();
        request.setEmail(user.getEmail());
        request.setAmount(amount.multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString()); 
        request.setCallbackUrl(frontendUrl + "/dashboard?payment=verify");
        
        request.setMetadata(Map.of(
            "tenant_id", tenant.getTenantId(),
            "custom_fields", java.util.List.of(
                Map.of("display_name", "Tenant Name", "variable_name", "tenant_name", "value", tenant.getTenantName())
            )
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + secretKey);

        HttpEntity<PaystackInitRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<PaystackInitResponse> response = restTemplate.postForEntity(
                    baseUrl + "/transaction/initialize", entity, PaystackInitResponse.class);

            if (response.getBody() != null && response.getBody().isStatus()) {
                return response.getBody().getData().getAuthorization_url();
            } else {
                throw new RuntimeException("Paystack initialization failed.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to Paystack: " + e.getMessage());
        }
    }
}