package com.mydeseret.mydeseret.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mydeseret.mydeseret.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    // Total Revenue (Selling Price)
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s " +
           "WHERE s.status = 'COMPLETED' " +
           "AND s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalRevenue(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);

    // Total COGS (Cost Price)
    // This Joins Sale -> SaleItems to sum (costPrice * quantity)
    @Query("SELECT COALESCE(SUM(i.costPrice * i.quantity), 0) FROM Sale s " +
           "JOIN s.items i " +
           "WHERE s.status = 'COMPLETED' " +
           "AND s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalCOGS(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);
}