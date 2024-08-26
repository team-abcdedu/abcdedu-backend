package com.abcdedu_backend.member.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.member.controller.dto.request.UpdateMemberInfoRequest;
import com.abcdedu_backend.member.controller.dto.response.MemberInfoResponse;
import com.abcdedu_backend.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberInfoController {

    private final MemberService memberService;

    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@JwtValidation Long memberId){
        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(memberInfoResponse);
    }
    @PatchMapping("/info")
    public ResponseEntity<Void> updateMemberInfo(@JwtValidation Long memberId,
                                                 @Valid @ModelAttribute UpdateMemberInfoRequest updateMemberInfoRequest,
                                                 @RequestPart("file") MultipartFile profileImage){
        memberService.updateMemberInfo(memberId, updateMemberInfoRequest, profileImage);
        return ResponseEntity.ok().build();
    }
}
