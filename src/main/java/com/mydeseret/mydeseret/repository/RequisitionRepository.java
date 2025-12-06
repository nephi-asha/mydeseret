package com.mydeseret.mydeseret.repository;

import com.mydeseret.mydeseret.model.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mydeseret.mydeseret.model.enums.RequisitionStatus;

import java.util.UUID;

@Repository
public interface RequisitionRepository extends JpaRepository<Requisition, Long> {
    boolean existsByApprover_UserIdAndStatus(UUID userId, RequisitionStatus status);
}