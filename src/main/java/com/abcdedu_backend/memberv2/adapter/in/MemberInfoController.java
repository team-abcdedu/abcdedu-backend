package com.abcdedu_backend.memberv2.adapter.in;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.memberv2.adapter.in.dto.request.UpdateMemberInfoRequest;
import com.abcdedu_backend.memberv2.adapter.in.dto.request.UpdatePasswordRequest;
import com.abcdedu_backend.memberv2.adapter.in.dto.response.MemberBasicInfoResponse;
import com.abcdedu_backend.memberv2.adapter.in.dto.response.MemberInfoResponse;
import com.abcdedu_backend.memberv2.adapter.in.dto.response.MemberNameAndRoleResponse;
import com.abcdedu_backend.memberv2.application.MemberInfoUseCase;
import com.abcdedu_backend.memberv2.application.dto.MemberBasicInfoDto;
import com.abcdedu_backend.memberv2.application.dto.MemberInfoDto;
import com.abcdedu_backend.memberv2.application.dto.NameAndRoleDto;
import com.abcdedu_backend.memberv2.application.dto.command.UpdateMemberInfoCommand;
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

    private final MemberInfoUseCase memberInfoUseCase;

    @Operation(summary = "프로필 조회", description = "프로필을 조회합니다.")
    @GetMapping("/info")
    public Response<MemberInfoResponse> getMemberInfo(@JwtValidation Long memberId){
        MemberInfoDto memberInfoDto = memberInfoUseCase.getMemberInfo(memberId);
        MemberInfoResponse memberInfoResponse = MemberInfoResponse.of(memberInfoDto);
        return Response.success(memberInfoResponse);
    }

    @ApiResponses(value ={
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.", content = @Content),
    })
    @Operation(summary = "프로필 정보 수정", description = "프로필을 정보를 수정합니다.")
    @PatchMapping("/info")
    public Response<Void> updateMemberInfo(@JwtValidation Long memberId,
                                                 @Valid @ModelAttribute UpdateMemberInfoRequest updateMemberInfoRequest,
                                                 @RequestPart(value = "file", required = false) MultipartFile multipartFile){
        UpdateMemberInfoCommand updateMemberInfoCommand = UpdateMemberInfoCommand.of(
                memberId,
                updateMemberInfoRequest.name(),
                updateMemberInfoRequest.school(),
                updateMemberInfoRequest.studentId(),
                multipartFile);
        memberInfoUseCase.updateMemberInfo(updateMemberInfoCommand);
        return Response.success();
    }


    @Operation(summary = "프로필 이름, 역할 정보 조회", description = "프로필 이름, 역할을 조회합니다.")
    @GetMapping("/info/name-and-role")
    public Response<MemberNameAndRoleResponse> getMemberNameAndRoleInfo(@JwtValidation Long memberId){
        NameAndRoleDto nameAndRoleDto = memberInfoUseCase.getMemberNameAndRoleInfo(memberId);
        MemberNameAndRoleResponse memberNameAndRoleResponse = new MemberNameAndRoleResponse(nameAndRoleDto.name(), nameAndRoleDto.role());
        return Response.success(memberNameAndRoleResponse);
    }

    @Operation(summary = "프로필 이름, 역할, 이메일 정보 조회", description = "프로필 이름, 역할, 이메일을 조회합니다.")
    @GetMapping("/basic-info")
    public Response<MemberBasicInfoResponse> getMemberBasicInfo(@JwtValidation Long memberId){
        MemberBasicInfoDto memberBasicInfoDto = memberInfoUseCase.getMemberBasicInfo(memberId);
        MemberBasicInfoResponse memberBasicInfoResponse = MemberBasicInfoResponse.of(memberBasicInfoDto);
        return Response.success(memberBasicInfoResponse);
    }

    @ApiResponses(value ={
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.", content = @Content),
    })
    @Operation(summary = "비밀번호 수정", description = "비밀번호를 수정합니다.")
    @PatchMapping("/password")
    public Response<Void> updatePassword(@JwtValidation Long memberId,
                                         @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest){
        memberInfoUseCase.updatePassword(memberId, updatePasswordRequest.newPassword());
        return Response.success();
    }
}
