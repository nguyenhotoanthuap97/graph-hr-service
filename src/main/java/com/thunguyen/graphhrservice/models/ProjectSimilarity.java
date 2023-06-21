package com.thunguyen.graphhrservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectSimilarity {
  private String employeeId;
  private String projectName;
  private Double score;
}
