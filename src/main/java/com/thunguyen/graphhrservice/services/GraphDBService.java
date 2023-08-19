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

import com.thunguyen.graphhrservice.dao.GraphHRDAO;
import com.thunguyen.graphhrservice.models.Employee;
import com.thunguyen.graphhrservice.models.EmployeeDto;
import com.thunguyen.graphhrservice.models.Job;
import com.thunguyen.graphhrservice.models.JobDto;
import com.thunguyen.graphhrservice.models.Project;
import com.thunguyen.graphhrservice.models.Rating;
import com.thunguyen.graphhrservice.models.Skill;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Record;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class GraphDBService {

  private final GraphHRDAO graphHRDAO;

  public List<Employee> getEmployee() {
    List<Employee> employees = graphHRDAO.getEmployeeInfo();
    log.info("Get Employees!");
    return employees;
  }

  public List<Project> getProject() {
    List<Project> projects = graphHRDAO.getProject();
    log.info("Projects: {}", projects);
    return projects;
  }

  public List<Map<String, String>> getProjectInfo() {
    List<Map<String, String>> projects = graphHRDAO.getProjectInfo();
    log.info("Projects info: {}", projects);
    return projects;
  }

  public List<Job> getJob(String teamName) {
    List<Job> jobs;
    if (Objects.isNull(teamName)) {
      jobs = graphHRDAO.getJob();
    } else {
      jobs = graphHRDAO.getJobByTeam(teamName);
    }
    log.info("Jobs: {}", jobs);
    return jobs;
  }

  public int[][] getRating() {
    List<Record> ratingRecords = graphHRDAO.getRatingMatrix();
    List<String> employees = graphHRDAO.getEmployee().stream().map(Employee::getId).toList();
    List<String> skills = graphHRDAO.getSkill().stream().map(Skill::getName).toList();
    return getRatingMatrix(ratingRecords, employees, skills);
  }

  public int[][] getRating(String role) {
    List<Record> ratingRecords = graphHRDAO.getRatingMatrix(role);
    List<String> employees = graphHRDAO.getEmployee(role).stream().map(Employee::getId).toList();
    List<String> skills = graphHRDAO.getSkill(role).stream().map(Skill::getName).toList();
    return getRatingMatrix(ratingRecords, employees, skills);
  }

  private static int[][] getRatingMatrix(List<Record> ratingRecords, List<String> employees,
      List<String> skills) {
    int employeeSize = employees.size();
    int skillSize = skills.size();
    int[][] matrix = new int[employeeSize][skillSize];
    for (int i = 0; i < employeeSize; i++) {
      for (int j = 0; j < skillSize; j++) {
        matrix[i][j] = 0;
      }
    }

    for (Record record : ratingRecords) {
      int row = employees.indexOf(record.get("employeeId").asString());
      int col = skills.indexOf(record.get("skillName").asString());
      matrix[row][col] = record.get("rating").asInt();
    }

    return matrix;
  }

  public int[][] getFullRating() {
    int[][] ratingMatrix = getRating();
    int[][] requireMatrix = getRequire();
    return getCombineMatrix(requireMatrix, ratingMatrix);
  }

  public int[][] getRequire() {
    List<Record> requireRecords = graphHRDAO.getRequireMatrix();
    List<Integer> jobs = graphHRDAO.getJob().stream().map(Job::getJobId).toList();
    List<String> skills = graphHRDAO.getSkill().stream().map(Skill::getName).toList();
    return getRequireMatrix(requireRecords, jobs, skills);
  }

  public int[][] getRequire(String role) {
    List<Record> requireRecords = graphHRDAO.getRequireMatrix(role);
    List<Integer> jobs = graphHRDAO.getJob(role).stream().map(Job::getJobId).toList();
    List<String> skills = graphHRDAO.getSkill(role).stream().map(Skill::getName).toList();
    return getRequireMatrix(requireRecords, jobs, skills);
  }

  private static int[][] getRequireMatrix(List<Record> requireRecords, List<Integer> jobs,
      List<String> skills) {
    int jobSize = jobs.size();
    int skillSize = skills.size();
    int[][] matrix = new int[jobSize][skillSize];

    for (int i = 0; i < jobSize; i++) {
      for (int j = 0; j < skillSize; j++) {
        matrix[i][j] = 0;
      }
    }

    for (Record record : requireRecords) {
      int row = jobs.indexOf(record.get("jobId").asInt());
      int col = skills.indexOf(record.get("skillName").asString());
      matrix[row][col] = record.get("rating").asInt();
    }

    return matrix;
  }


  private static int[][] getCombineMatrix(int[][] requireMatrix, int[][] ratingMatrix) {
    int skillNum = requireMatrix[0].length;
    int employeeNum = ratingMatrix.length;
    int jobNum = requireMatrix.length;
    int[][] fullMatrix = new int[employeeNum + jobNum][skillNum];
    for (int i = 0; i < employeeNum; i++) {
      System.arraycopy(ratingMatrix[i], 0, fullMatrix[i], 0, skillNum);
    }

    for (int i = employeeNum; i < employeeNum + jobNum; i++) {
      System.arraycopy(requireMatrix[i - employeeNum], 0, fullMatrix[i], 0, skillNum);
    }

    return fullMatrix;
  }

  public int[][] getFullRating(String role) {
    int[][] ratingMatrix = getRating(role);
    int[][] requireMatrix = getRequire(role);
    return getCombineMatrix(requireMatrix, ratingMatrix);
  }

  public void addEmployee(EmployeeDto employeeDto) {
    String employeeId = graphHRDAO.createEmployee(employeeDto);
    log.info("Employee {} created!", employeeId);
    String title = employeeDto.getTitle();
    String roleName = switch (title) {
      case SA, SSE, SE -> ROLE_DEVELOPER;
      case SQA, QA -> ROLE_TESTER;
      case SBA, BA -> ROLE_BUSINESS_ANALYST;
      default -> "";
    };
    graphHRDAO.createIsARelationship(employeeId, roleName);
    log.info("Employee link to role {} created!", roleName);
    List<Rating> rates = employeeDto.getRates();
    rates.forEach(rate -> {
      graphHRDAO.createRatesRelationship(employeeId, rate.getSkillName(), rate.getRating());
      log.info("Employee rates skill {} created!", rate.getSkillName());
    });
  }

  public void addJob(JobDto jobDto) {
    Integer jobId = graphHRDAO.createJob(jobDto.getProjectName(), jobDto.getName());
    log.info("Job {} created!", jobId);
    List<Rating> requires = jobDto.getRequires();
    requires.forEach(require -> {
      graphHRDAO.createRequireRelationship(jobId, require.getSkillName(), require.getRating());
      log.info("Job requires skill {} created!", require.getSkillName());
    });
  }
}
