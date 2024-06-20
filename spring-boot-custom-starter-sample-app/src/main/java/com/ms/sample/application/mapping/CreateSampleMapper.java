package com.ms.sample.application.mapping;

import com.ms.sample.application.income.CreateSampleInPort.SampleRequest;
import com.ms.sample.domain.Sample;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateSampleMapper {

  Sample createSample(SampleRequest request);
}
