package com.mydeseret.mydeseret.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mydeseret.mydeseret.model.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>{

}
