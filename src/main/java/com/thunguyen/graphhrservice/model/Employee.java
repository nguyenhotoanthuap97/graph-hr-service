package com.thunguyen.graphhrservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Employee {

    private String id;
    private String name;
    private String title;
}
