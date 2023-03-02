package com.thunguyen.graphhrservice.endpoints;

import com.thunguyen.graphhrservice.model.Employee;
import com.thunguyen.graphhrservice.services.GraphHRService;
import org.neo4j.driver.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GraphHREndpoint {

  @Autowired
  private GraphHRService graphHRService;

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

  @GetMapping(value = "/sim")
  @ResponseBody
  private String getRequires(@RequestParam String employeeId) {
    return graphHRService.findTopKWeightedPathSim(employeeId);
  }
}
