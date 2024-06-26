# Custom Starter Message Module

Based on Spring cloud stream, this message module provide a common message domain entities for 
events and commands messages and Kafka publisher to guarantee that all our messages published in kafka have the same 
headers.

Also, if you are inside a @Transactional the message will be sent after the transaction is committed
successfully to avoid publish events for operations not committed in the database.

## What includes?
1. **Custom EventMessage**. Domain entity that contains all the mandatory metadata and data to send events messages.
2. **Custom CommandMessage**. Domain entity that contains all the mandatory metadata and data to send command messages.
3. **KafkaPublisher**. Publish the events with all the metadata and data provided.
4. **KafkaCustomerDecoder**. Map generic Spring Cloud messages to our Domain entities to be able to get the headers in the consumers.

## Setup

To start using this module you have to add this dependency in your `pom.xml` file.

```xml
    <dependency>
      <groupId>com.starter.custom.common.library</groupId>
      <artifactId>spring-boot-custom-starter-message</artifactId>
    </dependency>
```

### Project configuration
Add configuration for your publishers/producers:

	spring
	  cloud:
	    stream:
         bindings:
           your-topic-name:
             destination: event.entity
             content-type: application/json
             producer:
               useNativeEncoding: true
               
Then add the KafkaPublisher to your class and send the messages to this topic.

#### Environment vars needed:

* ```KAFKA_BOOTSTRAP_SERVERS```
* ```KAFKA_USER```
* ```KAFKA_PASSWORD```
* ```SCHEMA_REGISTRY_URL```

Defaults are defined for localhost usage.

### How to use it?

1.- Autowire or Inject a bean of ```KafkaPublisher```.

2.- Create a ```EventMessage<T>``` or ```CommandMessage<T>``` with all necessary metadata and T payload.
In case you want to send a delete message you need to send a ```org.springframework.kafka.support.KafkaNull.INSTANCE``` as a Payload
(Spring bridge standard).

3.- Use the ```KafkaPublisher.send(message)``` to send it. Remember, if you are inside a @Transactional
your message will be sent after the transaction is committed.


#### Component testing with messages
In order to do component tests, without the need of kafka running in your system, add the following dependency:

    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-stream-test-binder</artifactId>
      <scope>test</scope>
    </dependency>

Then, in your tests, use the following annotation in your test class:
	
	@Import({TestChannelBinderConfiguration.class}) 
	
And the following bean, where all the messages will go:
	
	@Autowired
	private OutputDestination output;	
	
You might need to clear the output (or do something else) after or before each test.

