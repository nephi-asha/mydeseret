package com.mydeseret.mydeseret.audit;

import jakarta.persistence.*;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@Table(name = "revinfo")
@RevisionEntity(AuditRevisionListener.class) 
public class AuditRevisionEntity extends DefaultRevisionEntity {

    @Column(name = "user_id")
    private String userId;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}