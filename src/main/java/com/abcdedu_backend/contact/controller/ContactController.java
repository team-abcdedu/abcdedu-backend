package com.abcdedu_backend.contact.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.common.request.PagingRequest;
import com.abcdedu_backend.common.response.PagedResponse;
import com.abcdedu_backend.contact.service.ContactService;
import com.abcdedu_backend.contact.dto.request.ContactCreateRequest;
import com.abcdedu_backend.contact.dto.response.ContactListResponse;
import com.abcdedu_backend.contact.dto.response.ContactGetResponse;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/")
    @Operation(summary = "상담 생성", description = "로그인을 하지 않아도 가능한 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 : 요청을 공백 없이 모두 채워주세요", content = @Content),
    })
    public Response<Long> createContract(@RequestBody ContactCreateRequest contactCreateRequest) {
        Long contactId = contactService.createContact(contactCreateRequest);
        return Response.success(contactId);
    }


    @GetMapping("/")
    @Operation(summary = "상담 리스트 조회", description = "관리자만 조회 가능 합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "관리자 전용 기능입니다.", content = @Content),
    })
    public Response<PagedResponse<ContactListResponse>> readListContact(@JwtValidation Long memberdId, PagingRequest pagingRequest) {
        Page<ContactListResponse> contacts = contactService.readListContact(memberdId, pagingRequest.toPageRequest());
        return Response.success(PagedResponse.from(contacts));
    }

    @GetMapping("/{contactId}")
    @Operation(summary = "상담 조회", description = "관리자만 조회 가능 합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "관리자 전용 기능입니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 id에 해당하는 상담을 찾을 수 없습니다.", content = @Content)
    })
    public Response<ContactGetResponse> readContact(@JwtValidation Long memberdId, @PathVariable Long contactId) {
        ContactGetResponse contactResponse = contactService.readContact(contactId, memberdId);
        return Response.success(contactResponse);
    }



}
