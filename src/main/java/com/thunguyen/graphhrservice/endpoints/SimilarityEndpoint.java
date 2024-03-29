package com.thunguyen.graphhrservice.endpoints;

import com.thunguyen.graphhrservice.models.Employee;
import com.thunguyen.graphhrservice.models.Job;
import com.thunguyen.graphhrservice.services.RecommendationService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/recommend")
public class SimilarityEndpoint {

  private final RecommendationService recommendationService;

  @GetMapping(value = "/process/improved-offline")
  @ResponseBody
  public void triggerImprovedOfflineCalculating() {
    recommendationService.startImprovedOfflineCalculating();
  }

  @GetMapping(value = "/process/offline")
  @ResponseBody
  public void triggerOfflineCalculating() {
    recommendationService.startOfflineCalculating();
  }

  @GetMapping(value = "/process/pearson-offline")
  @ResponseBody
  public void triggerPearsonOfflineCalculating() {
    recommendationService.startPearsonOfflineCalculating();
  }

  @GetMapping(value = "/employee/{employeeId}")
  @ResponseBody
  public List<Job> getEmployeeRecommendedJob(
      @PathVariable(value = "employeeId") String employeeId) {
    return recommendationService.getRecommendedJobsForEmployee(employeeId, 20);
  }

  @GetMapping(value = "/job/{jobId}")
  @ResponseBody
  public List<Employee> getJobRecommendedEmployee(@PathVariable(value = "jobId") Integer jobId) {
    return recommendationService.getRecommendedEmployeesForJob(jobId, 20);
  }

  @GetMapping(value = "/process/improved-offline/job/{jobId}")
  @ResponseBody
  public void triggerOfflineCalculatingForJob(@PathVariable(value = "jobId") Integer jobId) {
    recommendationService.buildImprovedSimOfflineMatrixForJob(jobId);
  }

  @GetMapping(value = "/process/improved-offline/employee/{employeeId}")
  @ResponseBody
  public void triggerOfflineCalculatingForEmployee(
      @PathVariable(value = "employeeId") String employeeId) {
    recommendationService.buildImprovedSimOfflineMatrixForEmployee(employeeId);
  }
}
