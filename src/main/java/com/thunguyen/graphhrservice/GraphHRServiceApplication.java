package com.thunguyen.graphhrservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class GraphHRServiceApplication {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/graph/team-info").allowedOrigins("http://ec2-3-133-161-239.us-east-2.compute.amazonaws.com:3000")
//            .allowedMethods("GET", "POST");
//        registry.addMapping("/recommend").allowedOrigins("http://ec2-3-133-161-239.us-east-2.compute.amazonaws.com:3000")
//            .allowedMethods("GET", "POST");
        registry.addMapping("/**").allowedOrigins("http://ec2-3-133-161-239.us-east-2.compute.amazonaws.com:3000")
            .allowedMethods("GET", "POST");
      }
    };
  }

  public static void main(String[] args) {
    SpringApplication.run(GraphHRServiceApplication.class, args);
  }

}
