package com.thunguyen.graphhrservice.repositories;

import com.thunguyen.graphhrservice.models.Similarity;
import com.thunguyen.graphhrservice.models.SimilarityId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SimilarityMongoRepository extends MongoRepository<Similarity, SimilarityId> {
  List<Similarity> findSimilaritiesBySimilarityId(SimilarityId similarityId);

  List<Similarity> findSimilaritiesBySimilarityId_EmployeeId(String employeeId);

  List<Similarity> findSimilaritiesBySimilarityId_JobId(Integer jobId);
}
