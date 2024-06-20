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
import lombok.RequiredArgsConstructor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApplication.class,
properties = "spring.cloud.openfeign.client.config.default.readTimeout=100")
@RequiredArgsConstructor
public class FeignClientConfigurationTest {

  protected static WireMockServer mockServer;

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
  public void feignClient_happyPath() {

    mockServer.stubFor(
        WireMock.get(urlPathMatching("/info"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("OK")
                    .withFixedDelay(90)));
    assertThat(feignClient.getInfo()).isEqualTo("OK");
  }

  @Test
  public void feignClient_TimeoutAndRetryer() {
    mockServer.resetRequests();
    mockServer.stubFor(
        WireMock
            .get(urlPathMatching("/info"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("OK")
                    .withFixedDelay(110)));

    Throwable throwable = catchThrowable(() -> feignClient.getInfo());

    WireMock.verify(3,getRequestedFor(urlEqualTo("/info")));
    assertThat(throwable).isInstanceOf(FeignException.class);
  }





}