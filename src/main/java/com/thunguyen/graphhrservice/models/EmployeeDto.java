package com.thunguyen.graphhrservice.models;

import java.util.List;
import lombok.Data;

@Data
public class EmployeeDto {

  private String name;
  private String id;
  private String title;
  private String address;
  private String sex;
  private String sibn;
  private String pit;
  private String dateOfBirth;
  private List<Rating> rates;
}
