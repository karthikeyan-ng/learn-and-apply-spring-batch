package com.techstack.batch.job.listener;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SkipItemWriter implements ItemWriter<String> {

    private int attemptCount = 0;

    @Override
    public void write(List<? extends String> items) throws Exception {
        for (String item : items) {
            if(item.equalsIgnoreCase("-84")) {
                throw new CustomException("Write failed.  Attempt:" + attemptCount);
            }
            else {
                System.out.println(item);
            }
        }
    }
}