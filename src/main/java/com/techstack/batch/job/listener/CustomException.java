package com.techstack.batch.job.listener;

public class CustomException extends Exception {

    public CustomException() {
        super();
    }

    public CustomException(String msg) {
        super(msg);
    }
}