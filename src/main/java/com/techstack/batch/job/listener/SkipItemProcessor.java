package com.techstack.batch.job.listener;

import org.springframework.batch.item.ItemProcessor;

public class SkipItemProcessor implements ItemProcessor<String, String> {

    private int attemptCount = 0;

    @Override
    public String process(String item) throws Exception {
        if(item.equalsIgnoreCase("42")) {
            throw new CustomException("Process failed.  Attempt:" + attemptCount);
        }
        else {
            return String.valueOf(Integer.valueOf(item) * -1);
        }
    }
}