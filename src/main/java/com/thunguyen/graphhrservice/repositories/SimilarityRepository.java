package com.thunguyen.graphhrservice.repositories;

import com.thunguyen.graphhrservice.models.Similarity;
import com.thunguyen.graphhrservice.models.SimilarityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimilarityRepository extends JpaRepository<Similarity, SimilarityId> {
  List<Similarity> findSimilaritiesBySimilarityId(SimilarityId similarityId);
  List<Similarity> findSimilaritiesBySimilarityId_EmployeeId(String employeeId);
  List<Similarity> findSimilaritiesBySimilarityId_ProjectName(String projectName);
}
