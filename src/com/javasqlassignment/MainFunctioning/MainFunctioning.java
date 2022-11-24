package com.javasqlassignment.MainFunctioning;
import com.javasqlassignment.MainFunctioning.HandleQueries;
import com.javasqlassignment.MainFunctioning.ControlFile;
import java.util.*;;
public class MainFunctioning{
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
