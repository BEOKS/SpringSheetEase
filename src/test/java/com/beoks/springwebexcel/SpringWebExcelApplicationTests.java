package com.beoks.springwebexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringWebExcelApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void pet() throws Exception {
        mockMvc.perform(get("/pet"))
                .andExpect(status().isOk())
                .andExpect(content().string(readJsonFromFile()));
    }

    @Test
    void petRest() throws Exception {
        mockMvc.perform(get("/rest/pet"))
                .andExpect(status().isOk())
                .andExpect(content().string(readJsonFromFile()));
    }

    @Test
    void petExcel() throws Exception {
        MvcResult result = mockMvc.perform(get("/pet/excel"))
                .andExpect(status().isOk())
                .andReturn();

        byte[] responseBytes = result.getResponse().getContentAsByteArray();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(responseBytes)) {
            XSSFWorkbook workbook = new XSSFWorkbook(bis);
            System.out.println(workbook.getSheetName(0));
            // 첫 번째 시트 접근
            Sheet sheet = workbook.getSheetAt(0);

            // 시트의 모든 행과 셀 순회
            for (Row row : sheet) {
                for (Cell cell : row) {
                    // 셀의 타입에 따라 적절하게 값을 읽음
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t");
                            break;
                        case FORMULA:
                            System.out.print(cell.getCellFormula() + "\t");
                            break;
                        default:
                            System.out.print(" " + "\t");
                    }
                }
                System.out.println(); // 행이 끝날 때마다 줄바꿈
            }

            // XSSFWorkbook 사용이 끝난 후 반드시 close 호출
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void petExcelAop() throws Exception {
        MvcResult result = mockMvc.perform(get("/pet/excel/aop"))
                .andExpect(status().isOk())
                .andReturn();
        byte[] responseBytes = result.getResponse().getContentAsByteArray();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(responseBytes)) {
            XSSFWorkbook workbook = new XSSFWorkbook(bis);
            System.out.println(workbook.getSheetName(0));
            // 첫 번째 시트 접근
            Sheet sheet = workbook.getSheetAt(0);

            // 시트의 모든 행과 셀 순회
            for (Row row : sheet) {
                for (Cell cell : row) {
                    // 셀의 타입에 따라 적절하게 값을 읽음
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t");
                            break;
                        case FORMULA:
                            System.out.print(cell.getCellFormula() + "\t");
                            break;
                        default:
                            System.out.print(" " + "\t");
                    }
                }
                System.out.println(); // 행이 끝날 때마다 줄바꿈
            }

            // XSSFWorkbook 사용이 끝난 후 반드시 close 호출
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readJsonFromFile() throws IOException {
        File file = ResourceUtils.getFile("classpath:petDummy.json");
        return new String(Files.readAllBytes(file.toPath()));
    }

}
