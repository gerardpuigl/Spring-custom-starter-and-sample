package com.custom.starter.message.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.messaging.Message;

@Getter
public class TransactionalEventMessage<T> extends ApplicationEvent {

  private final Message<T> message;
  private final String topicName;

  public TransactionalEventMessage(Object source, String topicName, Message<T> message) {
    super(source);
    this.topicName = topicName;
    this.message = message;
  }
}
