package com.example.book;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadExcelBook {
    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_TITLE = 1;
    public static final int COLUMN_INDEX_PRICE = 2;
    public static final int COLUMN_INDEX_QUANTITY = 3;
    public static final int COLUMN_INDEX_TOTAL = 4;

    public static void main(String[] args) throws IOException {
        final String excelFilePath = "Book.xlsx";
        final List<Book> books = readExcel(excelFilePath);
        for(Book book: books){
            //System.out.println(book);
            book.printInfo();
        }
    }

    public static List<Book> readExcel(String excelFilePath) throws IOException {
        List<Book> listBooks = new ArrayList<>();

        // Get file
        InputStream inputStream = new FileInputStream(new File(excelFilePath));

        // Get workbook
        //Workbook workbook = getWorkbook(inputStream, excelFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        // Get sheet (1st sheet)
        //Sheet sheet = workbook.getSheetAt(0);
        XSSFSheet sheet = workbook.getSheetAt(0);

        // Get all rows
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()){
            Row nextRow = iterator.next();
            if(nextRow.getRowNum() == 0){
                continue; //ignore header
            }

            if (checkIfRowIsEmpty(nextRow)) break;
            // Get all cells
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            // Read cells and set value for book object
            Book book = new Book();
            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if(cellValue == null || cellValue.toString().isEmpty()){
                    //continue;
                    break;
                }

                // Set value for book object
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex){
                    case COLUMN_INDEX_ID:
                        // the 1st column is id
                        book.setId(BigDecimal.valueOf((double) cellValue).intValue());
                        break;

                        case COLUMN_INDEX_TITLE:
                        // the 2nd column is title
                        book.setTitle((String) getCellValue(cell));
                        break;

                    case COLUMN_INDEX_QUANTITY:
                        // the 3rd column is quantity
                        book.setQuantity(BigDecimal.valueOf((double) cellValue).intValue());
                        break;

                    case COLUMN_INDEX_PRICE:
                        // the 4th column is price
                        book.setPrice((Double) getCellValue(cell));
                        break;

                    case COLUMN_INDEX_TOTAL:
                        // the 5th column is total money
                        book.setTotalMoney((Double) getCellValue(cell));
                        break;

                    default:
                        break;
                }
            }
            listBooks.add(book);
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
        return listBooks;
    }


    // Get Workbook
    public static Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")){
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")){
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IOException("The specified file is not existed!\n");
        }
        return workbook;
    }

    public static Object getCellValue(Cell cell){
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
                cellValue = cell.getNumericCellValue();
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

    public static boolean checkIfRowIsEmpty(Row row) {
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
}
