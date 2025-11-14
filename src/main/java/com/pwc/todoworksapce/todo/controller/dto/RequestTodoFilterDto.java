package com.pwc.todoworksapce.todo.controller.dto;

public record RequestTodoFilterDto(
        Long memberId,
        String tagName,
        Boolean isCompleted,
        String sortBy,
        String sortOrder
        ) {

}
