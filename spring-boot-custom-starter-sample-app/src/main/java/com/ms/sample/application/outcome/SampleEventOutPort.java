package com.ms.sample.application.outcome;

import com.ms.sample.domain.Sample;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface SampleEventOutPort {

  void publishSampleEvent(Sample sample, EventType type);
  void publishDeleteSampleEvent(UUID sampleId);

  @RequiredArgsConstructor
  @Getter
  enum EventType {
    SAMPLE_CREATED("sample.created"),
    SAMPLE_DELETED("sample.deleted");

    private final String value;
  }
}
