package com.pwc.todoworksapce.member.controller.dto;

public record RequestCreateMemberDto(
        String username,
        String password,
        String nickname
) {
}
