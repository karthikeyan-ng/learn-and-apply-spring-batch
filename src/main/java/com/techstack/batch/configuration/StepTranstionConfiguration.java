package com.techstack.batch.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class StepTranstionConfiguration {

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step step1() {
        return stepBuilderFactory
                .get("step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> This is step 1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory
                .get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> This is step 2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory
                .get("step3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> This is step 3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    /**
     * Job Construction Scenario - 1
     *
     * This version of job, we have created 3 different steps.
     * once Step1 FINISHED, move on to next step Step2
     * once Step2 FINISHED, move on to next step Step3
     */
    /*@Bean
    public Job transitionJobSimple_Using_Next() {
        return jobBuilderFactory
                .get("transitionJobSimple_Using_Next")
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }*/

    /**
     * Job Construction Scenario - 2
     *
     * This version of job, we have created 3 different steps.
     * once Step1 FINISHED, move on to next step Step3
     * once Step3 FINISHED, move on to next step Step2
     */
    /*@Bean
    public Job transitionJobSimple_Using_Next_ByChangingTheStepOrder() {
        return jobBuilderFactory
                .get("transitionJobSimple_Using_Next_ByChangingTheStepOrder")
                .start(step1())
                .next(step3())
                .next(step2())
                .build();
    }*/

    /**
     * Job Construction Scenario - 3
     *
     * This version of job, we have created 3 different steps.
     * once Step1 FINISHED, move on to next step Step3
     * once Step3 FINISHED, move on to next step Step2
     * once Step2 FINISHED, repeat the same Step2
     */
    @Bean
    public Job transitionJobSimple_Using_Next_ByChangingAndRepeatTheStepOrder() {
        return jobBuilderFactory
                .get("transitionJobSimple_Using_Next_ByChangingAndRepeatTheStepOrder")
                .start(step1())
                .next(step3())
                .next(step2())
                .next(step2())
                .build();
    }
}
