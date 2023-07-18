package com.example.adminservice.config;

import com.example.adminservice.service.VisitCountService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import static org.mockito.BDDMockito.given;

@TestConfiguration
public class GlobalControllerConfig {
    @MockBean
    private VisitCountService service;

    @BeforeTestMethod
    public void securitySetup() {
        given(service.visitCount()).willReturn(0L);
    }
}
