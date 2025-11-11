package com.pwc.todoworksapce.member.controller.dto;

public record RequestMemberDto(
        String username,
        String password,
        String nickname
) {
}
