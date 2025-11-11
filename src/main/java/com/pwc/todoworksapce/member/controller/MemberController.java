package com.pwc.todoworksapce.member.controller;

import com.pwc.todoworksapce.member.service.MemberService;
import com.pwc.todoworksapce.member.controller.dto.RequestMemberDto;
import com.pwc.todoworksapce.member.controller.dto.ResponseMemberDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "사용자", description = "사용자 API")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("")
    @Operation(summary = "일반 사용자 생성")
    public ResponseEntity<ResponseMemberDto> createMember(
            @RequestBody RequestMemberDto requestMemberDto
    ) {
        ResponseMemberDto responseMemberDto = memberService.saveMember(requestMemberDto);
        return ResponseEntity.ok(responseMemberDto);
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "일반 사용자 삭제")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long memberId
    ) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok().build();
    }
}
