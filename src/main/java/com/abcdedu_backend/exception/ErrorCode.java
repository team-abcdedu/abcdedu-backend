package com.abcdedu_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 공통
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
    // 역할별 에러
    STUDENT_VALID_PERMISSION(HttpStatus.FORBIDDEN, "학생등급 이상 가능"),
    ADMIN_VALID_PERMISSION(HttpStatus.FORBIDDEN, "관리자 전용"),
    ADMIN_OR_WRITER_PERMISSION(HttpStatus.FORBIDDEN, "본인과 관리자 전용"),
    // 설문
    SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 설문이 없습니다."),
    SURVEY_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "지원하지 않는 설문 타입입니다."),
    SURVEY_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 설문 질문이 없습니다."),
    SURVEY_QUESTION_CHOICE_IS_ESSAY(HttpStatus.BAD_REQUEST, "서술형 문제는 choice를 가질 수 없습니다."),
    SURVEY_REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 설문 응답이 없습니다."),
    SURVEY_CHOICE_NUMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 번호의 질문 선택지를 찾을 수 없습니다."),
    // 상담
    CONTACT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상담이 없습니다."),
    CONTACT_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 상담 타입입니다."),

    // 게시판
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트가 없습니다."),
    POST_NOT_ALLOWED_COMMENT(HttpStatus.BAD_REQUEST, "댓글을 허용하지 않는 게시글 입니다."),

    // 댓글
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 없습니다."),

    // 카테고리
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),
    BOARD_DUPLICATION(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),

    //회원
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일 또는 비밀번호입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    TO_ADMIN_REQUEST_IS_NOT_ALLOWED(HttpStatus.FORBIDDEN, "관리자로의 역할 변경은 불가합니다. 개발자에게 문의해주세요."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일 또는 회원 탈퇴한 유저입니다."),
    DELETED_USER(HttpStatus.NOT_FOUND, "회원 탈퇴한 유저입니다."),

    //클래스
    CLASS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 클래스입니다."),
    SUB_CLASS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 서브클래스입니다."),
    ASSIGNMENT_ANSWER_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "지원하지 않는 응답 타입입니다."),
    ASSIGNMENT_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "지원하지 않는 평가 타입입니다."),
    ASSIGNMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 과제입니다."),
    SUBMISSION_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 시험 답안입니다."),
    ASSIGNMENT_FILE_DUPLICATION(HttpStatus.CONFLICT, "이미 존재하는 파일입니다."),

    //S3
    S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드에서 에러가 발생했습니다."),
    S3_OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "s3 오브젝트를 찾을 수 없습니다."),
    S3_DIRECTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "s3 파일 디렉토리를 찾을 수 없습니다."),
    ASSIGNMENT_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    ASSIGNMENT_ANSWER_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "문제지 파일을 찾을 수 없습니다."),
    FILE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 관련 서버 에러입니다"),

    // 공통 과제
    HOMEWORK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 공통과제를 찾을 수 없습니다."),
    REPRESENTATIVE_HOMEWORK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 대표 공통과제를 찾을 수 없습니다."),

    //파일 에러
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다."),

    //email 에러
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 인증에 실패하였습니다."),
    CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
    EMAIL_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일로 등록된 코드가 존재하지 않습니다."),

    //export 에러
    EXPORT_IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 추출하는데 실패하였습니다."),
    EXPORT_ILLEGAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "추출할 파일 형식과 맞지 않은 타입의 데이터가 입력되었습니다."),
    EXPORT_MAPPING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "엑셀 추출 값의 매핑이 잘못되었습니다.");


    private HttpStatus status;
    private String message;
}
