package com.thunguyen.graphhrservice.services;

import com.thunguyen.graphhrservice.dao.GraphHRDAO;
import com.thunguyen.graphhrservice.models.Employee;
import com.thunguyen.graphhrservice.models.Job;
import com.thunguyen.graphhrservice.models.Project;
import com.thunguyen.graphhrservice.models.Skill;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Record;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GraphDBService {

  private final GraphHRDAO graphHRDAO;

  public List<Employee> getEmployee() {
    List<Employee> employees = graphHRDAO.getEmployee();
    log.info("Employees: {}", employees);
    return employees;
  }

  public List<Project> getProject() {
    List<Project> projects = graphHRDAO.getProject();
    log.info("Projects: {}", projects);
    return projects;
  }

  public List<Job> getJob() {
    List<Job> jobs = graphHRDAO.getJob();
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

  private static int[][] getRatingMatrix(List<Record> ratingRecords, List<String> employees, List<String> skills) {
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
    List<Integer> jobs = graphHRDAO.getJob().stream().map(Job::getJobId).toList();
    List<String> skills = graphHRDAO.getSkill(role).stream().map(Skill::getName).toList();
    return getRequireMatrix(requireRecords, jobs, skills);
  }

  private static int[][] getRequireMatrix(List<Record> requireRecords, List<Integer> jobs, List<String> skills) {
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
}
