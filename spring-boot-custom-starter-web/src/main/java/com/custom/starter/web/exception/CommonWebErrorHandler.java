package com.custom.starter.web.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import com.custom.starter.web.exception.CustomRuntimeException.InvalidParam;
import com.custom.starter.web.exception.dto.ErrorDTO;
import com.custom.starter.web.exception.dto.InvalidParamDTO;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.MimeType;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class CommonWebErrorHandler {

  @Value("spring.application.name")
  private String applicationName;

  @ExceptionHandler(CustomRuntimeException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleCustomRuntimeException(CustomRuntimeException ex) {
    int status = ex.getStatus();
    if (status >= 500) {
      log.error("CustomRuntimeException caught:", ex);
    } else if (status >= 400) {
      log.warn("CustomRuntimeException caught:", ex);
    } else {
      log.info("CustomRuntimeException caught:", ex);
    }

    return buildResponseEntity(
        ex.getTitle(),
        ex.getDetail(),
        ex.getCode(),
        HttpStatus.valueOf(ex.getStatus()),
        toDTO(ex.getInvalidParams()));
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleAccessDeniedException(AccessDeniedException ex) {
    log.warn("AccessDeniedException caught:", ex);

    String title = ex.getClass().getName();
    String message = getMessage(ex);

    return buildResponseEntity(
        title, message, "unauthorized", HttpStatus.FORBIDDEN, Collections.EMPTY_LIST);
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleNoSuchElementException(NoSuchElementException ex) {
    log.warn("NoSuchElementException caught", ex);

    String title = ex.getClass().getName();
    String message = getMessage(ex);

    return buildResponseEntity(
        title, message, "not-found", HttpStatus.NOT_FOUND, Collections.EMPTY_LIST);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> NoHandlerFoundException(NoHandlerFoundException ex) {
    log.warn("NoHandlerFoundException caught", ex);

    String title = ex.getClass().getName();
    String message = getMessage(ex);

    return buildResponseEntity(
        title, message, "not-found", HttpStatus.NOT_FOUND, Collections.EMPTY_LIST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    log.warn("MethodArgumentNotValidException caught:", ex);

    String title = ex.getClass().getName();
    String message = getMessage(ex);

    BindingResult result = ex.getBindingResult();
    List<InvalidParamDTO> errorList =
        result.getFieldErrors().stream()
            .map(
                error ->
                    new InvalidParamDTO(
                        error.getField(),
                        error.getRejectedValue() + ": " + error.getDefaultMessage()))
            .collect(Collectors.toList());

    return buildResponseEntity(title, message, "bad-request", HttpStatus.BAD_REQUEST, errorList);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    log.warn("MethodArgumentTypeMismatchException caught:", ex);

    String title = ex.getClass().getName();
    String message = getMessage(ex);

    MethodParameter mp = ex.getParameter();
    List<InvalidParamDTO> errorList =
        Arrays.asList(
            new InvalidParamDTO(
                mp.getParameterName(),
                "Parameter type should be " + mp.getParameterType().getName()));

    return buildResponseEntity(title, message, "bad-request", HttpStatus.BAD_REQUEST, errorList);
  }

  @ExceptionHandler(ServletRequestBindingException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleServletRequestBindingException(
      ServletRequestBindingException ex) {
    log.warn("ServletRequestBindingException caught:", ex);

    String title = ex.getClass().getName();
    String message = getMessage(ex);

    return buildResponseEntity(
        title, message, "bad-request", HttpStatus.BAD_REQUEST, Collections.EMPTY_LIST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    log.warn("HttpMessageNotReadableException caught:", ex);

    String title = ex.getClass().toString();
    String message = getMessage(ex);

    return buildResponseEntity(
        title, message, "bad-request", HttpStatus.BAD_REQUEST, Collections.EMPTY_LIST);
  }

  @ExceptionHandler(InvalidFormatException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleInvalidFormatException(InvalidFormatException ex) {
    log.warn("InvalidFormatException caught:", ex);

    String title = ex.getClass().getName();
    String message = getMessage(ex);

    return buildResponseEntity(
        title, message, "bad-request", HttpStatus.BAD_REQUEST, Collections.EMPTY_LIST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleConstraintViolationException(
      ConstraintViolationException ex) {
    log.warn("ConstraintViolationException caught:", ex);

    String title = ex.getClass().getName();
    String message = getMessage(ex);

    Set<ConstraintViolation<?>> result = ex.getConstraintViolations();
    List<InvalidParamDTO> errorList =
        result.stream()
            .map(
                error ->
                    new InvalidParamDTO(
                        error.getPropertyPath().toString(),
                        ((PathImpl) error.getPropertyPath()).getLeafNode().getName()
                            + ": "
                            + error.getMessage()))
            .collect(Collectors.toList());

    return buildResponseEntity(title, message, "bad-request", HttpStatus.BAD_REQUEST, errorList);
  }

  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleConstraintViolationException( HttpMediaTypeNotAcceptableException ex) {
    log.warn("HttpMediaTypeNotAcceptableException caught:", ex);

    String title = ex.getClass().getName();
    String supportedMediaTypesString =
        ex.getSupportedMediaTypes().stream()
            .map(MimeType::toString)
            .collect(Collectors.joining(", "));
    String message = String.format("%s. Check 'Accept' header of your request. The accepted media types are: %s",
        getMessage(ex),supportedMediaTypesString);

    return buildResponseEntity(
        title, message, "bad-request", HttpStatus.BAD_REQUEST, Collections.EMPTY_LIST);
  }


  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleException(Exception ex) {
    log.error("Exception caught:", ex);

    String title = ex.getClass().getName();
    String message = getMessage(ex);

    return buildResponseEntity(
        title, message, "unexpected", HttpStatus.INTERNAL_SERVER_ERROR, Collections.EMPTY_LIST);
  }

  // Helpers

  protected ResponseEntity<ErrorDTO> buildResponseEntity(
      String title,
      String detail,
      String code,
      HttpStatus status,
      List<InvalidParamDTO> errorList) {
    ErrorDTO errorDTO =
        new ErrorDTO(title, detail, buildErrorCode(code), status.value(), errorList);
    log.info(errorDTO.toString());
    return ResponseEntity.status(status).body(errorDTO);
  }

  protected List<InvalidParamDTO> toDTO(List<InvalidParam> invalidParams) {
    List<InvalidParamDTO> invalidParamsDTO = new ArrayList<>();
    invalidParams.forEach(
        invalidParam -> {
          invalidParamsDTO.add(new InvalidParamDTO(invalidParam.name, invalidParam.reason));
        });
    return invalidParamsDTO;
  }

  protected String buildErrorCode(String code) {
    return applicationName + "-" + code;
  }

  protected String getMessage(Exception ex) {
    if (ex instanceof HttpMessageNotReadableException) {
      return "Request body is empty or malformed";
    } else {
      String message = ex.getMessage();
      if (message == null || message.isBlank()) {
        if (ex.toString() != null) {
          message = ex.toString();
        } else {
          message = "No detail message";
        }
      }
      return message;
    }
  }
}
