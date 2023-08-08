package com.thunguyen.graphhrservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SimTest {

  @Test
  void pathSimTest() {
    int[][] adjMatrix = {
        {7, 6, 7, 4, 5, 4},
        {6, 7, 0, 4, 3, 4},
        {0, 3, 3, 1, 1, 0},
        {1, 2, 2, 3, 3, 4},
        {1, 0, 1, 2, 3, 3}
    };

    double[] sims = PathSim.getSimilarityList(2, adjMatrix, true);
    System.out.println("PathSim: ");
    for (int i = 0; i < sims.length; i++) {
      System.out.println("  Sim(1," + i + "): " + sims[i]);
    }
  }

  @Test
  void weightedPathSimTest() {
    int[][] adjMatrix = {
        {7, 6, 7, 4, 5, 4},
        {6, 7, 0, 4, 3, 4},
        {0, 3, 3, 1, 1, 0},
        {1, 2, 2, 3, 3, 4},
        {1, 0, 1, 2, 3, 3}
    };

    double[] sims = PathSim.getImprovedWeightedSimilarityList(3, adjMatrix, true);
    System.out.println("Weighted PathSim: ");
    for (int i = 0; i < sims.length; i++) {
      System.out.println("  Sim(3," + i + "): " + sims[i]);
    }
  }

  @Test
  void pearsonTest() {
    int[][] adjMatrix = {
        {7, 6, 7, 4, 5, 4},
        {6, 7, 0, 4, 3, 4},
        {0, 3, 3, 1, 1, 0},
        {1, 2, 2, 3, 3, 4},
        {1, 0, 1, 2, 3, 3}
    };

    double[] sims = PearsonSimilarity.getPearsonSimilarities(2, adjMatrix);

    System.out.println("Pearson: ");
    for (int i = 0; i < sims.length; i++) {
      System.out.println("  Sim(1," + i + "): " + sims[i]);
    }
  }

}
