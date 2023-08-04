package com.thunguyen.graphhrservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Document("similarity")
public class Similarity {

  @Id
  private SimilarityId similarityId;
  private Double simScore;

  public Similarity(SimilarityId similarityId, Double simScore) {
    this.similarityId = similarityId;
    this.simScore = simScore;
  }
}
