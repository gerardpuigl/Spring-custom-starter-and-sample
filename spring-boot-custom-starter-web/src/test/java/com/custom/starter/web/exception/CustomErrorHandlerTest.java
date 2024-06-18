package com.custom.starter.web.exception;

import com.custom.starter.web.exception.CommonWebErrorHandler;
import com.custom.starter.web.exception.CustomRuntimeException;
import com.custom.starter.web.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.custom.starter.web.exception.dto.ErrorDTO;
import java.io.IOException;

import java.util.Collections;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.ServletRequestBindingException;

@TestInstance(Lifecycle.PER_CLASS)
public class CustomErrorHandlerTest {

  private CommonWebErrorHandler handler = new CommonWebErrorHandler();

  @BeforeAll
  public void BeforeAll() {
    ReflectionTestUtils.setField(handler, "applicationName", "application-name");
  }

  @Test
  public void testHandleException_OK() {
    ErrorDTO expectedDTO =
        new ErrorDTO(
            Exception.class.getName(),
            "test",
            "application-name-unexpected",
            500,
            Collections.EMPTY_LIST);
    Exception ex = new Exception("test");
    ResponseEntity<ErrorDTO> response;
    try {
      response = handler.handleException(ex);
      Assertions.assertEquals(500, response.getStatusCode().value());
      Assertions.assertEquals(expectedDTO, response.getBody());
    } catch (Exception e) {
      Assertions.fail("Exception not expected");
    }
  }

  @Test
  public void testHandleException_Empty_OK() {
    ErrorDTO expectedDTO =
        new ErrorDTO(
            Exception.class.getName(),
            "java.lang.Exception",
            "application-name-unexpected",
            500,
            Collections.EMPTY_LIST);
    Exception ex = new Exception();
    ResponseEntity<ErrorDTO> response;
    try {
      response = handler.handleException(ex);
      Assertions.assertEquals(500,  response.getStatusCode().value());
      Assertions.assertEquals(expectedDTO, response.getBody());
    } catch (Exception e) {
      Assertions.fail("Exception not expected");
    }
  }

  @Test
  public void testHandleException_Null_OK() {
    ErrorDTO expectedDTO =
        new ErrorDTO(
            Exception.class.getName(),
            "java.lang.Exception",
            "application-name-unexpected",
            500,
            Collections.EMPTY_LIST);
    Exception ex = new Exception((Throwable) null);
    ResponseEntity<ErrorDTO> response;
    try {
      response = handler.handleException(ex);
      Assertions.assertEquals(500,  response.getStatusCode().value());
      Assertions.assertEquals(expectedDTO, response.getBody());
    } catch (Exception e) {
      Assertions.fail("Exception not expected");
    }
  }

  @Test
  public void testHandleCustomRuntimeException_OK() {
    ErrorDTO expectedDTO =
        new ErrorDTO("title", "detail", "application-name-code", 422, Collections.EMPTY_LIST);
    CustomRuntimeException ex =
        new CustomRuntimeException(new ErrorCode("title", "code", 422), "detail");
    ResponseEntity<ErrorDTO> response;
    try {
      response = handler.handleCustomRuntimeException(ex);
      Assertions.assertEquals(422,  response.getStatusCode().value());
      Assertions.assertEquals(expectedDTO, response.getBody());
    } catch (Exception e) {
      Assertions.fail("Exception not expected");
    }
  }

  @Test
  public void testHandleNoSuchElementException() {
    ErrorDTO expectedDTO =
            new ErrorDTO(
                    NoSuchElementException.class.getName(),
                    "msg",
                    "application-name-not-found",
                    HttpStatus.NOT_FOUND.value(),
                    Collections.EMPTY_LIST);
    NoSuchElementException ex = new NoSuchElementException("msg");
    ResponseEntity<ErrorDTO> response;
    try {
      response = handler.handleNoSuchElementException(ex);
      Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),  response.getStatusCode().value());
      Assertions.assertEquals(expectedDTO, response.getBody());
    } catch (Exception e) {
      Assertions.fail("Exception not expected");
    }
  }

  @Test
  public void testHandleServletRequestBindingException_OK() {
    ErrorDTO expectedDTO =
        new ErrorDTO(
            ServletRequestBindingException.class.getName(),
            "msg",
            "application-name-bad-request",
            HttpStatus.BAD_REQUEST.value(),
            Collections.EMPTY_LIST);

    ServletRequestBindingException ex = new ServletRequestBindingException("msg");
    ResponseEntity<ErrorDTO> response;
    try {
      response = handler.handleServletRequestBindingException(ex);
      Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),  response.getStatusCode().value());
      Assertions.assertEquals(expectedDTO, response.getBody());
    } catch (Exception e) {
      Assertions.fail("Exception not expected");
    }
  }

  @Test
  public void testHandleInvalidFormatException_OK() throws IOException {
    ErrorDTO expectedDTO =
        new ErrorDTO(
            InvalidFormatException.class.getName(),
            "-",
            "application-name-bad-request",
            HttpStatus.BAD_REQUEST.value(),
            Collections.EMPTY_LIST);

    ObjectMapper mapper = new ObjectMapper();
    InvalidFormatException ex =
        new InvalidFormatException(mapper.createParser("string"), "msg", "value", Integer.TYPE);
    ResponseEntity<ErrorDTO> response;
    try {
      response = handler.handleInvalidFormatException(ex);
      Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),  response.getStatusCode().value());
      Assertions.assertEquals(expectedDTO.getCode(), response.getBody().getCode());
    } catch (Exception e) {
      Assertions.fail("Exception not expected");
    }
  }
}