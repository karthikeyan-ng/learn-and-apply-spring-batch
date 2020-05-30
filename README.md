## Learn Spring Batch

#### Simple HelloWorld Job and it's Step
Steps:
* Select a Sprint Batch dependency from IO section in start.spring.io
* Create a `@Configuration` class which also `@EnableBatchProcessing` boot strapping the Spring Batch
```java
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchJobConfiguration {
}
``` 
* `@Autowired` the `JobBuilderFactory` and `StepBuilderFactory` in the `@Configuration` class
* Create a `Step` bean which uses a `StepBuilderFactory` to create a `Tasklet`
```java
@Bean
public Step step1() {
    return stepBuilderFactory
            .get("step1")
            .tasklet(new Tasklet() {
                @Override
                public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                    System.out.println("Hello World!");
                    return RepeatStatus.FINISHED;
                }
            })
            .build();

}
```
* Create a `Job` and use the `Step` for execution as shown below.
```java
@Bean
public Job helloWorldJob() {
    return jobBuilderFactory
            .get("helloWorldJob")
            .start(step1())
            .build();
}
```

**The Job:** Defines the flow that the processing will take through those states.
Each Step represents the State with in the State machine. So the Job defines
the list of states and how to transition from one state to another.  

**The Step**: Represents independent piece of processing that makes up a job. A job may have many steps.  
There are two different types of Steps.  

* **`Tasklet`** is a single method interface which contains `execute()`. Spring Batch provides a single method within the 
scope of the transaction.  

* **`Chunk`** is a Item based step. When you look at the Chunk step we expect to be processing Items individually.
Within the type of this Step there are three main components.
    * `ItemReader` is responsible for all the inputs for this `Step`
    * `ItemProcessor` which is optional; provides any additional transformation / validation / logic that needs to be applied on each Item
    * `ItemWriter` is responsible for output of the Step
 
![alt text](./images/chunk_based_process.png "Chunk Based Processing")

![alt text](./images/chunk_based_process1.png "Chunk Based Process Flow")

![alt text](./images/JobRepo.png "Job Repository Flow")