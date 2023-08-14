package com.thunguyen.graphhrservice.endpoints;

import com.thunguyen.graphhrservice.dao.GraphHRDAO;
import com.thunguyen.graphhrservice.models.Employee;
import com.thunguyen.graphhrservice.models.Job;
import com.thunguyen.graphhrservice.models.Project;
import com.thunguyen.graphhrservice.models.Skill;
import com.thunguyen.graphhrservice.services.GraphDBService;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/graph")
public class GraphHREndpoint {

  private final GraphDBService graphHRService;
  private final GraphHRDAO graphHRDAO;

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
  private List<Job> getJob(@RequestParam(required = false) String teamName) {
    return graphHRService.getJob(teamName);
  }

  @GetMapping(value = "/job/requirement")
  @ResponseBody
  private List<Map<String, String>> getJobRequirement(@RequestParam() String jobId) {
    return graphHRDAO.getJobRequirement(jobId);
  }

  @GetMapping(value = "/team")
  @ResponseBody
  private List<Project> getProject() {
    return graphHRService.getProject();
  }

  @GetMapping(value = "/team-info")
  @ResponseBody
  private List<Map<String, String>> getProjectInfo() {
    return graphHRService.getProjectInfo();
  }

  @GetMapping(value = "/skill")
  @ResponseBody
  private List<Skill> getSkill() {
    return graphHRDAO.getSkill();
  }

//  @PostMapping(value = "/job")
//  @ResponseBody
//  private void addJob(@@RequestBody Map<String, String> body) {
//    graphHRDAO.createJob()
//    return
//  }
}
