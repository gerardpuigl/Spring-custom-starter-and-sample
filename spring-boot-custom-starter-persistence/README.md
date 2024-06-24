# Wefox Starter Persistence Module

Add multitenancy configuration in Posgresql database. In the future may include
more libraries or configuration related with persistency.

## What includes?
1. **tenant-postgresql-spring-boot-starter**. Enables wefox passport propagation for postresql database in order to support multi tenancy.
2. **passport-context-threadlocal**. Enables basic passport context classes.

## Setup

To start using this module you have to add this dependency in your `pom.xml` file.

```xml
    <dependency>
      <groupId>com.wefox.server.starter</groupId>
      <artifactId>wefox-server-ms-starter-persistence</artifactId>
    </dependency>
```

### MultiTenantDbEntity

Entity to be used for each entity that has database tenant_id column. This will extract the
passport tenant_id from the context to tenant_id field in each persist operation.

#### How to use it?

Just extend your EntityDbo class from this MultiTenantDbEntity:

```
@Entity
public class PersonDbo extends MultiTenantDbEntity implements Serializable {
...
```