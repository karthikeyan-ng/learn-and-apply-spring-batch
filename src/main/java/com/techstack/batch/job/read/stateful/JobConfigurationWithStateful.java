package com.techstack.batch.job.read.stateful;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JobConfigurationWithStateful {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public StatefulItemReader itemReader() {
        List<String> items = new ArrayList<>(100);

        for(int i = 1; i <= 100; i++) {
            items.add(String.valueOf(i));
        }

        return new StatefulItemReader(items);
    }

    @Bean
    public ItemWriter itemWriter() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> items) throws Exception {
                for (String item : items) {
                    System.out.println(">> " + item);
                }
            }
        };
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(10)
                .reader(itemReader())
                .writer(itemWriter())
                .stream(itemReader())
                .build();
    }

    @Bean
    public Job statefulJob() {
        return jobBuilderFactory.get("statefulJob")
                .start(step())
                .build();
    }
}
