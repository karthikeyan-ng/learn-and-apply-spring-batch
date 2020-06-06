package com.techstack.batch.job.skip;

public class CustomRetryableException extends Exception {

    public CustomRetryableException() {
        super();
    }

    public CustomRetryableException(String msg) {
        super(msg);
    }
}