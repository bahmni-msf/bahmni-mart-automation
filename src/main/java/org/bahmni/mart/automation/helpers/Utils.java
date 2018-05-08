package org.bahmni.mart.automation.helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
}
