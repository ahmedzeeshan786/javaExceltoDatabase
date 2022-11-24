package com.javasqlassignment.MainFunctioning;
import java.sql.*;
import java.util.*;

import com.javasqlassignment.MainFunctioning.ControlFile;
public class HandleQueries {
    private Connection con;
    private Statement stmt;
    private ResultSet rslt;
    private List<String> arrTable;
    private String dataBaseName;
    private List arrDataTypes;
    private Map allTablesDataTypes;
    public HandleQueries(String db,String user,String pass)throws Exception{
        dataBaseName=db;
        // no hard coded
        try{
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,user , pass);
        if (con != null)            
                System.out.println("Connected");           
         else           
                System.out.println("Not Connected");
        stmt=con.createStatement();
        
        }
        catch (SQLException e1){
            System.out.println("Please give relevant data ---"+e1);
            System.out.println("Not Connected to your Database");
        }
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


