package com.thunguyen.graphhrservice.services;

import com.thunguyen.graphhrservice.dao.GraphHRDAO;
import com.thunguyen.graphhrservice.dao.SimGraphDAO;
import com.thunguyen.graphhrservice.models.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RecommendationService {

  private final GraphHRDAO graphHRDAO;
  private final SimGraphDAO simGraphDAO;
  private final GraphDBService graphDBService;

  public void buildSimOfflineMatrix() {
    List<Role> roles = graphHRDAO.getRoles();
    List<Similarity> offlineSimilarity = new ArrayList<>();
    roles.forEach(role -> {
      log.info("Calculate for role: {}", role.getName());
      offlineSimilarity.addAll(buildSimMatrixForRole(role.getName()));
    });
    simGraphDAO.saveSimilarityScores(offlineSimilarity);
  }

  private List<Similarity> buildSimMatrixForRole(String role) {
    List<Integer> jobs = graphHRDAO.getJob().stream().map(Job::getJobId).toList();
    List<String> employees = graphHRDAO.getEmployee(role).stream().map(Employee::getId).toList();
    int[][] matrix = graphDBService.getFullRating(role);
    int skillNum = matrix[0].length;
    int jobNum = jobs.size();
    int employeeNum = employees.size();
    List<Similarity> jobSimilarities = new ArrayList<>();
    employees.forEach(employee -> {
      log.info("Calculate for employee: {}", employee);
      int ePos = employees.indexOf(employee);
      int[][] subMatrix = new int[jobNum + 1][matrix[0].length];

      System.arraycopy(matrix[ePos], 0, subMatrix[0], 0, skillNum); // Add employee's rate matrix
      for (int i = 0; i < jobs.size(); i++) { // Add all job's require matrix
        System.arraycopy(matrix[employeeNum + i], 0, subMatrix[i + 1], 0, skillNum);
      }
      double[] sims = PathSim.getWeightedSimilarityList(0, subMatrix, true);
      for (int i = 1; i < sims.length; i++) {
        Similarity jobSimilarity = Similarity.builder()
            .similarityId(new SimilarityId(employee, jobs.get(i - 1)))
            .simScore(sims[i]).build();
        if (jobSimilarity.getSimScore() > 0) {
          jobSimilarities.add(jobSimilarity);
        }
      }
    });
    return jobSimilarities;
  }

  public void startOfflineCalculating() {
    log.info("Offline calculation START");
    buildSimOfflineMatrix();
    log.info("Offline calculation DONE");
  }

  public void calculateEmployeePathSimScores(String employeeId) {
    int[][] matrix = graphDBService.getFullRating();
    List<String> employees = graphHRDAO.getEmployee().stream().map(Employee::getId).toList();
    List<Integer> jobs = graphHRDAO.getJob().stream().map(Job::getJobId).toList();

    int ePos = employees.indexOf(employeeId);
    double[] sims = PathSim.getWeightedSimilarityList(ePos, matrix, true);
    List<Double> simList = new java.util.ArrayList<>(Arrays.stream(sims).boxed().toList());
    List<Double> projectSimList = simList.subList(employees.size(), simList.size());
    for (int i = 0; i < projectSimList.size(); i++) {
      Similarity similarity = new Similarity(new SimilarityId(employeeId, jobs.get(i)), projectSimList.get(i));
      simGraphDAO.saveSimilarityScore(similarity);
    }
  }

  public List<Similarity> getRecommendedProjectsForEmployee(String employeeId) {
    List<Similarity> sortedSimilarities = simGraphDAO.getSimilaritiesForEmployeeId(employeeId).stream().sorted((sim1, sim2) -> Double.compare(sim2.getSimScore(), sim1.getSimScore())).toList();
    return sortedSimilarities.subList(0, 3);
  }
}
