package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.model.Role;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.model.enums.ApplicationPermission;
import com.mydeseret.mydeseret.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Retrieve all permissions from all roles
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
            
            for (ApplicationPermission perm : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(perm.name()));
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                user.is_active(), 
                true, true, true, 
                authorities
        );
    }
}