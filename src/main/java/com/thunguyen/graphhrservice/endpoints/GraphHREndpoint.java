package com.thunguyen.graphhrservice.endpoints;

import com.thunguyen.graphhrservice.models.Employee;
import com.thunguyen.graphhrservice.models.Job;
import com.thunguyen.graphhrservice.services.GraphDBService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/graph")
public class GraphHREndpoint {

  private final GraphDBService graphHRService;

  @GetMapping(value = "/employee")
  @ResponseBody
  private List<Employee> getEmployee() {
    return graphHRService.getEmployee();
  }

  @GetMapping(value = "/rating")
  @ResponseBody
  private int[][] getRatings() {
    return graphHRService.getRating();
  }

  @GetMapping(value = "/require")
  @ResponseBody
  private int[][] getRequires() {
    return graphHRService.getRequire();
  }

  @GetMapping(value = "/job")
  @ResponseBody
  private List<Job> getJob() {
    return graphHRService.getJob();
  }

}
