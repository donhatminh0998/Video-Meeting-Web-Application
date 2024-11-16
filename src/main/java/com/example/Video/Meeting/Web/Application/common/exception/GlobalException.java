package com.example.Video.Meeting.Web.Application.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    private GlobalErrorCode errorCode;
}
