package com.pwc.todoworksapce.member.service;

import com.pwc.todoworksapce.member.controller.dto.RequestMemberDto;
import com.pwc.todoworksapce.member.controller.dto.ResponseMemberDto;

public interface MemberService {
    ResponseMemberDto saveMember(RequestMemberDto dto);

    void deleteMember(Long memberId);
}
