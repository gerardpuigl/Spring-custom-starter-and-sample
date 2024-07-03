package com.custom.starter.web.client;

import com.custom.starter.web.client.application.TestApplication;
import com.custom.starter.web.client.application.TestFeignClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import feign.FeignException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApplication.class,
//improve test performance
properties = "spring.cloud.openfeign.client.config.default.readTimeout=100")
public class FeignClientConfigurationTest {

  private static WireMockServer mockServer;

  @Autowired
  private TestFeignClient feignClient;

  @BeforeAll
  public static void beforeAll() {
    mockServer = new WireMockServer(8080);
    mockServer.start();
  }

  @BeforeEach
  public void beforeEach(){
    mockServer.resetRequests();
  }

  @AfterAll
  public static void afterAll() {
    mockServer.stop();
  }

  @Test
  @DisplayName("Test feign client without non retries.")
  public void feignClient_happyPath() {

    mockServer.stubFor(
        WireMock.get(urlPathMatching("/test"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("OK")
                    .withFixedDelay(90)));
    WireMock.verify(1,getRequestedFor(urlEqualTo("/test")));
    assertThat(feignClient.getInfo()).isEqualTo("OK");
  }

  @Test
  @DisplayName("Test feign client timout after 3 retries.")
  public void feignClient_TimeoutAndRetryer() {
    mockServer.resetRequests();
    mockServer.stubFor(
        WireMock
            .get(urlPathMatching("/test"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("OK")
                    .withFixedDelay(110)));

    Throwable throwable = catchThrowable(() -> feignClient.getInfo());

    WireMock.verify(3,getRequestedFor(urlEqualTo("/test")));
    assertThat(throwable).isInstanceOf(FeignException.class);
  }
}