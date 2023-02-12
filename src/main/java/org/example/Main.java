package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.IOException;

public class Main {

    private static void readFromExl() {
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook("C:\\shift\\staff.xlsx");
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                for (Cell cell: row) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = cell.getStringCellValue();
                    System.out.println(value);
                }
            }

            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        readFromExl();
    }
}