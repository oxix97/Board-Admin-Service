package com.example.noticeboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {
    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        int start = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0);
        int end = Math.min(start + BAR_LENGTH, totalPages);

        return IntStream.range(start, end).boxed().toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }
}
