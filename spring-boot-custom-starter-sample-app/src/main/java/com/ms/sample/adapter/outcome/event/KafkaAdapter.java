package com.ms.sample.adapter.outcome.event;

import com.custom.starter.message.KafkaPublisher;
import com.custom.starter.message.domain.CommandMessage;
import com.custom.starter.message.domain.EventMessage;
import com.ms.sample.adapter.outcome.event.dto.SampleCommandDto;
import com.ms.sample.adapter.outcome.event.mapping.EventMapper;
import com.ms.sample.application.outcome.SampleEventOutPort;
import static com.ms.sample.application.outcome.SampleEventOutPort.EventType.SAMPLE_DELETED;
import com.ms.sample.domain.Sample;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAdapter implements SampleEventOutPort {

  @Value("${spring.application.name}")
  private String source;

  public static final String SAMPLE_EVENT = "producer-sample-event";
  public static final String SAMPLE_COMMAND = "producer-sample-command";

  private final KafkaPublisher kafkaPublisher;
  private final EventMapper mapper;

  @Override
  public void publishSampleEvent(Sample sample, EventType type) {
    publishEventTopic(sample.getId().toString(), type.getValue(), mapper.toSampleEventDto(sample));
  }

  @Override
  public void publishProcessSampleCommand(Sample sample) {
    SampleCommandDto payload = new SampleCommandDto(sample.getId());
    CommandMessage<SampleCommandDto> message =
        new CommandMessage<>(SAMPLE_COMMAND,source,sample.getId().toString(), payload);
    kafkaPublisher.send(message);
  }

  @Override
  public void publishDeleteSampleEvent(UUID sampleId) {
    publishEventTopic(sampleId.toString(), SAMPLE_DELETED.getValue(), KafkaNull.INSTANCE);
  }

  private <T> void publishEventTopic(String key, String eventType, T payload) {
    EventMessage<T> message = new EventMessage<>(SAMPLE_EVENT, eventType, key, payload);
    kafkaPublisher.send(message);
  }
}
