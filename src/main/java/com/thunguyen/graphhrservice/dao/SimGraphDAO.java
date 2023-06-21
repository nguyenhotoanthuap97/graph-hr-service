package com.thunguyen.graphhrservice.dao;

import com.thunguyen.graphhrservice.models.Similarity;
import com.thunguyen.graphhrservice.repositories.SimilarityRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class SimGraphDAO {

  @Autowired
  private SimilarityRepository similarityRepository;

  public void saveSimilarityScores(List<Similarity> similarities) {
    similarityRepository.saveAll(similarities);
  }

  public void saveSimilarityScore(Similarity similarity) {
    similarityRepository.save(similarity);
  }

  public List<Similarity> getSimilaritiesForEmployeeId(String employeeId) {
    return similarityRepository.findSimilaritiesBySimilarityId_EmployeeId(employeeId);
  }

  public List<Similarity> getSimilaritiesForProjectName(String projectName) {
    return similarityRepository.findSimilaritiesBySimilarityId_ProjectName(projectName);
  }
}
