package com.pwc.todoworksapce.error;

import lombok.Getter;

/**
 * 커스텀 예외 클래스
 */
@Getter
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
