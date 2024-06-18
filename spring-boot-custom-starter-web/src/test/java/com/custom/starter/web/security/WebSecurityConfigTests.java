package com.custom.starter.web.security;

import com.custom.starter.web.security.application.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
public class WebSecurityConfigTests {

  @Autowired private MockMvc mvc;

  @Test
  void healthEndpoint_withNoAuthenticatedUserRequest() throws Exception {
    mvc.perform(get("/health").contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk());
  }

  @Test
  void infoEndpoint_withNoAuthenticatedUserRequest() throws Exception {
    mvc.perform(get("/info").contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk());
  }

  @Test
  void metricsEndpoint_withNoAuthenticatedUserRequest() throws Exception {
    mvc.perform(get("/metrics").contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk());
  }

  @Test
  void defaultEndpoint_withNoAuthenticatedUserRequest() throws Exception {
    mvc.perform(get("/default").contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser()
  void defaultEndpoint_withAuthenticatedUserRequest() throws Exception {
    mvc.perform(get("/default").contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(authorities = {"SCOPE_invalid"})
  void defaultSecuredScopeEndpoint_withInvalidScope() throws Exception {
    mvc.perform(get("/secured/scope").contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"SCOPE_test:scopes"})
  void defaultSecuredScopeEndpoint_withValidScope() throws Exception {
    mvc.perform(get("/secured/scope").contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk());
  }
}
