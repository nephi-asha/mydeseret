package com.mydeseret.mydeseret.config;

import com.mydeseret.mydeseret.model.Tenant;
import com.mydeseret.mydeseret.repository.TenantRepository;
import com.mydeseret.mydeseret.service.SchemaProvisioningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TenantMigrationRunner implements CommandLineRunner {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private SchemaProvisioningService schemaProvisioningService;

    @Override
    public void run(String... args) {
        System.out.println("Checking for tenant database updates...");
        
        List<Tenant> tenants = tenantRepository.findAll();
        
        for (Tenant tenant : tenants) {
            try {
                schemaProvisioningService.migrateTenantSchema(tenant.getSchemaName());
            } catch (Exception e) {
                System.err.println("Failed to migrate tenant: " + tenant.getTenantName());
                e.printStackTrace();
            }
        }
        
        System.out.println("Tenant database checks completed.");
    }
}