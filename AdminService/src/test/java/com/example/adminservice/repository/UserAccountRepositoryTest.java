package com.example.adminservice.repository;

import com.example.adminservice.domain.AdminAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@DataJpaTest
class JpaRepositoryTest {
    private final AdminAccountRepository adminAccountRepository;

    public JpaRepositoryTest(@Autowired AdminAccountRepository adminAccountRepositorysitory) {
        this.adminAccountRepository = adminAccountRepositorysitory;
    }

    @DisplayName("[SELECT] 회원 정보 테스트")
    @Test
    void givenUserAccounts_whenSelecting_thenWorksFine() {
        //given

        //when
        List<AdminAccount> userAccounts = adminAccountRepository.findAll();

        //then
        assertThat(userAccounts)
                .isNotNull()
                .hasSize(4);
    }

}
