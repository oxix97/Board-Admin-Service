package com.example.noticeboard.service;

import com.example.noticeboard.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("비즈니스 로직 - 페이지네이션")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Void.class)
class PaginationServiceTest {
    private final PaginationService service;

    public PaginationServiceTest(@Autowired PaginationService service) {
        this.service = service;
    }

    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 만들어 준다.")
    @MethodSource
    @ParameterizedTest(name = "[{index}] {0}, {1} => {2}")
    void test1(int currentPageNumber, int totalPages, List<Integer> expected) {
        //given

        //when
        List<Integer> actual = service.getPaginationBarNumbers(currentPageNumber, totalPages);

        //then
        assertEquals(actual, expected);
    }

    static Stream<Arguments> test1() {
        return Stream.of(
                arguments(0, 13, List.of(0, 1, 2, 3, 4)),
                arguments(1, 13, List.of(0, 1, 2, 3, 4)),
                arguments(2, 13, List.of(0, 1, 2, 3, 4)),
                arguments(3, 13, List.of(1, 2, 3, 4, 5)),
                arguments(4, 13, List.of(2, 3, 4, 5, 6))
        );
    }

    @DisplayName("현재 설정되어 있는 페이지네이션 바의 길이를 알려준다.")
    @Test
    void test2() {
        //given

        //when
        int barLength = service.currentBarLength();

        //then

    }
}