package com.mydeseret.mydeseret.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mydeseret.mydeseret.dto.LoginRequestDto;
import com.mydeseret.mydeseret.dto.LoginResponseDto;
import com.mydeseret.mydeseret.dto.UserRequestDto;
import com.mydeseret.mydeseret.dto.UserResponseDto;
import com.mydeseret.mydeseret.mapper.UserMapper;
import com.mydeseret.mydeseret.model.Role;
import com.mydeseret.mydeseret.model.Tenant;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.RoleRepository;
import com.mydeseret.mydeseret.repository.TenantRepository;
import com.mydeseret.mydeseret.repository.UserRepository;
import com.mydeseret.mydeseret.util.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    UserRepository userRepository;
    TenantRepository tenantRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    EmailService emailService;
    JwtUtil jwtUtil;
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        TenantRepository tenantRepository,
        RoleRepository roleRepository,
        EmailService emailService,
        JwtUtil jwtUtil,
        CustomUserDetailsService customUserDetailsService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tenantRepository = tenantRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Transactional
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        Tenant newTenant = new Tenant();
        newTenant.setTenantName(userRequestDto.getBusinessName());
        newTenant.setSchemaName(userRequestDto.getBusinessName().toLowerCase().replaceAll("\\s+","_"));
        
        if (userRequestDto.getTimeZone() != null && !userRequestDto.getTimeZone().isEmpty()) {
            newTenant.setTimeZone(userRequestDto.getTimeZone());
        } else {
            newTenant.setTimeZone("UTC");
        }

        newTenant = tenantRepository.save(newTenant);

        User user = UserMapper.toModel(userRequestDto);
        user.setTenant(newTenant);

        Role ownerRole = roleRepository.findByRoleName("OWNER")
            .orElseThrow(() -> new RuntimeException("Error: Owner Role not found in DB. Database not seeded?"));
        user.setRoles(null);
        user.setRoles(Collections.singleton(ownerRole));

        String encodedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);

        userRepository.save(user);

        String subject = "Welcome to MyDeseret Technologies - Awaiting Approval";
        String body = "Hello " + user.getFirstName() + ",\n\n" +
                    "Thank you for registering '" + user.getBusinessName() + "'.\n" +
                    "Your account is currently PENDING APPROVAL by our administrators.\n" +
                    "You will receive another email once your account is active.\n\n" +
                    "Best Regards,\nMyDeseret Technologies Team";
        
        emailService.sendEmail(user.getEmail(), subject, body);
        return UserMapper.toResponseDto(user);
    }

    public LoginResponseDto loginUser(LoginRequestDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid password");
        }

        if (!user.is_active()) {
            throw new RuntimeException("Account is not active yet. Please wait for approval.");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        String roleName = user.getRoles().isEmpty() ? "USER" : user.getRoles().iterator().next().getRoleName();
        
        return new LoginResponseDto(token, user.getEmail(), roleName);
    }

}
