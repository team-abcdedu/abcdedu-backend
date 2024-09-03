package com.abcdedu_backend.contact.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.contact.service.ContactService;
import com.abcdedu_backend.contact.dto.request.ContactCreateRequest;
import com.abcdedu_backend.contact.dto.response.ContactListResponse;
import com.abcdedu_backend.contact.dto.response.ContactResponse;
import com.abcdedu_backend.contact.entity.ContactType;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.service.MemberService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content),
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다.", content = @Content)
})
@Tag(name = "문의 기능", description = "문의 관련 api입니다. 생성과 조회만 가능합니다.")
public class ContactController {

    private final ContactService contactService;
    private final MemberService memberService;

    @PostMapping("/training")
    @Operation(summary = "상담 생성 - 교사 연수")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 : 핸드폰번호/이메일/제목은 공백이어선 안됩니다.", content = @Content),
    })
    public Response<Long> createTrainingContract(@RequestBody ContactCreateRequest contactCreateRequest) {
        Long contactId = contactService.createContact(contactCreateRequest, ContactType.TRAINING);
        return Response.success(contactId);
    }

    @PostMapping("/class")
    @Operation(summary = "상담 생성 - 수업")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 : 핸드폰번호/이메일/제목은 공백이어선 안됩니다.", content = @Content),
    })
    public Response<Long> createClassContract(@RequestBody ContactCreateRequest contactCreateRequest) {
        Long contactId = contactService.createContact(contactCreateRequest, ContactType.CLASS);
        return Response.success(contactId);
    }

    @PostMapping("/etc")
    @Operation(summary = "상담 생성 - 기타")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 : 핸드폰번호/이메일/제목은 공백이어선 안됩니다.", content = @Content),
    })
    public Response<Long> createEtcContract(@RequestBody ContactCreateRequest contactCreateRequest) {
        Long contactId = contactService.createContact(contactCreateRequest, ContactType.ETC);
        return Response.success(contactId);
    }


    @GetMapping("/")
    @Operation(summary = "상담 리스트 조회", description = "관리자만 조회 가능 합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 : 핸드폰번호/이메일/제목은 공백이어선 안됩니다.", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한 검사 실패 : 관리자 권한만 조회 가능합니다.", content = @Content),
    })
    public Response<List<ContactListResponse>> readListContact(@JwtValidation Long memberdId) {
        List<ContactListResponse> contacts = contactService.readListContact(memberdId);
        return Response.success(contacts);
    }

    @GetMapping("/{contactId}")
    @Operation(summary = "상담 조회", description = "관리자만 조회 가능 합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "권한 검사 실패 : 관리자 권한만 조회 가능합니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 id에 해당하는 상담을 찾을 수 없습니다.", content = @Content)
    })
    public Response<ContactResponse> readContact(@JwtValidation Long memberdId, @PathVariable Long contactId) {
        ContactResponse contactResponse = contactService.readContact(contactId, memberdId);
        return Response.success(contactResponse);
    }



}
