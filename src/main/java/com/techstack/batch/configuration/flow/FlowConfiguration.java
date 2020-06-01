package com.techstack.batch.configuration.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FlowConfiguration {

    public final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step step1() {
        return stepBuilderFactory
                .get("step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Step 1 from inside flow foo");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory
                .get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Step 2 from inside flow foo");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    /**
     * Here in this scenario, if you would like to reuse the Step(s) configuration / logic
     * more than one, you can think of using FlowBuilder.
     *
     * FlowBuilder will take Flow as type and name of the flow (ex. foo).
     * Using FlowBuilder, you can start with Step and continue with next() and so on.
     * Using end() you can end the Flow.
     *
     * Flow itself is a reusable component which you can use it in Job, either before or after.
     */
    @Bean
    public Flow foo() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("foo");

        flowBuilder
                .start(step1())
                .next(step2())
                .end();

        return flowBuilder.build();
    }

    @Bean
    public Flow bar() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("bar");

        flowBuilder
                .start(step1())
                .next(step2())
                .end();

        return flowBuilder.build();
    }
}
