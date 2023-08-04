package com.thunguyen.graphhrservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobSimilarity {
  private String employeeId;
  private String jobId;
  private Double score;
}
