package com.ms.sample.adapter.outcome.event.mapping;

import com.ms.sample.adapter.outcome.event.dto.SampleEventDto;
import com.ms.sample.domain.Sample;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {

  SampleEventDto toSampleEventDto(Sample sample);

}
