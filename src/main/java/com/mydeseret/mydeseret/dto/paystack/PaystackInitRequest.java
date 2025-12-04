package com.mydeseret.mydeseret.dto.paystack;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class PaystackInitRequest {
    private String email;
    private String amount;
    private String currency = "NGN";
    
    @JsonProperty("callback_url")
    private String callbackUrl;
    
    private Map<String, Object> metadata;
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getCallbackUrl() { return callbackUrl; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}