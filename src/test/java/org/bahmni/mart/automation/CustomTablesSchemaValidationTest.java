
package org.bahmni.mart.automation;


import org.bahmni.mart.automation.helpers.Utils;
import org.bahmni.mart.automation.models.CustomTableDefinition;
import org.bahmni.mart.automation.readers.CustomTablesJsonReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomTablesSchemaValidationTest {

    private static Connection openmrsconnection = null;
    private static Connection postgresconnection = null;

    private ResultSet allmartTables = null;

    @BeforeClass
    public static void connSetUp() {
        openmrsconnection = new Utils().getDBConnection("mysql");
        postgresconnection = new Utils().getDBConnection("postgres");
    }

    @AfterClass
    public static void connTearDown() throws SQLException {

        if (openmrsconnection != null) {
            openmrsconnection.close();
        }

        if (postgresconnection != null) {
            postgresconnection.close();
        }

    }

    @Before
    public void setUp() throws SQLException {

        allmartTables = Utils.fetchTables(postgresconnection, "TABLE");

    }

    @After
    public void tearDown() {

    }

    @Test
    public void validateCustomTablesSchema() {

        CustomTablesJsonReader ctjsonreader = new CustomTablesJsonReader();
        List<CustomTableDefinition> customTables = null;
        String inputTableName = null;
        ArrayList<String> inputColumns = null;
        ArrayList<String> martTableColumns = null;
        int formCount = 0;

        try {

            customTables = ctjsonreader.getCustomTablesfromJson();
            System.out.println("Validating Schema for Custom Tables. Total tables count: " + customTables.size());
            for (CustomTableDefinition table : customTables) {
                inputTableName = table.getTableName();
                inputColumns = ctjsonreader.getCustomTablesColumnsList(inputTableName);
                martTableColumns = Utils.getTableColumns(postgresconnection, inputTableName);
                Collections.sort(inputColumns);
                Collections.sort(martTableColumns);

                Assert.assertTrue("The schema validation for Custom Table: " + inputTableName + " failed." + "Json Columns: " + inputColumns + " " + "Columns from mart: " + martTableColumns, inputColumns.equals(martTableColumns));
            }
        } catch (FileNotFoundException | SQLException e) {

            e.printStackTrace();
        }

    }

}
