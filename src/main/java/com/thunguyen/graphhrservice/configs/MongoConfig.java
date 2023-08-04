package com.thunguyen.graphhrservice.configs;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.thunguyen.graphhrservice.configs.properties.SimGraphDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableMongoRepositories(basePackages = "com.thunguyen.graphhrservice.repositories")
public class MongoConfig extends AbstractMongoClientConfiguration {

  @Autowired
  private SimGraphDatasource simGraphDatasource;

  @Override
  protected String getDatabaseName() {
    return simGraphDatasource.getOfflineSimDBName();
  }

  @Override
  public MongoClient mongoClient() {
    ConnectionString connectionString = new ConnectionString(simGraphDatasource.getOfflineSimDBUrl());
    MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .build();
    return MongoClients.create(mongoClientSettings);
  }

  @Override
  public Collection getMappingBasePackages() {
    return Collections.singleton("com.thunguyen");
  }
}
