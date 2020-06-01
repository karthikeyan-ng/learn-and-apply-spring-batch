package com.techstack.batch.configuration.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
//@Configuration
public class FlowLastConfiguration {

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step myStepInFlowLast() {
        return stepBuilderFactory.get("myStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("myStep was executed from FlowLastConfiguration#myStepInFlowLast");
                    return RepeatStatus.FINISHED;
                }).build();
    }

	@Bean
	public Job flowLastJob(@Qualifier("bar") Flow flow) {
		return jobBuilderFactory.get("flowLastJob")
				.start(myStepInFlowLast())
				.on("COMPLETED").to(flow)
				.end()
				.build();
	}
}
