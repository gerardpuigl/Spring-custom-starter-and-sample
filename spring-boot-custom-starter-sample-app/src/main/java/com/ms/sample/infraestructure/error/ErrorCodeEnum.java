package com.ms.sample.infraestructure.error;

import com.custom.starter.web.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCodeEnum {
  SAMPLE_NOT_FOUND(new ErrorCode("Sample not found", "SP_01", 404)),
  UNEXPECTED_SAMPLE_NOT_FOUND(new ErrorCode("Unexpected sample not found", "SP_02", 500)),
  SAMPLE_PROCESS_ALREADY_INITIATED(new ErrorCode("The sample is already being processed", "SP_03", 409)),
  SAMPLE_PROCESS_FAILED(new ErrorCode("The sample process failed", "SP_04", 500));

  private final ErrorCode errorCode;

}
