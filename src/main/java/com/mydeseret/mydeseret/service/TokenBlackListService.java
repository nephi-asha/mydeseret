package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.model.TokenBlackList;
import com.mydeseret.mydeseret.repository.TokenBlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenBlackListService {

    @Autowired
    private TokenBlackListRepository repository;

    public void blacklistToken(String token) {
        if (!repository.existsByToken(token)) {
            repository.save(new TokenBlackList(token));
        }
    }

    public boolean isBlacklisted(String token) {
        return repository.existsByToken(token);
    }
}