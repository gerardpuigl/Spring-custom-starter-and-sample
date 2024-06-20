package com.ms.sample.adapter.income.restapi.controller.mapping;

import com.ms.sample.adapter.income.restapi.controller.dto.SampleRequestDto;
import com.ms.sample.adapter.income.restapi.controller.dto.SampleResponseDto;
import com.ms.sample.application.income.CreateSampleInPort.SampleRequest;
import com.ms.sample.domain.Sample;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SampleControllerMapper {

  SampleResponseDto toDto(Sample sample);

  SampleRequest toInPortRequest(SampleRequestDto requestDto);
}
