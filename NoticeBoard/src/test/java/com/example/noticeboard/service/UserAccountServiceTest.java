package com.example.noticeboard.service;

import com.example.noticeboard.domain.UserAccount;
import com.example.noticeboard.dto.UserAccountDto;
import com.example.noticeboard.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[비즈니스 로직] 유저")
@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @InjectMocks
    private UserAccountService userAccountService;

    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("회원 ID를 검색하면, 회원 데이터를 (Optional)객체로 반환한다.")
    @Test
    void test1() {
        //given
        String userName = "chan";
        given(userAccountRepository.findById(userName)).willReturn(Optional.of(createUserAccount(userName)));

        //when
        Optional<UserAccountDto> actual = userAccountService.searchUser(userName);

        //then
        assertThat(actual).isPresent();
        then(userAccountRepository).should().findById(userName);
    }

    @DisplayName("존재하지 않는 회원 ID를 검색하면, 비어있는(Optional)객체로 반환한다.")
    @Test
    void test2() {
        //given
        String userName = "empty";
        given(userAccountRepository.findById(userName)).willReturn(Optional.empty());

        //when
        Optional<UserAccountDto> actual = userAccountService.searchUser(userName);

        //then
        assertThat(actual).isEmpty();
        then(userAccountRepository).should().findById(userName);
    }

    @DisplayName("회원 정보를 저장하고 해당 데이터를 리턴한다.")
    @Test
    void test3() {
        //given
        UserAccount userAccount = createUserAccount("chan");
        UserAccount savedUserAccount = createSigningUpUserAccount("chan");
        given(userAccountRepository.save(userAccount)).willReturn(savedUserAccount);

        //when
        UserAccountDto actual = userAccountService.saveUser(
                userAccount.getUserId(),
                userAccount.getUserPassword(),
                userAccount.getNickname(),
                userAccount.getCreatedBy(),
                userAccount.getMemo()
        );

        //then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("userId", userAccount.getUserId())
                .hasFieldOrPropertyWithValue("userPassword", userAccount.getUserPassword())
                .hasFieldOrPropertyWithValue("email", userAccount.getEmail())
                .hasFieldOrPropertyWithValue("nickname", userAccount.getNickname())
                .hasFieldOrPropertyWithValue("memo", userAccount.getMemo())
                .hasFieldOrPropertyWithValue("createdBy", userAccount.getUserId())
                .hasFieldOrPropertyWithValue("modifiedBy", userAccount.getUserId());
        then(userAccountRepository).should().save(userAccount);
    }

    private UserAccount createUserAccount(String username) {
        return createUserAccount(username, null);
    }

    private UserAccount createSigningUpUserAccount(String username) {
        return createUserAccount(username, username);
    }

    private UserAccount createUserAccount(String username, String createdBy) {
        return UserAccount.of(
                username,
                "password",
                "e@mail.com",
                "nickname",
                "memo",
                createdBy
        );
    }
}