package com.techstack.batch.job.integration.startajob;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobLaunchingController {

    /**
     * JobLauncher doesn't have restart facility
     */
	private final JobLauncher jobLauncher;

    /**
     * For more robust options use JobOperator
     */
    private final JobOperator jobOperator;

	private final Job job;

    @PostMapping(value = "/scenario1")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void launch(@RequestParam("name") String name) throws Exception {
		JobParameters jobParameters =
				new JobParametersBuilder()
						.addString("name", name)
						.toJobParameters();
		this.jobLauncher.run(job, jobParameters);
    }

    @PostMapping(value = "/scenario2")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void launch1(@RequestParam("name") String name) throws Exception {
        this.jobOperator.start("job", String.format("name=%s", name));
    }
}
