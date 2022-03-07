package ru.academits.model;

public class ErrorInfo {
    private final String message;

    public ErrorInfo(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
