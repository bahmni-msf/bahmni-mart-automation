package org.bahmni.mart.automation;

import org.bahmni.mart.automation.helpers.Utils;
import org.bahmni.mart.automation.models.ObsForm;
import org.bahmni.mart.automation.readers.ObsJsonReader;
import org.junit.After;
import org.junit.AfterClass;
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

import static org.junit.Assert.assertEquals;

public class ObsSchemaValidationTest {

    private static Connection openmrsConnection = null;
    private static Connection postgresConnection = null;

    private ResultSet allmartTables = null;

    @BeforeClass
    public static void connSetUp() {
        openmrsConnection = new Utils().getDBConnection("mysql");
        postgresConnection = new Utils().getDBConnection("postgres");
    }

    @AfterClass
    public static void connTearDown() throws SQLException {

        if (openmrsConnection != null) {
            openmrsConnection.close();
        }

        if (postgresConnection != null) {
            postgresConnection.close();
        }

    }

    @Before
    public void setUp() throws SQLException {

        allmartTables = Utils.fetchTables(postgresConnection, "TABLE");

    }

    @After
    public void tearDown() {

    }

    @Test
    public void validateAllObsFormsSchema() {

        ObsJsonReader obsjsonreader = new ObsJsonReader();
        List<ObsForm> obsForms = null;
        String formName = null;
        ArrayList<String> formConcepts = null;
        ArrayList<String> tableColumns = null;
        int formCount = 0;

        try {

            obsForms = obsjsonreader.getObsFormsfromJson();
            System.out.println("Validating Schema for Obs forms. Total forms count: " + obsForms.size());
            for (ObsForm form : obsForms) {
                formName = form.getFormName();
                formConcepts = obsjsonreader.getObsFormConceptList(formName);
                tableColumns = Utils.getTableColumns(postgresConnection, formName);
                Collections.sort(formConcepts);
                Collections.sort(tableColumns);

                assertEquals("The schema validation for Obs Form: " + formName + " failed." + "FormConcepts: " + formConcepts + " " + "Columns from mart: " + tableColumns, formConcepts, tableColumns);
            }
        } catch (FileNotFoundException | SQLException e) {

            e.printStackTrace();
        }

    }

}
