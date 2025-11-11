package com.pwc.todoworksapce.member.service;

import com.pwc.todoworksapce.member.controller.dto.RequestCreateMemberDto;
import com.pwc.todoworksapce.member.controller.dto.ResponseCreateMemberDto;

public interface MemberService {
    ResponseCreateMemberDto createMember(RequestCreateMemberDto dto);

    void deleteMember(Long memberId);
}
