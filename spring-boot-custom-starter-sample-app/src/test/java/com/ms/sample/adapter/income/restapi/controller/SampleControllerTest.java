package com.ms.sample.adapter.income.restapi.controller;

import com.ms.sample.infraestructure.main.SampleApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@ActiveProfiles(profiles = {"test"})
class SampleControllerTest {

  @Autowired(required = false)
  private MockMvc mockMvc;

  @Test
  @DisplayName("Create complete Sample")
  @WithMockUser(authorities = {"SCOPE_sample:book:create"})
  public void createSample_Complete() throws Exception {
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
    response
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(content().json("""
              {
                "name":"name",
                "description":"test_description",
                "type":"IN_PROGRESS"
              }
            """));
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
  @WithMockUser(authorities = {"SCOPE_sample:book:create"})
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
                   "detail":"Error in fields: [name, type]",
                   "code":"spring.application.name-bad-request",
                   "status":400,
                   "invalid_params":[
                     {"name":"name", "reason":"null: must not be blank"},
                     {"name":"type", "reason":"null: must not be blank"}
                   ]
                 }
            """));
  }
}