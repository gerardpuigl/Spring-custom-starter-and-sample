package com.ms.sample.adapter.outcome.event;

import com.custom.starter.message.KafkaPublisher;
import com.custom.starter.message.domain.EventMessage;
import com.ms.sample.adapter.outcome.event.mapping.EventMapper;
import com.ms.sample.application.outcome.SampleEventOutPort;
import static com.ms.sample.application.outcome.SampleEventOutPort.EventType.SAMPLE_DELETED;
import com.ms.sample.domain.Sample;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAdapter implements SampleEventOutPort {

  public static final String SAMPLE_TOPIC = "producer-sample-topic";

  private final KafkaPublisher kafkaPublisher;
  private final EventMapper mapper;

  @Override
  public void publishSampleEvent(Sample sample, EventType type) {
    publishEventTopic(sample.getId().toString(), type.getValue(), mapper.toSampleEventDto(sample));
  }

  @Override
  public void publishDeleteSampleEvent(UUID sampleId) {
    publishEventTopic(sampleId.toString(), SAMPLE_DELETED.getValue(), KafkaNull.INSTANCE);
  }

  private <T> void publishEventTopic(String key, String eventType, T payload) {
    EventMessage<T> message = new EventMessage<>(SAMPLE_TOPIC, eventType, key, payload);
    kafkaPublisher.send(message);
  }
}
