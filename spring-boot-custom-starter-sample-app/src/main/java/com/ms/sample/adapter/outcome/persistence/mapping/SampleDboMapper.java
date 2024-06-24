package com.ms.sample.adapter.outcome.persistence.mapping;

import com.ms.sample.adapter.outcome.persistence.dbo.SampleDbo;
import com.ms.sample.domain.Sample;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SampleDboMapper {

  @Mapping(target = "version", source = "version")
  SampleDbo toDbo(Sample sample);


  Sample toDom(SampleDbo sample);
}
