package com.beoks.springwebexcel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface ExcelFileConverterBuilder<T> {

    ExcelFileConverterBuilder<T> data(List<?> objectList);
    ExcelFileConverterBuilder<T> fileName(String fileName);

    ExcelFileConverterBuilder<T> useFlatten(Boolean useFlatten);

    ExcelFileConverterBuilder<T> sheetName(String sheetName);

    T build();
}
