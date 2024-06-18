package com.custom.starter.message;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.custom.starter.message.domain.CommandMessage;
import com.custom.starter.message.domain.EventMessage;
import java.nio.charset.StandardCharsets;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.BDDMockito;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import org.mockito.Captor;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.transaction.PseudoTransactionManager;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBootTest(classes = {TestApplication.class, PseudoTransactionManager.class},
    properties = "spring.application.name=ms-name")
class KafkaPublisherTest {

  @MockBean
  private StreamBridge streamBridge;

  @Autowired
  private PlatformTransactionManager transactionManager;

  @Autowired
  private KafkaPublisher kafkaPublisher;

  public static String BINDING_TOPIC_NAME = "binding-test-topic";
  public static String EVENT_TYPE = "event.customer.test";
  public static String COMMAND_SOURCE = "test-command-class";
  public static String COMMAND_TYPE = "String";
  public static String KEY = "c32b6d1d-1343-4986-9eaa-08020321357a";
  public static String APP_NAME = "ms-name";

  @Captor
  private ArgumentCaptor<GenericMessage> messageCaptor;

  @Test
  @DisplayName("Send event message inside transaction successfully")
  public void test_send_event_message_inside_transaction_ok() {

    // initiate transaction manually
    TransactionStatus status =
        transactionManager.getTransaction(new DefaultTransactionDefinition());

    // given EventMessage
    EventMessage event = getEvent();

    // when trigger the method send
    kafkaPublisher.send(event);

    // then @TransactionalEventListener don't sent event before finish transaction
    verify(streamBridge, times(0))
        .send(any(), any());

    // when commit transaction
    transactionManager.commit(status);

    // then @TransactionalEventListener sent kafka event
    verify(streamBridge, times(1))
        .send(eq(BINDING_TOPIC_NAME), messageCaptor.capture());

    // then verify message sent
    Message message = messageCaptor.getValue();
    assertEquals(message.getPayload(), event.getPayload());

    // then verify message headers sent
    MessageHeaders messageHeaders = message.getHeaders();
    assertNotNull(messageHeaders.getId());
    assertNotNull(messageHeaders.get("created_at"));
    assertEquals(decodeToString(messageHeaders.get("created_by")), APP_NAME);
    assertEquals(decodeToString(messageHeaders.get("type")), EVENT_TYPE);
    assertEquals(messageHeaders.get(KafkaHeaders.KEY), KEY);
  }

  @Test
  @DisplayName("Send command message inside transaction successfully")
  public void test_send_command_message_inside_transaction_ok() {

    // initiate transaction manually
    TransactionStatus status =
        transactionManager.getTransaction(new DefaultTransactionDefinition());

    // given CommandMessage
    CommandMessage commandMessage = getCommandMessage();

    // when trigger the method send
    kafkaPublisher.send(commandMessage);

    // then @TransactionalEventListener don't sent event before finish transaction
    verify(streamBridge, times(0))
        .send(any(), any());

    // when commit transaction
    transactionManager.commit(status);

    // then @TransactionalEventListener sent kafka event
    verify(streamBridge, times(1))
        .send(eq(BINDING_TOPIC_NAME), messageCaptor.capture());

    // then verify message sent
    Message<CommandMessage> message = messageCaptor.getValue();
    assertEquals(message.getPayload(), commandMessage.getPayload());

    // then verify message headers sent
    MessageHeaders messageHeaders = message.getHeaders();
    assertNotNull(messageHeaders.getId());
    assertNotNull(messageHeaders.get("created_at"));
    assertEquals(decodeToString(messageHeaders.get("created_by")), APP_NAME);
    assertEquals(decodeToString(messageHeaders.get("source")), COMMAND_SOURCE);
    assertEquals(decodeToString(messageHeaders.get("type")), COMMAND_TYPE);
    assertEquals(messageHeaders.get(KafkaHeaders.KEY), KEY);
  }

  @Test
  @DisplayName("Event is sent even when there is no transaction")
  public void test_send_event_message_outside_transaction_ok() {

    // given EventMessage
    EventMessage event = getEvent();

    // when trigger the method send
    kafkaPublisher.send(event);

    // then @TransactionalEventListener sent kafka event anyway
    verify(streamBridge, times(1))
        .send(eq(BINDING_TOPIC_NAME), messageCaptor.capture());

    // then verify message sent
    Message message = messageCaptor.getValue();
    assertEquals(message.getPayload(), event.getPayload());
  }

  @Test
  @DisplayName("Transaction is committed but send process fails after commit")
  public void test_send_inside_transaction_exception_ko() {
    // given
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    Logger logger = (Logger) LoggerFactory.getLogger(KafkaPublisher.class);
    logger.addAppender(listAppender);

    //initiate transaction manually
    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

    given(streamBridge.send(any(), any())).willThrow(new RuntimeException());

    //when
    kafkaPublisher.send(getEvent());

    //then
    // verify send method was NOT called before the commit
    verify(streamBridge, times(0))
        .send(any(), any());

    assertDoesNotThrow(() -> transactionManager.commit(status));

    // verify send method was called after the commit
    verify(streamBridge, times(1))
        .send(any(), any());

    // verify error happened during the send process and it was logged
    assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
    assertThat(listAppender.list.get(0).getMessage()).contains("Error publishing message with key");
  }

  @Test
  @DisplayName("No event is sent when there is an exception during commit")
  public void test_send_inside_transaction_exception_ko_2() {
    // given
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    Logger logger = (Logger) LoggerFactory.getLogger(KafkaPublisher.class);
    logger.addAppender(listAppender);

    transactionManager = spy(new PseudoTransactionManager());

    //initiate transaction manually
    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

    kafkaPublisher.send(getEvent());

    // verify send method was NOT called before the commit
    verify(streamBridge, times(0))
        .send(any(), any());

    //make the commit fail
    willThrow(RuntimeException.class).given(transactionManager).commit(any());

    catchThrowable(() -> transactionManager.commit(status));

    // verify send method was never called
    BDDMockito.then(streamBridge).shouldHaveNoInteractions();

    // verify no logging
    assertThat(listAppender.list).hasSize(0);
  }

  @Test
  @DisplayName("Event is sent even when there is no transaction, send fails and is logged")
  public void test_exception_outside_transaction_ko() {
    //given
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    Logger logger = (Logger) LoggerFactory.getLogger(KafkaPublisher.class);
    logger.addAppender(listAppender);

    EventMessage event = getEvent();

    //when streamBridge try to send return exception
    when(streamBridge.send(any(), any())).thenThrow(new RuntimeException());

    // Then trigger the method send
    assertThrows(RuntimeException.class, () -> kafkaPublisher.send(event));
    // verify error happened during the send process and it was logged
    assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
    assertThat(listAppender.list.get(0).getMessage()).contains("Error publishing message with key");

  }

  private EventMessage getEvent() {
    return new EventMessage(BINDING_TOPIC_NAME, EVENT_TYPE, KEY, KEY);
  }

  private CommandMessage getCommandMessage() {
    return new CommandMessage(BINDING_TOPIC_NAME, COMMAND_SOURCE, KEY, KEY);
  }

  private String decodeToString(Object created_by) {
    return new String((byte[]) created_by, StandardCharsets.UTF_8);
  }
}
