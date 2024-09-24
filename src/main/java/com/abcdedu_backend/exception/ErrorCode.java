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
    ADMIN_INVALID_PERMISSION(HttpStatus.FORBIDDEN, "관리자만 가능한 기능 입니다."),
    BASIC_INVALID_PERMISSION(HttpStatus.FORBIDDEN, "학생 이상만 가능한 기능 입니다."),
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
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

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
    HOMEWORK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 공통과제를 찾을 수 없습니다.");

    private HttpStatus status;
    private String message;
}
