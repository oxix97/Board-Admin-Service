package com.example.adminservice.service;

import com.example.adminservice.domain.constant.RoleType;
import com.example.adminservice.dto.AdminAccountDto;
import com.example.adminservice.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AdminAccountService {
    // 단일 유저 관리, 유저 삭제,
    private final AdminAccountRepository repository;

    public Optional<AdminAccountDto> searchUser(String username) {
        return Optional.empty();
    }

    public AdminAccountDto saveUser(
            String userId,
            String userPassword,
            Set<RoleType> roleTypes,
            String email,
            String nickname,
            String memo
    ) {
        return AdminAccountDto.of(
                userId,
                userPassword,
                roleTypes,
                email,
                nickname,
                memo
        );
    }

    public List<AdminAccountDto> getUsers() {
        return List.of();
    }

    public void deleteUser(String userId) {

    }
}
