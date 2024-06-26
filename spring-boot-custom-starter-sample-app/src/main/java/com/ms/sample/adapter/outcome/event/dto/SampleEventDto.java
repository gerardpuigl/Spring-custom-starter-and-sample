package com.ms.sample.adapter.outcome.event.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record SampleEventDto(
    UUID id,
    String name,
    String processStatus,
    String description
) {

}
