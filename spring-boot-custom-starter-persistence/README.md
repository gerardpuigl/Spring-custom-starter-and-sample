# Custom Starter Persistence Module

Based on Spring Jpa and Postgresql, contains persistence dependencies and a class to make the entities auditable.

## What includes?
1. **Persistence dependencies**
2. **AuditEntity** It needs to be extend from it to include Audit to manage automatically

## Setup

To start using this module you have to add this dependency in your `pom.xml` file.

```xml
    <dependency>
      <groupId>com.wefox.server.starter</groupId>
      <artifactId>wefox-server-ms-starter-persistence</artifactId>
    </dependency>
```

### AuditEntity

Entity to be used for each entity that has the columns created_date, last_modified_date and version.
Jpa Auditing is activated when you include this dependency. The version will avoid race conditions update and potential loss of information.

#### How to use it?

Just extend your EntityDbo class from this AuditEntity:

```
@Entity
public class YourEntityDbo extends AuditEntity {
...
```