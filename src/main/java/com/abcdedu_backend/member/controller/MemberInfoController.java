package com.abcdedu_backend.member.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.member.controller.dto.request.UpdateMemberInfoRequest;
import com.abcdedu_backend.member.controller.dto.response.MemberInfoResponse;
import com.abcdedu_backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "프로필 기능", description = "프로필 관련 api입니다.")
public class MemberInfoController {

    private final MemberService memberService;

    @Operation(summary = "프로필 조회", description = "프로필을 조회합니다.")
    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@JwtValidation Long memberId){
        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(memberInfoResponse);
    }

    @Operation(summary = "프로필 정보 수정", description = "프로필을 정보를 수정합니다.")
    @PatchMapping("/info")
    public ResponseEntity<Void> updateMemberInfo(@JwtValidation Long memberId,
                                                 @Valid @ModelAttribute UpdateMemberInfoRequest updateMemberInfoRequest,
                                                 @RequestPart("file") MultipartFile profileImage){
        memberService.updateMemberInfo(memberId, updateMemberInfoRequest, profileImage);
        return ResponseEntity.ok().build();
    }
}
