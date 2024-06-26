# Custom Microservice Starter

## Introduction
The purpose of this repository is to develop some common libraries as example that can be imported as dependency in each microservice,
this libraries may include common configurations and/or common classes to standardize some aspects of our microservices.

I included a sample app that use them as example.

## :floppy_disk: Modules available:

### **Custom Starter Web Module**

Based on Spring Web and Security, this module contains common rest based microservices configuration, dependencies, security and error handling.

[More info.](spring-boot-custom-starter-web/README.md)

### **Custom Starter Message Module**

Based on Spring cloud stream, this message module provide a common message entities and publisher
to guarantee that our events have the same metadata, also include a system to link the transactional status to message sender.

[More info.](spring-boot-custom-starter-message/README.md)

### **Custom Starter Web Client Module**

Based on spring openfeign, added libraries and classes related with spring rest client and its configuration.

[More info.](spring-boot-custom-starter-web-client/README.md)

### **Custom Starter Persistence Module**

Based on Spring Jpa and Postgresql, contains persistence dependencies and a class to make the entities auditable.

[More info.](spring-boot-custom-starter-persistence/README.md)

### **Sample Application that use the Custom Starter Modules*

Based on Spring boot, this applications pretends to show how to implement some of the modules on this
repository also is an example about how I understand the hexagonal architecture.

[More info.](spring-boot-custom-starter-sample-app/README.md)

### Test in local

Check you have java 21:
```shell
java --version
```

Install common dependencies in your local maven repository:
```shell
./mvnw clear install
```


### Release process

In a business context this would be build and upload to a private common repository to be used by all the projects related.