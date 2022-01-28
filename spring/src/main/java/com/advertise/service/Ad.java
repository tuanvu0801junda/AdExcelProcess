package com.advertise.service;

import com.advertise.entity.Advertisement;
import com.advertise.entity.Campaign;
import com.advertise.entity.ErrorExcel;
import com.advertise.repository.AdvertisementRepo;
import com.advertise.repository.CampaignRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class Ad {

    public static final String ActiveStatus = "Active";
    public static final String PausedStatus = "Paused";
    public static final String RemovedStatus = "Removed";
    private CampaignRepo campaignRepo;
    private AdvertisementRepo advertisementRepo;

    @Autowired
    public Ad(CampaignRepo campaignRepo, AdvertisementRepo advertisementRepo) {
        this.campaignRepo = campaignRepo;
        this.advertisementRepo = advertisementRepo;
    }

    private static SimpleDateFormat simpleDateFormat;


    public void readExcel() throws NullPointerException,IOException{
        String excelFilePath = "Advertising_Template_1.xlsx";
        /*
        Định dạng file error là XLSX, chỉ có duy nhất 1 sheet ERROR, thông tin error bao gồm:
        - Tên sheet (VD: Campaign)
        - Tên header (VD: Campaign Name)
        - Row number (VD: 3)
        - Error message (VD: Không được phép vượt quá 10 ký tự)
         */
        List<Campaign> campaigns = readCampaignFromExcel(excelFilePath);
        List<Advertisement> advertisements = readAdvertisementFromExcel(excelFilePath);
    }

    public List<Campaign> readCampaignFromExcel(String excelFilePath) throws NullPointerException, IOException {
        List<Campaign> campaigns = new ArrayList<>();
        InputStream inputStream = new FileInputStream(excelFilePath);
        ErrorExcel excel = new ErrorExcel();

        //get Workbook & Sheet Campaign (index = 0th)
        Workbook workbook = getWorkbook(inputStream, excelFilePath);
        if (workbook == null) throw new NullPointerException("workbook is null");
        Sheet sheet = workbook.getSheetAt(0);

        if(!sheet.getSheetName().equals("Campaign")) {
            excel.setErrMessage("Sheet 1: Campaign!");
        }
        else {
            for (Row nextRow : sheet) {
                if (nextRow.getRowNum() == 0) continue;
                if (checkIfRowIsEmpty(nextRow)) break;

                // Get all cells
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                // 1 row = 1 Campaign object
                Campaign campaign = new Campaign();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Object cellValue = getCellValue(cell);
                    if (cellValue == null || cellValue.toString().isEmpty()) continue;

                    // Set value for book object
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex) {
                        case Campaign.campaignID_ColumnIndex:
                            if (cell.getCellType() != CellType.NUMERIC)
                                throw new IOException("CampaignId must be a numberic");
                            else {
                                int id = (int) Math.round((Double) getCellValue(cell));
                                if (id > 1000000000) throw new IOException("Campaign Id surpass 10 digits");
                                else campaign.setCampaignID(id);
                            }
                            break;

                        case Campaign.campaignName_ColumnIndex:
                            if (cell.getCellType() != CellType.STRING)
                                throw new IOException("CampaignName must be format string");
                            else {
                                String name = (String) getCellValue(cell);
                                if (name.length() <= 25) campaign.setCampaignName(name);
                                else {
                                    throw new IOException("CampaignName length cannot surpass 25");
                                }
                            }
                            break;

                        case Campaign.campaignStatus_ColumnIndex:
                            if (cell.getCellType() != CellType.STRING)
                                throw new IOException("CampaignStatus must be format string");
                            else {
                                String status = (String) getCellValue(cell);
                                if (checkIfStatusValid(status)) {
                                    campaign.setCampaignStatus(status);
                                } else {
                                    throw new IOException("CampaignStatus must be \"Active\", \"Paused\" or \"Removed\"");
                                }
                            }
                            break;

                        case Campaign.startDate_ColumnIndex:
                            //check format of date !!!
                            Date start = (Date) getCellValue(cell);
                            if(start.compareTo(new Date()) < 0){
                                throw new IOException("startDate cannot be before today: "+simpleDateFormat.format(new Date()));
                            } else campaign.setStartDate(start);
                            break;

                        case Campaign.endDate_ColumnIndex:
                            //check format of date !!!
                            Date end = (Date) getCellValue(cell);
                            if(end.compareTo(new Date()) < 0){
                                throw new IOException("endDate cannot be before today: "+simpleDateFormat.format(new Date()));
                            } else campaign.setEndDate(end);
                            break;

                        case Campaign.budget_ColumnIndex:
                            if (cell.getCellType() != CellType.NUMERIC)
                                throw new IOException("Campaign's budget must be a numberic");
                            else {
                                int budget = (int) Math.round((Double) getCellValue(cell));
                                if (budget > 100000000) throw new IOException("Campaign Id surpass 9 digits");
                                else campaign.setBudget(budget);
                            }
                            break;

                        default:
                            break;
                    }
                }
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
        }
        return campaigns;
    }

    public List<Advertisement> readAdvertisementFromExcel(String excelFilePath) throws NullPointerException, IOException {
        List<Advertisement> advertisements = new ArrayList<>();
        InputStream inputStream = new FileInputStream(new File(excelFilePath));
        ErrorExcel excel = new ErrorExcel();

        //get Workbook & sheet 1st = "Ad"
        Workbook workbook = getWorkbook(inputStream, excelFilePath);
        if (workbook == null) throw new NullPointerException("Extension must be .xlsx");
        Sheet sheet = workbook.getSheetAt(1);

        if(!sheet.getSheetName().equals("Ad")) {
            excel.setErrMessage("Sheet 2: ");
        } else {
            for (Row nextRow : sheet) {
                if (nextRow.getRowNum() == 0) continue;
                if (checkIfRowIsEmpty(nextRow)) break;
                // Get all cells
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                // Read cells and set value for CAMPAIGN objects
                Advertisement advertisement = new Advertisement();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Object cellValue = getCellValue(cell);
                    if (cellValue == null || cellValue.toString().isEmpty()) {
                        continue;
                    }

                    // Set value for book object
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex) {
                        case Advertisement.adID_ColumnIndex:
                            if(cell.getCellType() != CellType.NUMERIC){
                                throw new IOException("Ad-ID must be format as numeric");
                            } else {
                                int id = (int) Math.round((Double) getCellValue(cell));
                                if (id > 1000000000) throw new IOException("Campaign Id surpass 10 digits");
                                else advertisement.setAdID(id);
                            }
                            break;

                        case Advertisement.adName_ColumnIndex:
                            if(cell.getCellType() != CellType.STRING){
                                throw new IOException("Ad-Name must be format as string");
                            } else {
                                String name = (String) getCellValue(cell);
                                if (name.length() <= 25) advertisement.setAdName(name);
                                else {
                                    throw new IOException("CampaignName length cannot surpass 25");
                                }
                            }
                            break;

                        case Advertisement.adStatus_ColumnIndex:
                            if (cell.getCellType() != CellType.STRING)
                                throw new IOException("Ad-Status must be format string");
                            else {
                                String status = (String) getCellValue(cell);
                                if (!checkIfStatusValid(status)) {
                                    throw new IOException("CampaignStatus must be \"Active\", \"Paused\" or \"Removed\"");
                                } else {
                                    advertisement.setAdStatus(status);
                                }
                            }
                            break;

                        case Advertisement.adType_ColumnIndex:
//                            if (cell.getCellType() != CellType.STRING)
//                                throw new Exception("Ad-Status must be format string");
//                            else {
//                                String type = (String) getCellValue(cell);
//                                if (!checkIfStatusValid(type)) {
//                                    throw new Exception("CampaignStatus must be \"Active\", \"Paused\" or \"Removed\"");
//                                } else {
//                                    advertisement.setAdStatus(status);
//                                    advertisement.setAdType();
//                                }
//                            }
//                            advertisement.setAdType(cell.getStringCellValue());
                            break;

                        case Advertisement.bigModifier_ColumnIndex:
                            if (cell.getCellType() != CellType.NUMERIC)
                                throw new IOException("Ad's bigModifier must be a numberic");
                            else {
                                int big = (int) Math.round((Double) getCellValue(cell));
                                if (big > 5000) throw new IOException("Campaign Id surpass 5000");
                                else advertisement.setBigModifier(big);
                            }
                            break;

                        default:
                            break;
                    }
                }
                advertisements.add(advertisement);
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
        }
        return advertisements;
    }

    public Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException{
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")){
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    public Object getCellValue(Cell cell){
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType){
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;

            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)){
                    cellValue = cell.getDateCellValue();
                } else {
                    cellValue = cell.getNumericCellValue();
                }
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
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK){
                return false;
            }
        }
        return true;
    }

    public boolean checkIfStatusValid(String status) throws NullPointerException{
        boolean result = true;
        if (!status.equals(ActiveStatus)){
            if(!status.equals(PausedStatus)){
                if(!status.equals(RemovedStatus)){
                    result = false;
                }
            }
        }
        return result;
    }
}
