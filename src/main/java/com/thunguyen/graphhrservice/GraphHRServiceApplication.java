package com.thunguyen.graphhrservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class GraphHRServiceApplication {

  @Value("${ui-uri:http://localhost:3000}")
  private String uri;

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/graph/*").allowedOrigins(uri)
            .allowedMethods("GET", "POST");
        registry.addMapping("/recommend/*").allowedOrigins(uri)
            .allowedMethods("GET", "POST");
        registry.addMapping("/**").allowedOrigins(uri)
            .allowedMethods("GET", "POST");
      }
    };
  }

  public static void main(String[] args) {
    SpringApplication.run(GraphHRServiceApplication.class, args);
  }

}
