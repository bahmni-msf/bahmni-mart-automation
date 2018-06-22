package org.bahmni.mart.automation.helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Utils {

    public static ResultSet fetchTables (Connection dbconn, String types_of_tables) throws SQLException{
        // types_of_tables should be a list of table types, e.g. "TABLE", "VIEW" etc. if all are to be returned then it should be "null"

        DatabaseMetaData dbm = dbconn.getMetaData();
        ResultSet allTables = dbm.getTables(null,null,null, new String[] {types_of_tables});

        return allTables;
    }

    public static ArrayList<String> getTableColumns (Connection dbconn, String table_name) throws SQLException {
        //String table_name = allTables.getString(3); // This is faster compared to using TABLE_NAME
        ArrayList<String> columnList = new ArrayList<String>();
        String[] columnsList = {};
        DatabaseMetaData dbm = dbconn.getMetaData();
        ResultSet tableSchemaSet = dbm.getColumns(null, null, table_name, null);
        //System.out.println("Schema for table: " + table_name);
        while (tableSchemaSet.next()) {
//                System.out.print("Column Name: " + tableSchemaSet.getString("COLUMN_NAME") + " "
//                        + "Column Data: " + tableSchemaSet.getString("TYPE_NAME"));
            columnList.add(tableSchemaSet.getString("COLUMN_NAME"));
        }

        return columnList;
    }

    public  Connection getDBConnection(String dbms) {

        Properties connprops = new Properties();
        FileInputStream propsfilestream = null;
        Connection DBconn = null;
        String URL = "";
        String username = "";
        String password = "";

        try {

            //String File_Path = this.getClass().getClassLoader().getResourceAsStream("base/connection.properties");

            //propsfilestream = new FileInputStream().getClassLoader().getResourceAsStream("base/connection.properties"));
            //connprops.load(new InputStreamReader(getClass().getClass().getClassLoader().getResourceAsStream("connection.properties")));
            InputStream ism = this.getClass().getClassLoader().getResourceAsStream("connection.properties");
            connprops.load(ism);

            if (dbms.equals("mysql")) {

                Class.forName(connprops.getProperty("mysql_Driver"));
                URL = connprops.getProperty("mysqlURL");
                username = connprops.getProperty("mysqlUsername");
                password = connprops.getProperty("mysqlPassword");
            }
            else if (dbms.equals("postgres")) {

                Class.forName(connprops.getProperty("postgres_Driver"));
                URL = connprops.getProperty("postgresURL");
                username = connprops.getProperty("postgresUsername");
                password = connprops.getProperty("postgresPassword");
            }

            DBconn = DriverManager.getConnection(URL, username, password);

        }

        catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return DBconn;
    }

    public static ResultSet getTableData (Connection dbconn, String table_name, int patientID) throws SQLException {

        int encounter_id = 0;
        //String table_name = allTables.getString(3); // This is faster compared to using TABLE_NAME
        DatabaseMetaData dbm = dbconn.getMetaData();
        Statement stmt = dbconn.createStatement();
        String query;
        query = String.format("Select * from %s where patient_id=%d order by obs_datetime desc limit 1", table_name,patientID);
        System.out.println("Executing query for patient: "+ query);
        //ResultSet rs = stmt.executeQuery("Select * from "+ table_name + " " + "where patient_id="+ patientID +" order by obs_datetime desc limit 1");
        ResultSet rs1 = stmt.executeQuery(query);

        while(rs1.next()) {

            encounter_id=rs1.getInt("encounter_id");
        }

        query = String.format("Select * from %s where patient_id=%d and encounter_id=%d order by obs_datetime", table_name,patientID, encounter_id);
        System.out.println("Executing Query: "+ query);
        ResultSet rs = stmt.executeQuery(query);

        return rs;
    }

    public static Map<String,String> getTableColumnsAndTypes (Connection dbconn, String table_name) throws SQLException {
        //String table_name = allTables.getString(3); // This is faster compared to using TABLE_NAME
        ArrayList<String> columnList = new ArrayList<String>();
        Map<String, String> columnNameTypeMap = new HashMap<>();
        String[] columnsList = {};
        DatabaseMetaData dbm = dbconn.getMetaData();
        ResultSet tableSchemaSet = dbm.getColumns(null, null, table_name, null);
        //System.out.println("Schema for table: " + table_name);
        while (tableSchemaSet.next()) {
//                System.out.print("Column Name: " + tableSchemaSet.getString("COLUMN_NAME") + " "
//                        + "Column Data: " + tableSchemaSet.getString("TYPE_NAME"));
            //columnList.add(tableSchemaSet.getString("COLUMN_NAME"));
            //columnNameTypeMap.put(tableSchemaSet.getString("COLUMN_NAME"),tableSchemaSet.getString("TYPE_NAME"));
            // index value "4" below is equivalent to using "COLUMN_NAME" and "6" is "TYPE_NAME" but indexes are faster
            columnNameTypeMap.put(tableSchemaSet.getString(4),tableSchemaSet.getString(6));
        }

        return columnNameTypeMap;
    }

    public static int getPatientIdentifier (Connection dbconn, String patientUI_ID) throws SQLException {
        //String table_name = allTables.getString(3); // This is faster compared to using TABLE_NAME
        //String columnName = "identifier";
        DatabaseMetaData dbm = dbconn.getMetaData();
        Statement stmt = dbconn.createStatement();
        String stmt1 = String.format("select * from patient_identifier where identifier='%s'", patientUI_ID);

        System.out.println(stmt1);
        ResultSet rs = stmt.executeQuery(stmt1);
//        while(rs.next()) {
//                System.out.println(rs.getInt(1));
//        }
        rs.next();
        return rs.getInt(2);
    }

    public String getRestURL() throws  IOException {

        String restURL;
        Properties connprops = new Properties();
        InputStream ism = this.getClass().getClassLoader().getResourceAsStream("connection.properties");
        connprops.load(ism);

        restURL = connprops.getProperty("restEndPointURL");

        return restURL;

    }
}
