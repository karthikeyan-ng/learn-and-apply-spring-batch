package com.techstack.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableBatchProcessing
@IntegrationComponentScan
@SpringBootApplication
public class LearnAndApplySpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnAndApplySpringBatchApplication.class, args);
	}

}
