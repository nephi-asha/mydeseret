package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.config.TenantContext;
import com.mydeseret.mydeseret.model.Item;
import com.mydeseret.mydeseret.model.Tenant;
import com.mydeseret.mydeseret.model.enums.StockReason;
import com.mydeseret.mydeseret.repository.ItemRepository;
import com.mydeseret.mydeseret.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class DailyJobService {

    @Autowired private TenantRepository tenantRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private EmailService emailService;
    @Autowired private InventoryService inventoryService;

    // 1. LOW STOCK ALERT: Run every hour at minute 0 (e.g., 10:00, 11:00)
    @Scheduled(cron = "0 0 * * * ?")
    public void checkLowStockLevels() {
        List<Tenant> tenants = tenantRepository.findAll();

        for (Tenant tenant : tenants) {
            try {
                // Check Tenant's Local Time
                ZoneId zone = ZoneId.of(tenant.getTimeZone());
                ZonedDateTime now = ZonedDateTime.now(zone);

                // Only run this check at 6 AM in their local time
                if (now.getHour() == 6) {
                    System.out.println("Running Stock Check for " + tenant.getTenantName() + " (It's 6 AM there)");
                    TenantContext.setCurrentTenant(tenant.getSchemaName());
                    checkStockForCurrentTenant(tenant);
                }
            } catch (Exception e) {
                System.err.println("Error in Stock Check for " + tenant.getTenantName() + ": " + e.getMessage());
            } finally {
                TenantContext.clear();
            }
        }
    }

    // END OF DAY CLOSING: Run every hour at minute 59
    @Scheduled(cron = "0 59 * * * ?")
    public void runEndOfDayClosing() {
        List<Tenant> tenants = tenantRepository.findAll();

        for (Tenant tenant : tenants) {
            try {
                // Check Tenant's Local Time
                ZoneId zone = ZoneId.of(tenant.getTimeZone());
                ZonedDateTime now = ZonedDateTime.now(zone);

                // This only runs if it is 11 PM (23) in their local time
                if (now.getHour() == 23) {
                    System.out.println("Running EOD for " + tenant.getTenantName() + " (It's 11:59 PM there)");
                    TenantContext.setCurrentTenant(tenant.getSchemaName());
                    processBakeryOverstock();
                }
            } catch (Exception e) {
                System.err.println("Error in EOD for " + tenant.getTenantName() + ": " + e.getMessage());
            } finally {
                TenantContext.clear();
            }
        }
    }

    @Transactional
    public void processBakeryOverstock() {
        List<Item> freshItems = itemRepository.findAll().stream()
            .filter(i -> i.getName().toLowerCase().contains("fresh")) 
            .toList();

        for (Item item : freshItems) {
            int unsoldQty = item.getQuantityOnHand();
            if (unsoldQty > 0) {
                inventoryService.adjustStock(
                    item.getItemId(), 
                    -unsoldQty, 
                    StockReason.ADJUSTMENT, 
                    "EOD-" + LocalDate.now(), 
                    "End of Day Clearance"
                );
            }
        }
    }
    
    @Transactional
    public void checkStockForCurrentTenant(Tenant tenant) {
         List<Item> lowStockItems = itemRepository.findAll().stream()
                .filter(item -> item.getQuantityOnHand() < item.getReorderPoint())
                .toList();

        if (!lowStockItems.isEmpty()) {
            StringBuilder body = new StringBuilder();
            body.append("The following items are low in stock:\n\n");
            for (Item item : lowStockItems) {
                body.append("- ").append(item.getName())
                    .append(" (Current: ").append(item.getQuantityOnHand())
                    .append(", Reorder At: ").append(item.getReorderPoint()).append(")\n");
            }
            if (tenant.getUserId() != null) {
                emailService.sendEmail(tenant.getUserId().getEmail(), "Low Stock Alert", body.toString());
            }
        }
    }
}