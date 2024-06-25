package com.custom.starter.message;

import com.custom.starter.message.domain.CommandMessage;
import com.custom.starter.message.domain.EventMessage;
import com.custom.starter.message.domain.TransactionalEventMessage;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPublisher {

  @Value("${spring.application.name}")
  private String applicationName;

  private final StreamBridge streamBridge;

  private final ApplicationEventPublisher applicationEventPublisher;

  public <T> void send(EventMessage<T> eventMessage) {

    Message<T> message =
        MessageBuilder.withPayload(eventMessage.getPayload())
            .setHeader(KafkaHeaders.KEY, eventMessage.getKey())
            .setHeader("created_at", String.valueOf(Instant.now().toEpochMilli()))
            .setHeader("created_by", applicationName)
            .setHeader("type", eventMessage.getEventType())
            .build();

    transactionalFilter(eventMessage.getTopicBindingName(), message);
  }

  public <T> void send(CommandMessage<T> commandMessage) {

    Message<T> message =
        MessageBuilder.withPayload(commandMessage.getPayload())
            .setHeader(KafkaHeaders.KEY, commandMessage.getKey())
            .setHeader("created_at", String.valueOf(Instant.now().toEpochMilli()))
            .setHeader("created_by", applicationName)
            .setHeader("source", commandMessage.getSource())
            .setHeader("type", commandMessage.getCommandType())
            .build();

    transactionalFilter(commandMessage.getTopicBindingName(), message);
  }

  private <T> void transactionalFilter(String topicName, Message<T> message) {

    // If we are in a transaction, instead of send we put in a EventList
    // and it will send after the transaction is committed successfully
    TransactionalEventMessage<T> transactionalEventMessage =
        new TransactionalEventMessage<>(this, topicName, message);
    applicationEventPublisher.publishEvent(transactionalEventMessage);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
  public <T> void sendAfterTransaction(TransactionalEventMessage<T> transactionalEvent){
    try {
      streamBridge.send(transactionalEvent.getTopicName(), transactionalEvent.getMessage());
    }catch (Exception ex){
      log.error(String.format("Error publishing message with key: %s published in topic: %s.",
              transactionalEvent.getMessage().getHeaders().get(KafkaHeaders.KEY),
              transactionalEvent.getTopicName()),
          ex);
      throw ex;
    }
  }
}
