package com.mydeseret.mydeseret.model;

import jakarta.persistence.*;
// import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// @Data
@Entity
@Table(name = "token_black_list")
@NoArgsConstructor
public class TokenBlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_black_list_id")    
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Column(nullable = false, name = "blacklisted_at")
    private LocalDateTime blacklistedAt = LocalDateTime.now();

    public TokenBlackList(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getBlacklistedAt() {
        return blacklistedAt;
    }

    public void setBlacklistedAt(LocalDateTime blacklistedAt) {
        this.blacklistedAt = blacklistedAt;
    }

}