package com.thunguyen.graphhrservice.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mongo-hr")
public class SimGraphDatasource {

  private String offlineSimDBUrl;
  private String offlineSimDBName;
}
