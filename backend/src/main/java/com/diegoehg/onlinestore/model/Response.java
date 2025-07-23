package com.diegoehg.onlinestore.model;

public class Response<T> {
    private ResponseStatus status;
    private int code;
    private T data;
    private String message;

    // Private constructor to enforce use of factory methods
    private Response() {
    }

    // Factory method for success response
    public static <T> Response<T> success(T data, int code) {
        Response<T> response = new Response<>();
        response.status = ResponseStatus.SUCCESS;
        response.code = code;
        response.data = data;
        return response;
    }

    // Factory method for error response
    public static <T> Response<T> error(String message, int code) {
        Response<T> response = new Response<>();
        response.status = ResponseStatus.ERROR;
        response.code = code;
        response.message = message;
        return response;
    }

    // Getters and setters
    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}