package com.ms.sample.infraestructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"com.ms.sample.adapter.outcome.persistence.repository"})
@EntityScan({"com.ms.sample.adapter.outcome.persistence.dbo"})
@EnableTransactionManagement
public class JpaConfiguration {

}
