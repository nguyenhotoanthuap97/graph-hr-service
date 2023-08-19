package com.thunguyen.graphhrservice.endpoints;

import com.thunguyen.graphhrservice.dao.GraphHRDAO;
import com.thunguyen.graphhrservice.models.Employee;
import com.thunguyen.graphhrservice.models.EmployeeDto;
import com.thunguyen.graphhrservice.models.Job;
import com.thunguyen.graphhrservice.models.JobDto;
import com.thunguyen.graphhrservice.models.Project;
import com.thunguyen.graphhrservice.models.Skill;
import com.thunguyen.graphhrservice.services.GraphDBService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/graph")
public class GraphHREndpoint {

  @Value("ui-uri")
  public final String UI_URI = "http://localhost:3000";
  private final GraphDBService graphHRService;
  private final GraphHRDAO graphHRDAO;

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/employee")
  @ResponseBody
  private List<Employee> getEmployee() {
    return graphHRService.getEmployee();
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/rating")
  @ResponseBody
  private int[][] getRatings() {
    return graphHRService.getRating();
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/require")
  @ResponseBody
  private int[][] getRequires() {
    return graphHRService.getRequire();
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/job")
  @ResponseBody
  private List<Job> getJob(@RequestParam(required = false) String teamName) {
    return graphHRService.getJob(teamName);
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/job/requirement")
  @ResponseBody
  private List<Map<String, String>> getJobRequirement(@RequestParam() String jobId) {
    return graphHRDAO.getJobRequirement(jobId);
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/employee/rating")
  @ResponseBody
  private List<Map<String, String>> getEmployeeRating(@RequestParam() String employeeId) {
    return graphHRDAO.getEmployeeRating(employeeId);
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/team")
  @ResponseBody
  private List<Project> getProject() {
    return graphHRService.getProject();
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/team-info")
  @ResponseBody
  private List<Map<String, String>> getProjectInfo() {
    return graphHRService.getProjectInfo();
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/skill")
  @ResponseBody
  private List<Skill> getSkill() {
    return graphHRDAO.getSkill();
  }

  @PostMapping(value = "/job")
  @ResponseBody
  private void addJob(@RequestBody JobDto jobDto) {
    graphHRService.addJob(jobDto);
  }

  @PostMapping(value = "/employee")
  @ResponseBody
  private void addEmployee(@RequestBody EmployeeDto employeeDto) {
    graphHRService.addEmployee(employeeDto);
  }
}
