package com.thunguyen.graphhrservice.services;

import static com.thunguyen.graphhrservice.configs.GraphHRConstants.BA;
import static com.thunguyen.graphhrservice.configs.GraphHRConstants.QA;
import static com.thunguyen.graphhrservice.configs.GraphHRConstants.ROLE_BUSINESS_ANALYST;
import static com.thunguyen.graphhrservice.configs.GraphHRConstants.ROLE_DEVELOPER;
import static com.thunguyen.graphhrservice.configs.GraphHRConstants.ROLE_TESTER;
import static com.thunguyen.graphhrservice.configs.GraphHRConstants.SA;
import static com.thunguyen.graphhrservice.configs.GraphHRConstants.SBA;
import static com.thunguyen.graphhrservice.configs.GraphHRConstants.SE;
import static com.thunguyen.graphhrservice.configs.GraphHRConstants.SQA;
import static com.thunguyen.graphhrservice.configs.GraphHRConstants.SSE;
import static com.thunguyen.graphhrservice.configs.GraphHRConstants.TYPO_QA;

import com.thunguyen.graphhrservice.dao.GraphHRDAO;
import com.thunguyen.graphhrservice.dao.SimGraphDAO;
import com.thunguyen.graphhrservice.models.Employee;
import com.thunguyen.graphhrservice.models.Job;
import com.thunguyen.graphhrservice.models.Role;
import com.thunguyen.graphhrservice.models.Similarity;
import com.thunguyen.graphhrservice.models.SimilarityId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RecommendationService {

  private final GraphHRDAO graphHRDAO;
  private final SimGraphDAO simGraphDAO;
  private final GraphDBService graphDBService;

  public void buildImprovedSimOfflineMatrix() {
    List<Role> roles = graphHRDAO.getRoles();
    List<Similarity> offlineSimilarity = new ArrayList<>();
    roles.forEach(role -> {
      log.info("Calculate for role: {}", role.getName());
      offlineSimilarity.addAll(buildImprovedSimMatrixForRole(role.getName()));
    });
    simGraphDAO.saveSimilarityScores(offlineSimilarity);
  }

  public void buildImprovedSimOfflineMatrixForEmployee(String employeeId) {
    List<Similarity> offlineSimilarity = new ArrayList<>(
        buildImprovedSimMatrixForEmployee(employeeId));
    simGraphDAO.saveSimilarityScores(offlineSimilarity);
  }

  public void buildImprovedSimOfflineMatrixForJob(Integer jobId) {
    List<Similarity> offlineSimilarity = new ArrayList<>(buildImprovedSimMatrixForJob(jobId));
    simGraphDAO.saveSimilarityScores(offlineSimilarity);
  }

  public void buildSimOfflineMatrix() {
    log.info("Calculate for all role");
    List<Similarity> offlineSimilarity = new ArrayList<>(buildSimMatrix());
    simGraphDAO.saveSimilarityScores(offlineSimilarity);
  }

  public void buildPearsonSimOfflineMatrix() {
    log.info("Calculate for all role");
    List<Similarity> offlineSimilarity = new ArrayList<>(buildPearsonSimMatrix());
    simGraphDAO.saveSimilarityScores(offlineSimilarity);
  }

  private List<Similarity> buildImprovedSimMatrixForRole(String role) {
    List<Integer> jobs = graphHRDAO.getJob(role).stream().map(Job::getJobId).toList();
    List<String> employees = graphHRDAO.getEmployee(role).stream().map(Employee::getId).toList();
    int[][] matrix = graphDBService.getFullRating(role);
    int skillNum = matrix[0].length;
    int jobNum = jobs.size();
    int employeeNum = employees.size();
    List<Similarity> jobSimilarities = new ArrayList<>();
    employees.forEach(employee -> {
      log.info("Calculate for employee: {}", employee);
      int ePos = employees.indexOf(employee);
      int[][] subMatrix = extractSubMatrix(jobs, matrix, skillNum, jobNum, employeeNum, ePos);
      double[] sims = PathSim.getImprovedWeightedSimilarityList(0, subMatrix, true);
      buildSimilarityList(jobs, jobSimilarities, employee, sims);
    });
    return jobSimilarities;
  }

  private List<Similarity> buildImprovedSimMatrixForEmployee(String employeeId) {
    Employee employeeInfo = graphHRDAO.getEmployeeInfoById(employeeId);
    List<String> employees = Collections.singletonList(employeeId);
    String role = switch (employeeInfo.getTitle()) {
      case SA, SSE, SE -> ROLE_DEVELOPER;
      case SQA, QA, TYPO_QA -> ROLE_TESTER;
      case SBA, BA -> ROLE_BUSINESS_ANALYST;
      default -> "";
    };
    List<Integer> jobs = graphHRDAO.getJob(role).stream().map(Job::getJobId).toList();
    int[][] matrix = graphDBService.getSingleEmployeeRating(employeeId, role);
    int skillNum = matrix[0].length;
    int jobNum = jobs.size();
    int employeeNum = employees.size();
    List<Similarity> jobSimilarities = new ArrayList<>();
    employees.forEach(employee -> {
      log.info("Calculate for employee: {}", employee);
      int ePos = employees.indexOf(employee);
      int[][] subMatrix = extractSubMatrix(jobs, matrix, skillNum, jobNum, employeeNum, ePos);
      double[] sims = PathSim.getImprovedWeightedSimilarityList(0, subMatrix, true);
      buildSimilarityList(jobs, jobSimilarities, employee, sims);
    });
    return jobSimilarities;
  }

  private List<Similarity> buildImprovedSimMatrixForJob(Integer jobId) {
    List<Integer> jobs = Collections.singletonList(jobId);
    Job jobInfo = graphHRDAO.getJobInfoByJobId(jobId);
    String role = switch (jobInfo.getJobName()) {
      case SA, SSE, SE -> ROLE_DEVELOPER;
      case SQA, QA, TYPO_QA -> ROLE_TESTER;
      case SBA, BA -> ROLE_BUSINESS_ANALYST;
      default -> "";
    };
    List<String> employees = graphHRDAO.getEmployee(role).stream().map(Employee::getId).toList();
    int[][] matrix = graphDBService.getSingleJobRating(jobId, role);
    int skillNum = matrix[0].length;
    int jobNum = jobs.size();
    int employeeNum = employees.size();
    List<Similarity> jobSimilarities = new ArrayList<>();
    employees.forEach(employee -> {
      log.info("Calculate for employee: {}", employee);
      int ePos = employees.indexOf(employee);
      int[][] subMatrix = extractSubMatrix(jobs, matrix, skillNum, jobNum, employeeNum, ePos);
      double[] sims = PathSim.getImprovedWeightedSimilarityList(0, subMatrix, true);
      buildSimilarityList(jobs, jobSimilarities, employee, sims);
    });
    return jobSimilarities;
  }


  private void buildSimilarityList(List<Integer> jobs, List<Similarity> jobSimilarities,
      String employee, double[] sims) {
    for (int i = 1; i < sims.length; i++) {
      Similarity jobSimilarity = Similarity.builder()
          .similarityId(new SimilarityId(employee, jobs.get(i - 1)))
          .simScore(sims[i]).build();
      if (jobSimilarity.getSimScore() > 0) {
        jobSimilarities.add(jobSimilarity);
      }
    }
  }

  private List<Similarity> buildSimMatrix() {
    List<Integer> jobs = graphHRDAO.getJob().stream().map(Job::getJobId).toList();
    List<String> employees = graphHRDAO.getEmployee().stream().map(Employee::getId).toList();
    int[][] matrix = graphDBService.getFullRating();
    int skillNum = matrix[0].length;
    int jobNum = jobs.size();
    int employeeNum = employees.size();
    List<Similarity> jobSimilarities = new ArrayList<>();
    employees.forEach(employee -> {
      log.info("Calculate for employee: {}", employee);
      int ePos = employees.indexOf(employee);
      int[][] subMatrix = extractSubMatrix(jobs, matrix, skillNum, jobNum, employeeNum, ePos);
      double[] sims = PathSim.getWeightedSimilarityList(0, subMatrix, true);
      buildSimilarityList(jobs, jobSimilarities, employee, sims);
    });
    return jobSimilarities;
  }

  private List<Similarity> buildPearsonSimMatrix() {
    List<Integer> jobs = graphHRDAO.getJob().stream().map(Job::getJobId).toList();
    List<String> employees = graphHRDAO.getEmployee().stream().map(Employee::getId).toList();
    int[][] matrix = graphDBService.getFullRating();
    int skillNum = matrix[0].length;
    int jobNum = jobs.size();
    int employeeNum = employees.size();
    List<Similarity> jobSimilarities = new ArrayList<>();
    employees.forEach(employee -> {
      log.info("Calculate for employee: {}", employee);
      int ePos = employees.indexOf(employee);
      int[][] subMatrix = extractSubMatrix(jobs, matrix, skillNum, jobNum, employeeNum, ePos);
      double[] sims = PearsonSimilarity.getPearsonSimilarities(0, subMatrix);
      buildSimilarityList(jobs, jobSimilarities, employee, sims);
    });
    return jobSimilarities;
  }

  private static int[][] extractSubMatrix(List<Integer> jobs, int[][] matrix, int skillNum,
      int jobNum,
      int employeeNum, int ePos) {
    int[][] subMatrix = new int[jobNum + 1][matrix[0].length];

    System.arraycopy(matrix[ePos], 0, subMatrix[0], 0, skillNum); // Add employee's rate matrix
    for (int i = 0; i < jobs.size(); i++) { // Add all job's require matrix
      System.arraycopy(matrix[employeeNum + i], 0, subMatrix[i + 1], 0, skillNum);
    }
    return subMatrix;
  }

  public void startImprovedOfflineCalculating() {
    log.info("Offline improved calculation START");
    buildImprovedSimOfflineMatrix();
    log.info("Offline improved calculation DONE");
  }

  public void startOfflineCalculating() {
    log.info("Offline calculation START");
    buildSimOfflineMatrix();
    log.info("Offline calculation DONE");
  }

  public void startPearsonOfflineCalculating() {
    log.info("Offline Pearson calculation START");
    buildPearsonSimOfflineMatrix();
    log.info("Offline Pearson calculation DONE");
  }

  public List<Job> getRecommendedProjectsForEmployee(String employeeId, int recommendSize) {
    List<Similarity> sortedSimilarities = simGraphDAO.getSimilaritiesForEmployeeId(employeeId)
        .stream().sorted((sim1, sim2) -> Double.compare(sim2.getSimScore(), sim1.getSimScore()))
        .toList();
    int size = Math.min(sortedSimilarities.size(), recommendSize);
    List<Similarity> topK = sortedSimilarities.subList(0, size);
    List<Job> response = topK.stream()
        .map(sim -> graphHRDAO.getJobInfoByJobId(sim.getSimilarityId().getJobId()))
        .toList();
    return response;
  }

  public List<Employee> getRecommendedEmployeesForJob(Integer jobId, int recommendSize) {
    List<Similarity> sortedSimilarities = simGraphDAO.getSimilaritiesForJobId(jobId)
        .stream().sorted((sim1, sim2) -> Double.compare(sim2.getSimScore(), sim1.getSimScore()))
        .toList();

    int size = Math.min(sortedSimilarities.size(), recommendSize);

    List<Similarity> topK = sortedSimilarities.subList(0, size);
    List<Employee> response = new ArrayList<>();
    topK.forEach(sim -> {
      Employee employee = graphHRDAO.getEmployeeInfoById(sim.getSimilarityId().getEmployeeId());
      response.add(employee);
    });

    return response;
  }
}
