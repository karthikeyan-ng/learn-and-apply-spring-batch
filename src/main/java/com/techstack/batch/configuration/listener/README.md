Listeners:  
Spring Batch provides the ability to handle complex processing across a number of different use cases.  

In some use cases...
* It can be helpful to insert logic at given points in the job lifecycle.  

* Listeners provided by Spring Batch allow a developer to take control of the execution flow at just about any junction point
within a given batch of listener functionality is implemented by implementing the appropriate interface and then adding
that implementation to your job or steps configuration each of the interfaces also has a support class.

* In other words the name of the interface with the word support at the end that provides a new up implementation of each
of the methods on the interface for easy extension the listener interface is provided by Spring.

* **`JobExecutionListener`**: Include the job execution listener which provides before and after job execution listeners.
* **`StepExecutionListener`**: Step execution listener which provides before and after step capabilities.
* **`ChunkListener`**: Provides a before and after chunk as well as method to be called an error within a chunk for 
additional error handling.
* **`ItemReadListener`**: **`ItemProcessListener`**: **`ItemWriteListener`**: These all provide before and after each item's
interaction with the appropriate component as well as an on air if something failed within that interaction.

Beyond these interfaces you an also use the annotations for each one of the expected methods in the previously mentioned interfaces.  

This allows you to use regular POJOs instead of implementing Spring batches interfaces.  

`@BeforeJob`  
`@AfterJob`
`@BeforeStep`  
`@AfterStep`  
`@BeforeChunk`  
`@AfterChunk`  
`@AfterChunkError`  
`@BeforeRead`  
`@AfterRead`  

This listener examples will send a mail when processing each Chunk before and after execution.
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=michael@michaelminella.com
spring.mail.password=cldhfguhpnwaijmv
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```


  