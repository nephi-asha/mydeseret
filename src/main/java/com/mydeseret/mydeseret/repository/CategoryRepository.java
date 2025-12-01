package com.mydeseret.mydeseret.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mydeseret.mydeseret.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByActiveTrue(Pageable pageable);
}
