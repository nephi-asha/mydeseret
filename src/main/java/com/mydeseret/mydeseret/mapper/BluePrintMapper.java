package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.BluePrintResponseDto;
import com.mydeseret.mydeseret.model.BluePrint;
import com.mydeseret.mydeseret.model.BluePrintItem;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class BluePrintMapper {

    public BluePrintResponseDto toResponseDto(BluePrint blueprint) {
        if (blueprint == null) return null;

        BluePrintResponseDto dto = new BluePrintResponseDto();
        dto.setId(blueprint.getId());
        dto.setName(blueprint.getName());
        dto.setOutputQuantity(blueprint.getOutputQuantity());
        
        if (blueprint.getOutputItem() != null) {
            dto.setOutputItemName(blueprint.getOutputItem().getName());
        }

        if (blueprint.getComponents() != null) {
            dto.setComponents(blueprint.getComponents().stream()
                .map(this::toComponentDto)
                .collect(Collectors.toList()));
        }

        return dto;
    }

    private BluePrintResponseDto.ComponentDto toComponentDto(BluePrintItem item) {
        BluePrintResponseDto.ComponentDto dto = new BluePrintResponseDto.ComponentDto();
        dto.setQuantityNeeded(item.getQuantityNeeded());
        if (item.getInputItem() != null) {
            dto.setInputItemName(item.getInputItem().getName());
        }
        return dto;
    }
}