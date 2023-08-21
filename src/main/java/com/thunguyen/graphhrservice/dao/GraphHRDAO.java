package com.thunguyen.graphhrservice.dao;

import com.thunguyen.graphhrservice.models.Employee;
import com.thunguyen.graphhrservice.models.EmployeeDto;
import com.thunguyen.graphhrservice.models.Job;
import com.thunguyen.graphhrservice.models.Project;
import com.thunguyen.graphhrservice.models.Role;
import com.thunguyen.graphhrservice.models.Skill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;
import org.springframework.stereotype.Component;

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

  public List<Employee> getEmployeeInfo() {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query = """
          match (e:Employee)-[:REPORTS_TO]->(s:Employee)
          return e.employeeId as employeeId, e.name as name, e.title as title, e.address as address, e.sex as sex, e.sibn as sibn, e.pit as pit, e.dateOfBirth as dateOfBirth, s.name as superiorName
          order by toInteger(e.employeeId)""";
      Value params = Values.parameters();

      List<Record> employeeRecords = session.run(query, params).list();
      List<Employee> employees = new ArrayList<>();
      for (Record record : employeeRecords) {
        Employee employee = Employee.builder()
            .id(record.get("employeeId").asString())
            .name(record.get("name").asString())
            .title(record.get("title").asString())
            .address(record.get("address").asString())
            .sex(record.get("sex").asString())
            .sibn(record.get("sibn").asString())
            .pit(record.get("pit").asString())
            .dateOfBirth(record.get("dateOfBirth").asString())
            .superiorName(record.get("superiorName").asString())
            .build();
        employees.add(employee);
      }
      return employees;
    }
  }

  public Employee getEmployeeInfoById(String employeeId) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "match (e:Employee {employeeId: \"" + employeeId + "\"}) \n"
              + "optional match (e)-[:REPORTS_TO]->(s:Employee)\n"
              + "return e.employeeId as employeeId, e.name as name, e.title as title, e.address as address, e.sex as sex, e.sibn as sibn, e.pit as pit, e.dateOfBirth as dateOfBirth, s.name as superiorName\n"
              + "order by toInteger(e.employeeId)";
      Value params = Values.parameters();

      List<Record> employeeRecords = session.run(query, params).list();
      List<Employee> employees = new ArrayList<>();
      for (Record record : employeeRecords) {
        return Employee.builder()
            .id(record.get("employeeId").asString())
            .name(record.get("name").asString())
            .title(record.get("title").asString())
            .address(record.get("address").asString())
            .sex(record.get("sex").asString())
            .sibn(record.get("sibn").asString())
            .pit(record.get("pit").asString())
            .dateOfBirth(record.get("dateOfBirth").asString())
            .superiorName(record.get("superiorName").asString().equals("null") ? ""
                : record.get("superiorName").asString())
            .build();
      }
      return null;
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
      String query = """
          match (t:Team)
          return t.teamName as name
          order by t.name""";
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

  public List<Map<String, String>> getProjectInfo() {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query = """
          match (t:Team)-[]->(bu:BusinessUnit)
          return t.teamName as teamName, bu.name as buName
          order by t.teamName""";
      Value params = Values.parameters();

      List<Record> projectRecords = session.run(query, params).list();
      List<Map<String, String>> projects = new ArrayList<>();
      for (Record record : projectRecords) {
        Map<String, String> project = new HashMap<>();
        project.put("teamName", record.get("teamName").asString());
        project.put("buName", record.get("buName").asString());
        projects.add(project);
      }
      return projects;
    }
  }

  public List<Map<String, String>> getJobRequirement(String jobId) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query = "match (j:Job {jobId: " + jobId + "})-[r:REQUIRES]->(s:Skill) \n" +
          "return j.jobId as jobId, j.jobName as jobName, r.rating as rating, s.name as skillName \n"
          + "order by j.jobId";
      Value params = Values.parameters();

      List<Record> requirementRecords = session.run(query, params).list();
      List<Map<String, String>> requirements = new ArrayList<>();
      for (Record record : requirementRecords) {
        Map<String, String> requirement = new HashMap<>();
        requirement.put("jobName", record.get("jobName").asString());
        requirement.put("rating", Integer.toString(record.get("rating").asInt()));
        requirement.put("skillName", record.get("skillName").asString());
        requirements.add(requirement);
      }
      return requirements;
    }
  }

  public List<Map<String, String>> getEmployeeRating(String employeeId) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "match (e:Employee {employeeId: \"" + employeeId + "\"})-[r:RATES]->(s:Skill)\n"
              + "return e.employeeId as employeeId, e.name as employeeName, r.rating as rating, s.name as skillName\n"
              + "order by s.name";
      Value params = Values.parameters();
      List<Record> ratingRecords = session.run(query, params).list();
      List<Map<String, String>> ratings = new ArrayList<>();
      for (Record record : ratingRecords) {
        Map<String, String> rating = new HashMap<>();
        rating.put("employeeId", record.get("employeeId").asString());
        rating.put("employeeName", record.get("employeeName").asString());
        rating.put("rating", Integer.toString(record.get("rating").asInt()));
        rating.put("skillName", record.get("skillName").asString());
        ratings.add(rating);
      }
      return ratings;
    }
  }

  public List<Job> getJob() {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query = """
          match (j:Job)
          return j.jobId as jobId, j.jobName as jobName
          order by j.jobId""";
      Value params = Values.parameters();

      List<Record> jobRecords = session.run(query, params).list();
      List<Job> jobs = new ArrayList<>();
      for (Record record : jobRecords) {
        Job job = Job.builder()
            .jobId(record.get("jobId").asInt())
            .jobName(record.get("jobName").asString())
            .build();
        jobs.add(job);
      }
      return jobs;
    }
  }

  public Job getJobInfoByJobId(Integer jobId) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query = "match (j:Job {jobId: " + jobId + "})-[]->(t:Team)-[]->(b:BusinessUnit) \n"
          + "return j.jobId as jobId, j.jobName as jobName, t.teamName as teamName, b.name as buName \n"
          + "order by j.jobId";
      Value params = Values.parameters();

      List<Record> jobRecords = session.run(query, params).list();
      for (Record record : jobRecords) {
        return Job.builder()
            .jobId(record.get("jobId").asInt())
            .jobName(record.get("jobName").asString())
            .teamName(record.get("teamName").asString())
            .buName(record.get("buName").asString())
            .build();
      }
      return null;
    }
  }

  public List<Job> getJobByTeam(String teamName) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query = "match (j:Job)-[]->(t:Team)\n" +
          "where t.teamName = \"" + teamName + "\"\n" +
          "return j.jobId as jobId, j.jobName as jobName\n" +
          "order by j.jobId";
      Value params = Values.parameters();

      List<Record> jobRecords = session.run(query, params).list();
      List<Job> jobs = new ArrayList<>();
      for (Record record : jobRecords) {
        Job job = Job.builder()
            .jobId(record.get("jobId").asInt())
            .jobName(record.get("jobName").asString())
            .build();
        jobs.add(job);
      }
      return jobs;
    }
  }

  public List<Job> getJob(String role) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "match (j:Job {roleName: \"" + role + "\"})\n" +
              "return j.jobId as jobId, j.jobName as jobName\n" +
              "order by j.jobId";
      Value params = Values.parameters();

      List<Record> jobRecords = session.run(query, params).list();
      List<Job> jobs = new ArrayList<>();
      for (Record record : jobRecords) {
        Job job = Job.builder()
            .jobId(record.get("jobId").asInt())
            .jobName(record.get("jobName").asString())
            .roleName(record.get("roleName").asString())
            .build();
        jobs.add(job);
      }
      return jobs;
    }
  }

  public List<Record> getRatingMatrix() {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          """
              match (e:Employee)-[r:RATES]->(s:Skill)
              return e.employeeId as employeeId, s.name as skillName, r.rating as rating
              order by e.employeeId, s.name""";
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

  public List<Record> getRatingMatrixByEmployeeId(String employeeId, String role) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "match (e:Employee {employeeId: \"" + employeeId + "\"})-[i:IS_A]->(ro:Role {name: \""
              + role + "\"})\n" +
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
          """
              match (j:Job)-[r:REQUIRES]->(s:Skill)
              return j.jobId as jobId, s.name as skillName, r.rating as rating
              order by j.jobId, s.name""";
      Value params = Values.parameters();
      return session.run(query, params).list();
    }
  }

  public List<Record> getRequireMatrix(String role) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "  match (j:Job {roleName: \"" + role
              + "\"})-[r:REQUIRES]->(s:Skill)-[]->(ro:Role {name: \"" + role + "\"})\n" +
              "      return j.jobId as jobId, s.name as skillName, r.rating as rating\n" +
              "  order by j.jobId, s.name";
      Value params = Values.parameters();
      return session.run(query, params).list();
    }
  }

  public List<Record> getRequireMatrixByJobId(Integer jobId, String role) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "  match (j:Job {roleName: \"" + role
              + "\", jobId: " + jobId + "})-[r:REQUIRES]->(s:Skill)-[]->(ro:Role {name: \"" + role
              + "\"})\n" +
              "      return j.jobId as jobId, s.name as skillName, r.rating as rating\n" +
              "  order by j.jobId, s.name";
      Value params = Values.parameters();
      return session.run(query, params).list();
    }
  }

  public List<Role> getRoles() {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "match (ro:Role)\n" +
              "return ro.name as name";
      Value params = Values.parameters();
      List<Record> roleRecords = session.run(query, params).list();
      List<Role> roles = new ArrayList<>();
      for (Record record : roleRecords) {
        Role role = Role.builder()
            .name(record.get("name").asString())
            .build();
        roles.add(role);
      }
      return roles;
    }
  }

  public Integer getMatchedRatingCount(String employeeId, Integer jobId) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "match (e:Employee {employeeId: \"" + employeeId + "\"})-[]->(ro:Role)\n" +
              "with e, ro\n" +
              "match (e)-[r:RATES]->(s:Skill)-[]->(ro)\n" +
              "with e, r, s\n" +
              "match (p:Job {jobId: \"" + jobId + "\"})-[re:REQUIRES]->(s)\n" +
              "where r.rating = re.rating\n" +
              "return count(r) as c";

      Value params = Values.parameters();
      return session.run(query, params).list().get(0).get("c").asInt();
    }
  }

  public Integer getRequiredSkillCount(String employeeId, Integer jobId) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "match (e:Employee {employeeId: \"" + employeeId + "\"})-[]->(r:Role)\n" +
              "with e, r\n" +
              "match (p:Job {jobId: \"" + jobId + "\"})-[re:REQUIRES]->(sk:Skill)-[]->(r)\n" +
              "return count(re) as c";

      Value params = Values.parameters();
      return session.run(query, params).list().get(0).get("c").asInt();
    }
  }

  public Integer createJob(String teamName, String title, String roleName) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "MATCH (t:Team {teamName: \"" + teamName + "\"}), (lj:Job)\n"
              + "with t, max(lj.jobId) as maxId \n"
              + "CREATE (j:Job {jobName: \"" + title + "\", jobId: maxId + 1, roleName: \""
              + roleName + "\"})-[:IS_OF]->(t)\n"
              + "return j.jobId as jobId";

      Value params = Values.parameters();
      return session.run(query, params).list().get(0).get("jobId").asInt();
    }
  }

  public void createRequireRelationship(Integer jobId, String skillName, int rating) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "MATCH (j:Job {jobId: " + jobId + "}), (s:Skill {name: \"" + skillName + "\"})\n"
              + "CREATE (j)-[r:REQUIRES {rating: " + rating + "}]->(s)\n"
              + "return r";

      Value params = Values.parameters();
      session.run(query, params);
    }
  }

  public String createEmployee(EmployeeDto employeeDto) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "MATCH (e:Employee)\n"
              + "WITH max(toInteger(e.employeeId)) + 1 as nextEmployeeId\n"
              + "CREATE (ne:Employee {employeeId: toString(nextEmployeeId),"
              + " name: \"" + employeeDto.getName() + "\","
              + " title: \"" + employeeDto.getTitle() + "\","
              + " address: \"" + employeeDto.getAddress() + "\","
              + " sex: \"" + employeeDto.getSex() + "\","
              + " sibn: \"" + employeeDto.getSibn() + "\","
              + " pit: \"" + employeeDto.getPit() + "\","
              + " dateOfBirth: \"" + employeeDto.getDateOfBirth() + "\"})\n"
              + "RETURN ne.employeeId as employeeId\n";

      Value params = Values.parameters();
      return session.run(query, params).list().get(0).get("employeeId").asString();
    }
  }

  public String createIsARelationship(String employeeId, String roleName) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "MATCH (e:Employee {employeeId: \"" + employeeId + "\"}), (r:Role {name: \"" + roleName
              + "\"})\n"
              + "CREATE (e)-[i:IS_A]->(r)\n"
              + "return type(i) as type";

      Value params = Values.parameters();
      return session.run(query, params).list().get(0).get("type").asString();
    }
  }

  public String createRatesRelationship(String employeeId, String skillName, Integer rating) {
    try (Session session = neo4jDriver.session(sessionConfig)) {
      String query =
          "MATCH (e:Employee {employeeId: \"" + employeeId + "\"}), (s:Skill {name: \"" + skillName
              + "\"})\n"
              + "CREATE (e)-[r:RATES {rating: " + rating + "}]->(s)\n"
              + "return type(r) as type";

      Value params = Values.parameters();
      return session.run(query, params).list().get(0).get("type").asString();
    }
  }

}
