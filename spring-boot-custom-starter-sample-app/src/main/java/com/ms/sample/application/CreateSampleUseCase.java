package com.ms.sample.application;

import com.ms.sample.application.income.CreateSampleInPort;
import com.ms.sample.application.mapping.CreateSampleMapper;
import com.ms.sample.application.outcome.SaveSampleOutPort;
import com.ms.sample.domain.Sample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateSampleUseCase implements CreateSampleInPort {

  private final CreateSampleMapper mapping;
  private final SaveSampleOutPort saveSampleOutPort;

  @Override
  @Transactional
  public Sample createSample(SampleRequest sampleRequest) {
    Sample sample = mapping.createSample(sampleRequest);
    return saveSampleOutPort.save(sample);
  }
}
