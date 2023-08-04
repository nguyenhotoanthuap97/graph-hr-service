package com.thunguyen.graphhrservice.endpoints;

import com.thunguyen.graphhrservice.models.Similarity;
import com.thunguyen.graphhrservice.services.EvaluateService;
import com.thunguyen.graphhrservice.services.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/recommend")
public class SimilarityEndpoint {

  private final RecommendationService recommendationService;
  private final EvaluateService evaluateService;

  @GetMapping(value = "/process/offline")
  @ResponseBody
  public void triggerOfflineCalculating() {
    recommendationService.startOfflineCalculating();
  }

  @GetMapping(value = "/employee/{employeeId}")
  @ResponseBody
  public List<Similarity> getEmployeeRecommendedProjects(@PathVariable(value = "employeeId") String employeeId) {
    return recommendationService.getRecommendedProjectsForEmployee(employeeId);
  }

//  @GetMapping(value = "/evaluate")
//  public void evaluate() {
//    evaluateService.validateSimilarity();
//  }
}
