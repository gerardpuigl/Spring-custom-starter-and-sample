package com.ms.sample.application.outcome;

import com.ms.sample.domain.Sample;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface SampleEventOutPort {

  void publishSampleEvent(Sample sample, EventType type);
  void publishProcessSampleCommand(Sample sample);
  void publishDeleteSampleEvent(UUID sampleId);

  @RequiredArgsConstructor
  @Getter
  enum EventType {
    SAMPLE_CREATED("sample.created"),
    SAMPLE_UPDATED_IN_PROGRESS("sample.updated.in-progress"),
    SAMPLE_UPDATED_PROCESSED("sample.updated.processed"),
    SAMPLE_UPDATED_FAILED("sample.updated.failed"),
    SAMPLE_DELETED("sample.deleted");

    private final String value;
  }
}
