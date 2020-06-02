package com.techstack.batch.configuration.decisions;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
//@Configuration
public class DecisionBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step startStep() {
        return stepBuilderFactory
                .get("startStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("This is the start tasklet");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step evenStep() {
        return stepBuilderFactory
                .get("evenStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("This is the even tasklet");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step oddStep() {
        return stepBuilderFactory
                .get("oddStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("This is the odd tasklet");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    /**
     * Spring Batch provides an interface called JobExecutionDecider
     * for you to implement to allow you to use complex logic to
     * make decisions what the next step you are going to transition too,
     */
    @Bean
    public JobExecutionDecider decider() {
        return new OddDecider();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory
                .get("job")
                .start(startStep())
                .next(decider())
                .from(decider()).on("ODD").to(oddStep())
                .from(decider()).on("EVEN").to(evenStep())
                .from(oddStep()).on("*").to(decider())

                /**
                 * Technically below two decider() are not required.
                 * Because, once the previous ODD and EVEN steps completed,
                 * It would for again the cyclic process with the hint called "*"
                 */

//				.from(decider()).on("ODD").to(oddStep())
//				.from(decider()).on("EVEN").to(evenStep())
                .end()
                .build();
    }

    public static class OddDecider implements JobExecutionDecider {

        private int count = 0;

        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            count++;

            if(count % 2 == 0) {
                return new FlowExecutionStatus("EVEN");
            }
            else {
                return new FlowExecutionStatus("ODD");
            }
        }
    }
}
