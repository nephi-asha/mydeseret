package com.mydeseret.mydeseret.listener;

import com.mydeseret.mydeseret.event.SaleCreatedEvent;
import com.mydeseret.mydeseret.model.Customer;
import com.mydeseret.mydeseret.model.enums.PaymentMethod;
import com.mydeseret.mydeseret.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FinancialEventListener {

    @Autowired private CustomerRepository customerRepository;

    @EventListener
    public void handleCreditUpdate(SaleCreatedEvent event) {
        var sale = event.getSale();

        // This only runs/cares only if it's a CREDIT sale
        if (sale.getPaymentMethod() == PaymentMethod.CREDIT) {
            Customer customer = sale.getCustomer();
            if (customer == null) {
                throw new RuntimeException("Credit sale requires a valid customer.");
            }

            BigDecimal newDebt = customer.getCurrentDebt().add(sale.getTotalAmount());
            
            if (newDebt.compareTo(customer.getCreditLimit()) > 0) {
                throw new RuntimeException("Credit Limit Exceeded during processing!");
            }

            customer.setCurrentDebt(newDebt);
            customerRepository.save(customer);
            
            System.out.println("Updated Debt for Customer: " + customer.getName());
        }
    }
}