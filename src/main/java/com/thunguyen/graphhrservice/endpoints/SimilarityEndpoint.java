package com.thunguyen.graphhrservice.endpoints;

import com.thunguyen.graphhrservice.models.Employee;
import com.thunguyen.graphhrservice.models.Job;
import com.thunguyen.graphhrservice.services.RecommendationService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/recommend")
public class SimilarityEndpoint {

  @Value("ui-uri")
  public final String UI_URI = "http://localhost:3000";
  private final RecommendationService recommendationService;

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/process/improved-offline")
  @ResponseBody
  public void triggerImprovedOfflineCalculating() {
    recommendationService.startImprovedOfflineCalculating();
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/process/offline")
  @ResponseBody
  public void triggerOfflineCalculating() {
    recommendationService.startOfflineCalculating();
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/process/pearson-offline")
  @ResponseBody
  public void triggerPearsonOfflineCalculating() {
    recommendationService.startPearsonOfflineCalculating();
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/employee/{employeeId}")
  @ResponseBody
  public List<Job> getEmployeeRecommendedJob(
      @PathVariable(value = "employeeId") String employeeId) {
    return recommendationService.getRecommendedProjectsForEmployee(employeeId, 20);
  }

  @CrossOrigin(origins = UI_URI, methods = RequestMethod.GET)
  @GetMapping(value = "/job/{jobId}")
  @ResponseBody
  public List<Employee> getJobRecommendedEmployee(@PathVariable(value = "jobId") Integer jobId) {
    return recommendationService.getRecommendedEmployeesForJob(jobId, 20);
  }
}
