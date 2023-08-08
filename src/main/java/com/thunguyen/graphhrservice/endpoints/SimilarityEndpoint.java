package com.thunguyen.graphhrservice.endpoints;

import com.thunguyen.graphhrservice.models.Similarity;
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
  public List<Similarity> getEmployeeRecommendedProjects(
      @PathVariable(value = "employeeId") String employeeId) {
    return recommendationService.getRecommendedProjectsForEmployee(employeeId);
  }
}
