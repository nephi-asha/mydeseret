package com.mydeseret.mydeseret.service;

// import com.mydeseret.mydeseret.config.TenantContext;
// import com.mydeseret.mydeseret.model.Item;
// import com.mydeseret.mydeseret.model.Tenant;
// import com.mydeseret.mydeseret.model.User;
// import com.mydeseret.mydeseret.model.enums.StockReason;
// import com.mydeseret.mydeseret.repository.ItemRepository;
// import com.mydeseret.mydeseret.repository.TenantRepository;
// import com.mydeseret.mydeseret.repository.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.time.LocalDate;
// import java.util.List;

// @Service
// public class DailyJobService {

//     @Autowired private TenantRepository tenantRepository;
//     @Autowired private ItemRepository itemRepository;
//     @Autowired private EmailService emailService;
//     @Autowired private InventoryService inventoryService;
//     @Autowired private UserRepository userRepository;

//     // Run every day at 6:00 AM (West Africa Time)
//     @Scheduled(cron = "0 0 6 * * ?", zone = "Africa/Lagos")
//     public void checkLowStockLevels() {
//         System.out.println("Starting Daily Stock Check...");

//         List<Tenant> tenants = tenantRepository.findAll();

//         for (Tenant tenant : tenants) {
//             try {
//                 TenantContext.setCurrentTenant(tenant.getSchemaName());
//                 checkStockForCurrentTenant(tenant);
//             } catch (Exception e) {
//                 System.err.println("Error processing tenant " + tenant.getTenantName() + ": " + e.getMessage());
//             } finally {
//                 TenantContext.clear();
//             }
//         }
//     }

//     @Transactional
//     public void checkStockForCurrentTenant(Tenant tenant) {
//         List<Item> lowStockItems = itemRepository.findAll().stream()
//                 .filter(item -> item.getQuantityOnHand() < item.getReorderPoint())
//                 .toList();

//         if (!lowStockItems.isEmpty()) {
//             StringBuilder body = new StringBuilder();
//             body.append("The following items are low in stock:\n\n");
            
//             for (Item item : lowStockItems) {
//                 body.append("- ").append(item.getName())
//                     .append(" (Current: ").append(item.getQuantityOnHand())
//                     .append(", Reorder At: ").append(item.getReorderPoint()).append(")\n");
//             }

//             User owner = tenant.getUserId(); 
//             if (owner != null) {
//                 emailService.sendEmail(
//                     owner.getEmail(), 
//                     "Low Stock Alert - " + tenant.getTenantName(), 
//                     body.toString()
//                 );
//             }
//         }
//     }

//     // Run at 11:59 PM (23:59) WAT (Africa/Lagos)
//     // This ensures the "Day Old" logic happens right before the date changes.
//     @Scheduled(cron = "0 59 23 * * ?", zone = "Africa/Lagos")
//     public void runEndOfDayClosing() {
//         System.out.println("Running End of Day Closing...");
        
//         List<Tenant> tenants = tenantRepository.findAll();
//         for (Tenant tenant : tenants) {
//             try {
//                 TenantContext.setCurrentTenant(tenant.getSchemaName());
//                 processBakeryOverstock();
//             } catch (Exception e) {
//                 System.err.println("Error running EOD for tenant " + tenant.getTenantName());
//             } finally {
//                 TenantContext.clear();
//             }
//         }
//     }

//     @Transactional
//     public void processBakeryOverstock() {
//         List<Item> freshItems = itemRepository.findAll().stream()
//             .filter(i -> i.getName().toLowerCase().contains("fresh")) 
//             .toList();

//         for (Item item : freshItems) {
//             int unsoldQty = item.getQuantityOnHand();
            
//             if (unsoldQty > 0) {
//                 inventoryService.adjustStock(
//                     item.getItemId(), 
//                     -unsoldQty, 
//                     StockReason.ADJUSTMENT, 
//                     "EOD-" + LocalDate.now(), 
//                     "End of Day Clearance - Moved to Overstock/Waste"
//                 );

//                 System.out.println("Cleared " + unsoldQty + " of " + item.getName());
//             }
//         }
//     }
// }

public class DailyJobService_Main {

}