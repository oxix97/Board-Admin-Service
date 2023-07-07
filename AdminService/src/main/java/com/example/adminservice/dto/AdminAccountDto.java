package com.example.adminservice.dto;

import com.example.adminservice.domain.AdminAccount;
import com.example.adminservice.domain.constant.RoleType;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO for the {@link AdminAccount} entity
 */
public record AdminAccountDto(
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
    public static AdminAccountDto of(
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
        return new AdminAccountDto(userId, userPassword,roleTypes, email, nickname, memo, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static AdminAccountDto of(
            String userId,
            String userPassword,
            Set<RoleType> roleTypes,
            String email,
            String nickname,
            String memo
    ) {
        return new AdminAccountDto(userId, userPassword,roleTypes, email, nickname, memo, null, null, null, null);
    }


    public static AdminAccountDto from(AdminAccount entity) {
        return new AdminAccountDto(
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

    public AdminAccount toEntity() {
        return AdminAccount.of(
                userId,
                userPassword,
                roleTypes,
                email,
                nickname,
                memo
        );
    }
}