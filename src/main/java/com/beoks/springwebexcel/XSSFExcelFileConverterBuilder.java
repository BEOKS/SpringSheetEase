package com.beoks.springwebexcel;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.tomcat.websocket.Util.isPrimitive;

public class XSSFExcelFileConverterBuilder implements ExcelFileConverterBuilder<XSSFWorkbook> {
    private Boolean useFlatten;
    private String fileName;
    private List<?> excelData;
    private String sheetName="Sheet";
    public XSSFExcelFileConverterBuilder() {
        this.fileName = getDefaultFileName();
        this.sheetName="Sheet";
        this.useFlatten=false;
        this.excelData =null;
    }

    private static String getDefaultFileName() {
        return "default_" + DateFormat.getDateTimeInstance().format(new Date()) + ".xlsx";
    }

    @Override
    public ExcelFileConverterBuilder<XSSFWorkbook> data(List<?> objectList) {
        this.excelData =objectList;
        return this;
    }

    @Override
    public ExcelFileConverterBuilder<XSSFWorkbook> fileName(String fileName) {
        this.fileName=fileName;
        return this;
    }

    @Override
    public ExcelFileConverterBuilder<XSSFWorkbook> useFlatten(Boolean useFlatten) {
        this.useFlatten=useFlatten;
        return this;
    }

    @Override
    public ExcelFileConverterBuilder<XSSFWorkbook> sheetName(String sheetName) {
        this.sheetName=sheetName;
        return this;
    }

    @Override
    public XSSFWorkbook build() {
        checkDataValidation();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet=workbook.createSheet("sample data");
        int rowNum=0;
        for(Object object: this.excelData){
            XSSFRow row = sheet.createRow(rowNum++);
            boolean isObjectPrimitive = setPrimitive(object, row);
            if(!isObjectPrimitive){
                try {
                    int colNum=0;
                    Collection<String> values;
                    if(this.useFlatten){
                        values=JsonFlatter.flattenJson(object).values();
                    }
                    else{
                        Map<String, String> map = new ObjectMapper().convertValue(object, Map.class);
                        values=map.values().stream()
                                .map(Object::toString)
                                .collect(Collectors.toList());
                    }
                    for (String value : values) {
                        row.createCell(colNum++).setCellValue(value);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("cannot flatten excel data",e);
                }
            }

        }
        return workbook;
    }

    private static boolean setPrimitive(Object object, XSSFRow row) {
        if(object.equals(boolean.class)){
            row.createCell(0).setCellValue((boolean) object);
            return true;
        }
        if(object.equals(double.class) || object.equals(int.class) || object.equals(float.class) || object.equals(short.class)){
            row.createCell(0).setCellValue((double) object);
            return true;
        }
        if(object.equals(Date.class)){
            assert object instanceof Date;
            row.createCell(0).setCellValue((Date) object);
            return true;
        }
        if(object.equals(LocalDateTime.class)){
            assert object instanceof LocalDateTime;
            row.createCell(0).setCellValue((LocalDateTime) object);
            return true;
        }
        if(object.equals(Calendar.class)){
            assert object instanceof Calendar;
            row.createCell(0).setCellValue((Calendar) object);
            return true;
        }
        if(object.equals(String.class)){
            assert object instanceof String;
            row.createCell(0).setCellValue((String) object);
            return true;
        }
        if(object.equals(RichTextString.class)){
            assert object instanceof RichTextString;
            row.createCell(0).setCellValue((RichTextString) object);
            return true;
        }
        if(object.equals(LocalDate.class)){
            assert object instanceof LocalDate;
            row.createCell(0).setCellValue((LocalDate) object);
            return true;
        }
        return false;
    }

    private void checkDataValidation() {
        if(excelData ==null){
            throw new IllegalArgumentException("xlsx data is null, set data properly with 'data()' function");
        }
        if(!fileName.endsWith(".xlsx")){
            throw new IllegalArgumentException(fileName+" is invalid file name, file name must end with 'xlsx'");
        }
    }
}
