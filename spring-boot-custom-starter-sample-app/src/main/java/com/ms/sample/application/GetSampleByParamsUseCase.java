package com.ms.sample.application;

import com.ms.sample.application.income.GetSamplesByParamsInPort;
import com.ms.sample.application.outcome.SamplePersistenceOutPort;
import com.ms.sample.domain.Sample;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSampleByParamsUseCase implements GetSamplesByParamsInPort {

  private final SamplePersistenceOutPort samplePersistenceOutPort;

  @Override
  public Page<Sample> execute(GetSampleParamRequest request, Pageable pageable) {
    return samplePersistenceOutPort.getSampleByParams(
        new SamplePersistenceOutPort.GetSampleParamRequest(
            request.name(),
            request.processStatus()),
        pageable);
  }
}
