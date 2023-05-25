package com.example.noticeboard.repository;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

}
