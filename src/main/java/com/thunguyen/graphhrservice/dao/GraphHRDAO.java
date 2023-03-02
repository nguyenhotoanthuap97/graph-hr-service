package com.thunguyen.graphhrservice.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thunguyen.graphhrservice.model.Employee;
import com.thunguyen.graphhrservice.model.Project;
import com.thunguyen.graphhrservice.model.Skill;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GraphHRDAO {

  private final Driver neo4jDriver;

  private final SessionConfig sessionConfig;

  public GraphHRDAO(Driver neo4jDriver) {
    this.neo4jDriver = neo4jDriver;
    this.sessionConfig = SessionConfig.builder()
        .withDefaultAccessMode(AccessMode.WRITE)
        .withDatabase("neo4j")
        .build();
  }

  public List<Employee> getEmployee() {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query = """
          match (e:Employee)
          return e.employeeId as id, e.name as name, e.title as title
          order by e.employeeId""";
      Value params = Values.parameters();

      List<Record> employeeRecords = session.run(query, params).list();
      List<Employee> employees = new ArrayList<>();
      for (Record record : employeeRecords) {
        Employee employee = Employee.builder()
            .id(record.get("id").asString())
            .name(record.get("name").asString())
            .title(record.get("title").asString())
            .build();
        employees.add(employee);
      }
      return employees;
    }
  }

  public List<Employee> getEmployee(String role) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query = "match (e:Employee)-[]->(ro:Role {name: \"" + role + "\"})\n" +
          "return e.employeeId as id, e.name as name, e.title as title\n" +
          "order by e.employeeId";
      Value params = Values.parameters();

      List<Record> employeeRecords = session.run(query, params).list();
      List<Employee> employees = new ArrayList<>();
      for (Record record : employeeRecords) {
        Employee employee = Employee.builder()
            .id(record.get("id").asString())
            .name(record.get("name").asString())
            .title(record.get("title").asString())
            .build();
        employees.add(employee);
      }
      return employees;
    }
  }

  public List<Skill> getSkill() {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query = """
          match (s:Skill)
          return s.name as name
          order by s.name""";
      Value params = Values.parameters();

      List<Record> skillRecords = session.run(query, params).list();
      List<Skill> skills = new ArrayList<>();
      for (Record record : skillRecords) {
        Skill skill = Skill.builder()
            .name(record.get("name").asString())
            .build();
        skills.add(skill);
      }
      return skills;
    }
  }

  public List<Skill> getSkill(String role) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query = "match (s:Skill)-[]->(ro:Role {name: \"" + role + "\"})\n" +
          "return s.name as name\n" +
          "order by s.name";
      Value params = Values.parameters();

      List<Record> skillRecords = session.run(query, params).list();
      List<Skill> skills = new ArrayList<>();
      for (Record record : skillRecords) {
        Skill skill = Skill.builder()
            .name(record.get("name").asString())
            .build();
        skills.add(skill);
      }
      return skills;
    }
  }

  public List<Project> getProject() {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query = "match (t:Team)\n" +
          "return t.name as name\n" +
          "order by t.name";
      Value params = Values.parameters();

      List<Record> projectRecords = session.run(query, params).list();
      List<Project> projects = new ArrayList<>();
      for (Record record : projectRecords) {
        Project project = Project.builder()
            .name(record.get("name").asString())
            .build();
        projects.add(project);
      }
      return projects;
    }
  }

  public List<Record> getRatingMatrix() {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "match (e:Employee)-[r:RATES]->(s:Skill)\n" +
              "return e.employeeId as employeeId, s.name as skillName, r.rating as rating\n" +
              "order by e.employeeId, s.name";
      Value params = Values.parameters();
      return session.run(query, params).list();
    }
  }

  public List<Record> getRatingMatrix(String role) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "match (e:Employee)-[i:IS_A]->(ro:Role {name: \"" + role + "\"})\n" +
              "with e, ro\n" +
              "match (e)-[r:RATES]->(s:Skill)-[]->(ro)\n" +
              "with e, r, s\n" +
              "return e.employeeId as employeeId, s.name as skillName, r.rating as rating\n" +
              "order by e.employeeId, s.name";
      Value params = Values.parameters();
      return session.run(query, params).list();
    }
  }


  public List<Record> getRequireMatrix() {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "match (t:Team)-[r:REQUIRES]->(s:Skill)\n" +
              "return t.name as teamName, s.name as skillName, r.rating as rating\n" +
              "order by t.name, s.name";
      Value params = Values.parameters();
      return session.run(query, params).list();
    }
  }

  public List<Record> getRequireMatrix(String role) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "  match (t:Team)-[r:REQUIRES]->(s:Skill)-[]->(ro:Role {name: \"" + role + "\"})\n" +
              "      return t.name as teamName, s.name as skillName, r.rating as rating\n" +
              "  order by t.name, s.name";
      Value params = Values.parameters();
      return session.run(query, params).list();
    }
  }
}
