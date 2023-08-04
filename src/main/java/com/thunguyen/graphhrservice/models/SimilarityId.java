package com.thunguyen.graphhrservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class SimilarityId implements Serializable {
  private String employeeId;
  private Integer jobId;
}
