package com.mydeseret.mydeseret.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity audit = (AuditRevisionEntity) revisionEntity;
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            // Stores the email (username) of the person making the change
            audit.setUserId(auth.getName());
        } else {
            audit.setUserId("SYSTEM"); // For scheduled jobs or startups
        }
    }
}