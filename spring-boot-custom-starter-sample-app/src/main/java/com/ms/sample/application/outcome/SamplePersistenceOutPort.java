package com.ms.sample.application.outcome;

import com.ms.sample.domain.Sample;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SamplePersistenceOutPort {

  Sample save(Sample sample);

  Optional<Sample> findById(UUID sampleId);

  Page<Sample> getSampleByParams(GetSampleParamRequest request, Pageable pageable);

  record GetSampleParamRequest(
      Optional<String> name,
      Optional<String> processStatus) {

  }
}
