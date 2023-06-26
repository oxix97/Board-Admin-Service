package com.example.noticeboard.service;

import com.example.noticeboard.domain.Hashtag;
import com.example.noticeboard.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[비즈니스 로직] - 해시태그 관련")
@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

    @InjectMocks
    private HashtagService hashtagService;

    @Mock
    private HashtagRepository hashtagRepository;

    @DisplayName("본문 파싱 -> 해시태그 중복 없이 반환")
    @MethodSource(value = "parseHashtag")
    @ParameterizedTest(name = "[{index}] \"{0}\" => {1}")
    void test1(String input, Set<String> expected) {
        //given

        //when
        Set<String> actual = hashtagService.parseHashtagNames(input);

        //then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        then(hashtagRepository).shouldHaveNoInteractions();
    }

    static Stream<Arguments> parseHashtag() {
        return Stream.of(
                arguments(null, Set.of()),
                arguments("", Set.of()),
                arguments("   ", Set.of()),
                arguments("#", Set.of()),
                arguments("  #", Set.of()),
                arguments("#   ", Set.of()),
                arguments("java", Set.of()),
                arguments("java#", Set.of()),
                arguments("ja#va", Set.of("va")),
                arguments("#java", Set.of("java")),
                arguments("#java_spring", Set.of("java_spring")),
                arguments("#java-spring", Set.of("java")),
                arguments("#_java_spring", Set.of("_java_spring")),
                arguments("#-java-spring", Set.of()),
                arguments("#_java_spring__", Set.of("_java_spring__")),
                arguments("#java#spring", Set.of("java", "spring")),
                arguments("#java #spring", Set.of("java", "spring")),
                arguments("#java  #spring", Set.of("java", "spring")),
                arguments("#java   #spring", Set.of("java", "spring")),
                arguments("#java     #spring", Set.of("java", "spring")),
                arguments("  #java     #spring ", Set.of("java", "spring")),
                arguments("   #java     #spring   ", Set.of("java", "spring")),
                arguments("#java#spring#부트", Set.of("java", "spring", "부트")),
                arguments("#java #spring#부트", Set.of("java", "spring", "부트")),
                arguments("#java#spring #부트", Set.of("java", "spring", "부트")),
                arguments("#java,#spring,#부트", Set.of("java", "spring", "부트")),
                arguments("#java.#spring;#부트", Set.of("java", "spring", "부트")),
                arguments("#java|#spring:#부트", Set.of("java", "spring", "부트")),
                arguments("#java #spring  #부트", Set.of("java", "spring", "부트")),
                arguments("   #java,? #spring  ...  #부트 ", Set.of("java", "spring", "부트")),
                arguments("#java#java#spring#부트", Set.of("java", "spring", "부트")),
                arguments("#java#java#java#spring#부트", Set.of("java", "spring", "부트")),
                arguments("#java#spring#java#부트#java", Set.of("java", "spring", "부트")),
                arguments("#java#스프링 아주 긴 글~~~~~~~~~~~~~~~~~~~~~", Set.of("java", "스프링")),
                arguments("아주 긴 글~~~~~~~~~~~~~~~~~~~~~#java#스프링", Set.of("java", "스프링")),
                arguments("아주 긴 글~~~~~~#java#스프링~~~~~~~~~~~~~~~", Set.of("java", "스프링")),
                arguments("아주 긴 글~~~~~~#java~~~~~~~#스프링~~~~~~~~", Set.of("java", "스프링"))
        );
    }

    @DisplayName("해시태그 이름들을 입력 => 저장된 해시태그 중 이름에 매칭하는 것들을 중복 없이 반환한다.")
    @Test
    void test2() {
        //given
        Set<String> hashtagNames = Set.of("java", "spring", "boots");
        given(hashtagRepository.findByHashtagNameIn(hashtagNames)).willReturn(List.of(
                Hashtag.of("java"),
                Hashtag.of("spring")
        ));

        //when
        Set<Hashtag> hashtags = hashtagService.findHashtagsByNames(hashtagNames);

        //then
        assertThat(hashtags).hasSize(2);
        then(hashtagRepository).should().findByHashtagNameIn(hashtagNames);
    }
}