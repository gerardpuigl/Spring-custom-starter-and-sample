# Custom Starter Web Module

Based on Spring Web and Security, this module contains common rest based microservices configuration, dependencies, security and error handling.

## What includes?
1. **Common dependencies** for web microservices.
2. **Custom runtime exception handling**. Provides a common exception handling to standardize the common error responses
3. **Jackson common configuration**.
4. **Security configuration**

## Setup
To start using this module you need to have it in your maven dependencies folder, if you don't have it use maven install
you have to add this dependency in your `pom.xml` file.

```xml
    <dependency>
      <groupId>com.starter.custom.common.library</groupId>
      <artifactId>spring-boot-application-name-web</artifactId>
    </dependency>
```

If you want to exclude the security configuration for any reason you need to do it explicitly including the following property:

```properties
custom.starter.web.security.enable=false
```


### Custom Runtime Exceptions
We recommend creating in your app an enum containing all the exceptions that you're going to instance in your app. This way you'll have an easy to manage list of all your managed error and it'll be easier to locate them. Example:

	public enum ErrorCodeEnum {
	
	  KC_CLIENT_JWT_INSTANCE(new ErrorCode("JWT Authorization error", "KC_Cln_1", 401)),
	  KC_CLIENT_EDIT(new ErrorCode("Keycloak edit error", "KC_Cln_2", 500)),
	  KC_ADAPTER_LOGIN_FAILURE(new ErrorCode("Keycloak login", "KA_1", 500)),
	
	
	  ErrorCode errorCode;
	
	  ErrorCodeEnum(ErrorCode errorCode) {
	    this.errorCode = errorCode;
	  }
	
	  public ErrorCode getErrorCode() {
	    return errorCode;
	  }
	}

The details of the error will be set when throwing the exception, to give more detailed information.
Throw the exception like this:

	throw new CustomRuntimeException(
	          ErrorCodeEnum.KC_ADAPTER_CREATE_USER_FAILURE.getErrorCode(),
	          "Email already exists: " + req.getEmail());
