package com.pwc.todoworksapce.member.controller.dto;

import com.pwc.todoworksapce.member.entity.enums.Department;

public record RequestCreateMemberDto(
        String username,
        String password,
        String nickname,
        String department
) {
}
