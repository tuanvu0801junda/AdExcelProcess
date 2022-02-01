package com.advertise.service;

import com.advertise.entity.ErrorExcel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;

@Service
public class ErrorExcelHandler {
    private final String errExcelPath = System.getProperty("user.home") + "/Downloads/uploads/Error.xlsx";
    //private final String errExcelPath = "Error.xlsx";
    public final int SHEET_NAME_COL = 0;
    public final int HEADER_NAME_COL = 1;
    public final int ROW_NUM_COL = 2;
    public final int ERR_MSG_COL = 3;

    public Workbook getWorkbookToWrite(String excelPath) throws IOException{
        // File extension: XLSX
        if (excelPath.endsWith("xlsx")) return new XSSFWorkbook();
        else throw new IOException("Extension must be \"xlsx\"");
    }

    public void writeHeader(Sheet sheet, int rowIndex){
        // create cell style
        CellStyle cellStyle = createStyleForHeader(sheet);

        // Create 1 header row
        Row row = sheet.createRow(rowIndex);

        // Create cells (1 row = row(s))
        Cell cell = row.createCell(SHEET_NAME_COL);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Sheet Name");

        cell = row.createCell(HEADER_NAME_COL);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Header Name");

        cell = row.createCell(ROW_NUM_COL);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Error Row Index");

        cell = row.createCell(ERR_MSG_COL);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Error Message");
    }

    public CellStyle createStyleForHeader(Sheet sheet){
        Font font = sheet.getWorkbook().createFont(); //init font obj
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); //font size
        font.setColor(IndexedColors.WHITE.getIndex()); //header's text WHITE

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle(); //init cellStyle obj
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex()); //enum is an obj
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }

    public void writeEachRow(ErrorExcel errorExcel, Row row){
        Cell cell = row.createCell(SHEET_NAME_COL);
        cell.setCellValue(errorExcel.getSheetName());

        cell = row.createCell(HEADER_NAME_COL);
        cell.setCellValue(errorExcel.getHeaderName());

        cell = row.createCell(ROW_NUM_COL);
        cell.setCellValue(errorExcel.getRowNum());

        cell = row.createCell(ERR_MSG_COL);
        cell.setCellValue(errorExcel.getErrMessage());
    }

    // Auto resize column width
    public void autoSizeColumn(Sheet sheet, int lastColumn){
        for(int columnIndex = 0; columnIndex < lastColumn; columnIndex++){
            sheet.autoSizeColumn(columnIndex);
        }
    }

    public void createOutputFile(Workbook workbook){
        try{
            OutputStream os = new FileOutputStream(errExcelPath);
            workbook.write(os);
        }  catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    public void writeErrorExcel(ArrayList<ErrorExcel> errExcelList) throws IOException{
        Workbook workbook = getWorkbookToWrite(errExcelPath);

        // Contain ONLY 1 SHEET: Error
        Sheet sheet = workbook.createSheet("Error");

        // Write Header (black foreground, white letters)
        int rowIndex = 0;
        writeHeader(sheet,rowIndex);

        // Write data for each row, with 1 instance of excelErr
        rowIndex++;
        for(ErrorExcel excel: errExcelList){
            Row row = sheet.createRow(rowIndex);    //line number i-th
            writeEachRow(excel,row);
            rowIndex++;
        }

        // Auto resize column width (number of cell in a row = number of column)
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        autoSizeColumn(sheet,numberOfColumn);

        // Create file
        createOutputFile(workbook);
    }

}
