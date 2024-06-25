package com.ms.sample.adapter.income.restapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record SampleRequestDto(
    @NotBlank
    @Size(min = 1, max = 20)
    String name,
    @Size(max = 255)
    String description) {

}
