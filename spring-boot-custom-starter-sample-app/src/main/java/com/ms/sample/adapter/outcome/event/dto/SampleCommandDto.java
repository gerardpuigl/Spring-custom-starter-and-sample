package com.ms.sample.adapter.outcome.event.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record SampleCommandDto(
    UUID id
) {

}
