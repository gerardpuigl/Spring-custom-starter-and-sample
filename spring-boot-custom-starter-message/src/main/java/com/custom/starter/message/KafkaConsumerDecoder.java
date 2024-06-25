package com.custom.starter.message;


import com.custom.starter.message.domain.CommandMessage;
import com.custom.starter.message.domain.EventMessage;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaConsumerDecoder {

  public <T> EventMessage<T> decodeGenericMessageToEventMessage(Message<T> event) {
    return new EventMessage<>(
        (String) event.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC),
        (String) event.getHeaders().get("type"),
        (String) event.getHeaders().get(KafkaHeaders.RECEIVED_KEY),
        event.getPayload());
  }

  public <T> CommandMessage<T> decodeGenericMessageToCommandMessage(Message<T> event) {
    return new CommandMessage<>(
        (String) event.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC),
        (String) event.getHeaders().get("source"),
        (String) event.getHeaders().get(KafkaHeaders.RECEIVED_KEY),
        event.getPayload());
  }
}
