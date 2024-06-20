package com.ms.sample.application;

import com.ms.sample.application.income.CreateSampleInPort;
import com.ms.sample.application.mapping.CreateSampleMapper;
import com.ms.sample.domain.Sample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSampleUseCase implements CreateSampleInPort {

  private final CreateSampleMapper mapping;

  @Override
  public Sample createSample(SampleRequest sampleRequest) {
    Sample sample = mapping.createSample(sampleRequest);
    return sample;
  }
}
