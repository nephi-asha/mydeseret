package com.mydeseret.mydeseret.specification;

import com.mydeseret.mydeseret.model.Sale;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SaleSpecification {

    public static Specification<Sale> isWithinDateRange(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) return null;
            if (start != null && end != null) return cb.between(root.get("saleDate"), start, end);
            if (start != null) return cb.greaterThanOrEqualTo(root.get("saleDate"), start);
            return cb.lessThanOrEqualTo(root.get("saleDate"), end);
        };
    }

    public static Specification<Sale> hasMinAmount(BigDecimal minAmount) {
        return (root, query, cb) -> {
            if (minAmount == null) return null;
            return cb.greaterThanOrEqualTo(root.get("totalAmount"), minAmount);
        };
    }
}