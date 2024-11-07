package com.abcdedu_backend.contact.controller;


import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.common.page.PageManager;
import com.abcdedu_backend.common.page.request.PagingRequest;
import com.abcdedu_backend.common.page.request.SortRequest;
import com.abcdedu_backend.common.page.response.PagedResponse;
import com.abcdedu_backend.contact.dto.response.ContactGetResponse;
import com.abcdedu_backend.contact.dto.response.ContactListResponse;
import com.abcdedu_backend.contact.service.ContactService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/contacts")
@RequiredArgsConstructor
@Tag(name = "관리자 기능")
public class AdminContactController {
    private final ContactService contactService;

    @GetMapping()
    @Operation(summary = "상담 리스트 조회")
    public Response<PagedResponse<ContactListResponse>> readListContact(@JwtValidation Long memberdId, PagingRequest pagingRequest, SortRequest sortRequest) {
        Page<ContactListResponse> contacts = contactService.readListContact(memberdId, new PageManager(pagingRequest, sortRequest).makePageRequest());
        return Response.success(PagedResponse.from(contacts));
    }

    @GetMapping("/{contactId}")
    @Operation(summary = "상담 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "관리자 전용 기능입니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 id에 해당하는 상담을 찾을 수 없습니다.", content = @Content)
    })
    public Response<ContactGetResponse> readContact(@JwtValidation Long memberdId, @PathVariable Long contactId) {
        ContactGetResponse contactResponse = contactService.readContact(contactId, memberdId);
        return Response.success(contactResponse);
    }
}
