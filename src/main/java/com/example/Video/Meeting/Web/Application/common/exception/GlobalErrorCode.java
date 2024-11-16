package com.example.Video.Meeting.Web.Application.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public enum GlobalErrorCode {
    // user errors
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "1001", "이미 가입된 이메일입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "1002", "존재하지 않는 사용자 이메일입니다."),
    EMAIL_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "1003", "이메일과 패스워드가 일치하지 않습니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "1004", "비밀번호가 일치하지 않습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "1005", "인증에 실패했습니다."),
    USER_DETAILS_INVALID_FORMAT(HttpStatus.UNAUTHORIZED, "1006", "UserDetails의 형식이 올바르지 않습니다."),
    USER_MISMATCH(HttpStatus.UNAUTHORIZED, "1007", "일치하지 않는 회원입니다."),
    PROFILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "1008", "프로필 이미지 저장에 실패했습니다."),
    PROFILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "1009", "프로필 이미지 삭제에 실패했습니다."),
    ROLE_UPGRADE_DENIED(HttpStatus.FORBIDDEN, "1010", "권한이 없습니다. 일반 회원만 가능합니다."),
    DUPLICATE_MANAGER_REQUEST(HttpStatus.BAD_REQUEST, "1011", "이미 매니저 신청이 진행 중입니다."),
    MANAGER_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "1012", "존재하지 않는 매니저 신청입니다."),
    MANAGER_REQUEST_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "1013", "이미 처리된 매니저 신청입니다."),
    ROLE_UNAUTHORIZED(HttpStatus.NOT_FOUND, "1014", "권한이 없습니다. 관리자 혹은 시스템 관리자만 가능합니다."),
    ROLE_MISMATCH(HttpStatus.BAD_REQUEST, "1015", "권한이 없습니다. 시스템 관리자만 가능합니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "1016", "사용자 이름이 존재합니다"),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "1017", "이메일이 확인되지 않았습니다."),
//    INVALID_ROLE_FOR_REQUEST(HttpStatus.BAD_REQUEST, "1018", "일반 사용자만 역할을 전환할 수 있습니다."),
//    DUPLICATE_DRIVER_REQUEST(HttpStatus.BAD_REQUEST, "1019", "사용자가 이미 드라이브 역할을 전달하도록 요청했습니다."),
//    BUSINESS_NUMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "1020", "사업 허가증이 이미 존재합니다."),
//    LICENSE_NUMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "1021", "운전면허증이 이미 존재합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "1022", "존재하지 않는 사용자입니다."),
    // email errors
    EMAIL_SENDING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "2001", "이메일 발송에 실패했습니다."),
    VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "2002", "인증 번호가 틀렸습니다."),
    VERIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "2003", "이메일 인증 정보가 없습니다."),
    VERIFICATION_INVALID_STATUS(HttpStatus.BAD_REQUEST, "2004", "인증의 상태가 올바르지 않습니다."),
    VERIFICATION_EXPIRED(HttpStatus.BAD_REQUEST, "2005", "인증 코드가 만료되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
