package com.thunguyen.graphhrservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Project {

    private String name;
    private String description;
    private int jobCount;
    private int headCount;
}
