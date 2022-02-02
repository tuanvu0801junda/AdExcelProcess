package com.advertise.service;

import com.advertise.entity.Campaign;
import com.advertise.entity.ErrorExcel;
import com.advertise.repository.CampaignRepo;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class CampaignExcelService extends ExcelService {

    @Autowired
    private CampaignRepo campaignRepo;

    public List<Campaign> readCampaignFromExcel(String filename) throws NullPointerException, IOException {
        List<Campaign> campaigns = new ArrayList<>();
        String headerName;
        String sheetName = "Campaign";
        ErrorExcel errorExcel;
        String path = this.excelPath + filename;
        this.errList = new ArrayList<>();
        InputStream inputStream = new FileInputStream(path);

        //get Workbook & Sheet Campaign (index = 0th)
        Workbook workbook = getWorkbookToRead(inputStream, path);
        Sheet sheet = workbook.getSheetAt(0);

        if(!sheet.getSheetName().equals("Campaign")) {
            errorExcel = new ErrorExcel();
            errorExcel.setSheetName(sheet.getSheetName());
            errorExcel.setHeaderName("");
            errorExcel.setRowNum(0);
            errorExcel.setErrMessage("Sheet 1 != Campaign");
            errList.add(errorExcel);
            return null;
        }

        for (Row nextRow : sheet) {
            if (nextRow.getRowNum() == 0) continue; // Check Header as well
            if (checkIfRowIsEmpty(nextRow)) break;

            // Get all cells
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            Campaign campaign = new Campaign();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if (cellValue == null || cellValue.toString().isEmpty()) continue;

                // Read row with type
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case Campaign.campaignID_ColumnIndex:
                        headerName = "Campaign ID";
                        if (cell.getCellType() != CellType.NUMERIC) {
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not Int");
                            errList.add(errorExcel);
                        }
                        else {
                            int id = (int) Math.round((Double) getCellValue(cell));
                            if (id < 1000000000) campaign.setCampaignID(id);
                            else{
                                errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "> 10^9");
                                errList.add(errorExcel);
                            }
                        }
                        break;

                    case Campaign.campaignName_ColumnIndex:
                        headerName = "Campaign Name";
                        if (cell.getCellType() != CellType.STRING) {
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not String");
                            errList.add(errorExcel);
                        }
                        else {
                            String name = (String) getCellValue(cell);
                            if (name.length() <= 25) campaign.setCampaignName(name);
                            else {
                                errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Length > 25");
                                errList.add(errorExcel);
                            }
                        }
                        break;

                    case Campaign.campaignStatus_ColumnIndex:
                        headerName = "Campaign Status";
                        if (cell.getCellType() != CellType.STRING) {
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not String");
                            errList.add(errorExcel);
                        }
                        else {
                            String status = (String) getCellValue(cell);
                            if (checkIfStatusValid(status)) campaign.setCampaignStatus(status);
                            else {
                                errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not Active, Paused | Removed");
                                errList.add(errorExcel);
                            }
                        }
                        break;

                    case Campaign.startDate_ColumnIndex:
                        /*
                        java.util.Date utilStartDate = jDateChooserStart.getDate();
                        java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
                        */
                        headerName = "Start Date";
                        Date start = (Date) getCellValue(cell);
                        if(start.compareTo(new Date()) > 0) campaign.setStartDate(new java.sql.Date(start.getTime()));
                        else{
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Before today!");
                            errList.add(errorExcel);
                        }
                        break;

                    case Campaign.endDate_ColumnIndex:
                        headerName = "End Date";
                        Date end = (Date) getCellValue(cell);
                        if(end.compareTo(new Date()) < 0){
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Before today!");
                            errList.add(errorExcel);
                        } else if (end.compareTo(campaign.getStartDate()) < 0){
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Before StartDate!");
                            errList.add(errorExcel);
                        } else campaign.setEndDate(new java.sql.Date(end.getTime()));
                        break;

                    case Campaign.budget_ColumnIndex:
                        headerName = "Budget";
                        if (cell.getCellType() != CellType.NUMERIC) {
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not Int");
                            errList.add(errorExcel);
                        }
                        else {
                            int budget = (int) Math.round((Double) getCellValue(cell));
                            if (budget < 100000000) campaign.setBudget(budget);
                            else{
                                errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "> 10^8");
                                errList.add(errorExcel);
                            }
                        }
                        break;

                    default:
                        break;
                } // switch
            } // while
            campaigns.add(campaign);
        }
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return campaigns;
    }

    public void insertCampaignsIntoDB(List<Campaign> campaigns){
        for(Campaign campaign: campaigns){
            campaignRepo.save(campaign);
        }
    }
}
