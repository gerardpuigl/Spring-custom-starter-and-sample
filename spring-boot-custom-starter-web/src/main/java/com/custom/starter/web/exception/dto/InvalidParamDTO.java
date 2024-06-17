package com.custom.starter.web.exception.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class InvalidParamDTO {

  private final String name;
  private final String reason;

  public InvalidParamDTO(String name, String reason) {
    this.name = name;
    this.reason = reason;
  }
}
