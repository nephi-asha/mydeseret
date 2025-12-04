package com.mydeseret.mydeseret.specification;

import com.mydeseret.mydeseret.model.Item;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

public class ItemSpecification {

    public static Specification<Item> hasSearchText(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isEmpty()) return null;
            String likePattern = "%" + text.toLowerCase() + "%";
            // Search in Name OR SKU OR Description
            return cb.or(
                cb.like(cb.lower(root.get("name")), likePattern),
                cb.like(cb.lower(root.get("sku")), likePattern),
                cb.like(cb.lower(root.get("description")), likePattern)
            );
        };
    }

    public static Specification<Item> hasPriceRange(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min != null && max != null) return cb.between(root.get("sellingPrice"), min, max);
            if (min != null) return cb.greaterThanOrEqualTo(root.get("sellingPrice"), min);
            return cb.lessThanOrEqualTo(root.get("sellingPrice"), max);
        };
    }

    public static Specification<Item> hasCategory(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return null;
            return cb.equal(root.get("category").get("categoryId"), categoryId);
        };
    }

    public static Specification<Item> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }
}