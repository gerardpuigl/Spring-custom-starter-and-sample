package com.ms.sample.adapter.income.restapi.controller.mapping;

import com.custom.starter.web.pagination.dto.PageOrderDto;
import com.custom.starter.web.pagination.dto.PageResponseDto;
import com.ms.sample.adapter.income.restapi.controller.dto.SampleRequestDto;
import com.ms.sample.adapter.income.restapi.controller.dto.SampleResponseDto;
import com.ms.sample.application.income.CreateSampleInPort.SampleRequest;
import com.ms.sample.domain.Sample;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@Mapper(componentModel = "spring")
public interface SampleControllerMapper {

  SampleResponseDto toDto(Sample sample);

  SampleRequest toInPortRequest(SampleRequestDto requestDto);

  @Mapping(target = "orders", source = "samples.sort")
  @Mapping(target = "pageNumber", source = "samples.pageable.pageNumber")
  @Mapping(target = "pageSize", source = "samples.pageable.pageSize")
  @Mapping(target = "content", source = "content", defaultExpression = "java(java.util.Collections.emptyList())")
  PageResponseDto<SampleResponseDto> toPageResponse(Page<Sample> samples);

  List<PageOrderDto> mapOrderDto(Sort sort);

  PageOrderDto toOrderDto(Order order);
}
