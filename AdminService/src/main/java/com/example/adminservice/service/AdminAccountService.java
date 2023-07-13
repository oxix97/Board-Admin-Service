package com.example.adminservice.service;

import com.example.adminservice.domain.AdminAccount;
import com.example.adminservice.domain.constant.RoleType;
import com.example.adminservice.dto.AdminAccountDto;
import com.example.adminservice.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AdminAccountService {
    // 단일 유저 관리, 유저 삭제,
    private final AdminAccountRepository repository;

    public AdminAccountDto saveUser(
            String userId,
            String userPassword,
            Set<RoleType> roleTypes,
            String email,
            String nickname,
            String memo
    ) {
        AdminAccount admin = AdminAccount.of(userId, userPassword, roleTypes, email, nickname, memo);
        repository.save(admin);

        return AdminAccountDto.from(admin);
    }

    @Transactional(readOnly = true)
    public Optional<AdminAccountDto> searchUser(String userId) {
        return repository.findById(userId)
                .map(AdminAccountDto::from);
    }

    @Transactional(readOnly = true)
    public List<AdminAccountDto> users() {
        return repository.findAll().stream()
                .map(AdminAccountDto::from)
                .toList();
    }


    public void deleteUser(String userId) {
        repository.deleteById(userId);
    }
}
