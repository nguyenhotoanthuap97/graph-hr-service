package com.thunguyen.graphhrservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "graph.db")
public class GraphDBProperty {

  private String uri;
  private String username;
  private String password;
}
