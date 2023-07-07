package com.example.adminservice.repository;

import com.example.adminservice.domain.AdminAccount;
import com.example.adminservice.domain.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
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

    @DisplayName("[INSERT] 회원 정보 테스트")
    @Test
    void givenAdminAccount_whenInserting_thenWorkFine() {
        //given
        long preCount = adminAccountRepository.count();
        AdminAccount admin = createAdminAccount();

        //when
        adminAccountRepository.save(admin);

        //then
        assertThat(adminAccountRepository.count()).isEqualTo(preCount + 1);
    }

    @DisplayName("[UPDATE] 회원 정보 테스트")
    @Test
    void givenAdminAccountAndRoleType_whenUpdating_thenWorkFine() {
        //given
        AdminAccount admin = adminAccountRepository.getReferenceById("chan1");
        admin.addRoleType(RoleType.USER, RoleType.DEVELOPER);
        admin.removeRoleType(RoleType.ADMIN);
        //when
        AdminAccount updateAdmin = adminAccountRepository.saveAndFlush(admin);

        //then
        assertThat(updateAdmin)
                .hasFieldOrPropertyWithValue("userId", "chan1")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.DEVELOPER, RoleType.USER));
    }

    @DisplayName("[DELETE] 회원 정보 테스트")
    @Test
    void givenAdminAccount_whenDeleting_thenWorkFine() {
        //given
        long preCount = adminAccountRepository.count();
        AdminAccount admin = adminAccountRepository.getReferenceById("chan1");
        //when
        adminAccountRepository.delete(admin);

        //then
        assertThat(adminAccountRepository.count()).isEqualTo(preCount - 1);
    }

    private AdminAccount createAdminAccount() {
        return AdminAccount.of(
                "test",
                "pw",
                Set.of(RoleType.USER),
                "daldkjf",
                "test@mail.com",
                "memo"
        );
    }

    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("test");
        }
    }

}
