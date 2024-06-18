package com.custom.starter.message.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandMessage<T> {
  private String topicBindingName;
  private String source;
  private String commandType;
  private String key;
  private T payload;

  public CommandMessage(String topicBindingName, String source, String key, T payload) {
    this.topicBindingName = topicBindingName;
    this.source = source;
    this.commandType = payload.getClass().getSimpleName();
    this.key = key;
    this.payload = payload;
  }
}
