package com.thunguyen.graphhrservice.services;

import com.thunguyen.graphhrservice.dao.SimGraphDAO;
import com.thunguyen.graphhrservice.models.Similarity;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Generator {

  private final RecommendationService recommendationService;
  private final SimGraphDAO simGraphDAO;

  public Map<Integer, Object[]> getEmployeeJobRelationship() {
    Map<Integer, Object[]> relationships = new HashMap<>();
    relationships.put(0, List.of("employeeId", "jobId", "startDate").toArray());
    int count = 1;
    List<String> chosenEmployees = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      int randomYear = new Random().nextInt(10);
      int randomMonth = new Random().nextInt(1, 13);
      int randomDay = new Random().nextInt(1, 30);
      List<Similarity> employees = simGraphDAO.getSimilaritiesForJobId(i + 1).stream()
          .sorted((sim1, sim2) -> Double.compare(sim2.getSimScore(), sim1.getSimScore()))
          .toList();
      int randomIndex = Math.min(5, employees.size());
      if (randomIndex == 0) {
        continue;
      }
      String chosenEmployee = employees.get(new Random().nextInt(0, randomIndex)).getSimilarityId()
          .getEmployeeId();
      int retry = 0;
      while (chosenEmployees.contains(chosenEmployee)) {
        chosenEmployee = employees.get(new Random().nextInt(0, randomIndex)).getSimilarityId()
            .getEmployeeId();
        retry++;
        if (retry > 5) {
          break;
        }
      }
      if (!chosenEmployees.contains(chosenEmployee)) {
        chosenEmployees.add(chosenEmployee);
        relationships.put(count,
            List.of(chosenEmployee, i + 1,
                    (LocalDate.now().getYear() - randomYear) + "-" + randomMonth + "-" + randomDay)
                .toArray());
        count++;
      }
      log.info("Job {}", i + 1);
    }
    return relationships;
  }

  public void writeData(String sheetName, Map<Integer, Object[]> data, String outputPath) {
    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
      XSSFSheet sheet = workbook.createSheet(sheetName);
      XSSFRow row;
      Set<Integer> keyId = data.keySet();

      int rowId = 0;
      for (Integer key : keyId) {
        row = sheet.createRow(rowId++);
        Object[] objectArr = data.get(key);
        int cellId = 0;

        for (Object obj : objectArr) {
          Cell cell = row.createCell(cellId++);
          cell.setCellValue(String.valueOf(obj));
        }
      }

      FileOutputStream out = new FileOutputStream(outputPath);
      workbook.write(out);
      out.close();
      log.info("Done writing for {}!!!", outputPath);
    } catch (IOException ex) {
      log.error("Error while generating data", ex);
    }
  }
}
