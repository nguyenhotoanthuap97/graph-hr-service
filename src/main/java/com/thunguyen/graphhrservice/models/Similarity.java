package com.thunguyen.graphhrservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Builder
@Table(name = "similarity")
public class Similarity {

  @EmbeddedId
  private SimilarityId similarityId;

  @Column(name = "sim_score")
  private Double simScore;

  public Similarity(SimilarityId similarityId, Double simScore) {
    this.similarityId = similarityId;
    this.simScore = simScore;
  }
}
