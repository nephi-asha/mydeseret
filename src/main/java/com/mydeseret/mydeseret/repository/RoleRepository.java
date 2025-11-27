package com.mydeseret.mydeseret.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mydeseret.mydeseret.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Optional<Role> findByRoleName(String roleName);

    @Query("SELECT r FROM Role r WHERE r.roleName = :name AND (r.tenant IS NULL OR r.tenant.tenantId = :tenant_id)")
    Optional<Role> findByNameAndTenant(@Param("name") String name, @Param("tenant_id") Long tenant_id);
}