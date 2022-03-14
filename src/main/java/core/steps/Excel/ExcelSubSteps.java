package core.steps.Excel;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.annotations.StepExt;
import core.sql.SQLManager;
import core.steps.common.UtilSteps.UtilSubSteps;
import core.steps.sql.SQLSubSteps;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Iterator;


public class ExcelSubSteps  {

    private static HSSFWorkbook book;
    private static HSSFSheet sheet;
    UtilSubSteps utilSubSteps = new UtilSubSteps();
    SQLSubSteps sqlSubSteps = new SQLSubSteps();

    public void close() {
        try {
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @StepExt("open book by path \"{path}\"")
    public void openBook(String path) {
        if (book!=null) {
            try {
                book.close();
            } catch (IOException e) {
                Assert.fail(String.format("can not open document %s",path));
                e.printStackTrace();
            }
        }
        try {
            book = new HSSFWorkbook(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EncryptedDocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @StepExt
    public int getColumnByValue(String value)  {
        String name;
        int i = 0;
        HSSFRow row = sheet.getRow(0);
        boolean isFind=false;
        while (row.getCell(i) != null && !isFind ) {
            name = getCellValue(0,i);
            if (name.trim().equals(value.trim())) {
                isFind = true;
            }
            else
                i++;
        }
        if(!isFind) {
            Assert.fail("Can not get column name: " + value);}
        return i;
    }

    public String getCellValue(int rowNum, int cellNum) {
        String result=null;
        HSSFRow row = sheet.getRow(rowNum);
        switch (row.getCell(cellNum).getCellTypeEnum()) {
            case STRING:
                result = row.getCell(cellNum).getStringCellValue();
                break;
            case NUMERIC:
                final NumberFormat format = NumberFormat.getInstance();
                format.setGroupingUsed(false);
                if (format.format( row.getCell(cellNum).getNumericCellValue()  ).toString().contains(",")){
                    result=format.format( row.getCell(cellNum).getNumericCellValue()  ).toString().replace(",","");
                }
                else
                    result=format.format( row.getCell(cellNum).getNumericCellValue()  ).toString();
                break;
            case BLANK:
                result=" ";
                break;
            case FORMULA:
                final NumberFormat FORMULA = NumberFormat.getInstance();
                FORMULA.setGroupingUsed(false);
                if (FORMULA.format(row.getCell(cellNum).getNumericCellValue()).toString().contains(",")){
                    result=FORMULA.format( row.getCell(cellNum).getNumericCellValue()  ).toString().replace(",","");
                }
                else
                    result=FORMULA.format( row.getCell(cellNum).getNumericCellValue()  ).toString();
                break;
            default:
                Assert.fail("Unknown string format  " + row.getCell(cellNum).getCellTypeEnum());
        }
        return result;
    }

    @StepExt("Set value \"{5}\" to row \"{4}\"")
    public void setExelVal(String name,String file,String nameSheet,int rowNum,String value)  {
        sheet = book.getSheet(nameSheet);
        HSSFRow row = sheet.getRow(rowNum);
        int numCell = this.getColumnByValue(name);
        Cell cell = row.getCell(numCell);
        cell.setCellValue(value);
        try {
            book.write(new FileOutputStream(file));
        } catch (IOException e) {
            Assert.fail(String.format("Can not write to file %s text %s",file,value));
            e.printStackTrace();
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("compare excel file \"{file1}\" with excel file \"{file2}\"")
    public void stepCompareExcelFileWithExcelFile(String file1, String file2){
        try {
            XSSFWorkbook workbook1 = new XSSFWorkbook(new FileInputStream(file1));
            XSSFWorkbook workbook2 = new XSSFWorkbook(new FileInputStream(file2));

            XSSFSheet workSheet1 = workbook1.getSheetAt(0);
            XSSFSheet workSheet2 = workbook2.getSheetAt(0);
            String acctIDValueWB1 = "";
            String statusValueWB1 = "";
            String acctIDValueWB2 = "";
            String statusValueWB2 = "";


            Iterator<Row> rows = workSheet1.rowIterator();
            while (rows.hasNext()) {
                XSSFRow row = (XSSFRow) rows.next();
                XSSFCell cell1 = row.getCell(0);
                XSSFCell cell2 = row.getCell(1);
                acctIDValueWB1 += cell1.getStringCellValue();
                acctIDValueWB1 += "\n";
                statusValueWB1 += cell2.getStringCellValue();
                statusValueWB1 += "\n";
            }
            Iterator<Row> rows2 = workSheet2.rowIterator();
            while (rows2.hasNext()) {
                XSSFRow row = (XSSFRow) rows2.next();
                XSSFCell cell1 = row.getCell(0);
                XSSFCell cell2 = row.getCell(1);
                acctIDValueWB2 += cell1.getStringCellValue();
                acctIDValueWB2 += "\n";
                statusValueWB2 += cell2.getStringCellValue();
                statusValueWB2 += "\n";
                int lastCellNum = row.getLastCellNum();
            }
            utilSubSteps.stepValueEquals(acctIDValueWB2,acctIDValueWB1);
            utilSubSteps.stepValueEquals(statusValueWB2,statusValueWB1);
        }catch (IOException ex){
            throw new AssertionError("File not found");
        }
    }


    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("update T24 DB from file OFS \"{file}\" from sheet \"{sheet}\"")
    public void stepWhenUpdateT24DBFromFile(String file, String sheet){
        XSSFWorkbook workbook1 = null;
        try {
            workbook1 = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet1 = workbook1.getSheet(sheet);
            Iterator<Row> iteratorRow =  sheet1.rowIterator();
            while(iteratorRow.hasNext()){
                XSSFRow row = (XSSFRow) iteratorRow.next();
                Iterator<Cell> iteratorCell = row.iterator();
                while(iteratorCell.hasNext()){
                    XSSFCell cell = (XSSFCell) iteratorCell.next();
                    if(cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                        switch(cell.getCachedFormulaResultType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                break;
                            case Cell.CELL_TYPE_STRING:
                                if(cell.getStringCellValue().contains(System.getProperty("testDB.schema"))){
                                    XSSFRichTextString query = cell.getRichStringCellValue();
                                    SQLManager.getInstance().get().LocalDB.executeUpdate(query.getString());
                                }
                                break;
                        }
                    }
                }
            }
        }catch (IOException ex){
            throw new AssertionError("File not found");
        }
        finally {
            try {
                workbook1.close();
            }catch (IOException ex){
                throw new AssertionError("File not found");
            }
        }
    }
}
