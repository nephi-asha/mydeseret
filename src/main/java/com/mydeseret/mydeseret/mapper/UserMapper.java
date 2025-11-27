package com.mydeseret.mydeseret.mapper;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mydeseret.mydeseret.dto.UserRequestDto;
import com.mydeseret.mydeseret.dto.UserResponseDto;
import com.mydeseret.mydeseret.model.User;

public class UserMapper {

    public static User toModel(UserRequestDto userRequestDto){
        if(userRequestDto == null) {
            return null;
        }
        User user = new User();

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setUserName(userRequestDto.getUserName());
        user.setEmail(userRequestDto.getEmail()); 
        user.setBusinessName(userRequestDto.getBusinessName()); 
        user.setPasswordHash(userRequestDto.getPassword());
        user.setCustomPermissions(userRequestDto.getCustomPermissions());
        user.setApprovalToken(UUID.randomUUID().toString());
        user.setActive(false);

        return user;
    }

    public static UserResponseDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();
        
        userResponseDto.setUserId(user.getUserId());
        userResponseDto.setUserName(user.getUserName());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setBusinessName(user.getBusinessName());
        userResponseDto.setActive(user.is_active());
        userResponseDto.setCreatedDate(user.getCreatedDate());

        if (user.getTenant() != null) {
            userResponseDto.setTenantId(user.getTenant().getTenantId());
        }
        
        if (user.getRoles() != null) {
            Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toSet());
            userResponseDto.setRoles(roleNames);
        }

        return userResponseDto;
    }
}