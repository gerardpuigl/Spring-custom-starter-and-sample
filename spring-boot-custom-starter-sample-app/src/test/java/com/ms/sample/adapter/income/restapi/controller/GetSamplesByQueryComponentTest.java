package com.ms.sample.adapter.income.restapi.controller;

import com.ms.sample.infraestructure.main.SampleApplication;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SampleApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"test"})
class GetSamplesByQueryComponentTest {

  @Autowired(required = false)
  private MockMvc mockMvc;


  @Test
  @DisplayName("Get samples by query params empty")
  @WithMockUser(authorities = {"SCOPE_sample:read"})
  @Sql(scripts = "/sql/SampleComponentTest.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  public void getSampleByQueryParamsEmpty_Complete() throws Exception {
    //when:
    ResultActions response = mockMvc.perform(
        get(new URI("/sample"))
            .accept("application/json"));

    //then:
    response
        .andExpect(status().isOk())
        .andExpect(content().json("""
        {
           "content":[
              {
                 "id":"63266b18-fe31-4e41-a409-208e9ae1d643",
                 "name":"Test Sample",
                 "process_status":"ACCEPTED",
                 "description":"unstable sample"
              },
              {
                 "id":"f6049dc9-08fe-490c-8bb2-3d8643c6edb4",
                 "name":"Sample Processed",
                 "process_status":"PROCESSED",
                 "description":"Solid sample"
              },
              {
                 "id":"e7f936b5-3fca-49d4-800d-6b16a7911c1d",
                 "name":"Test Sample",
                 "process_status":"ACCEPTED",
                 "description":"Solid sample"
              }
           ],
           "orders":[
              {
                 "property":"createdDate",
                 "direction":"DESC"
              }
           ],
           "total_pages":1,
           "total_elements":3,
           "page_number":0,
           "page_size":25
        }
            """));
  }

  @Test
  @DisplayName("Get samples by query all params")
  @WithMockUser(authorities = {"SCOPE_sample:read"})
  @Sql(scripts = "/sql/SampleComponentTest.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  public void getSampleByQueryAllParams_Complete() throws Exception {
    //when:
    ResultActions response = mockMvc.perform(
        get(new URI(
            "/sample?name=Test%20Sample&process_status=ACCEPTED&page_size=1&page_number=1&sort=last_modified_date"))
            .accept("application/json"));

    //then:
    response
        .andExpect(status().isOk())
        .andExpect(content().json("""
            {
                "content":[
                   {
                      "id":"e7f936b5-3fca-49d4-800d-6b16a7911c1d",
                      "name":"Test Sample",
                      "process_status":"ACCEPTED",
                      "description":"Solid sample"
                   }
                ],
                "orders":[
                   {
                      "property":"lastModifiedDate",
                      "direction":"DESC"
                   }
                ],
                "total_pages":2,
                "total_elements":2,
                "page_number":1,
                "page_size":1
             }
            """));
  }

  @Test
  @DisplayName("Get samples by name and find 1")
  @WithMockUser(authorities = {"SCOPE_sample:read"})
  @Sql(scripts = "/sql/SampleComponentTest.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  public void getSampleByName_Complete() throws Exception {
    //when:
    ResultActions response = mockMvc.perform(
        get(new URI("/sample?name=Sample%20Processed"))
            .accept("application/json"));

    //then:
    response
        .andExpect(status().isOk())
        .andExpect(content().json("""
            {
                "content":[
                   {
                      "id":"f6049dc9-08fe-490c-8bb2-3d8643c6edb4",
                      "name":"Sample Processed",
                      "process_status":"PROCESSED",
                      "description":"Solid sample"
                   }
                ],
                "orders":[
                   {
                      "property":"createdDate",
                      "direction":"DESC"
                   }
                ],
                "total_pages":1,
                "total_elements":1,
                "page_number":0,
                "page_size":25
             }
            """));
  }

  @Test
  @DisplayName("Get samples by name and find 0")
  @WithMockUser(authorities = {"SCOPE_sample:read"})
  @Sql(scripts = "/sql/SampleComponentTest.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  public void getSampleByName_NonExist() throws Exception {
    //when:
    ResultActions response = mockMvc.perform(
        get(new URI("/sample?name=Non%20Existing%20Name"))
            .accept("application/json"));

    //then:
    response
        .andExpect(status().isOk())
        .andExpect(content().json("""
            {
                "content":[],
                "orders":[
                   {
                      "property":"createdDate",
                      "direction":"DESC"
                   }
                ],
                "total_pages":0,
                "total_elements":0,
                "page_number":0,
                "page_size":25
             }
            """));
  }

  @Test
  @DisplayName("Get samples by process status and find 1")
  @WithMockUser(authorities = {"SCOPE_sample:read"})
  @Sql(scripts = "/sql/SampleComponentTest.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  public void getSampleByProcessStatus_OK() throws Exception {
    //when:
    ResultActions response = mockMvc.perform(
        get(new URI(
            "/sample?process_status=PROCESSED"))
            .accept("application/json"));

    //then:
    response
        .andExpect(status().isOk())
        .andExpect(content().json("""
            {
                "content":[
                   {
                      "id":"f6049dc9-08fe-490c-8bb2-3d8643c6edb4",
                      "name":"Sample Processed",
                      "process_status":"PROCESSED",
                      "description":"Solid sample"
                   }
                ],
                "orders":[
                   {
                      "property":"createdDate",
                      "direction":"DESC"
                   }
                ],
                "total_pages":1,
                "total_elements":1,
                "page_number":0,
                "page_size":25
             }
            """));
  }

  @Test
  @DisplayName("Get samples by process status and find 0")
  @WithMockUser(authorities = {"SCOPE_sample:read"})
  @Sql(scripts = "/sql/SampleComponentTest.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  public void getSampleByProcessStatus_NonExist() throws Exception {
    //when:
    ResultActions response = mockMvc.perform(
        get(new URI("/sample?process_status=Non%20Existing%20Name"))
            .accept("application/json"));

    //then:
    response
        .andExpect(status().isOk())
        .andExpect(content().json("""
            {
                "content":[],
                "orders":[
                   {
                      "property":"createdDate",
                      "direction":"DESC"
                   }
                ],
                "total_pages":0,
                "total_elements":0,
                "page_number":0,
                "page_size":25
             }
            """));
  }

  @Test
  @DisplayName("Get by query params endpoint protected")
  @WithMockUser(authorities = {"SCOPE_wrong_scope"})
  public void createSample_endpointProtected() throws Exception {
    //when:
    ResultActions response = mockMvc.perform(
        get("/sample").accept("application/json"));

    //then:
    response.andExpect(status().isForbidden());
  }


}