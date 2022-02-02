package com.advertise.service;

import com.advertise.entity.Advertisement;
import com.advertise.entity.ErrorExcel;
import com.advertise.repository.AdvertisementRepo;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AdExcelService extends ExcelService {

    @Autowired
    private AdvertisementRepo advertisementRepo;

    public boolean isAdTypeValid(String adType) {
        boolean result = true;
        if (!adType.equals("Search")){
            if(!adType.equals("Video")){
                if(!adType.equals("Display")) result = false;
            }
        }
        return result;
    }

    public List<Advertisement> readAdFromExcel(String filename) throws NullPointerException, IOException {
        List<Advertisement> advertisements = new ArrayList<>();
        String headerName;
        String sheetName = "Ad";
        String path = this.excelPath + filename;
        ErrorExcel errorExcel;
        this.errList = new ArrayList<>();
        InputStream inputStream = new FileInputStream(path);

        // get Workbook & second sheet = "Ad"
        Workbook workbook = getWorkbookToRead(inputStream, path);
        Sheet sheet = workbook.getSheetAt(1);

        if(!sheet.getSheetName().equals("Ad")) {
            errorExcel = new ErrorExcel();
            errorExcel.setSheetName(sheet.getSheetName());
            errorExcel.setHeaderName("");
            errorExcel.setRowNum(0);
            errorExcel.setErrMessage("Sheet 2 != Ad");
            errList.add(errorExcel);
            return null;
        }

        for (Row nextRow : sheet) {
            if (nextRow.getRowNum() == 0) continue; // Check Header as well
            if (checkIfRowIsEmpty(nextRow)) break;

            // Get all cells
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            Advertisement advertisement = new Advertisement();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if (cellValue == null || cellValue.toString().isEmpty()) continue;

                // Read row with type
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case Advertisement.adID_ColumnIndex:
                        headerName = "Ad ID";
                        if (cell.getCellType() != CellType.NUMERIC) {
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not Int");
                            errList.add(errorExcel);
                        } else {
                            int id = (int) Math.round((Double) getCellValue(cell));
                            if (id < 1000000000) advertisement.setAdID(id);
                            else{
                                errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "> 10^9");
                                errList.add(errorExcel);
                            }
                        }
                        break;

                    case Advertisement.adName_ColumnIndex:
                        headerName = "Ad Name";
                        if (cell.getCellType() != CellType.STRING) {
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not String");
                            errList.add(errorExcel);
                        } else {
                            String name = (String) getCellValue(cell);
                            if (name.length() <= 25) advertisement.setAdName(name);
                            else {
                                errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Length > 25");
                                errList.add(errorExcel);
                            }
                        }
                        break;

                    case Advertisement.adStatus_ColumnIndex:
                        headerName = "Ad Status";
                        if (cell.getCellType() != CellType.STRING){
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not String");
                            errList.add(errorExcel);
                        } else {
                            String status = (String) getCellValue(cell);
                            if (checkIfStatusValid(status)) advertisement.setAdStatus(status);
                            else {
                                errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not Active, Paused | Removed");
                                errList.add(errorExcel);
                            }
                        }
                        break;

                    case Advertisement.adType_ColumnIndex:
                        headerName = "Ad Type";
                        if (cell.getCellType() != CellType.STRING) {
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not String");
                            errList.add(errorExcel);
                        }
                        else {
                            String type = (String) getCellValue(cell);
                            if (isAdTypeValid(type)) advertisement.setAdType(cell.getStringCellValue());
                            else{
                                errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not Search, Display | Video");
                                errList.add(errorExcel);
                            }
                        }
                        break;

                    case Advertisement.bigModifier_ColumnIndex:
                        headerName = "Big Modifier";
                        if (cell.getCellType() != CellType.NUMERIC) {
                            errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "Not Int");
                            errList.add(errorExcel);
                        }
                        else {
                            int big = (int) Math.round((Double) getCellValue(cell));
                            if (big < 5000) advertisement.setBigModifier(big);
                            else{
                                errorExcel = new ErrorExcel(sheetName, headerName, nextRow.getRowNum(), "> 5000");
                                errList.add(errorExcel);
                            }
                        }
                        break;

                    default:
                        break;
                    } //switch
                } //while
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
        return advertisements;
    }

    public void insertNewAdsIntoDB(List<Advertisement> adList){
        for(Advertisement ad: adList){
            advertisementRepo.save(ad);
        }
    }

}
