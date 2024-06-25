package com.ms.sample.application.income;

import com.ms.sample.domain.Sample;

public interface CreateSampleInPort {

  Sample createSample(SampleRequest sampleRequest);


  record SampleRequest(
      String name,
      String description) {

  }
}