package com.ms.sample.adapter.income.restapi.controller;

import com.custom.starter.web.exception.CustomRuntimeException;
import com.custom.starter.web.pagination.dto.PageResponseDto;
import com.ms.sample.adapter.income.restapi.controller.dto.SampleRequestDto;
import com.ms.sample.adapter.income.restapi.controller.dto.SampleResponseDto;
import com.ms.sample.adapter.income.restapi.controller.mapping.SampleControllerMapper;
import com.ms.sample.application.income.CreateSampleInPort;
import com.ms.sample.application.income.GetSamplesByParamsInPort;
import com.ms.sample.application.income.GetSamplesByParamsInPort.GetSampleParamRequest;
import com.ms.sample.domain.Sample;
import com.ms.sample.infraestructure.error.ErrorCodeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  private final GetSamplesByParamsInPort getSamplesByParamsInPort;
  private final SampleControllerMapper mapper;

  @PostMapping
  @PreAuthorize("hasAnyAuthority('SCOPE_sample:create')")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public SampleResponseDto createSample(@RequestBody @Valid SampleRequestDto sampleRequestDto) {
    Sample sample = createSampleInPort.createSample(mapper.toInPortRequest(sampleRequestDto));
    return mapper.toDto(sample);
  }

  @GetMapping
  @PreAuthorize("hasAnyAuthority('SCOPE_sample:read')")
  @ResponseStatus(HttpStatus.OK)
  public PageResponseDto<SampleResponseDto> getSamplesByParams(
      @RequestParam(name = "name", required = false) Optional<String> name,
      @RequestParam(name = "process_status", required = false) Optional<String> processStatus,
      @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber,
      @RequestParam(value = "page_size", defaultValue = "25", required = false) @Min(1) @Max(50) Integer pageSize,
      @RequestParam(name = "sort", defaultValue = "createdDate", required = false) String sort,
      @RequestParam(name = "order", defaultValue = "desc", required = false) String order) {


    GetSampleParamRequest request = new GetSampleParamRequest(name, processStatus);
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(getDirection(order), toCamelCase(sort)));

    Page<Sample> response = getSamplesByParamsInPort.execute(request, pageable);

    return mapper.toPageResponse(response);
  }

  private Direction getDirection(String order) throws CustomRuntimeException {
    try {
      return Direction.fromString(order);
    } catch (Exception e) {
      throw new CustomRuntimeException(
          ErrorCodeEnum.WRONG_PARAM.getErrorCode(),
          "Wrong order param provided, accepted params 'asc' or 'desc'");
    }
  }

  // in order to accept also snake case as sort property
  private String toCamelCase(String str) {
    return Pattern.compile("_([a-z,A-Z])").matcher(str).replaceAll(m -> m.group(1).toUpperCase());
  }


}
