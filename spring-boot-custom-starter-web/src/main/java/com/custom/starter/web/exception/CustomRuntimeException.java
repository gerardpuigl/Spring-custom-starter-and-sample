package com.custom.starter.web.exception;

import com.custom.starter.web.exception.dto.ErrorDTO;
import com.custom.starter.web.exception.dto.InvalidParamDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CustomRuntimeException extends RuntimeException {

  private final String title;
  private final String detail;
  private final String code;
  private final int status;
  private final List<InvalidParam> invalidParams;

  @Data
  public class InvalidParam {
    final String name;
    final String reason;
  }

  /**
   * Base constructor to be used if we create a new exception as scratch
   *
   * @param errorCode
   * @param detail
   */
  public CustomRuntimeException(ErrorCode errorCode, String detail) {
    super(errorCode.getTitle());
    this.title = errorCode.getTitle();
    this.detail = detail;
    this.code = errorCode.getCode();
    this.status = errorCode.getStatus();
    this.invalidParams = new ArrayList<>();
  }

  /**
   * Constructor to be used if the client receives a an ErrorDTO from other ms-microService
   *
   * @param errorDTO
   */
  public CustomRuntimeException(ErrorDTO errorDTO) {
    super(errorDTO.getTitle());
    this.title = errorDTO.getTitle();
    this.detail = errorDTO.getDetail();
    this.code = errorDTO.getCode();
    this.status = errorDTO.getStatus();
    this.invalidParams = toInvalidParam(errorDTO.getInvalidParams());
  }

  /**
   * Constructor with exception cause
   *
   * @param errorCode
   * @param detail
   * @param cause
   */
  public CustomRuntimeException(ErrorCode errorCode, String detail, Throwable cause) {
    super(errorCode.getTitle(), cause);
    this.title = errorCode.getTitle();
    this.detail = detail;
    this.code = errorCode.getCode();
    this.status = errorCode.getStatus();
    this.invalidParams = new ArrayList<>();
  }

  /**
   * Add an InvalidParam with name and reason
   *
   * @param name
   * @param reason
   */
  public void addInvalidParam(String name, String reason) {
    invalidParams.add(new InvalidParam(name, reason));
  }

  /**
   * Transform InvalidParamDTO to InvalidParam
   *
   * @param listInvalidParamDTO
   * @return
   */
  private List<InvalidParam> toInvalidParam(List<InvalidParamDTO> listInvalidParamDTO) {
    List<InvalidParam> ips = new ArrayList<>();
    for (InvalidParamDTO invalidParamDTO : listInvalidParamDTO) {
      ips.add(new InvalidParam(invalidParamDTO.getName(), invalidParamDTO.getReason()));
    }
    return ips;
  }
}
