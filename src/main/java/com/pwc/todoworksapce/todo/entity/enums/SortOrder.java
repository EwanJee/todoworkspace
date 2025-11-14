package com.pwc.todoworksapce.todo.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 정렬 순서
 */
@Getter
@RequiredArgsConstructor
public enum SortOrder {
    ASC("asc", "오름차순"),
    DESC("desc", "내림차순");

    private final String value;
    private final String description;

    public static SortOrder fromValue(String value) {
        if (value == null) {
            return ASC; // 기본값
        }

        for (SortOrder order : values()) {
            if (order.value.equalsIgnoreCase(value)) {
                return order;
            }
        }
        return ASC; // 알 수 없는 값인 경우 기본값
    }
}
