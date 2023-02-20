package com.attornatus.attornatus.resources.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class FieldMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String fieldError;
    private String message;

    public FieldMessage() {
    }

    public FieldMessage(String fieldError, String message) {
        this.fieldError = fieldError;
        this.message = message;
    }

    public String getFieldError() {
        return fieldError;
    }

    public void setFieldError(String fieldError) {
        this.fieldError = fieldError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
