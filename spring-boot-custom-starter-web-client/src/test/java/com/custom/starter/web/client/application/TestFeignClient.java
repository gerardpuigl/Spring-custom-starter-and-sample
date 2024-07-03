package com.custom.starter.web.client.application;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "TestFeignClient", url = "localhost:8080/")
public interface TestFeignClient {

  @RequestMapping(method = RequestMethod.GET, value = "/test")
  String getInfo();
}