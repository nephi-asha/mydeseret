package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.audit.AuditRevisionEntity;
import com.mydeseret.mydeseret.dto.AuditLogDto;
import com.mydeseret.mydeseret.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<AuditLogDto> getEntityHistory(String entityName, Long id) {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        Class<?> type = resolveEntityClass(entityName);

        List<Object[]> results = reader.createQuery()
                .forRevisionsOfEntity(type, false, true)
                .add(AuditEntity.id().eq(id))
                .addOrder(AuditEntity.revisionNumber().desc())
                .getResultList();

        return results.stream().map(row -> {
            Object entity = row[0];
            AuditRevisionEntity revEntity = (AuditRevisionEntity) row[1];
            RevisionType revType = (RevisionType) row[2];

            return new AuditLogDto(
                revEntity.getId(),
                revEntity.getUserId(),
                revEntity.getTimestamp(),
                revType,
                entity
            );
        }).collect(Collectors.toList());
    }

    private Class<?> resolveEntityClass(String name) {
        switch (name.toLowerCase()) {
            case "item": return Item.class;
            case "customer": return Customer.class;
            case "supplier": return Supplier.class;
            case "sale": return Sale.class;
            case "employee": return Employee.class;
            default: throw new IllegalArgumentException("Unknown entity type: " + name);
        }
    }
}