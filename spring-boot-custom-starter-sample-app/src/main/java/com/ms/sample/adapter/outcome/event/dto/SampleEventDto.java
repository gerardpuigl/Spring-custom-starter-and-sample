package com.ms.sample.adapter.outcome.event.dto;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import java.util.UUID;
import lombok.Builder;

@Builder
//Avoid consumer native deserialization problems. font: https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-json.html#json-schema-deserializer
@JsonSchemaInject(strings =
    {@JsonSchemaString(path="javaType", value="com.ms.sample.adapter.outcome.event.dto.SampleEventDto")})
public record SampleEventDto(
    UUID id,
    String name,
    String processStatus,
    String description
) {

}
