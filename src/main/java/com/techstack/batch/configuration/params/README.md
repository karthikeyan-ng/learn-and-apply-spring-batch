Notes:

One of the biggest advantages to Spring Batch is that it's built upon spring.

This allows you batch applications to be configured using all the standard spring mechanism you're used

to using such as property files etc. However can also be useful to be able to provide additional configuration

at runtime.

Spring Batch provides a mechanism for providing parameters to a job to allow you to customize its configuration.

These parameters also serve as special additional purpose job primers are used to identify a job instance.

If we take a look at the diagram you can see that we have the concept of a job and a job can have a

job instance a job instance can have many job executions a job instance is essentially a logical run.

So in this example if I have the end of the day job for each day I'll have a job instance a logical

run.

So in this case I can pass on a parameter for each day indicating the new job instance.

So September 1st of 2015 September 2nd and so forth.

Each time I physically run the job I'll get a job execution but it's that job parameter that identifies

a job instance.

Let's take a look at how we actually interact with parameters within a job here we have our standard

shell application that we've been working with in any of the other videos.

I have a spring good application.

This case is called Job parameters application calling the typical spring implication not run.

I've annotated this class with spring good application as rolls enabled batch processing so I've got

my hooks in there for batch processing as well as all the others Spring Boot magic.

Create a single Configuration class called Job configuration.

In this is the class that I'm going to use to actually build my job.

I've wired in the typical builders that we've seen in the past.

Again this is a single step job.

This seminal work from the bottom up explaining the code I start off with the job parameters job.

In this case it's a single step jobs so I'm just doing job builder factory does get job premier's job

that the job that start I started Step 1 and then called up built Step 1 is built like any other stuff

I use my step builder factory decade Step 1 it's a task what steps so I called a task split passing

it into reference to the task and then I'll be building then I called up build in this case you'll notice

I've got hello world task clit that method reference and I'm passing and no reason I'm passing in nulls

because this method is actually never going to be called This Way spring is going to use this essentially

as a reference to the being that is generated from the hello world task method which in this case is

just above it it's got some special configuration we haven't seen before it's still an app being method

so it's still going to be you've been definition I've edited the step scope annotation Now this is new

what the step scope annotation does is it provides a new scope that is available within Spring Batch

and what this does is it tells spring to instantiate this object once the step that is using it calls

it.

So unlike typical spring beings that are all created upon startup as singletons step scope beings are

lazily instantiated when the step that is using them is executed.

So in this case the hello world task that won't actually be instantiated on startup it'll be instantiated

when Step 1 is executed.

In the meantime a proxy is used in its place to satisfy the dependency we use the step scope on an object

being used within Spring Batch to allow that lazy initialization so that we can pass in job parameters

and other parameters that are available within Spring Batch.

In this case we're passing in a job parameter called message and we do that with this added value annotation

we'll add value then we use a spell expression that is hashtag open curly brace job parameters that's

indicating that we're looking for a job parameter and it's essentially referencing a value like you

wouldn't a map.

So in this case I'm looking for the message job parameter the value that is passed in for this will

be passed into this method as its variable message.

In this case I'm using a lambda expression for my task list but this is nothing more than implementing

the task with interface.

So this is my execute method.

In this case I'm just going to print out the value I get injected into my job I'll finish and that's

pretty much it.

So if we run this out of the box as is I don't pass on anything.

Let's see what happens.

I'll go out and build my project

build successful and I'll go out and run the job.

In this case we'll run it with no job parameters to start

so you can see Step 1 was executed and knows I'll put it I didn't pass in the message parameter so there

is nothing output.

Let's try running that again this time we actually pass a parameter you do that by simply specifying

parameter name equals value.

So in this case we'll do.

Message equals a low

and you can see this time.

Hello is output.

Now remember how I said that job parameters are used to identify job instances job instances can only

be run once to completion.

Since we just ran this job instance to completion if we try passing that same parameter again in orders

executing that same job instance again let's see what happens.

Well that's not good.

You'll see we got an exception.

The exception job instance already completed exception a job instance already exists and is complete

for parameters message equals Hello.

If you wanna run this trouble again change the parameters.

This is there for two reasons.

Number one it prevents you from rerunning the job again on accident.

And 2 it allows you to restart the job when a job fails.

So if I run a job instance and it fails I'll be able to pass in the same parameters and it will restart

where I left off.

We'll look at restart ability in a different video.