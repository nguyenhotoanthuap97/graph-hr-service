package com.thunguyen.graphhrservice.services;

import com.thunguyen.graphhrservice.dao.GraphHRDAO;
import com.thunguyen.graphhrservice.model.Employee;
import com.thunguyen.graphhrservice.model.Project;
import com.thunguyen.graphhrservice.model.Skill;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Record;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GraphHRService {

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

  public int[][] getRating() {
    List<Record> ratingRecords = graphHRDAO.getRatingMatrix();
    List<String> employees = graphHRDAO.getEmployee().stream().map(Employee::getId).toList();
    List<String> skills = graphHRDAO.getSkill().stream().map(Skill::getName).toList();
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
      matrix[row][col] = Integer.parseInt(record.get("rating").asString());
    }

    return matrix;
  }

  public int[][] getRequire() {
    List<Record> requireRecords = graphHRDAO.getRequireMatrix();
    List<String> projects = graphHRDAO.getProject().stream().map(Project::getName).toList();
    List<String> skills = graphHRDAO.getSkill().stream().map(Skill::getName).toList();
    int projectSize = projects.size();
    int skillSize = skills.size();
    int[][] matrix = new int[projectSize][skillSize];

    for (int i = 0; i < projectSize; i++) {
      for (int j = 0; j < skillSize; j++) {
        matrix[i][j] = 0;
      }
    }

    for (Record record : requireRecords) {
      int row = projects.indexOf(record.get("teamName").asString());
      int col = skills.indexOf(record.get("skillName").asString());
      matrix[row][col] = Integer.parseInt(record.get("rating").asString());
    }

    return matrix;
  }

  public int[][] getFullRating() {
    int[][] requireMatrix = getRequire();
    int[][] ratingMatrix = getRating();
    int skillNum = requireMatrix[0].length;
    int employeeNum = ratingMatrix.length;
    int projectNum = requireMatrix.length;
    int[][] fullMatrix = new int[employeeNum + projectNum][skillNum];
    for (int i = 0; i < employeeNum; i++) {
      System.arraycopy(ratingMatrix[i], 0, fullMatrix[i], 0, skillNum);
    }

    for (int i = employeeNum; i < employeeNum + projectNum; i++) {
      System.arraycopy(requireMatrix[i - employeeNum], 0, fullMatrix[i], 0, skillNum);
    }

    return fullMatrix;
  }

  public String findTopKWeightedPathSim(String employee) {
    int[][] matrix = getFullRating();
    List<String> employees = graphHRDAO.getEmployee().stream().map(Employee::getId).toList();
    List<String> projects = graphHRDAO.getProject().stream().map(Project::getName).toList();

    int ePos = employees.indexOf(employee);
    double[] sims = PathSim.getWeightedSimilarityList(ePos, matrix, true);
    List<Double> simList = new java.util.ArrayList<>(Arrays.stream(sims).boxed().toList());
    List<Double> projectSimList = simList.subList(employees.size(), simList.size());
    List<Double> sortedSimList = new java.util.ArrayList<>(new java.util.ArrayList<>(projectSimList.stream().toList()).stream().sorted().toList());
    Collections.reverse(sortedSimList);
    int first = projectSimList.indexOf(sortedSimList.get(0));
    int second = projectSimList.indexOf(sortedSimList.get(1));
    int third = projectSimList.indexOf(sortedSimList.get(2));
    log.info("Mem: {}", employees.get(ePos));
    log.info("   I: {}, II: {}, III: {}", projects.get(first), projects.get(second), projects.get(third));
    return "Mem: " + employees.get(ePos) + "   I: " + projects.get(first) + ", II: " + projects.get(second) + ", III: " + projects.get(third);
  }
}
