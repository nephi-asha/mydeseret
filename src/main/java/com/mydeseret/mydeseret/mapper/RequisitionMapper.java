package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.RequisitionResponseDto;
import com.mydeseret.mydeseret.model.Requisition;
import org.springframework.stereotype.Component;

@Component
public class RequisitionMapper {

    public RequisitionResponseDto toResponseDto(Requisition req) {
        if (req == null) return null;

        RequisitionResponseDto dto = new RequisitionResponseDto();
        dto.setId(req.getId());
        dto.setQuantity(req.getQuantity());
        dto.setReason(req.getReason());
        dto.setNeededByDate(req.getNeededByDate());
        dto.setStatus(req.getStatus());
        dto.setCreatedAt(req.getCreatedAt());
        dto.setRejectionReason(req.getRejectionReason());

        if (req.getItem() != null) {
            dto.setItemName(req.getItem().getName());
        }

        if (req.getRequester() != null && req.getRequester().getUser() != null) {
            dto.setRequesterName(req.getRequester().getUser().getFirstName() + " " + 
                                 req.getRequester().getUser().getLastName());
        }

        if (req.getApprover() != null) {
            dto.setApproverName(req.getApprover().getFirstName() + " " + req.getApprover().getLastName());
        }

        return dto;
    }
}