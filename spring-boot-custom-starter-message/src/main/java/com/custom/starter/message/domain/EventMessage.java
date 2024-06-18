package com.custom.starter.message.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventMessage<T> {
  private String topicBindingName;
  private String eventType;
  private String key;
  private T payload;

  public EventMessage(String topicBindingName, String eventType, String key, T payload) {
    this.topicBindingName = topicBindingName;
    this.eventType = eventType;
    this.key = key;
    this.payload = payload;
  }
}
