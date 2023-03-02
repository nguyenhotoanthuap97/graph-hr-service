package com.thunguyen.graphhrservice.services;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class PathSim {

  private static double calculateSimilarity(int nodeAPos, int nodeBPos, int[][] matrix) {
    if (nodeAPos == nodeBPos) {
      return 0.0; // If it's the same node => exclude it by setting the sim to 0
    }
    int ratingNum = matrix[nodeAPos].length;

    double product = 0.0;
    double aDiag = 0.0;
    double bDiag = 0.0;
    for (int i = 0; i < ratingNum; i++) {
      int aRating = matrix[nodeAPos][i] != 0 ? 1 : 0;
      int bRating = matrix[nodeBPos][i] != 0 ? 1 : 0;
      product += aRating * bRating;
      double aPow = pow(aRating, 2);
      double bPow = pow(bRating, 2);
      aDiag += aPow;
      bDiag += bPow;
    }
    return 2 * product / (aDiag + bDiag);
  }

  private static double calculateWeightedSimilarity(int nodeAPos, int nodeBPos, int[][] matrix) {
    if (nodeAPos == nodeBPos) {
      return 0.0; // If it's the same node => exclude it by setting the sim to 0
    }
    int ratingNum = matrix[nodeAPos].length;
    double product = 0.0;
    double aDiag = 0.0;
    double bDiag = 0.0;

    double simSum = 0.0;
    for (int w = 1; w <= 5; w++) {
      for (int i = 0; i < ratingNum; i++) {
        int aWeightedRating = matrix[nodeAPos][i] == w ? 1 : 0;
        int bWeightedRating = matrix[nodeBPos][i] == w ? 1 : 0;
        product += aWeightedRating * bWeightedRating;
        int aRating = matrix[nodeAPos][i] != 0 ? 1 : 0;
        int bRating = matrix[nodeBPos][i] != 0 ? 1 : 0;
        double aPow = pow(aRating, 2);
        double bPow = pow(bRating, 2);
        if (aRating + bRating == 2) {
          aDiag += aPow;
          bDiag += bPow;
        }
      }
      simSum += 2 * product / (aDiag + bDiag);
    }

    return simSum;
  }

  public static double[] getSimilarityList(int nodeAPos, int[][] matrix, boolean isSparse) {
    double[] sims = initSims(matrix);

    List<Integer> candidates = getCandidateNodes(nodeAPos, matrix, isSparse);
    for (Integer candidatePos : candidates) {
      sims[candidatePos] = calculateSimilarity(nodeAPos, candidatePos, matrix);
    }

    return sims;
  }

  private static double[] initSims(int[][] matrix) {
    int nodeNum = matrix.length;
    double[] sims = new double[nodeNum];
    for (int i = 0; i < nodeNum; i++) {
      sims[i] = 0.0;
    }
    return sims;
  }

  public static double[] getWeightedSimilarityList(int nodeAPos, int[][] matrix, boolean isSparse) {
    double[] sims = initSims(matrix);

    List<Integer> candidates = getCandidateNodes(nodeAPos, matrix, isSparse);
    for (Integer candidatePos : candidates) {
      sims[candidatePos] = calculateWeightedSimilarity(nodeAPos, candidatePos, matrix);
    }
    return sims;
  }

  private static List<Integer> getCandidateNodes(int nodeAPos, int[][] matrix, boolean isSparse) {
    List<Integer> candidates = new ArrayList<>();
    if (!isSparse) {
      for (int i = 0; i < matrix.length; i++) {
        candidates.add(i);
      }
      return candidates;
    }
    for (int i = 0; i < matrix[nodeAPos].length; i++) {
      if (matrix[nodeAPos][i] != 0) {
        for (int j = 0; j < matrix.length; j++) {
          if (matrix[j][i] != 0) {
            if (candidates.contains(j)) {
              continue;
            }
            candidates.add(j);
          }
        }
      }
    }
    return candidates;
  }
}
