package com.techstack.batch.configuration.flow.split;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@RequiredArgsConstructor
//@Configuration
public class SplitConfiguration {

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step myStep1_SplitConfiguration() {
        return stepBuilderFactory
                .get("myStep1_SplitConfiguration")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("myStep was executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step myStep2_SplitConfiguration() {
        return stepBuilderFactory
                .get("myStep2_SplitConfiguration")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("myStep was executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    /**
     * Here created a Job ("splitJob") which contains Two Steps
     * 1. myStep1_SplitConfiguration and 2. myStep2_SplitConfiguration
     * On completion of above Steps, it would move to another flow
     * which again uses split() method to add another two Flows for
     * parallel execution.
     */
    @Bean
    public Job splitJob(@Qualifier("foo") Flow foo, @Qualifier("foo") Flow bar) {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("split");

        Flow flow = flowBuilder
                .split(new SimpleAsyncTaskExecutor())
                .add(foo, bar)
                .end();

        return jobBuilderFactory.get("splitJob")
                .start(myStep1_SplitConfiguration())
                .next(myStep2_SplitConfiguration())
                .on("COMPLETED").to(flow)
                .end()
                .build();
    }
}
