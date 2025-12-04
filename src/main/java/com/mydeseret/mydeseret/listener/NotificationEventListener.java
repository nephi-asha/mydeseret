package com.mydeseret.mydeseret.listener;

import com.mydeseret.mydeseret.event.SaleCreatedEvent;
import com.mydeseret.mydeseret.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    @Autowired private EmailService emailService;

    // Run ASYNC! If email fails, do NOT roll back the sale.
    // Also, don't make the user wait for the email to send.
    @Async 
    @EventListener
    public void handleSaleNotification(SaleCreatedEvent event) {
        var sale = event.getSale();
        
        // This emails the owner if a massive/big sale ever happens
        if (sale.getTotalAmount().doubleValue() > 10000) {
            System.out.println("$$$ HUGE SALE ALERT: " + sale.getTotalAmount());
        }
    }
}