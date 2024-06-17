package com.custom.starter.web.exception.dto;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class ErrorDTO {

  private final String title;
  private final String detail;
  private final String code;
  private final int status;
  private final List<InvalidParamDTO> invalidParams;

  public ErrorDTO(
      String title, String detail, String code, int status, List<InvalidParamDTO> invalidParams) {

    this.title = title;
    this.detail = detail;
    this.code = code;
    this.status = status;
    this.invalidParams = invalidParams;
  }
}
