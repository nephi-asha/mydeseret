package com.mydeseret.mydeseret.config;

import com.mydeseret.mydeseret.model.Role;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.model.enums.ApplicationPermission;
import com.mydeseret.mydeseret.model.enums.DefaultRole;
import com.mydeseret.mydeseret.repository.RoleRepository;
import com.mydeseret.mydeseret.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Configuration
public class DatabaseSeeder {

    @Value("${app.super-admin.email}")
    private String superAdminEmail;

    @Value("${app.super-admin.password}")
    private String superAdminPassword;

    @Value("${app.super-admin.first-name}")
    private String superAdminFirstName;

    @Value("${app.super-admin.last-name}")
    private String superAdminLastName;

    @Value("${app.super-admin.username}")
    private String superAdminUsername;

    @Value("${app.super-admin.business-name}")
    private String superAdminBusinessName;
    
    // Create role if missing
    private void createRoleIfMissing(RoleRepository repo, DefaultRole defaultRole) {
        String roleName = defaultRole.name();
        if (repo.findByRoleName(roleName).isEmpty()) {
            Role role = new Role();
            role.setRoleName(roleName);
            role.setTenant(null);
            role.setPermissions(defaultRole.getPermissions());
            repo.save(role);
            System.out.println("Seeded Global Role: " + roleName);
        }
    }

    @Bean
    CommandLineRunner initDatabase(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {

            // Seed all default roles
            for (DefaultRole role : DefaultRole.values()) {
                createRoleIfMissing(roleRepository, role);
            }

            // Create Super Admin User
            Role superAdminRole = roleRepository.findByRoleName(DefaultRole.SUPER_ADMIN.name())
                    .orElseThrow(() -> new RuntimeException("Super Admin role not found after seeding"));

            if (userRepository.findByEmail(superAdminEmail).isEmpty()) {

                User superAdmin = new User();
                superAdmin.setFirstName(superAdminFirstName);
                superAdmin.setLastName(superAdminLastName);
                superAdmin.setUserName(superAdminUsername);
                superAdmin.setEmail(superAdminEmail);
                superAdmin.setBusinessName("Deseret Technologies");

                // Critical: is_active to TRUE immediately (Nah we be BOSS😎)
                superAdmin.setActive(true);
                superAdmin.setPasswordHash(passwordEncoder.encode(superAdminPassword));
                superAdmin.setRoles(Collections.singleton(superAdminRole));

                // SuperAdmin has all views so it should be under any tenant
                superAdmin.setTenant(null);

                superAdmin.setApprovalToken(UUID.randomUUID().toString());
                userRepository.save(superAdmin);

                System.out.println("---------------------------------------------");
                System.out.println("   SUPER ADMIN CREATED: " + superAdminEmail);
                System.out.println("---------------------------------------------");
            }
        };
    }
}