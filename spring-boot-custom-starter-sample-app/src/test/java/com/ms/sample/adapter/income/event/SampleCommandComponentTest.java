package com.ms.sample.adapter.income.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.sample.adapter.outcome.event.dto.SampleCommandDto;
import com.ms.sample.adapter.outcome.event.dto.SampleEventDto;
import com.ms.sample.infraestructure.main.SampleApplication;
import java.util.UUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(classes = SampleApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"test"})
@Import({TestChannelBinderConfiguration.class})
class SampleCommandComponentTest {

  @Autowired
  private InputDestination inputDestination;

  @Autowired
  private OutputDestination outputDestination;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void cleanEvents() {
    outputDestination.clear();
  }

  @Test
  @DisplayName("Consumer Sample Command that is not processed")
  @Sql(scripts = "/sql/SampleComponentTest.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  public void consumeCommand_OK() {
    //given:
    UUID sampleId = UUID.fromString("e7f936b5-3fca-49d4-800d-6b16a7911c1d");
    SampleCommandDto payload = new SampleCommandDto(sampleId);

    Message<SampleCommandDto> message = MessageBuilder.withPayload(payload)
        .setHeader(KafkaHeaders.RECEIVED_TOPIC, "command.process.sample")
        .setHeader(KafkaHeaders.RECEIVED_KEY, sampleId.toString())
        .setHeader("source", "test")
        .build();

    //when:
    inputDestination.send(message);

    //then:
    Message<byte[]> inProgressMessage = outputDestination.receive(1000, "event.sample");
    SampleEventDto inProgressPayload = objectMapper.convertValue(inProgressMessage.getPayload(), SampleEventDto.class);
    assertThat(inProgressPayload.id()).isEqualTo(sampleId);
    assertThat(inProgressPayload.processStatus()).isEqualTo("IN_PROGRESS");

    Message<byte[]> processedMessage = outputDestination.receive(5100, "event.sample");
    SampleEventDto processedPayload = objectMapper.convertValue(processedMessage.getPayload(), SampleEventDto.class);
    assertThat(processedPayload.id()).isEqualTo(sampleId);
    assertThat(processedPayload.processStatus()).isEqualTo("PROCESSED");

  }

  @Test
  @DisplayName("Consumer Sample Command that process fails")
  @Sql(scripts = "/sql/SampleComponentTest.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  public void consumeCommand_Failed() {
    //given:
    UUID sampleId = UUID.fromString("63266b18-fe31-4e41-a409-208e9ae1d643");
    SampleCommandDto payload = new SampleCommandDto(sampleId);

    Message<SampleCommandDto> message = MessageBuilder.withPayload(payload)
        .setHeader(KafkaHeaders.RECEIVED_TOPIC, "command.process.sample")
        .setHeader(KafkaHeaders.RECEIVED_KEY, sampleId.toString())
        .setHeader("source", "test")
        .build();

    //when:
    inputDestination.send(message);

    //then:
    Message<byte[]> inProgressMessage = outputDestination.receive(1000, "event.sample");
    SampleEventDto inProgressPayload = objectMapper.convertValue(inProgressMessage.getPayload(), SampleEventDto.class);
    assertThat(inProgressPayload.id()).isEqualTo(sampleId);
    assertThat(inProgressPayload.processStatus()).isEqualTo("IN_PROGRESS");

    Message<byte[]> processedMessage = outputDestination.receive(5100, "event.sample");
    SampleEventDto processedPayload = objectMapper.convertValue(processedMessage.getPayload(), SampleEventDto.class);
    assertThat(processedPayload.id()).isEqualTo(sampleId);
    assertThat(processedPayload.processStatus()).isEqualTo("FAILED");

  }


  @Test
  @DisplayName("Consumer Sample Command that already consumed and processed")
  @Sql(scripts = "/sql/SampleComponentTest.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  public void consumeCommand_already_processed() {
    //given:
    UUID sampleId = UUID.fromString("f6049dc9-08fe-490c-8bb2-3d8643c6edb4");
    SampleCommandDto payload = new SampleCommandDto(sampleId);

    Message<SampleCommandDto> message = MessageBuilder.withPayload(payload)
        .setHeader(KafkaHeaders.RECEIVED_TOPIC, "command.process.sample")
        .setHeader(KafkaHeaders.RECEIVED_KEY, sampleId.toString())
        .setHeader("source", "test")
        .build();

    //when:
    inputDestination.send(message);

    //then:
    Message<byte[]> inProgressMessage = outputDestination.receive(1000, "event.sample");
    assertThat(inProgressMessage).isNull();

  }
}