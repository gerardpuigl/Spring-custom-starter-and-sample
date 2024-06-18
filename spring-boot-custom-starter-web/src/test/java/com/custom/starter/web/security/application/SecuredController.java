package com.custom.starter.web.security.application;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE)
public class SecuredController {

  @GetMapping("/health")
  public String healthPublicEndpoint() {
    return "OK";
  }

  @GetMapping("/info")
  public String healthInfoEndpoint() {
    return "OK";
  }

  @GetMapping("/metrics")
  public String healthMetricsEndpoint() {
    return "OK";
  }

  @GetMapping("/default")
  public String securedEndpoint() {
    return "OK";
  }

  @GetMapping("/secured/scope")
  @PreAuthorize("hasAnyAuthority('SCOPE_test:scopes')")
  public String securedWithScopeEndpoint() {
    return "OK";
  }

}
