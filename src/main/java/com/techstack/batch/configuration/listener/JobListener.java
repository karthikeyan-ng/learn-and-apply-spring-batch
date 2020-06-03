package com.techstack.batch.configuration.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class JobListener implements JobExecutionListener {

    private final JavaMailSender mailSender;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();

        SimpleMailMessage mail =
                getSimpleMailMessage(String.format("%s is starting", jobName),
                        String.format("Per your request, we are informing you that %s is starting",
                                jobName));

        mailSender.send(mail);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();

        SimpleMailMessage mail =
                getSimpleMailMessage(String.format("%s has completed", jobName),
                        String.format("Per your request, we are informing you that %s has completed",
                                jobName));

        mailSender.send(mail);
    }

    private SimpleMailMessage getSimpleMailMessage(String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo("karthikeyan.ng.jobs@gmail.com");
        mail.setSubject(subject);
        mail.setText(text);
        return mail;
    }
}