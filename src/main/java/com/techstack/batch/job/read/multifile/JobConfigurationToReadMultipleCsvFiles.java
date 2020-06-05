package com.techstack.batch.job.read.multifile;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class JobConfigurationToReadMultipleCsvFiles {

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    @Value("classpath*:/data/multi-files/customer*.csv")
    private Resource[] inputFiles;

    @Bean
    public MultiResourceItemReader<Customer1> multiResourceItemReader() {
        MultiResourceItemReader<Customer1> reader = new MultiResourceItemReader<>();

        reader.setDelegate(Customer1ItemReader());
        reader.setResources(inputFiles);

        return reader;
    }

    @Bean
    public FlatFileItemReader<Customer1> Customer1ItemReader() {
        FlatFileItemReader<Customer1> reader = new FlatFileItemReader<>();

        DefaultLineMapper<Customer1> Customer1LineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[] {"id", "firstName", "lastName", "birthdate"});

        Customer1LineMapper.setLineTokenizer(tokenizer);
        Customer1LineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
        Customer1LineMapper.afterPropertiesSet();

        reader.setLineMapper(Customer1LineMapper);

        return reader;
    }

    @Bean
    public ItemWriter<Customer1> Customer1ItemWriter() {
        return items -> {
            for (Customer1 item : items) {
                System.out.println(item.toString());
            }
        };
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Customer1, Customer1>chunk(10)
                .reader(multiResourceItemReader())
                .writer(Customer1ItemWriter())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .build();
    }
}
