package com.advertise.service;

import com.advertise.entity.ErrorExcel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Service
public abstract class ExcelHandler {
    protected ArrayList<ErrorExcel> errList;
    public final String ActiveStatus = "Active";
    public final String PausedStatus = "Paused";
    public final String RemovedStatus = "Removed";
    protected final String excelPath = System.getProperty("user.home") + "/Downloads/uploads/";

    public ArrayList<ErrorExcel> getErrList() {
        return this.errList;
    }

    public Workbook getWorkbookToRead(InputStream inputStream, String excelFilePath) throws IOException {
        if (excelFilePath.endsWith("xlsx")) return new XSSFWorkbook(inputStream);
        else throw new IOException("Extension must be .xlsx");
    }

    public Object getCellValue(Cell cell){
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType){
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell))
                    cellValue = cell.getDateCellValue();
                else cellValue = cell.getNumericCellValue();
                break;

            case STRING:
                cellValue = cell.getStringCellValue();
                break;

            case _NONE:
            case BLANK:
            case ERROR:
            default:
                break;
        }
        return cellValue;
    }

    public boolean checkIfRowIsEmpty(Row row) {
        if (row == null) return true;
        if (row.getLastCellNum() <= 0) return true;
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

    public boolean checkIfStatusValid(String status) {
        boolean result = true;
        if (!status.equals(ActiveStatus)){
            if(!status.equals(PausedStatus)){
                if(!status.equals(RemovedStatus)) result = false;
            }
        }
        return result;
    }
}
