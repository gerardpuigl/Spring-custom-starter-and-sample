package com.ms.sample.domain;

import com.ms.sample.domain.enums.SampleType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@AllArgsConstructor
public class Sample {

  @Builder.Default
  private final UUID id = UUID.randomUUID();
  private String name;
  private String description;
  @Builder.Default
  private SampleType type = SampleType.ACCEPTED;
  private final Integer version;

}
