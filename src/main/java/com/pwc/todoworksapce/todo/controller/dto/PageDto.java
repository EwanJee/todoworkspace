package com.pwc.todoworksapce.todo.controller.dto;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * 페이지네이션 응답을 위한 커스텀 DTO Page 객체의 직렬화 문제를 해결하기 위해 사용
 */
public record PageDto<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int currentPage,
        int size,
        boolean hasNext,
        boolean hasPrevious,
        boolean isFirst,
        boolean isLast
        ) {

    /**
     * Spring Data의 Page 객체를 PageDto로 변환
     */
    public static <T> PageDto<T> of(Page<T> page) {
        return new PageDto<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.hasPrevious(),
                page.isFirst(),
                page.isLast()
        );
    }
}
