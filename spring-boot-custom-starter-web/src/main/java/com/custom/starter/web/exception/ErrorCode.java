package com.custom.starter.web.exception;

import lombok.Data;

@Data
public class ErrorCode {

  private final String title;
  private final String code;
  private final int status;
}
