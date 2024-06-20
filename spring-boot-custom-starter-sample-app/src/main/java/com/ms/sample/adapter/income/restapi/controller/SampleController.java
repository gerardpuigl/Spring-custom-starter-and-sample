package com.ms.sample.adapter.income.restapi.controller;

import com.ms.sample.adapter.income.restapi.controller.dto.SampleRequestDto;
import com.ms.sample.adapter.income.restapi.controller.dto.SampleResponseDto;
import com.ms.sample.adapter.income.restapi.controller.mapping.SampleControllerMapper;
import com.ms.sample.application.income.CreateSampleInPort;
import com.ms.sample.domain.Sample;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(
    value = "/sample",
    headers = "Accept=application/json"
)
public class SampleController {

  private final CreateSampleInPort createSampleInPort;
  private final SampleControllerMapper mapper;

  @PostMapping
  @PreAuthorize("hasAnyAuthority('SCOPE_sample:book:create')")
  @ResponseStatus(HttpStatus.CREATED)
  public SampleResponseDto createSample(@RequestBody @Valid SampleRequestDto sampleRequestDto) {
    Sample sample = createSampleInPort.createSample(mapper.toInPortRequest(sampleRequestDto));
    return mapper.toDto(sample);
  }

}
