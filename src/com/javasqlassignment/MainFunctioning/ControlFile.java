package com.javasqlassignment.MainFunctioning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.*;

public class ControlFile{
    private FileInputStream inputStream;
    private int rowNum;
    private int colNum;

    private XSSFSheet sheet;
    private XSSFWorkbook workbook;
    private Map fileData;
    private List arrData;
    private List arrRow;
    private int sheetNum;
    private List arrSheet;
    public List arrIdsSheet;
    public Map IdsSheet;


    public ControlFile(String p)throws Exception{
        try{
        final  String path=p ;   
        inputStream=new FileInputStream(path);
        }
        catch(FileNotFoundException e){
            System.out.println("File Not Found");
        }
        workbook=new XSSFWorkbook(inputStream);
        sheetNum=workbook.getNumberOfSheets();

        fileData= new HashMap<>();
       
       

    }
    public List getTotalSheets(){
        arrSheet=new ArrayList<Workbook>();
        for(int i=0;i<sheetNum;i++){
            arrSheet.add(workbook.getSheetName(i).toString());
        }
        return arrSheet;
    }
    public Map getData(){
        IdsSheet=new HashMap<>();

        for(int i=0;i<this.getTotalSheets().size();i++){
            sheet=workbook.getSheetAt(i);
            rowNum=sheet.getLastRowNum();
            colNum =sheet.getRow(1).getLastCellNum();
            arrData=new ArrayList<ArrayList>();
            arrIdsSheet=new ArrayList<>();
        
            for (int r=0;r<=rowNum;r++){
                arrRow=new ArrayList<XSSFCell>();

                XSSFRow row=sheet.getRow(r);
                for(int c=0;c<colNum;c++){
                    
                    XSSFCell cell=row.getCell(c);
                    if(r!=0 && c==0){
                        arrIdsSheet.add(cell);
                    }
                   if (cell.getCellType()==CellType.STRING){
                        arrRow.add(cell.getStringCellValue());
                   }
                   else if (cell.getCellType()==CellType.NUMERIC){
                        arrRow.add(cell.getNumericCellValue());
                   }
                   else if (cell.getCellType()==CellType.BOOLEAN){
                    arrRow.add(cell.getBooleanCellValue());
               }
                    
                }
                arrData.add(arrRow);


            }
            fileData.put(workbook.getSheetName(i), arrData);
            IdsSheet.put(sheet.getSheetName(), arrIdsSheet);
         }
         return fileData;
    }
   
}

