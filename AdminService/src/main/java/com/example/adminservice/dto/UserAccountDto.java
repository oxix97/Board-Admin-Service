package com.example.adminservice.dto;

import com.example.adminservice.domain.UserAccount;
import com.example.adminservice.domain.constant.RoleType;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO for the {@link com.example.adminservice.domain.UserAccount} entity
 */
public record UserAccountDto(
        String userId,
        String userPassword,
        Set<RoleType> roleTypes,
        String email,
        String nickname,
        String memo,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static UserAccountDto of(
            String userId,
            String userPassword,
            Set<RoleType> roleTypes,
            String email,
            String nickname,
            String memo,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new UserAccountDto(userId, userPassword,roleTypes, email, nickname, memo, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static UserAccountDto of(
            String userId,
            String userPassword,
            Set<RoleType> roleTypes,
            String email,
            String nickname,
            String memo
    ) {
        return new UserAccountDto(userId, userPassword,roleTypes, email, nickname, memo, null, null, null, null);
    }


    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getUserId(),
                entity.getUserPassword(),
                entity.getRoleTypes(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getMemo(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public UserAccount toEntity() {
        return UserAccount.of(
                userId,
                userPassword,
                roleTypes,
                email,
                nickname,
                memo
        );
    }
}