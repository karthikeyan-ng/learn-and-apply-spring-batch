package com.techstack.batch.job.skip;

import com.techstack.batch.job.retry.CustomRetryableException;
import org.springframework.batch.item.ItemProcessor;

public class SkipItemProcessor implements ItemProcessor<String, String> {

    private boolean skip = false;
    private int attemptCount = 0;

    @Override
    public String process(String item) throws Exception {
        System.out.println("processing item " + item);
        if(skip && item.equalsIgnoreCase("42")) {
            attemptCount++;

            System.out.println("Processing of item " + item + " failed");
            throw new CustomRetryableException("Process failed.  Attempt:" + attemptCount);
        }
        else {
            return String.valueOf(Integer.valueOf(item) * -1);
        }
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }
}
