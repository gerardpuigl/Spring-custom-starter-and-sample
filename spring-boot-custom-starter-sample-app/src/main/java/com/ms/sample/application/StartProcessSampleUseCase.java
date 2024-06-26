package com.ms.sample.application;


import com.custom.starter.web.exception.CustomRuntimeException;
import com.ms.sample.application.income.StartProcessSampleInPort;
import com.ms.sample.application.outcome.SampleEventOutPort;
import com.ms.sample.application.outcome.SampleEventOutPort.EventType;
import com.ms.sample.application.outcome.SamplePersistenceOutPort;
import com.ms.sample.domain.Sample;
import com.ms.sample.domain.enums.SampleProcessStatus;
import com.ms.sample.infraestructure.error.ErrorCodeEnum;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartProcessSampleUseCase implements StartProcessSampleInPort {

  private final SamplePersistenceOutPort persistence;
  private final SampleEventOutPort sampleEventOutPort;

  @Override
  @Transactional
  public void execute(UUID sampleId) {

    Sample sample = getSample(sampleId);
    sample = updateSampleStatus(sample);
    sampleEventOutPort.publishSampleEvent(sample, EventType.SAMPLE_UPDATED_IN_PROGRESS);

  }

  private Sample updateSampleStatus(Sample sample) {
    sample.setProcessStatus(SampleProcessStatus.IN_PROGRESS);
    sample = persistence.save(sample);
    return sample;
  }

  private Sample getSample(UUID sampleId) {
    return persistence.findById(sampleId)
        .orElseThrow(() -> new CustomRuntimeException(
            ErrorCodeEnum.SAMPLE_NOT_FOUND.getErrorCode(),
            "Sample with id %s not found, this should never happen.".formatted(sampleId)));
  }
}
