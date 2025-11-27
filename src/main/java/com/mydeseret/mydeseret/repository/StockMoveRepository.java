package com.mydeseret.mydeseret.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mydeseret.mydeseret.model.StockMove;

@Repository
public interface StockMoveRepository extends JpaRepository<StockMove, Long> {
    List<StockMove> findByItem_ItemId(Long item_id);
}