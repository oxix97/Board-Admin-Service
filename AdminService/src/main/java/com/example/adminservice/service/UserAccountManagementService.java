package com.example.adminservice.service;

import com.example.adminservice.dto.UserAccountDto;
import com.example.adminservice.dto.properties.ProjectProperties;
import com.example.adminservice.dto.response.UserAccountClientResponse;
import com.example.adminservice.dto.response.UserAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAccountManagementService {

    private final RestTemplate template;
    private final ProjectProperties properties;

    public List<UserAccountDto> getUserAccounts() {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/userAccounts")
                .queryParam("size",10000)
                .build()
                .toUri();

        UserAccountClientResponse response = template.getForObject(uri, UserAccountClientResponse.class);

        return Optional.ofNullable(response)
                .orElseGet(UserAccountClientResponse::empty)
                .userAccounts();
    }

    public UserAccountDto getUserAccount(String userId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/userAccounts/" + userId)
                .build()
                .toUri();

        UserAccountDto response = template.getForObject(uri, UserAccountDto.class);
        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다. user-id : " + userId));
    }

    public void deleteUserAccount(String userId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/userAccounts/" + userId)
                .build()
                .toUri();

        template.delete(uri);
    }
}
