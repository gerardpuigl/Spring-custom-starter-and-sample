package com.ms.sample.adapter.income.restapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.sample.adapter.outcome.event.dto.SampleCommandDto;
import com.ms.sample.adapter.outcome.event.dto.SampleEventDto;
import com.ms.sample.infraestructure.main.SampleApplication;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SampleApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"test", "noconsumer"})
@Import({TestChannelBinderConfiguration.class})
class CreateSampleComponentTest {

  @Autowired(required = false)
  private MockMvc mockMvc;

  @Autowired(required = false)
  private OutputDestination output;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("Create complete Sample")
  @WithMockUser(authorities = {"SCOPE_sample:create"})
  public void createSample_Complete() throws Exception {
    //when:
    ResultActions response = mockMvc.perform(
        post("/sample")
            .content("""
                {
                  "name":"name",
                  "description":"test_description"
                }
                """)
            .contentType("application/json")
            .accept("application/json"));

    //then:
    response
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(content().json("""
              {
                "name":"name",
                "description":"test_description",
                "process_status":"ACCEPTED"
              }
            """));

    Message<byte[]> eventMessage = output.receive(1000, "event.sample");
    SampleEventDto eventPayload = objectMapper.convertValue(eventMessage.getPayload(), SampleEventDto.class);
    assertThat(eventPayload.name()).isEqualTo("name");
    assertThat(eventPayload.description()).isEqualTo("test_description");
    assertThat(eventPayload.processStatus()).isEqualTo("ACCEPTED");

    Message<byte[]> commandMessage = output.receive(1000, "command.process.sample");
    SampleCommandDto commandPayoad = objectMapper.convertValue(commandMessage.getPayload(), SampleCommandDto.class);
    assertThat(commandPayoad).isNotNull();

  }

  @Test
  @DisplayName("Post sample endpoint protected")
  @WithMockUser(authorities = {"SCOPE_wrong_scope"})
  public void createSample_endpointProtected() throws Exception {
    //when:
    ResultActions response = mockMvc.perform(
        post("/sample")
            .content("""
                {
                  "name":"name",
                  "description":"test_description",
                  "type":"IN_PROGRESS"
                }
                """)
            .contentType("application/json")
            .accept("application/json"));

    //then:
    response.andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Post dto constraint")
  @WithMockUser(authorities = {"SCOPE_sample:create"})
  public void createSample_constraints() throws Exception {
    //when:
    ResultActions response = mockMvc.perform(
        post("/sample")
            .content("""
                {
                }
                """)
            .contentType("application/json")
            .accept("application/json"));

    //then:
    response.andExpect(status().isBadRequest())
        .andExpect(content().json("""
                 {
                   "title":"MethodArgumentNotValidException",
                   "detail":"Error in fields: [name]",
                   "code":"spring.application.name-bad-request",
                   "status":400,
                   "invalid_params":[
                     {"name":"name", "reason":"null: must not be blank"}
                   ]
                 }
            """));
  }
}