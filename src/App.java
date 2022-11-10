import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.poi.examples.xssf.usermodel.CreateTable;
import org.apache.poi.hmef.attribute.MAPIAttribute;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class MainFunctioning{
    private List updateTables;
    private List createTables;
    private Map dataTypesMap;
    private List<String> s;
    private List<String> t;
    private Map data;
    private ArrayList<ArrayList> dataSheet;
    private List arrDataType;
    private String sheetname;
    private HandleQueries db;
    private ControlFile file;
    private List arrColumn;
    private List rowDataType;
    private List firstRowData;
    private final String doubleType = "Double";
    private final String stringType = "String";

    public MainFunctioning(ControlFile f,HandleQueries d) throws Exception{
        data=f.getData();
        System.out.println(data);
        t=d.getTablesName();
        s=f.getTotalSheets();
        filteringTables(s, t);
        db=d;
        file=f;
        dataTypesMap=new HashMap<>();
        dataTypesMap=db.getAllDataTypes();
    }
    
    public void filteringTables(List s,List t){
        updateTables=new ArrayList<String>();
        createTables= new ArrayList<String>();
        for (int i=0;i<s.size();i++){
            if(!t.contains(s.get(i).toString().toLowerCase())){
                createTables.add(s.get(i));
            }
            else{
                updateTables.add(s.get(i));
        }}

    }

    public void createTable()throws Exception{
        for (int s=0;s<createTables.size();s++){
            sheetname =createTables.get(s).toString();
            System.out.println(sheetname);
            dataSheet= (ArrayList)data.get(sheetname);
            arrColumn=(ArrayList)dataSheet.get(0);
            rowDataType=new ArrayList<>();
            String createCmd="create table "+sheetname+"(";

           
            List row1=dataSheet.get(1);
            for(int i=0;i<row1.size();i++){
                if(row1.get(i).getClass().getSimpleName().equals(doubleType)){
                         rowDataType.add("int");
                }
                else if(row1.get(i).getClass().getSimpleName().equals(stringType)){
                    int len=row1.get(i).toString().length()+10;
                    String str=String.format("varchar(%d)",len);
                    rowDataType.add(str);
                }
            }
            
            for(int i=0;i<arrColumn.size();i++){
                if(i!=arrColumn.size()-1){
                createCmd=createCmd+arrColumn.get(i).toString()+" "+rowDataType.get(i)+",";
                }
                else{
                    createCmd=createCmd+arrColumn.get(i).toString()+" "+rowDataType.get(i)+",";
                    String str=String.format("primary key(%s)",arrColumn.get(0));

                    createCmd=createCmd+" "+str+")";


                }

            }
            
            db.updateQuery(createCmd);
            

            for (int i=1;i<dataSheet.size();i++) {
                String insertCmd="insert into "+sheetname+ " values (";

                ArrayList arrsheet=(ArrayList) dataSheet.get(i);

                for( int r=0;r<arrsheet.size();r++){
                    // System.out.println(arrsheet.get(r).toString().contains("int"));
                    if(r!=arrsheet.size()-1){
                        if(rowDataType.get(r).toString().contains("int")){
                            String str=String.valueOf((int)Float.parseFloat(arrsheet.get(r).toString()));
                            insertCmd=insertCmd+str+",";
                        }

                        else  if(rowDataType.get(r).toString().contains("varchar")){
                            insertCmd=insertCmd+"'"+arrsheet.get(r).toString()+"'"+",";
                        }
                        else if(rowDataType.get(r).toString().contains("float")){
                            insertCmd=insertCmd+Float.parseFloat(arrsheet.get(r).toString())+",";
                        }
                    }
                    else{
                        if(rowDataType.get(r).toString().contains("int")){
                            insertCmd=insertCmd+(int)Float.parseFloat(arrsheet.get(r).toString())+")";
                        }
                        else  if(rowDataType.get(r).toString().contains("varchar")){
                            insertCmd=insertCmd+"'"+arrsheet.get(r).toString()+"'"+")";
                        }
                        else if(rowDataType.get(r).toString().contains("float")){
                            insertCmd=insertCmd+Float.parseFloat(arrsheet.get(r).toString())+")";
                        }

                    }
                    
                }
                System.out.println(insertCmd);
                db.updateQuery(insertCmd);
            }
                
            }
    } 
    public void updateTable()throws Exception{
        Map Ids= db.getAllIdsDataBase();

        
        for (int s=0;s<updateTables.size();s++){
            sheetname =updateTables.get(s).toString();
            List arrIds=(ArrayList)Ids.get(sheetname.toLowerCase());
            // db.updateQuery("drop table "+sheetname);

            dataSheet= (ArrayList)data.get(sheetname);
            arrDataType=(ArrayList<String>)dataTypesMap.get(sheetname.toLowerCase());
            arrColumn=dataSheet.get(0);
            firstRowData=dataSheet.get(1);
            // System.out.println(sheetname);
            // System.out.println(dataSheet);
            // System.out.println(firstRowData);
            // System.out.println(arrDataType);
            System.out.println(firstRowData);
            if(firstRowData.size()>arrDataType.size()){
                if(firstRowData.get(firstRowData.size()-1).getClass().getSimpleName().equals(doubleType)){
                    arrDataType.add("int");
                }
                else if(firstRowData.get(firstRowData.size()-1).getClass().getSimpleName().equals(stringType)){
                    int len=firstRowData.get(firstRowData.size()-1).toString().length()+10;
                    String str=String.format("varchar(%d)",len);
                    arrDataType.add(str);
                }
                System.out.println("alter table "+sheetname+" add column "+arrColumn.get(arrColumn.size()-1)+" "+arrDataType.get(arrDataType.size()-1));
                db.updateQuery("alter table "+sheetname+" add column "+arrColumn.get(arrColumn.size()-1)+" "+arrDataType.get(arrDataType.size()-1));
                
            }
            for (int i=1;i<dataSheet.size();i++) {
                String insertCmd="insert into "+sheetname+ " values (";
                String updateCmd="update "+sheetname +" set ";

                ArrayList arrrow=(ArrayList) dataSheet.get(i);
                if(!arrIds.contains(String.valueOf((int)Float.parseFloat(arrrow.get(0).toString())))){

                    for( int r=0;r<arrrow.size();r++){
                            if(r!=arrrow.size()-1){
                                if(arrDataType.get(r).toString().contains("int")){
                                    String str=String.valueOf((int)Float.parseFloat(arrrow.get(r).toString()));
                                    insertCmd=insertCmd+str+",";
                                }

                                else  if(arrDataType.get(r).toString().contains("varchar")){
                                    insertCmd=insertCmd+"'"+arrrow.get(r).toString()+"'"+",";
                                }
                                
                            }
                            else{
                                if(arrDataType.get(r).toString().contains("int")){
                                    insertCmd=insertCmd+(int)Float.parseFloat(arrrow.get(r).toString())+")";
                                }
                                else  if(arrDataType.get(r).toString().contains("varchar")){
                                    insertCmd=insertCmd+"'"+arrrow.get(r).toString()+"'"+")";
                                }
                                else if(arrDataType.get(r).toString().contains("float")){
                                    insertCmd=insertCmd+Float.parseFloat(arrrow.get(r).toString())+")";
                                }
                            }
                          

                    }
                    System.out.println(insertCmd);
                    db.updateQuery(insertCmd);   
         }
        else{
            for( int r=0;r<arrrow.size();r++){
                if(r!=arrrow.size()-1){
                    if(arrDataType.get(r).toString().contains("int")){
                        String str=String.valueOf((int)Float.parseFloat(arrrow.get(r).toString()));
                        updateCmd=updateCmd+arrColumn.get(r).toString()+"="+str+",";
                    }

                    else  if(arrDataType.get(r).toString().contains("varchar")){
                        updateCmd=updateCmd+arrColumn.get(r).toString()+"="+"'"+arrrow.get(r).toString()+"'"+",";
                    }
                    
                }
                else{
                    if(arrDataType.get(r).toString().contains("int")){
                        String str=String.valueOf((int)Float.parseFloat(arrrow.get(r).toString()));

                        updateCmd=updateCmd+arrColumn.get(r).toString()+"="+str+" where "+arrColumn.get(0).toString()+"="+(int)Float.parseFloat(arrrow.get(0).toString());
                    }
                    else  if(arrDataType.get(r).toString().contains("varchar")){
                        updateCmd=updateCmd+arrColumn.get(r).toString()+"="+"'"+arrrow.get(r).toString()+"'"+" where "+arrColumn.get(0).toString()+"="+(int)Float.parseFloat(arrrow.get(0).toString());
                    }
                }
              }
              System.out.println(updateCmd);
              db.updateQuery(updateCmd);

         }
                    
                
                
     }

            
            // for (int i=1;i<dataSheet.size();i++) {
            //         ArrayList arr=(ArrayList) dataSheet.get(i);
            //         System.out.println(String.format(insertCmd,(int)Float.parseFloat(arr.get(0).toString()),"'"+arr.get(1).toString()+"'","'"+arr.get(2).toString()+"'"));
            //         db.updateQuery(String.format(insertCmd,(int)Float.parseFloat(arr.get(0).toString()),"'"+arr.get(1).toString()+"'","'"+arr.get(2).toString()+"'"));
            //         }
        }

        

    }       
}
class HandleQueries {
    private Connection con;
    private Statement stmt;
    private ResultSet rslt;
    private List<String> arrTable;
    private String dataBaseName;
    private List arrDataTypes;
    private Map allTablesDataTypes;
    public HandleQueries(String db,String user,String pass)throws Exception{
        dataBaseName=db;
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,user , pass);
        if (con != null)            
                System.out.println("Connected");           
            else           
                System.out.println("Not Connected");
        stmt=con.createStatement();
        allTablesDataTypes= new HashMap<>();


    }
    public List getTablesName() throws SQLException{
        rslt=stmt.executeQuery("show tables");
        arrTable=new ArrayList<String>();
        while(rslt.next()){
            arrTable.add(rslt.getString("Tables_in_"+dataBaseName));
            }
        return arrTable;
    }
    public Map getAllDataTypes() throws SQLException{
        List<String> arr=getTablesName();
        for (String x:arr){
            rslt=stmt.executeQuery("describe "+x);
            arrDataTypes=new ArrayList<String>();
            while(rslt.next()){
                arrDataTypes.add(rslt.getString("Type").toString());
                }
            allTablesDataTypes.put(x, arrDataTypes);
        }
        return allTablesDataTypes;
    }

    public Map getAllIdsDataBase() throws SQLException{
        Map allIds=new HashMap<String,ArrayList>();
        List<String> arr=getTablesName();
        for (String x:arr){
            
            List arrColumnNames=new ArrayList<String>();
            rslt=stmt.executeQuery("describe "+x);
            while(rslt.next()){
                arrColumnNames.add(rslt.getString("Field").toString());
                }
            
            String str=String.format("select %s from %s",arrColumnNames.get(0) , x);
            rslt=stmt.executeQuery(str);
            List arrIds=new ArrayList<String>();
            while(rslt.next()){
                arrIds.add(rslt.getString(arrColumnNames.get(0).toString()).toString());
            }
            allIds.put(x, arrIds);
        }

        return allIds;
    }
    public  void updateQuery(String s) throws Exception{
        int x=stmt.executeUpdate(s);

    }
    // public void outputQueries(String s) throws Exception{
    //     rslt=stmt.executeQuery(s);
    // }
    public void closeConnection() throws Exception{
        con.close();
    }
}


class ControlFile{
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

public class App {
    public static void main(String[] args) throws Exception,NumberFormatException {
        // creating connection and loading drivers
            System.out.println("ASSALAMU ALAIKUM");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Scanner sc=new Scanner(System.in);
            System.out.println("Type your Database name");
            String dataBaseName=sc.nextLine();
            System.out.println("Type your user name");
            String user=sc.nextLine();
            System.out.println("Type your password");
            String password=sc.nextLine();
            HandleQueries testDb=new HandleQueries(dataBaseName, user, password);

            // File excecution
            // ".//datafiles//testExcel.xlsx"
            System.out.println("Type your excel file path");
            String filePath= sc.nextLine();
            ControlFile file1=new ControlFile(".//datafiles//testExcel.xlsx");
            // Main Implementation by Adding data to database
            MainFunctioning m1=new MainFunctioning(file1,testDb);
            m1.createTable();   
            m1.updateTable();
            
        }

}
