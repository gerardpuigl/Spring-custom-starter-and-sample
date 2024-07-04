package com.ms.sample.adapter.income.event;

import com.custom.starter.message.KafkaConsumerDecoder;
import com.custom.starter.message.domain.CommandMessage;
import com.ms.sample.adapter.outcome.event.dto.SampleCommandDto;
import com.ms.sample.application.income.ProcessSampleInPort;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("consumer-sample-command")
@Slf4j
@AllArgsConstructor
public class SampleCommandConsumer implements Consumer<Message<SampleCommandDto>> {

  private final KafkaConsumerDecoder kafkaConsumerDecoder;
  private final ProcessSampleInPort processSampleInPort;

  @Override
  public void accept(Message<SampleCommandDto> event) {
    CommandMessage<SampleCommandDto> commandMessage = kafkaConsumerDecoder.decodeGenericMessageToCommandMessage(event);

      log.info("Sample command key: {} consumed to be processed.", commandMessage.getKey());
      processSampleInPort.execute(commandMessage.getPayload().id());
  }
}
