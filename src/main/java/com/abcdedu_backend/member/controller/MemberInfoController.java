package com.abcdedu_backend.member.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.member.dto.request.UpdateMemberInfoRequest;
import com.abcdedu_backend.member.dto.response.MemberInfoResponse;
import com.abcdedu_backend.member.dto.response.MemberShortInfoResponse;
import com.abcdedu_backend.member.service.MemberService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
})
@Tag(name = "프로필 기능", description = "프로필 관련 api입니다.")
public class MemberInfoController {

    private final MemberService memberService;

    @Operation(summary = "프로필 조회", description = "프로필을 조회합니다.")
    @GetMapping("/info")
    public Response<MemberInfoResponse> getMemberInfo(@JwtValidation Long memberId){
        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(memberId);
        return Response.success(memberInfoResponse);
    }

    @ApiResponses(value ={
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.", content = @Content),
    })
    @Operation(summary = "프로필 정보 수정", description = "프로필을 정보를 수정합니다.")
    @PatchMapping("/info")
    public Response<Void> updateMemberInfo(@JwtValidation Long memberId,
                                                 @Valid @ModelAttribute UpdateMemberInfoRequest updateMemberInfoRequest,
                                                 @RequestPart("file") MultipartFile profileImage){
        memberService.updateMemberInfo(memberId, updateMemberInfoRequest, profileImage);
        return Response.success();
    }


    @Operation(summary = "프로필 이름, 역할 정보 조회", description = "프로필 이름, 역할을 조회합니다.")
    @GetMapping("/info/name-and-role")
    public Response<MemberShortInfoResponse> getMemberNameAndRoleInfo(@JwtValidation Long memberId){
        MemberShortInfoResponse memberShortInfoResponse = memberService.getMemberNameAndRoleInfo(memberId);
        return Response.success(memberShortInfoResponse);
    }
}
