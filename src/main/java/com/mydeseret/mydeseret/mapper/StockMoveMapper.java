package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.StockMoveResponseDto;
import com.mydeseret.mydeseret.model.StockMove;
import org.springframework.stereotype.Component;

@Component
public class StockMoveMapper {

    public StockMoveResponseDto toResponseDto(StockMove move) {
        if (move == null) return null;

        StockMoveResponseDto dto = new StockMoveResponseDto();
        dto.setId(move.getMoveId());
        dto.setQuantityChange(move.getQuantityChange());
        dto.setReason(move.getReason());
        dto.setReferenceId(move.getReferenceId());
        dto.setNotes(move.getNotes());
        dto.setCreatedAt(move.getCreatedAt());

        if (move.getItem() != null) {
            dto.setItemName(move.getItem().getName());
        }
        return dto;
    }
}