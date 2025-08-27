package com.vervyle.mriqBackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class MedExException {
    private final String message;

    private final Throwable throwable;

    private final HttpStatus httpStatus;
}
