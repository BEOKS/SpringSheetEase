package com.beoks.springwebexcel.controller;

import com.beoks.springwebexcel.ExcelDownload;
import com.beoks.springwebexcel.XSSFExcelFileConverterBuilder;
import com.beoks.springwebexcel.model.Pet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/pet")
public class PetController {

    @GetMapping
    ResponseEntity<List<Pet>> get(){
        return ResponseEntity.ok(Pet.getDummy());
    }

    @GetMapping("/excel")
    ResponseEntity<String> getExcel(HttpServletResponse response){
        XSSFExcelFileConverterBuilder builder = new XSSFExcelFileConverterBuilder();
        try(XSSFWorkbook workbook = builder.fileName("test.xlsx").data(Pet.getDummy()).build()){
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            workbook.write(response.getOutputStream());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.fillInStackTrace().getLocalizedMessage());
        }
        return ResponseEntity.ok("");
    }

    @GetMapping("/excel/aop")
    @ResponseBody
    @ExcelDownload(fileName = "pet.xlsx")
    List<Pet> getExcelAop(HttpServletResponse response){
        return Pet.getDummy();
    }

    @GetMapping("/excel/aop/noFlatten")
    @ResponseBody
    @ExcelDownload(fileName = "pet.xlsx",useFlatten = false)
    List<Pet> getFlattenExcelAop(HttpServletResponse response){
        return Pet.getDummy();
    }
}
