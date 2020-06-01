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
    /*@Bean
    public Job transitionJobSimple_Using_Next_ByChangingAndRepeatTheStepOrder() {
        return jobBuilderFactory
                .get("transitionJobSimple_Using_Next_ByChangingAndRepeatTheStepOrder")
                .start(step1())
                .next(step3())
                .next(step2())
                .next(step2())
                .build();
    }*/

    /**
     * Job Construction Scenario - 4
     *
     * In this way you can control each Step and it's status. Based on that take
     * decision to execute NEXT Steps.
     *
     * Execute Step1 -> On Complete of Step1 -> move to Step2
     * From Step2 -> On Complete of Step2 -> move to Step3
     * From Step3 -> END
     *
     * on("COMPLETED") is a kind of exit code from the previous Step
     * end() is a indicator that tells last step in the flow
     *
     */
    /*@Bean
    public Job transitionJobSimple_Using_Next_MoreControlledWay() {
        return jobBuilderFactory
                .get("transitionJobSimple_Using_Next_MoreControlledWay")
                .start(step1()).on("COMPLETED").to(step2())
                .from(step2()).on("COMPLETED").to(step3())
                .from(step3()).end()
                .build();
    }*/

    /**
     * Job Construction Scenario - 5
     *
     * In this way you can control each Step and it's status. Based on that take
     * decision to execute NEXT Steps.
     *
     * Execute Step1 -> On Complete of Step1 -> move to Step2
     * From Step2 -> On Complete of Step2 -> fail()
     * From Step3 -> END
     *
     * fail() -> once failure occurred, the next steps will not be executed (step3 won't be executed)
     *
     */
    /*@Bean
    public Job transitionJobSimple_Using_Next_MoreControlledWay_WithFail() {
        return jobBuilderFactory
                .get("transitionJobSimple_Using_Next_MoreControlledWay_WithFail")
                .start(step1()).on("COMPLETED").to(step2())
                .from(step2()).on("COMPLETED").fail() //even though it is completed it is considered as fail
                .from(step3()).end()
                .build();
    }*/

    /**
     * Job Construction Scenario - 6
     *
     * In this way you can control each Step and it's status. Based on that take
     * decision to execute NEXT Steps.
     *
     * Execute Step1 -> On Complete of Step1 -> move to Step2
     * From Step2 -> On Complete of Step2 -> stop() or stopAndRestart()
     *
     * stopAndRestart() -> will force to restart the job and continue from the Step configured. (ex. step3())
     *
     */
    @Bean
    public Job transitionJobSimple_Using_Next_MoreControlledWay_WithStopAndRestart() {
        return jobBuilderFactory
                .get("transitionJobSimple_Using_Next_MoreControlledWay_WithStopAndRestart")
                .start(step1()).on("COMPLETED").to(step2())
                .from(step2()).on("COMPLETED").stopAndRestart(step3())
                .from(step3()).end()
                .build();
    }
}
