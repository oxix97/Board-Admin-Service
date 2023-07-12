package com.example.adminservice.service;

import com.example.adminservice.dto.ArticleDto;
import com.example.adminservice.dto.UserAccountDto;
import com.example.adminservice.dto.properties.ProjectProperties;
import com.example.adminservice.dto.response.ArticleClientResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@DisplayName("[비즈니스 로직] 게시글 관리")
class ArticleManagementServiceTest {

    @Disabled("실제 API 호출 결과를 관찰하는 목적으로 평상시에 비활성화")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {
        private final ArticleManagementService service;

        @Autowired
        public RealApiTest(ArticleManagementService service) {
            this.service = service;
        }

        @DisplayName("게시판 조회 - 정상 호출")
        @Test
        void test1() {
            //given

            //when
            List<ArticleDto> result = service.getArticles();

            //then
            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();
        }
    }


    @DisplayName("API mocking Test")
    @EnableConfigurationProperties({ProjectProperties.class})
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleManagementService.class)
    @Nested
    class RestTemplateTest {
        private final ArticleManagementService service;
        private final ProjectProperties properties;
        private final MockRestServiceServer server;
        private final ObjectMapper mapper;

        @Autowired
        public RestTemplateTest(
                ArticleManagementService service,
                ProjectProperties properties,
                MockRestServiceServer server,
                ObjectMapper mapper
        ) {
            this.service = service;
            this.properties = properties;
            this.server = server;
            this.mapper = mapper;
        }

        @DisplayName("게시판 조회 - 정상 호출")
        @Test
        void givenNothing_whenCallingArticlesApi_thenReturnsArticleList() throws Exception {
            //given
            ArticleDto expectedArticle = createArticleDto("제목", "본문");
            ArticleClientResponse expectedResponse = ArticleClientResponse.of(List.of(expectedArticle));
            server
                    .expect(requestTo(properties.board().url() + "/api/articles?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));

            //when
            List<ArticleDto> actual = service.getArticles();

            //then
            assertThat(actual).first()
                    .hasFieldOrPropertyWithValue("id", expectedArticle.id())
                    .hasFieldOrPropertyWithValue("title", expectedArticle.title())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.userAccount().nickname());

            server.verify();
        }

        @DisplayName("게시글 단건 조회 - 정상 호출")
        @Test
        void givenArticleId_whenCallingGetArticleApi_thenReturnsArticle() throws Exception {
            //given
            Long id = 1L;
            ArticleDto expectedArticle = createArticleDto("제목", "본문");
            server
                    .expect(requestTo(properties.board().url() + "/api/articles/" + id))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedArticle),
                            MediaType.APPLICATION_JSON
                    ));

            //when
            ArticleDto actual = service.getArticle(id);

            //then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue("id", expectedArticle.id())
                    .hasFieldOrPropertyWithValue("title", expectedArticle.title())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.userAccount().nickname());

            server.verify();
        }

        @DisplayName("게시글 삭제 - 정상 호출")
        @Test
        void givenArticleId_whenCallingDeleteArticleApi_thenDeletesArticle() throws Exception {
            //given
            Long id = 1L;

            server
                    .expect(requestTo(properties.board().url() + "/api/articles/" + id + "/delete"))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            //when
            service.deleteArticle(id);

            //then
            server.verify();
        }
    }

    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                null,
                LocalDateTime.now(),
                "test",
                LocalDateTime.now(),
                "test"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "test1",
                "pw",
                "test-title",
                "test-content",
                "test-memo"
        );
    }
}