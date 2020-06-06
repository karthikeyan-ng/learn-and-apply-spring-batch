package com.techstack.batch.job.listener;

import org.springframework.batch.core.SkipListener;

public class CustomSkipListener implements SkipListener {
    @Override
    public void onSkipInRead(Throwable t) {
    }

    @Override
    public void onSkipInWrite(Object item, Throwable t) {
        System.out.println(">> Skipping " + item + " because writing it caused the error: " + t.getMessage());
    }

    @Override
    public void onSkipInProcess(Object item, Throwable t) {
        System.out.println(">> Skipping " + item + " because processing it caused the error: " + t.getMessage());
    }
}

