package com.thunguyen.graphhrservice.services;

import com.thunguyen.graphhrservice.dao.GraphHRDAO;
import com.thunguyen.graphhrservice.dao.SimGraphDAO;
import com.thunguyen.graphhrservice.models.Project;
import com.thunguyen.graphhrservice.models.Similarity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@AllArgsConstructor
public class EvaluateService {

  private final GraphHRDAO graphHRDAO;
  private final SimGraphDAO simGraphDAO;

//  public void validateSimilarity() {
//    List<Similarity> allSims = new ArrayList<>();
//    List<Project> projects = graphHRDAO.getProject();
//
//    projects.forEach(project -> {
//      List<Similarity> sortedSims = simGraphDAO.getSimilaritiesForProjectName(project.getName()).stream().sorted((sim1, sim2) -> Double.compare(sim2.getSimScore(), sim1.getSimScore())).toList();
//      if (!CollectionUtils.isEmpty(sortedSims) && sortedSims.get(0) != null) {
//        allSims.add(sortedSims.get(0));
//      }
//    });
//
//    int sampleSize = allSims.size();
//    AtomicInteger passSize = new AtomicInteger();
//    log.info("project | employee | matchedSkill | requiredSkill");
//    allSims.forEach(sim -> {
//      int requiredSkillCount = graphHRDAO.getRequiredSkillCount(sim.getSimilarityId().getEmployeeId(), sim.getSimilarityId().getJobId());
//      int matchedCount = graphHRDAO.getMatchedRatingCount(sim.getSimilarityId().getEmployeeId(), sim.getSimilarityId().getJobId());
//       log.info("{} \t {} \t {} \t {}", sim.getSimilarityId().getProjectName(), sim.getSimilarityId().getEmployeeId(), matchedCount, requiredSkillCount);
//      if (matchedCount > 0) {
//        passSize.getAndIncrement();
//      }
//    });
//    log.info("Evaluate result: pass/total: {}/{}", passSize.get(), sampleSize);
//
//  }
}
