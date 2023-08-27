package com.thunguyen.graphhrservice.endpoints;

import com.thunguyen.graphhrservice.dao.GraphHRDAO;
import com.thunguyen.graphhrservice.models.Employee;
import com.thunguyen.graphhrservice.models.EmployeeDto;
import com.thunguyen.graphhrservice.models.Job;
import com.thunguyen.graphhrservice.models.JobDto;
import com.thunguyen.graphhrservice.models.Project;
import com.thunguyen.graphhrservice.models.Skill;
import com.thunguyen.graphhrservice.services.Generator;
import com.thunguyen.graphhrservice.services.GraphDBService;
import com.thunguyen.graphhrservice.services.RecommendationService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/graph")
public class GraphHREndpoint {

  private final GraphDBService graphHRService;
  private final RecommendationService recommendationService;
  private final GraphHRDAO graphHRDAO;
  private final Generator generator;

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
  private List<Job> getJob(@RequestParam(required = false, name = "teamName") String projectName) {
    return graphHRService.getJob(projectName);
  }

  @GetMapping(value = "/job/requirement")
  @ResponseBody
  private List<Map<String, String>> getJobRequirement(@RequestParam() String jobId) {
    return graphHRDAO.getJobRequirement(jobId);
  }

  @GetMapping(value = "/employee/rating")
  @ResponseBody
  private List<Map<String, String>> getEmployeeRating(@RequestParam() String employeeId) {
    return graphHRDAO.getEmployeeRating(employeeId);
  }

  @GetMapping(value = "/team")
  @ResponseBody
  private List<Project> getProject() {
    return graphHRService.getProject();
  }

  @GetMapping(value = "/team-info")
  @ResponseBody
  private List<Map<String, Object>> getProjectInfo() {
    return graphHRService.getProjectInfo();
  }

  @GetMapping(value = "/skill")
  @ResponseBody
  private List<Skill> getSkill() {
    return graphHRDAO.getSkill();
  }

  @PostMapping(value = "/job")
  @ResponseBody
  private Integer addJob(@RequestBody JobDto jobDto) {
    Integer jobId = graphHRService.addJob(jobDto);
    recommendationService.buildImprovedSimOfflineMatrixForJob(jobId);
    return jobId;
  }

  @PostMapping(value = "/employee")
  @ResponseBody
  private String addEmployee(@RequestBody EmployeeDto employeeDto) {
    String employeeId = graphHRService.addEmployee(employeeDto);
    recommendationService.buildImprovedSimOfflineMatrixForEmployee(employeeId);
    return employeeId;
  }

  @GetMapping(value = "/occupies")
  @ResponseBody
  private void occupies() {
    Map<Integer, Object[]> occupies = generator.getEmployeeJobRelationship();
    generator.writeData("Data", occupies,
        "D:/Study/Master/Thesis/Data/Final3/1000/OCCUPIES.xlsx");
  }
}
