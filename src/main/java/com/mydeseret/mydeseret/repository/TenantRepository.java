package com.mydeseret.mydeseret.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mydeseret.mydeseret.model.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long>{
    
}
