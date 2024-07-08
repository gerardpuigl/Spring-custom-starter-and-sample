package com.ms.sample.adapter.outcome.persistence;

import com.ms.sample.adapter.outcome.persistence.dbo.SampleDbo;
import com.ms.sample.adapter.outcome.persistence.mapping.SampleDboMapper;
import com.ms.sample.adapter.outcome.persistence.repository.SampleJpaRepository;
import com.ms.sample.application.outcome.SamplePersistenceOutPort;
import com.ms.sample.domain.Sample;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SampleAdapter implements SamplePersistenceOutPort {

  private final SampleJpaRepository repository;
  private final SampleSpecificationFactory specificationFactory;
  private final SampleDboMapper mapper;

  @Override
  public Sample save(Sample sample) {
    SampleDbo sampleDbo = repository.save(mapper.toDbo(sample));
    return mapper.toDom(sampleDbo);
  }

  @Override
  public Optional<Sample> findById(UUID sampleId) {
    return repository.findById(sampleId).map(mapper::toDom);
  }

  @Override
  public Page<Sample> getSampleByParams(GetSampleParamRequest request, Pageable pageable) {
    return repository.findAll(specificationFactory.getSpecifications(request),pageable).map(mapper::toDom);
  }
}
