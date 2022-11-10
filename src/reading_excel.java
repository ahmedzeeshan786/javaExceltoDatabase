import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.print.DocFlavor.STRING;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class reading_excel {
    public static void main(String[] args)throws FileNotFoundException,IOException,Exception {
        FileInputStream inputStream=null;
        try{
        final  String path=".//datafiles//testExcel.xlsx" ;   
        inputStream=new FileInputStream(path);
        }
        catch(FileNotFoundException e){
            System.out.println("File Not Found");
        }
        XSSFWorkbook workbook=new XSSFWorkbook(inputStream);
        System.out.println(workbook.getNumberOfSheets());
        XSSFSheet sheet=workbook.getSheetAt(0);
        int rowNum=sheet.getLastRowNum();
        int colNum=sheet.getRow(1).getLastCellNum();
        for (int r=0;r<=rowNum;r++){
            XSSFRow row=sheet.getRow(r);
            for(int c=0;c<colNum;c++){
                XSSFCell cell=row.getCell(c);

                switch(cell.getCellType()){
                    case STRING :System.out.println(cell.getStringCellValue());
                    break;
                    case NUMERIC:System.out.println(cell.getNumericCellValue());
                    break;
                    case BOOLEAN:System.out.println(cell.getBooleanCellValue());
                    break;
                }

            }
            System.out.println();

        }
        
        
         
    }
    
}
