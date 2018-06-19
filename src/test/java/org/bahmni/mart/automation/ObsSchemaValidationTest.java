package org.bahmni.mart.automation;

import org.bahmni.mart.automation.helpers.RestHelper;
import org.bahmni.mart.automation.helpers.Utils;
import org.bahmni.mart.automation.models.ObsForm;
import org.bahmni.mart.automation.readers.ObsJsonReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
    public void validateAllObsFormsSchemaWithMultiSelect() {

        validateAllObsFormsSchema(true);

    }

    @Test
    public void validateAllObsFormsSchemaWithoutMultiSelect(){

        validateAllObsFormsSchema(false);
    }


    public void validateAllObsFormsSchema(boolean isMultiSelectEnabled) {

        ObsJsonReader obsjsonreader = new ObsJsonReader();
        List<ObsForm> obsForms = null;
        String formName = null;
        ArrayList<String> formConcepts = null;
        ArrayList<String> tableColumns = null;
        String restURL;
        String jobExecutionId;
        int formCount = 0;

        try {

            restURL = new Utils().getRestURL();

            if (isMultiSelectEnabled) {

                //jobExecutionId = RestHelper.startBatchJob(restURL);
                obsForms = obsjsonreader.getObsFormsfromJson(true);
                System.out.println("Validating Schema for Obs forms with multi select. Total forms count: " + obsForms.size());
            }
            else {

                jobExecutionId = RestHelper.startBatchJob(restURL, false);
                obsForms = obsjsonreader.getObsFormsfromJson(false);
                System.out.println("Validating Schema for Obs forms without multiselect. Total forms count: " + obsForms.size());
            }
            //if (RestHelper.pollUntilComplete(restURL, jobExecutionId) != null) {
            if ( true ) {

                for (ObsForm form : obsForms) {
                    formName = form.getFormName();
                    formCount += 1;
                    if (isMultiSelectEnabled) {
                        formConcepts = obsjsonreader.getObsFormConceptList(formName, true);
                    }
                    else {
                        formConcepts = obsjsonreader.getObsFormConceptList(formName, false);
                    }
                    tableColumns = Utils.getTableColumns(postgresConnection, formName);
                    System.out.println("Validating schema for table: " + formName);
                    Collections.sort(formConcepts);
                    Collections.sort(tableColumns);

                    assertEquals("The schema validation for Obs Form: " + formName + " failed." + "FormConcepts: " + formConcepts + " " + "Columns from mart: " + tableColumns, formConcepts, tableColumns);
                }
                System.out.println("Validated total forms: " + formCount);
            }
        } catch ( Exception e) {

            e.printStackTrace();
        }

    }

}
