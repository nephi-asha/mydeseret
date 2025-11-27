package com.mydeseret.mydeseret.repository;

import com.mydeseret.mydeseret.model.AmortizationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmortizationScheduleRepository extends JpaRepository<AmortizationSchedule, Long> {
}