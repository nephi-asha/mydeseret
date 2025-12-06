package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.SaleRequestDto;
import com.mydeseret.mydeseret.event.SaleCreatedEvent; // Import Event
import com.mydeseret.mydeseret.model.*;
import com.mydeseret.mydeseret.model.enums.PaymentMethod;
import com.mydeseret.mydeseret.model.enums.SaleStatus;
import com.mydeseret.mydeseret.repository.*;
import com.mydeseret.mydeseret.specification.SaleSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher; // Import Publisher
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    // @Autowired private RabbitTemplate rabbitTemplate;

    @Transactional
    public Sale createSale(SaleRequestDto request) {
        Sale sale = new Sale();
        sale.setReceiptNumber("RCP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        sale.setPaymentMethod(request.getPaymentMethod());
        sale.setSaleDate(LocalDateTime.now());
        sale.setStatus(SaleStatus.COMPLETED);

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            sale.setCustomer(customer);
        }

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (SaleRequestDto.SaleItemDto itemDto : request.getItems()) {
            Item item = itemRepository.findById(itemDto.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found: " + itemDto.getItemId()));

            if (item.getQuantityOnHand() < itemDto.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + item.getName());
            }

            if (!item.isActive()) {
                throw new RuntimeException("Item is inactive/deleted: " + item.getName());
            }

            SaleItem lineItem = new SaleItem();
            lineItem.setSale(sale);
            lineItem.setItem(item);
            lineItem.setQuantity(itemDto.getQuantity());
            lineItem.setUnitPrice(item.getSellingPrice());
            lineItem.setCostPrice(item.getCostPrice());

            BigDecimal lineTotal = item.getSellingPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            lineItem.setSubTotal(lineTotal);

            sale.getItems().add(lineItem);
            grandTotal = grandTotal.add(lineTotal);
        }

        sale.setTotalAmount(grandTotal);

        if (request.getPaymentMethod() == PaymentMethod.CREDIT) {
            if (customer == null)
                throw new RuntimeException("Cannot sell on CREDIT to a Guest.");
            if (customer.getCurrentDebt().add(grandTotal).compareTo(customer.getCreditLimit()) > 0) {
                throw new RuntimeException("Credit Limit Exceeded!");
            }
        }

        Sale savedSale = saleRepository.save(sale);
        eventPublisher.publishEvent(new SaleCreatedEvent(this, savedSale));

        return savedSale;
    }

    public Page<Sale> getAllSales(LocalDateTime start, LocalDateTime end, BigDecimal minAmount, Pageable pageable) {

        Specification<Sale> spec = Specification.where(SaleSpecification.isWithinDateRange(start, end))
                .and(SaleSpecification.hasMinAmount(minAmount));

        return saleRepository.findAll(spec, pageable);
    }
}