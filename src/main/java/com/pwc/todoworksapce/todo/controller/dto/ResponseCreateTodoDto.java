package com.pwc.todoworksapce.todo.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.pwc.todoworksapce.todo.entity.enums.Visibility;

public record ResponseCreateTodoDto(
        Long id,
        Long memberId,
        String memberNickname,
        String title,
        String content,
        boolean isCompleted,
        LocalDateTime dueDate,
        int remainingDays,
        Visibility visibility,
        BigDecimal priority,
        List<String> tagNames,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        ) {

}
