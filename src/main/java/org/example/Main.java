package org.example;

import com.alibaba.fastjson.JSONArray;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.vo.CellDate;
import org.example.vo.PersonInfo;
import org.example.vo.PersonInfoOneDay;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    static String path="C:\\shift\\staff.xlsx";

    static String pathOut="C:\\shift\\staff.txt";
    static Integer DAY20230308=44993;

    private static List<Map<String,String>> readFromExlDef() {
        List<Map<String,String>> infos=new ArrayList<>();
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(path);
            XSSFSheet sheet = workbook.getSheetAt(2);
            for (int i = 0; i < 8; i++) {
                if(i==0) continue;
                int rowIndex=0;
                Map<String,String> personInfosYobi=new HashMap<>();
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
                    personInfosYobi.put(personName,personTime);
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

    private static List<PersonInfoOneDay> readFromExl(List<Map<String,String>> def) {
        List<PersonInfoOneDay> infos=new ArrayList<>();
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(path);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowIndex=0;
            for (Row row : sheet) {
                if(rowIndex==0){
                    rowIndex++;
                    continue;
                }
                PersonInfoOneDay personInfoOneDay=PersonInfoOneDay.builder().build();
                CellDate cellDate=getCellDate(row.getCell(0));
                personInfoOneDay.setDate(cellDate.getDateStr());
                int colIndex=0;
                List<PersonInfo> personInfos=new ArrayList<>();
                for (Cell cell: row) {
                    if(colIndex==0){
                        colIndex++;
                        continue;
                    }
                    PersonInfo per=PersonInfo.builder()
                            .name(getCellString(sheet.getRow(0).getCell(colIndex)))
                            .time(getCellString(row.getCell(colIndex))).build();
                    if("".equals(per.getTime().trim())){
                        per.setTime(def.get(cellDate.getYobi()-1).get(per.getName()));
                    }
                    if(per.getTime()!=null&&!"".equals(per.getTime().trim())){
                        personInfos.add(per);
                    }
                    colIndex++;
                }
                personInfoOneDay.setLabels(personInfos);
                infos.add(personInfoOneDay);
                rowIndex++;
            }

            workbook.close();
            return infos;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String getCellString(Cell cell){
        if(cell==null) return "";
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue();
    }
    private static CellDate getCellDate(Cell cell){
         if(cell==null) return CellDate.builder().dateStr("").build();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date dateKijun=sdf.parse("20230308");
            Calendar cal=Calendar.getInstance();
            cal.setTime(dateKijun);
            cal.add(Calendar.DATE,Integer.parseInt(getCellString(cell).split("\\.")[0])-DAY20230308);
            return CellDate.builder().date(cal.getTime()).dateStr(sdf.format(cal.getTime()))
                    .yobi(cal.get(Calendar.DAY_OF_WEEK)).build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static String outPut(List<PersonInfoOneDay> personInfoOneDays){
        String ans=JSONArray.toJSONString(personInfoOneDays);
        try {
            FileWriter fw=new FileWriter(pathOut);
            fw.write(ans);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ans;
    }
    public static void main(String[] args) {
        List<Map<String, String>> def = readFromExlDef();
        System.out.println(outPut(readFromExl(def)));
//        readFromExl();
    }
}