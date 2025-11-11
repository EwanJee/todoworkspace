package com.pwc.todoworksapce.member.controller.dto;

import java.time.LocalDateTime;

public record ResponseMemberDto(
        Long id,
        String username,
        String nickname,
        LocalDateTime createdAt
) {
}
