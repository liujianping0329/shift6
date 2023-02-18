package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.vo.PersonInfo;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static String path="C:\\shift\\staff.xlsx";

    private static List<List<PersonInfo>> readFromExlDef() {
        List<List<PersonInfo>> infos=new ArrayList<>();
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(path);
            XSSFSheet sheet = workbook.getSheetAt(2);
            for (int i = 0; i < 8; i++) {
                if(i==0) continue;
                int rowIndex=0;
                List<PersonInfo> personInfosYobi=new ArrayList<>();
                for (Row row : sheet){
                    if(rowIndex==0){
                        rowIndex++;
                        continue;
                    }
                    String personName=getCellString(sheet.getRow(rowIndex).getCell(0));
                    if("".equals(personName.trim())){
                        break;
                    }
                    String personTime=getCellString(sheet.getRow(rowIndex).getCell(i));
                    personInfosYobi.add(PersonInfo.builder()
                            .name(personName).time(personTime).build());
                    rowIndex++;
                }
                infos.add(personInfosYobi);
            }

            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return infos;
    }

    private static void readFromExl() {
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(path);
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
    private static String getCellString(Cell cell){
        if(cell==null) return "";
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue();
    }
    public static void main(String[] args) {
        readFromExlDef();
//        readFromExl();
    }
}