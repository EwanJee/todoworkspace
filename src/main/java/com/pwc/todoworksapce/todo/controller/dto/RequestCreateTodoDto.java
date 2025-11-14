package com.pwc.todoworksapce.todo.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.pwc.todoworksapce.todo.entity.enums.Visibility;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestCreateTodoDto(
        @NotNull(message = "회원 ID는 필수입니다.")
        Long memberId,
        @NotBlank(message = "제목은 필수입니다.")
        String title,
        String content,
        @NotNull(message = "마감일은 필수입니다.")
        LocalDateTime dueDate,
        List<String> tagNames,
        Visibility visibility,
        @DecimalMin(value = "1", message = "우선순위는 1 이상이어야 합니다.")
        BigDecimal priority
) {

}
