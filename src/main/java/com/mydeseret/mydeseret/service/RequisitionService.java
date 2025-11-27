package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.RequisitionRequestDto;
import com.mydeseret.mydeseret.dto.RequisitionResponseDto;
import com.mydeseret.mydeseret.mapper.RequisitionMapper;
import com.mydeseret.mydeseret.model.*;
import com.mydeseret.mydeseret.model.enums.RequisitionStatus;
import com.mydeseret.mydeseret.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class RequisitionService {

    @Autowired private RequisitionRepository requisitionRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RequisitionMapper requisitionMapper;

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Transactional
    public RequisitionResponseDto createRequisition(RequisitionRequestDto request) {
        User user = getAuthenticatedUser();
        
        Employee employee = employeeRepository.findByUser_UserId(user.getUserId());
        if (employee == null) {
            throw new RuntimeException("Only active employees can make requisitions.");
        }

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Requisition req = new Requisition();
        req.setRequester(employee);
        req.setItem(item);
        req.setQuantity(request.getQuantity());
        req.setReason(request.getReason());
        req.setNeededByDate(request.getNeededByDate());
        req.setStatus(RequisitionStatus.PENDING);

        return requisitionMapper.toResponseDto(requisitionRepository.save(req));
    }

    // Approves
    @Transactional
    public RequisitionResponseDto approveRequisition(Long id) {
        User manager = getAuthenticatedUser();
        
        Requisition req = requisitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisition not found"));

        if (req.getStatus() != RequisitionStatus.PENDING) {
            throw new RuntimeException("Requisition is not pending.");
        }

        req.setStatus(RequisitionStatus.APPROVED);
        req.setApprover(manager);
        req.setApprovalDate(LocalDate.now());

        return requisitionMapper.toResponseDto(requisitionRepository.save(req));
    }

    // Rejected
    @Transactional
    public RequisitionResponseDto rejectRequisition(Long id, String reason) {
        User manager = getAuthenticatedUser();

        Requisition req = requisitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisition not found"));

        req.setStatus(RequisitionStatus.REJECTED);
        req.setApprover(manager);
        req.setApprovalDate(LocalDate.now());
        req.setRejectionReason(reason);

        return requisitionMapper.toResponseDto(requisitionRepository.save(req));
    }

    public Page<RequisitionResponseDto> getAllRequisitions(Pageable pageable) {
        return requisitionRepository.findAll(pageable)
                .map(requisitionMapper::toResponseDto);
    }
}