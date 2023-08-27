package com.thunguyen.graphhrservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Employee {

    private String id;
    private String name;
    private String title;
    private String address;
    private String sex;
    private String sibn;
    private String pit;
    private String dateOfBirth;
    private String superiorName;
    private String projectName;
    private String buName;
}
