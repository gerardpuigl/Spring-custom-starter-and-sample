package com.gerardpuig.ms.example;

import org.springframework.boot.SpringApplication;

public class TestStarterAndExampleApplication {

  public static void main(String[] args) {
    SpringApplication.from(MsExampleApplication::main).with(TestcontainersConfiguration.class).run(args);
  }

}
