// package com.mydeseret.mydeseret.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.jdbc.datasource.init.ScriptUtils;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import javax.sql.DataSource;
// import java.sql.Connection;
// import java.sql.SQLException;
// import java.sql.Statement;

// @Service
// public class SchemaProvisioningService {

//     @Autowired
//     private DataSource dataSource;

//     @Transactional
//     public void createTenantSchema(String schema_name) {
        
//         try (Connection connection = dataSource.getConnection()) {
//             try (Statement stmt = connection.createStatement()) {
//                 stmt.execute("CREATE SCHEMA IF NOT EXISTS \"" + schema_name + "\"");
//             }

//             connection.setSchema(schema_name);
            
//             ClassPathResource resource = new ClassPathResource("tenant-schema.sql");
//             ScriptUtils.executeSqlScript(connection, resource);
            
//             System.out.println("Successfully created tables for schema: " + schema_name);
            
//         } catch (SQLException e) {
//             throw new RuntimeException("Failed to provision schema: " + schema_name, e);
//         }
//     }
// }

package com.mydeseret.mydeseret.service;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
public class SchemaProvisioningService {

    @Autowired
    private DataSource dataSource;

    @Transactional
    public void createTenantSchema(String schemaName) {
        // First I'll need to configure Flyway for this specific tenant
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schemaName) // Target the new schema
                .locations("classpath:db/migration/tenants") // I'll then use the tenant scripts
                .baselineOnMigrate(true) // I set this to true just in case the tables are already there so flyway wont complain
                .load();

        // So this will basically Run Migrate:
        // a) Create the schema if it doesn't exist
        // b) Create the tables from V1__init_tenant.sql
        // c) Create a history table to track this version
        flyway.migrate();
        
        System.out.println("Successfully provisioned schema: " + schemaName);
    }

    public void migrateTenantSchema(String schemaName) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schemaName)
                .locations("classpath:db/migration/tenants")
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
        System.out.println("Migrated schema: " + schemaName);
    }
}