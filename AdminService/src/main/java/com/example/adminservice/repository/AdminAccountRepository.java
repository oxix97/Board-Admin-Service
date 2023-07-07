package com.example.adminservice.repository;

import com.example.adminservice.domain.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AdminAccountRepository extends JpaRepository<AdminAccount, String> {

}
