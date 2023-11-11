package com.recruitmentsystem;

public class TestResponse<T> {
    public int code;
    public String messages;
    public T data;

    public TestResponse() {
    }

    public TestResponse(int code) {
        this.code = code;
    }

    public TestResponse(int code, String messages) {
        this.code = code;
        this.messages = messages;
    }

    public TestResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public TestResponse(int code, String messages, T data) {
        this.code = code;
        this.messages = messages;
        this.data = data;
    }
}