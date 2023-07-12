package com.example.adminservice.dto.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("project")
public record ProjectProperties(Board board) {
    /**
     * @param url 게시판 관련 프로퍼티
     */
    public record Board(String url) {
    }
}
