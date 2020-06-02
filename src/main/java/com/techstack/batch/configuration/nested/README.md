Here in this example we are executing two different jobs.
One is ParentJob and another is ChildJob.

ParentJob will execute the it's Steps and based on the configuration it would use ChildJob steps.

ParentJob and ChildJob both contains JobBuilderFactory to execute it's steps.
However, when you run this module ChildJob should not be executed. It should always depends on
ParentJob.

So, to resolve this issue, we have to configure below properties
```properties
spring.batch.job.names=parentJob
```