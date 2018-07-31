package org.bahmni.mart.automation;

import org.bahmni.mart.automation.Pages.NewPatientPage;
import org.bahmni.mart.automation.helpers.*;
import org.bahmni.mart.automation.models.FormData;
import org.bahmni.mart.automation.models.PatientData;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ObsDataValidationTest {

        private static Connection openmrsConnection = null;
        private static Connection postgresConnection = null;
        private static WebDriver driver;
        private static boolean isFormDataFilledOnce = false;
        private static int patientId = 0;

        private ResultSet allmartTables = null;

        @BeforeClass
        public static void connSetUp() {
            openmrsConnection = new Utils().getDBConnection("mysql");
            postgresConnection = new Utils().getDBConnection("postgres");

            System.setProperty("webdriver.gecko.driver", "/Users/admin/Documents/Drivers/geckodriver");
            System.setProperty("webdriver.chrome.driver", "/Users/manjunab/Documents/chromeDriver/chromedriver");
            driver = new ChromeDriver();
            //driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);


        }

        @AfterClass
        public static void connTearDown() throws SQLException {

            if (openmrsConnection != null) {
                openmrsConnection.close();
            }

            if (postgresConnection != null) {
                postgresConnection.close();
            }

            if (driver != null) {
                driver.close();
                driver.quit();
            }

        }

        @Before
        public void setUp() throws SQLException {

            allmartTables = Utils.fetchTables(postgresConnection, "TABLE");

        }

        @After
        public void tearDown() {

        }

        public void createPatientandfillDataFromUI(){

                List<FormData> formDataList = FormInputDataJsonLoader.readFormDataFromJson("formData.json");
                String formName;
                NewPatientPage np = PageFactory.initElements(driver, NewPatientPage.class);
                List<PatientData> patientDetailsList = PatientLoader.readFormDataFromJson();
                String patientIQ = "";

                try {

                    SeleniumHelper.login(driver);
                    SeleniumHelper.goToNewPatientForm(driver);

                    for (PatientData patientData : patientDetailsList) {

                        patientIQ = np.createPatient(patientData, driver);
                        ObsDataValidationTest.patientId = Utils.getPatientIdentifier(openmrsConnection, patientIQ);

                    }

                    SeleniumHelper.goToProgramDashBoard(driver, patientIQ);
                    SeleniumHelper.goToConsultation(driver, patientIQ);

                    for (FormData formData : formDataList) {
                        formName = formData.getFormName();
                        System.out.println("Filling Data for Obs form from UI: " + formName);
                        SeleniumHelper.fillFormData(driver, formName, formData);

                    }

                    isFormDataFilledOnce = true;

            }

            catch (Exception e) {

                e.printStackTrace();
            }
        }

        @Test
        public void validateObsFormsWithoutMultiSelect(){

            Map<String, String> columnNameType = new HashMap<>();
            List<FormData> FQDNDataList = ToBeVerifiedDataJSONLoader.readFormDataFromJson("formDataToBeVerified.json");
            String tableName;
            Map<String, String> FQDNfieldValueMap = new HashMap<>();
            String restURL;
            String jobExecutionId;

            try {

                restURL = new Utils().getRestURL();

                if (!isFormDataFilledOnce) {
                    createPatientandfillDataFromUI();
                }

                // Once data is filled in Obs forms above, run mart using Rest and poll till job is completed successfully

                jobExecutionId = RestHelper.startBatchJob(restURL, false);

                if (RestHelper.pollUntilComplete(restURL, jobExecutionId) != null) {
                    System.out.println("mart Job is executed successfully");

                    for (FormData formData : FQDNDataList) {
                        ArrayList<String> multiselectTestData = new ArrayList<>();
                        ArrayList<String> multiselectDBData = new ArrayList<>();
                        Map<String, ArrayList<String >> dataMap = new HashMap<>();
                        String[] multiselectColumnValues;

                        // Fetch the input data that was entered to Obs form
                        FQDNfieldValueMap = formData.getFieldValueMap();
                        tableName = formData.getFormName();

                        //From mart DB Fetch the column names and type ( int, str) of column along with data from the table for Obs form
                        columnNameType = Utils.getTableColumnsAndTypes(postgresConnection, tableName);
                        ResultSet rs = Utils.getTableData(postgresConnection, tableName, ObsDataValidationTest.patientId);
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int colcount = rsmd.getColumnCount();
                        int i;
                        System.out.println("Column Count is: "+ colcount);

                        ArrayList<String> nonMultiDBData = new ArrayList<>();

                        while (rs.next()) {

                            for (String columnName : FQDNfieldValueMap.keySet()) {
                                ArrayList<String> nonMultiTestData = new ArrayList<>();

                                if (columnName.endsWith("_multiselect")) {

                                    // First fetch the multiselect values into a list
                                    multiselectColumnValues = FQDNfieldValueMap.get(columnName).split(";");

                                    // Now remove the _multiselect suffix from the column name so that we can get the column values from DB
                                    columnName = columnName.split("_multiselect")[0];

                                    for (String word : multiselectColumnValues) {
                                        //System.out.println(word);
                                        if (!multiselectTestData.contains(word)) {
                                            multiselectTestData.add(word);
                                        }
                                    }

                                    if (!multiselectDBData.contains(rs.getString(columnName))) {
                                        multiselectDBData.add(rs.getString(columnName));
                                    }
                                }
                                else {
                                    String columnData;
                                    columnData = rs.getString(columnName);
                                    if (columnData != null && !nonMultiTestData.contains(columnData)) {
                                        nonMultiTestData.add(columnData);
                                    }
                                    if (!nonMultiTestData.isEmpty()) {
                                        dataMap.put(columnName, nonMultiTestData);
                                    }
                                    //System.out.println("Verifying column: " + columnName + ". str Data from DB: " + columnData + " Data from test: " + FQDNfieldValueMap.get(columnName));
                                }
                            }
                        }

                        Collections.sort(multiselectTestData);
                        Collections.sort(multiselectDBData);

                        System.out.println("Data from test file: " + multiselectTestData);
                        System.out.println("Data from DB: "+ multiselectDBData);

                        //assertEquals("The data validation for multiselect Table: " + tableName + " failed." + "multiselect value from Test: " + multiselectTestData + " " + "data values from mart: " + multiselectDBData, multiselectTestData, multiselectDBData );
                        System.out.println("Verifying multiselect values in false case for table: " + tableName + ". str Data from DB: " + multiselectDBData + " Data from test: " + multiselectTestData);
                        assertTrue("The data validation for multiselect Table: " + tableName + " failed." + "multiselect value from Test: " + multiselectTestData + " " + "data values from mart: " + multiselectDBData, multiselectTestData.containsAll(multiselectDBData));

                        for (String column: dataMap.keySet()) {
                            System.out.println("Verifying column: " + column + ". str Data from DB: " + dataMap.get(column).get(0) + " Data from test: " + FQDNfieldValueMap.get(column));
                            assertEquals("The data validation for multiselect Table: " + tableName + " failed." + "non-multiselect value from Test: " + FQDNfieldValueMap.get(column) + " " + "data values from mart: " + dataMap.get(column), FQDNfieldValueMap.get(column), dataMap.get(column).get(0));
                        }

                    }
                    System.out.print("Number of columns is: " + columnNameType.size());
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        @Test
        public void validateObsFormsWithMultiSelectAndAddMore(){

                String tableName, restURL, jobExecutionId;
                List<FormData> toBeVerifiedDataList = ToBeVerifiedDataJSONLoader.readFormDataFromJson("obsMultiSelectDataToBeVerified.json");
                Map<String, String> FQDNfieldValueMap = new HashMap<>();

                try {

                    restURL = new Utils().getRestURL();
                    if (!isFormDataFilledOnce) {
                        createPatientandfillDataFromUI();
                    }

                    // Once data is filled in Obs forms above, run mart using Rest and poll till job is completed successfully
                    jobExecutionId = RestHelper.startBatchJob(restURL);

                    if (RestHelper.pollUntilComplete(restURL, jobExecutionId) != null) {
                        System.out.println("mart Job is executed successfully");
                        for (FormData formData : toBeVerifiedDataList) {
                            ArrayList<String> multiselectTestData = new ArrayList<>();
                            ArrayList<String> multiselectDBData = new ArrayList<>();
                            String[] multiselectColumnValues;
                            // Fetch the input data that was entered to Obs form
                            FQDNfieldValueMap = formData.getFieldValueMap();
                            tableName = formData.getFormName();
                            //From mart DB Fetch the column names and type ( int, str) of column along with data from the table for Obs form
                            ResultSet rs = Utils.getTableData(postgresConnection, tableName, ObsDataValidationTest.patientId);

                            while (rs.next()) {
                                for (String columnName : FQDNfieldValueMap.keySet()) {
                                    if (columnName.endsWith("_multiselect")) {
                                        // First fetch the multiselect values into a list
                                        multiselectColumnValues = FQDNfieldValueMap.get(columnName).split(";");
                                        // Now remove the _multiselect suffix from the column name so that we can get the column values from DB
                                        columnName = columnName.split("_multiselect")[0];

                                        for (String word : multiselectColumnValues) {

                                            if (!multiselectTestData.contains(word)) {
                                                multiselectTestData.add(word);
                                            }
                                        }

                                        if (!multiselectDBData.contains(rs.getString(columnName))) {
                                            multiselectDBData.add(rs.getString(columnName));
                                        }
                                    }
                                }
                            }

                            Collections.sort(multiselectTestData);
                            Collections.sort(multiselectDBData);

                            System.out.println("Data from test file: " + multiselectTestData);
                            System.out.println("Data from DB: "+ multiselectDBData);

                            //assertEquals("The data validation for multiselect Table: " + tableName + " failed." + "multiselect value from Test: " + multiselectTestData + " " + "data values from mart: " + multiselectDBData, multiselectTestData, multiselectDBData );
                            assertTrue("The data validation for multiselect Table: " + tableName + " failed." + "multiselect value from Test: " + multiselectTestData + " " + "data values from mart: " + multiselectDBData, multiselectTestData.containsAll(multiselectDBData));

                        }
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

        }

        public void createPatientTest() throws SQLException {

            List<PatientData> patientDetailsList = PatientLoader.readFormDataFromJson();
            String patientIQ;
            int patientId = 0;
            Map<String, String> patientDetailValues = new HashMap<>();

            SeleniumHelper.login(driver);
            SeleniumHelper.goToNewPatientForm(driver);
            NewPatientPage np = PageFactory.initElements(driver, NewPatientPage.class);

            for (PatientData patientData: patientDetailsList) {

                patientIQ = np.createPatient(patientData,driver);
                patientId = Utils.getPatientIdentifier(openmrsConnection,patientIQ);

            }


//            patientId = Utils.getPatientIdentifier(openmrsConnection,"JO102056M");
            System.out.println("Patient Identifier is: " + patientId);



        }

        public void validateObsFormsData(boolean isMultiSelectEnabled, String inputDataFile, String toBeVerifiedDatafileName) {

        List<FormData> formDataList = FormInputDataJsonLoader.readFormDataFromJson(inputDataFile);
        String formName;
        Map<String, String> columnNameType = new HashMap<>();
        List<FormData> FQDNDataList = ToBeVerifiedDataJSONLoader.readFormDataFromJson(toBeVerifiedDatafileName);
        String tableName;
        Map<String, String> FQDNfieldValueMap = new HashMap<>();
        String restURL;
        String jobExecutionId;
        int formCount = 0;

        NewPatientPage np = PageFactory.initElements(driver, NewPatientPage.class);
        List<PatientData> patientDetailsList = PatientLoader.readFormDataFromJson();
        String patientIQ = "";
        int patientId = 0;
        Map<String, String> patientDetailValues = new HashMap<>();

        try {

            restURL = new Utils().getRestURL();

            SeleniumHelper.login(driver);
            SeleniumHelper.goToNewPatientForm(driver);

            for (PatientData patientData: patientDetailsList) {

                patientIQ = np.createPatient(patientData,driver);
                patientId = Utils.getPatientIdentifier(openmrsConnection,patientIQ);

            }

            SeleniumHelper.goToProgramDashBoard(driver, patientIQ);
            SeleniumHelper.goToConsultation(driver, patientIQ);


            // For each Obs form declared in the input JSON file fill the data using selenium
            // After filling the data and saving the form, also confirm that the data is correctly entered in the form

            for (FormData formData : formDataList) {
                formName = formData.getFormName();
                System.out.println("Filling Data for Obs form from UI: " + formName);
                SeleniumHelper.fillFormData(driver, formName, formData);

            }

            // Once data is filled in Obs forms above, run mart using Rest and poll till job is completed successfully

            if (isMultiSelectEnabled) {
                jobExecutionId = RestHelper.startBatchJob(restURL);
            }
            else {
                jobExecutionId = RestHelper.startBatchJob(restURL, false);
            }
            if (RestHelper.pollUntilComplete(restURL, jobExecutionId) != null) {

                System.out.println("mart Job is executed successfully");
                //Thread.sleep(2000);

                for (FormData formData : FQDNDataList) {

                    // Fetch the input data that was entered to Obs form
                    FQDNfieldValueMap = formData.getFieldValueMap();
                    tableName = formData.getFormName();

                    //From mart DB Fetch the column names and type ( int, str) of column along with data from the table for Obs form
                    columnNameType = Utils.getTableColumnsAndTypes(postgresConnection, tableName);
                    ResultSet rs = Utils.getTableData(postgresConnection, tableName, patientId);
                    while (rs.next()) {
                        for (String columnName : FQDNfieldValueMap.keySet()) {
                            if (columnNameType.get(columnName).equals("text") || columnNameType.get(columnName).equals("date")) {
                                String columnData ;
                                columnData = rs.getString(columnName);
                                //System.out.println("Column: " + columnName + " has data: " + columnData);
                                System.out.println("Verifying column: " + columnName + ". str Data from DB: " + columnData + " Data from test: " + FQDNfieldValueMap.get(columnName));
                                if (columnData != null) {
                                    assertTrue("The output column value is not matching input data ", FQDNfieldValueMap.get(columnName).contains(columnData));

                                }
                                else {
                                    assertEquals(columnData, FQDNfieldValueMap.get(columnName));
                                }
                            }
                            else if (columnNameType.get(columnName).contains("int")) {

                                Integer columnData = null;

                                columnData = rs.getInt(columnName);
                                //System.out.println("Column: " + columnName + " has data: " + columnData);
                                System.out.println("Verifying column: " + columnName + ". int Data from DB: " + columnData + " Data from test: " + FQDNfieldValueMap.get(columnName));
                                if (columnData != null) {
                                    assertTrue("The output column value is not matching input data ", FQDNfieldValueMap.get(columnName).contains(columnData.toString()));
                                }
                                else {
                                    assertEquals(columnData, FQDNfieldValueMap.get(columnName));
                                }
                            }
                        }

                    }
                    System.out.print("Number of columns is: " + columnNameType.size());
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
