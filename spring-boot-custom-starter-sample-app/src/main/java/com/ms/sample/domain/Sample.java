package com.ms.sample.domain;

import com.custom.starter.web.exception.CustomRuntimeException;
import com.ms.sample.domain.enums.SampleProcessStatus;
import com.ms.sample.infraestructure.error.ErrorCodeEnum;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Builder(toBuilder = true)
@Getter
public class Sample {

  @Builder.Default
  private UUID id = UUID.randomUUID();
  private String name;
  private String description;
  @Builder.Default
  private SampleProcessStatus processStatus = SampleProcessStatus.ACCEPTED;
  @Builder.Default
  private int version = 0;

  public Sample(UUID id, String name, String description, SampleProcessStatus processStatus, int version) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.processStatus = processStatus;
    this.version = version;
    validate();
  }

  private void validate() {
    if (id == null) {
      invalidParam("Sample.id can not be null.", "Sample.id", "is null");
    }

    if (name == null) {
      invalidParam("Sample.name can not be null.", "Sample.name", "is null");
    }

    if (name != null && (name.isEmpty() || name.length() > 20)) {
      invalidParam("Sample.name must be between 1 and 20 characters length.", "Sample.name", "Wrong name size");
    }

    if (description != null && (description.isEmpty() || description.length() > 255)) {
      invalidParam("Sample.name must be between 1 and 255 characters length.", "Sample.description", "Wrong name size");
    }

    if (processStatus == null) {
      invalidParam("Sample.processStatus can not be null.", "Sample.processStatus", "is null");
    }
  }

  private void invalidParam(String message, String name, String reason) {
    CustomRuntimeException customRuntimeException =
        new CustomRuntimeException(
            ErrorCodeEnum.SAMPLE_DOMAIN_VALIDATION.getErrorCode(),
            message + " with id: " + id);
    customRuntimeException.addInvalidParam(name, reason);
    throw customRuntimeException;
  }
}
