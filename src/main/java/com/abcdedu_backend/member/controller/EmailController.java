package com.abcdedu_backend.member.controller;

import com.abcdedu_backend.member.dto.request.SendMailRequest;
import com.abcdedu_backend.member.service.EmailService;
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

@Slf4j
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
})
@Tag(name = "이메일 인증 기능", description = "이메일 인증 기능입니다.")
public class EmailController {
    private final EmailService emailService;

    @Operation(summary = "인증 메일 전송", description = "인증 메일을 전송합니다.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일입니다.", content = @Content),
            @ApiResponse(responseCode = "500", description = "이메일 인증 메일 전송 실패하였습니다.", content = @Content)
    })
    @PostMapping("/code")
    public Response<Void> sendCode(@Valid @RequestBody SendMailRequest sendMailRequest){
        emailService.sendCodeToEmail(sendMailRequest.email());
        return Response.success();
    }

    @Operation(summary = "인증 코드 체크", description = "인증 코드를 체크합니다.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "400", description = "코드가 일치하지 않습니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이메일입니다.", content = @Content)
    })
    @GetMapping("/code")
    public Response<Void> codeVerification(@RequestParam String email, @RequestParam String code){
        emailService.checkCode(email, code);
        return Response.success();
    }

    @Operation(summary = "임시 비멀번호 전송", description = "메일로 임시 비밀번호를 전송합니다.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이메일입니다.", content = @Content),
            @ApiResponse(responseCode = "500", description = "이메일 전송을 실패하였습니다.", content = @Content)
    })
    @PostMapping("/temp-password")
    public Response<Void> sendTempPassword(@Valid @RequestBody SendMailRequest sendMailRequest){
        emailService.sendTempPasswordToEmail(sendMailRequest.email());
        return Response.success();
    }
}
