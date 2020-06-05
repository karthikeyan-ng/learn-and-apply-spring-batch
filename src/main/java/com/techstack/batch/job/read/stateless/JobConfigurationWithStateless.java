package com.techstack.batch.job.read.stateless;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JobConfigurationWithStateless {

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    @Bean
    public StatelessItemReader statelessItemReader() {

        return new StatelessItemReader(List.of("Foo", "Bar", "Baz"));
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(2)
                .reader(statelessItemReader())
                .writer(list -> {
                    for (String curItem : list) {
                        System.out.println("curItem = " + curItem);
                    }
                }).build();
    }

    @Bean
    public Job interfacesJob() {
        return jobBuilderFactory.get("interfacesJob")
                .start(step1())
                .build();
    }
}
