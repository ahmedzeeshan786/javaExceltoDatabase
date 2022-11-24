package com.javasqlassignment.main;
import com.javasqlassignment.MainFunctioning.*;
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




public class App {
    public static void main(String[] args) throws Exception,NumberFormatException {
        // creating connection and loading drivers
            System.out.println("ASSALAMU ALAIKUM");
            // fid out best way to 
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
            ControlFile file1=new ControlFile(filePath);
            // Main Implementation by Adding data to database
            MainFunctioning m1=new MainFunctioning(file1,testDb);
            m1.createTable();   
            m1.updateTable();
            testDb.closeConnection();
            
        }

}
