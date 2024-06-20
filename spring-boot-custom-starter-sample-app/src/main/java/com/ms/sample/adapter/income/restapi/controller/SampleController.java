package com.ms.sample.adapter.income.restapi.controller;

import com.ms.sample.adapter.income.restapi.controller.dto.SampleRequestDto;
import com.ms.sample.adapter.income.restapi.controller.dto.SampleResponseDto;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RequestMapping(
    value = "/sample",
    headers = "Accept=application/json"
)
public class SampleController {

  @PostMapping
  @PreAuthorize("hasAnyAuthority('SCOPE_sample:book:create')")
  @ResponseStatus(HttpStatus.CREATED)
  public SampleResponseDto createSample(@RequestBody @Valid SampleRequestDto sampleRequestDto){
    //TDD first attempt
    return SampleResponseDto.builder()
        .id(UUID.randomUUID())
        .name(sampleRequestDto.name())
        .type(sampleRequestDto.type())
        .description(sampleRequestDto.description())
        .build();
  }

}
