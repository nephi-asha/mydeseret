package com.mydeseret.mydeseret.config;

import com.mydeseret.mydeseret.model.Role;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.model.enums.ApplicationPermission;
import com.mydeseret.mydeseret.repository.RoleRepository;
import com.mydeseret.mydeseret.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Configuration
public class DatabaseSeeder {

    // Create role if missing
    private void createRoleIfMissing(RoleRepository repo, String name, Set<ApplicationPermission> perms) {
        if (repo.findByRoleName(name).isEmpty()) {
            Role role = new Role();
            role.setRoleName(name);
            role.setTenant(null); // Global Template
            role.setPermissions(perms);
            repo.save(role);
            System.out.println("Seeded Global Role: " + name);
        }
    }

    @Bean
    CommandLineRunner initDatabase(
        RoleRepository roleRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (roleRepository.findByRoleName("OWNER").isEmpty()) {
                
                Role ownerRole = new Role();
                ownerRole.setRoleName("OWNER");
                ownerRole.setTenant(null);
                
                // ownerRole.setPermissions(Set.of(
                //     ApplicationPermission.ITEM_CREATE,
                //     ApplicationPermission.ITEM_READ,
                //     ApplicationPermission.ITEM_UPDATE,
                //     ApplicationPermission.ITEM_DELETE
                // ));
                ownerRole.setPermissions(Set.of(
                    ApplicationPermission.values()
                ));
                
                roleRepository.save(ownerRole);
                System.out.println("Seeded Global OWNER Role");
            }

            Role superownerRole = roleRepository.findByRoleName("SUPER_ADMIN")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName("SUPER_ADMIN");
                    newRole.setTenant(null); 
                    newRole.setPermissions(Set.of(ApplicationPermission.values()));
                    return roleRepository.save(newRole);
                });

            String adminEmail = "nephiasha2017@gmail.com";
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                
                User superAdmin = new User();
                superAdmin.setFirstName("Nephi");
                superAdmin.setLastName("Asha");
                superAdmin.setUserName("ashanephi");
                superAdmin.setEmail(adminEmail);
                superAdmin.setBusinessName("Deseret Technologies");
                
                // Critical: is_active to TRUE immediately (Nah we be BOSS😎)
                superAdmin.setActive(true); 
                superAdmin.setPasswordHash(passwordEncoder.encode("@Deseret2025")); 
                superAdmin.setRoles(Collections.singleton(superownerRole));
            
                // SuperAdmin has all views so it should be under any tenant
                superAdmin.setTenant(null); 
                
                superAdmin.setApprovalToken(UUID.randomUUID().toString());
                userRepository.save(superAdmin);
                
                System.out.println("---------------------------------------------");
                System.out.println("   SUPER ADMIN CREATED: " + adminEmail);
                System.out.println("   PASSWORD: @Deseret2025");
                System.out.println("---------------------------------------------");
            }
            if (roleRepository.findByRoleName("EMPLOYEE").isEmpty()) {
                Role empRole = new Role();
                empRole.setRoleName("EMPLOYEE");
                empRole.setTenant(null);
                empRole.setPermissions(Set.of(
                    ApplicationPermission.ITEM_READ,
                    ApplicationPermission.SALE_CREATE
                ));
                roleRepository.save(empRole);
                System.out.println("Seeded EMPLOYEE Role");
            }

            // STANDARD TEMPLATE ROLES
            
            // A. WAREHOUSE MANAGER
            createRoleIfMissing(roleRepository, "WAREHOUSE_MANAGER", Set.of(
                ApplicationPermission.ITEM_CREATE, ApplicationPermission.ITEM_READ, 
                ApplicationPermission.ITEM_UPDATE, ApplicationPermission.ITEM_DELETE,
                ApplicationPermission.CATEGORY_CREATE, ApplicationPermission.CATEGORY_READ,
                ApplicationPermission.INVENTORY_ADJUST, 
                ApplicationPermission.PURCHASE_ORDER_RECEIVE, // Can accept deliveries
                ApplicationPermission.REQUISITION_CREATE // Can ask for supplies
            ));

            // B. CASHIER / SALES REP
            createRoleIfMissing(roleRepository, "CASHIER", Set.of(
                ApplicationPermission.SALE_CREATE, ApplicationPermission.SALE_READ,
                ApplicationPermission.CUSTOMER_READ, ApplicationPermission.CUSTOMER_CREATE,
                ApplicationPermission.ITEM_READ 
            ));

            // C. HR MANAGER
            createRoleIfMissing(roleRepository, "HR_MANAGER", Set.of(
                ApplicationPermission.EMPLOYEE_CREATE, ApplicationPermission.EMPLOYEE_READ, 
                ApplicationPermission.EMPLOYEE_UPDATE, ApplicationPermission.EMPLOYEE_DELETE,
                ApplicationPermission.PAYROLL_GENERATE, ApplicationPermission.PAYROLL_READ, 
                ApplicationPermission.PAYROLL_PROCESS,
                ApplicationPermission.TASK_CREATE
            ));

            // D. ACCOUNTANT
            createRoleIfMissing(roleRepository, "ACCOUNTANT", Set.of(
                ApplicationPermission.FINANCIAL_REPORT_READ, 
                ApplicationPermission.EXPENSE_CREATE, ApplicationPermission.EXPENSE_READ,
                ApplicationPermission.PURCHASE_ORDER_PAY,
                ApplicationPermission.PAYROLL_READ
            ));
        };
    }
}