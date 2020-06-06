package com.techstack.batch.job.retry;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class RetryItemWriter implements ItemWriter<String> {

    private boolean retry = false;
    private int attemptCount = 0;

    @Override
    public void write(List<? extends String> items) throws Exception {
        for (String item : items) {
            System.out.println("writing item " + item);
            if(retry && item.equalsIgnoreCase("-84")) {
                attemptCount++;

                if(attemptCount >= 5) {
                    System.out.println("Success!");
                    retry = false;
                    System.out.println(item);
                }
                else {
                    System.out.println("Writing of item " + item + " failed");
                    throw new CustomRetryableException("Write failed.  Attempt:" + attemptCount);
                }
            }
            else {
                System.out.println(item);
            }
        }
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }
}
