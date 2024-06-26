# Sample Application that use the Custom Starter Modules

Based on Spring boot, this applications pretends to show how to implement some of the modules of this
repository, also is a portfolio about how I understand the hexagonal architecture.


## What includes?
1. **SampleController**. Rest controller to create Samples.
2. **SampleCommandConsumer**. Kafka consumer to process samples asynchronously.
3. **SampleAdapter**. Output Persistence Adapter persist data to Postgresql.
4. **KafkaAdapter**. Output Kafka Adapter to publish Kafka events ands commands.
5. **CreateSampleUseCase**. Handle the logic to create samples and publish events and command to process the samples later.
6. **StartProcessSampleUseCase**. Handle the logic to update the process stats to IN_PROGRESS and publish update event.
7. **ProcessSampleUseCase**. Handle the logic to process the sample and finish the process.

### How to run it?

1.- Be sure you have all modules installed in your .m2 maven local folder. See root README for more information.

2.- Run a docker container service and run the docker-compose.yml provided in this folder:

```shell
docker-compose up
```

3.- Run the application 

## Results:

Happy path, after some seconds the sample will past though 3 process_status: 
[ACCEPTED, IN_PROGRESS, PROCESSED] publishing an event each time.
```curl
curl --location 'localhost:8080/sample' \
--header 'Content-Type: application/json' \
--data '{
    "name":"Sample 01",
    "description":"Solid sample"
}'
````

Unhappy path, after some seconds the sample will past though 3 process_status: 
[ACCEPTED, IN_PROGRESS, FAILED] publishing an event each time.
```curl
curl --location 'localhost:8080/sample' \
--header 'Content-Type: application/json' \
--data '{
    "name":"Sample 02",
    "description":"be careful this sample is unstable"
}'
````

In a debug mode we can force some errors like Sample not found or Sample already processed (Concurrency scenario), this would force the command message to go to a dlq.