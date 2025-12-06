package com.mydeseret.mydeseret.repository;

import com.mydeseret.mydeseret.model.ReportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRequestRepository extends JpaRepository<ReportRequest, UUID> {
    List<ReportRequest> findByUserId(UUID userId);
}
