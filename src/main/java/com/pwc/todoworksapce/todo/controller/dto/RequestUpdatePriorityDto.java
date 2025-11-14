package com.pwc.todoworksapce.todo.controller.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record RequestUpdatePriorityDto(
        @NotNull(message = "회원 ID는 필수입니다.")
        Long memberId,

        @NotNull(message = "우선순위는 필수입니다.")
        @DecimalMin(value = "1", message = "우선순위는 1 이상이어야 합니다.")
        BigDecimal priority
) {
}

