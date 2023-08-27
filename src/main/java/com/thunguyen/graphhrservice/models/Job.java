package com.thunguyen.graphhrservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Job {

  private Integer jobId;
  private String jobName;
  private String roleName;
  private String projectName;
  private String buName;
  private String stack;
  private String employee;
}
