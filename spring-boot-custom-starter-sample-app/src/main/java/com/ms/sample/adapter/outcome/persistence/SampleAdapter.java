package com.ms.sample.adapter.outcome.persistence;

import com.ms.sample.adapter.outcome.persistence.dbo.SampleDbo;
import com.ms.sample.adapter.outcome.persistence.mapping.SampleDboMapper;
import com.ms.sample.adapter.outcome.persistence.repository.SampleJpaRepository;
import com.ms.sample.application.outcome.SaveSampleOutPort;
import com.ms.sample.domain.Sample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SampleAdapter implements SaveSampleOutPort {

  private final SampleJpaRepository repository;
  private final SampleDboMapper mapper;

  @Override
  public Sample save(Sample sample) {
    SampleDbo sampleDbo = repository.save(mapper.toDbo(sample));
    return mapper.toDom(sampleDbo);
  }
}
