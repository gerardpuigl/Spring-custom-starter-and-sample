# Custom Starter Web Module

Contains common configurations and error handling for rest based microservices and rest and security spring boot dependencies.
This is used as starter for all our rest ms. 

## What includes?
1. **Common dependencies** for web microservices.
2. **Custom runtime exception handling**. Provides a common exception handling to standarize the common error responses
6. **Jackson common configuration**.

## Setup

To start using this module you have to add this dependency in your `pom.xml` file.

```xml
    <dependency>
      <groupId>com.starter.custom.common.library</groupId>
      <artifactId>spring-boot-application-name-web</artifactId>
    </dependency>
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
