package com.techstack.batch.job.integration.schedule;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class JobConfigurationToLaunchJobViaScheduler
        extends DefaultBatchConfigurer implements ApplicationContextAware {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public JobExplorer jobExplorer;

    @Autowired
    public JobRepository jobRepository;

    @Autowired
    public JobRegistry jobRegistry;

    @Autowired
    public JobLauncher jobLauncher;

    private ApplicationContext applicationContext;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistrar() throws Exception {
        JobRegistryBeanPostProcessor registrar = new JobRegistryBeanPostProcessor();

        registrar.setJobRegistry(this.jobRegistry);
        registrar.setBeanFactory(this.applicationContext.getAutowireCapableBeanFactory());
        registrar.afterPropertiesSet();

        return registrar;
    }

    @Bean
    public JobOperator jobOperator() throws Exception {
        SimpleJobOperator simpleJobOperator = new SimpleJobOperator();

        simpleJobOperator.setJobLauncher(this.jobLauncher);
        simpleJobOperator.setJobParametersConverter(new DefaultJobParametersConverter());
        simpleJobOperator.setJobRepository(this.jobRepository);
        simpleJobOperator.setJobExplorer(this.jobExplorer);
        simpleJobOperator.setJobRegistry(this.jobRegistry);

        simpleJobOperator.afterPropertiesSet();

        return simpleJobOperator;
    }

    @Bean
    @StepScope
    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");

            System.out.println(
                    String.format(">> I was run at %s",
                            formatter.format(new Date())));
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .start(stepBuilderFactory.get("step1")
                        .tasklet(tasklet())
                        .build())
                .build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public JobLauncher getJobLauncher() {
        SimpleJobLauncher jobLauncher = null;
        try {
            jobLauncher = new SimpleJobLauncher();
            jobLauncher.setJobRepository(jobRepository);
            jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
            jobLauncher.afterPropertiesSet();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jobLauncher;
    }
}

