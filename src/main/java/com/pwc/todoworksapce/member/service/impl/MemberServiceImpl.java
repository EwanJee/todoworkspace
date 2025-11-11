package com.pwc.todoworksapce.member.service.impl;

import com.pwc.todoworksapce.error.CustomException;
import com.pwc.todoworksapce.member.controller.dto.RequestMemberDto;
import com.pwc.todoworksapce.member.controller.dto.ResponseMemberDto;
import com.pwc.todoworksapce.member.entity.Member;
import com.pwc.todoworksapce.member.repository.MemberRepository;
import com.pwc.todoworksapce.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ResponseMemberDto saveMember(RequestMemberDto dto) {
        if (memberRepository.existsByUsername(dto.username())) {
            throw new CustomException("이미 존재하는 사용자 이름입니다.");
        }
        Member member = new Member(dto.username(), dto.password(), dto.nickname());
        Member savedMember = memberRepository.save(member);
        return new ResponseMemberDto(
                savedMember.getId(),
                savedMember.getUsername(),
                savedMember.getNickname(),
                savedMember.getCreatedAt()
        );
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        boolean deleted = memberRepository.deleteMemberById(memberId);
        System.out.println("deleted = " + deleted);
        if (!deleted) {
            throw new CustomException("사용자 삭제에 실패했습니다.");
        }
    }
}
