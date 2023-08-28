package com.thunguyen.graphhrservice.configs;

import com.thunguyen.graphhrservice.properties.GraphDBProperty;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Logging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class GraphHRConfiguration {

  @Bean
  public Config neo4jConfig() {
    return Config.builder()
        .withConnectionTimeout(30, TimeUnit.SECONDS)
        .withMaxConnectionLifetime(30, TimeUnit.MINUTES)
        .withMaxConnectionPoolSize(10)
        .withConnectionAcquisitionTimeout(20, TimeUnit.SECONDS)
        .withFetchSize(1000)
        .withDriverMetrics()
        .withLogging(Logging.console(Level.INFO))
        .build();
  }

  @Bean
  public Driver neo4jDriver(GraphDBProperty property, Config neo4jConfig) {
    return GraphDatabase.driver(
        property.getUri(),
        AuthTokens.basic(property.getUsername(), property.getPassword()),
        neo4jConfig);
  }

  @Bean
  public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(2);
    threadPoolTaskScheduler.setThreadNamePrefix("CronjobOfflineTrigger");
    return threadPoolTaskScheduler;
  }
}
