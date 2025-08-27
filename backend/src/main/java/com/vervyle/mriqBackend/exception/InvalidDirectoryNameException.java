package com.vervyle.mriqBackend.exception;

public class InvalidDirectoryNameException extends RuntimeException {
    public InvalidDirectoryNameException(String message) {
        super(message);
    }

    public InvalidDirectoryNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
