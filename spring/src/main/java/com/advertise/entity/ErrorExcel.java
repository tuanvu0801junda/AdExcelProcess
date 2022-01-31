package com.advertise.entity;

public class ErrorExcel {
    private String sheetName;
    private String headerName;
    private int rowNum;
    private String errMessage;

    public ErrorExcel(){}

    public ErrorExcel(String sheetName, String headerName, int rowNum, String errMessage) {
        this.sheetName = sheetName;
        this.headerName = headerName;
        this.rowNum = rowNum;
        this.errMessage = errMessage;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}