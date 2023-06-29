package com.example.noticeboard.service;

import com.example.noticeboard.domain.UserAccount;
import com.example.noticeboard.dto.UserAccountDto;
import com.example.noticeboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;

    // 예외처리를 할 수 있으나 호출한 곳에서 정하기 위해 Optional로 매핑된 것을 반환
    @Transactional(readOnly = true)
    public Optional<UserAccountDto> searchUser(String username) {
        return userAccountRepository.findById(username).map(UserAccountDto::from);
    }

    public UserAccountDto saveUser(
            String userName,
            String password,
            String email,
            String nickname,
            String memo
    ) {
        return UserAccountDto.from(
                userAccountRepository.save(UserAccount.of(userName, password, email, nickname, memo, userName))
        );
    }
}
