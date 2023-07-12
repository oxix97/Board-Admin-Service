package com.example.adminservice.service;

import com.example.adminservice.dto.ArticleCommentDto;
import com.example.adminservice.dto.ArticleCommentDto;
import com.example.adminservice.dto.UserAccountDto;
import com.example.adminservice.dto.properties.ProjectProperties;
import com.example.adminservice.dto.response.ArticleCommentClientResponse;
import com.example.adminservice.service.ArticleCommentManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@DisplayName("[비즈니스 로직] 댓글 관리")
class ArticleCommentManagementServiceTest {

    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {
        private final ArticleCommentManagementService service;

        @Autowired
        public RealApiTest(ArticleCommentManagementService service) {
            this.service = service;
        }

        @DisplayName("댓글 조회 - 정상 호출")
        @Test
        void test1() {
            //given

            //when
            List<ArticleCommentDto> result = service.getArticleComments();

            //then
            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();
        }
    }

    @DisplayName("API mocking Test")
    @EnableConfigurationProperties({ProjectProperties.class})
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleCommentManagementService.class)
    @Nested
    class RestTemplateTest {
        private final ArticleCommentManagementService service;
        private final ProjectProperties properties;
        private final MockRestServiceServer server;
        private final ObjectMapper mapper;

        @Autowired
        public RestTemplateTest(
                ArticleCommentManagementService service,
                ProjectProperties properties,
                MockRestServiceServer server,
                ObjectMapper mapper
        ) {
            this.service = service;
            this.properties = properties;
            this.server = server;
            this.mapper = mapper;
        }

        @DisplayName("댓글들을 가져오는 API - 정상 호출")
        @Test
        void givenNothing_whenCallingArticleCommentsApi_thenReturnsArticleComments() throws Exception {
            //given
            ArticleCommentDto expected = createArticleCommentDto("대앳글");
            ArticleCommentClientResponse expectedResponse = ArticleCommentClientResponse.of(List.of(expected));
            server
                    .expect(requestTo(properties.board().url() + "/api/article-comments"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));

            //when
            List<ArticleCommentDto> actual = service.getArticleComments();

            //then
            assertThat(actual).first()
                    .hasFieldOrPropertyWithValue("id", expected.id())
                    .hasFieldOrPropertyWithValue("content", expected.content())
                    .hasFieldOrPropertyWithValue("articleId", expected.articleId());

            server.verify();
        }

        @DisplayName("댓글 단건 조회 - 정상 호출")
        @Test
        void givenArticleCommentId_whenCallingGetArticleCommentApi_thenReturnsArticleComment() throws Exception {
            //given
            Long commentId = 1L;
            ArticleCommentDto expected = createArticleCommentDto(commentId, "댓글");
            server
                    .expect(requestTo(properties.board().url() + "/api/article-comments/" + commentId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expected),
                            MediaType.APPLICATION_JSON
                    ));

            //when
            ArticleCommentDto actual = service.getArticleComment(commentId);

            //then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue("id", expected.id())
                    .hasFieldOrPropertyWithValue("content", expected.content())
                    .hasFieldOrPropertyWithValue("parentCommentId", expected.parentCommentId())
                    .hasFieldOrPropertyWithValue("articleId", expected.articleId());

            server.verify();
        }

        @DisplayName("댓글 삭제 - 정상 호출")
        @Test
        void givenArticleCommentId_whenCallingDeleteArticleCommentApi_thenDeletesArticleComment() throws Exception {
            //given
            Long id = 1L;
            server
                    .expect(requestTo(properties.board().url() + "/api/article-comments/" + id + "/delete"))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            //when
            service.deleteArticleComment(id);

            //then
            server.verify();
        }
    }

    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                null,
                content,
                LocalDateTime.now(),
                "test",
                LocalDateTime.now(),
                "test"
        );
    }

    private ArticleCommentDto createArticleCommentDto(Long parentId, String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                parentId,
                content,
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
