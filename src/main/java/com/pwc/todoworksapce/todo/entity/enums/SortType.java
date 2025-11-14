package com.pwc.todoworksapce.todo.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Todo 정렬 타입
 */
@Getter
@RequiredArgsConstructor
public enum SortType {
    PRIORITY("priority", "우선순위"),
    DUE_DATE("dueDate", "마감일"),
    CREATED_AT("createdAt", "생성일");

    private final String value;
    private final String description;
}
