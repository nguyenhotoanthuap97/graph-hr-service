package com.thunguyen.graphhrservice.dao;

import com.thunguyen.graphhrservice.models.Similarity;
import com.thunguyen.graphhrservice.repositories.SimilarityMongoRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class SimGraphDAO {

  @Autowired
  private SimilarityMongoRepository similarityMongoRepository;
  private static final int BATCH = 2000;

  public void saveSimilarityScores(List<Similarity> similarities) {
    List<List<Similarity>> subLists = ListUtils.partition(similarities, BATCH);
    log.info("Batch size: {}", subLists.size());
    int count = 0;
    for (List<Similarity> subList : subLists) {
      log.info("Save batch: {}", count);
      count++;
      similarityMongoRepository.saveAll(subList);
    }
  }

  public void saveSimilarityScore(Similarity similarity) {
    similarityMongoRepository.save(similarity);
  }

  public List<Similarity> getSimilaritiesForEmployeeId(String employeeId) {
    return similarityMongoRepository.findSimilaritiesBySimilarityId_EmployeeId(employeeId);
  }

  public List<Similarity> getSimilaritiesForJobId(Integer jobId) {
    return similarityMongoRepository.findSimilaritiesBySimilarityId_JobId(jobId);
  }
}
