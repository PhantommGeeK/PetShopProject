package com.cg.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ErrorResponse {

    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	private int status;
    private String error;
    private String message;

    private List<FieldError> fieldErrors;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;


    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String error, String message) {
        this();
        this.status  = status;
        this.error   = error;
        this.message = message;
    }

    public static class FieldError {
        private String field;
        private String rejectedValue;
        private String message;

        public FieldError(String field, String rejectedValue, String message) {
            this.field         = field;
            this.rejectedValue = rejectedValue;
            this.message       = message;
        }

        public String getField()         { return field; }
        public String getRejectedValue() { return rejectedValue; }
        public String getMessage()       { return message; }
    }
    
}