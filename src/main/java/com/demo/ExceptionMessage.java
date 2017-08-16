package com.demo;

/**
 * Created by ayush on 7/27/16.
 */
public class ExceptionMessage {
    public ExceptionMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status="ERROR";
}
