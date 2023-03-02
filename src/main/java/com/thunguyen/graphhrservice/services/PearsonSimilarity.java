package com.thunguyen.graphhrservice.services;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class PearsonSimilarity {

  private static double calculateSimilarity(int nodeAPos, int nodeBPos, int[][] matrix) {
    if (nodeAPos == nodeBPos) {
      return 0.0;
    }
    int ratingNum = matrix[nodeAPos].length;
    double product = 0.0;
    double aDeviationSquare = 0.0;
    double bDeviationSquare = 0.0;
    int aValidRatingNum = 0;
    int bValidRatingNum = 0;
    for (int i = 0; i < ratingNum; i++) {
      if (matrix[nodeAPos][i] != 0) {
        aValidRatingNum++;
      }
      if (matrix[nodeBPos][i] != 0) {
        bValidRatingNum++;
      }
    }

    // Average rating
    double aAvg = Arrays.stream(matrix[nodeAPos]).sum() * 1.0 / aValidRatingNum;
    double bAvg = Arrays.stream(matrix[nodeBPos]).sum() * 1.0 / bValidRatingNum;

    for (int i = 0; i < ratingNum; i++) {
      if (matrix[nodeAPos][i] != 0 && matrix[nodeBPos][i] != 0) {
        product += ((matrix[nodeAPos][i] - aAvg) * (matrix[nodeBPos][i] - bAvg));

        // standard deviation
        aDeviationSquare += pow(matrix[nodeAPos][i] - aAvg, 2);
        bDeviationSquare += pow(matrix[nodeBPos][i] - bAvg, 2);
      }
    }

    return product / (sqrt(aDeviationSquare) * sqrt(bDeviationSquare));
  }

  public static double[] getPearsonSimilarities(int nodeAPos, int[][] matrix) {
    int nodeNum = matrix.length;
    double[] sims = new double[nodeNum];
    for (int i = 0; i < nodeNum; i++) {
      sims[i] = 0.0;
    }

    for (int i = 0; i < nodeNum; i++) {
      sims[i] = calculateSimilarity(nodeAPos, i, matrix);
    }

    return sims;
  }
}
