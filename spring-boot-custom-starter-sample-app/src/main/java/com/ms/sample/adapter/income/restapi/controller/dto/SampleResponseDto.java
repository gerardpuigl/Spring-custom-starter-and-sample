package com.ms.sample.adapter.income.restapi.controller.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record SampleResponseDto(
    UUID id,
    String name,
    String type,
    String description
) {

}
