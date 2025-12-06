package com.mydeseret.mydeseret.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mydeseret.mydeseret.model.BluePrint;

@Repository
public interface BluePrintRepository extends JpaRepository<BluePrint, Long> {
    boolean existsByOutputItem_ItemId(Long itemId);

    boolean existsByComponents_InputItem_ItemId(Long itemId);
}
