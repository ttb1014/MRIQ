package com.vervyle.mriqBackend.exception;

public class EmptyDirectoryException extends RuntimeException {
    public EmptyDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyDirectoryException(String message) {
        super(message);
    }
}
