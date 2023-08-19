package com.thunguyen.graphhrservice.models;

import java.util.List;
import lombok.Data;

@Data
public class JobDto {

  private String name;
  private String roleName;
  private String projectName;
  private List<Rating> requires;
}
