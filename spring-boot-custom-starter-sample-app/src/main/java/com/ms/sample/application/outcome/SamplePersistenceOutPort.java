package com.ms.sample.application.outcome;

import com.ms.sample.domain.Sample;
import java.util.Optional;
import java.util.UUID;

public interface SamplePersistenceOutPort {

  Sample save(Sample sample);

  Optional<Sample> findById(UUID sampleId);


}
