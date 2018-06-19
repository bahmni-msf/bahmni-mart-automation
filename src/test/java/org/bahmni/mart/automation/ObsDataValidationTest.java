package org.bahmni.mart.automation;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.bahmni.mart.automation.Pages.NewPatientPage;
import org.bahmni.mart.automation.Pages.RegistrationFirstPage;
import org.bahmni.mart.automation.helpers.*;
import org.bahmni.mart.automation.models.FormData;
import org.bahmni.mart.automation.models.PatientData;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ObsDataValidationTest {

        private static Connection openmrsConnection = null;
        private static Connection postgresConnection = null;
        private static WebDriver driver;

        private ResultSet allmartTables = null;

        @BeforeClass
        public static void connSetUp() {
            openmrsConnection = new Utils().getDBConnection("mysql");
            postgresConnection = new Utils().getDBConnection("postgres");

            System.setProperty("webdriver.gecko.driver", "/Users/admin/Documents/Drivers/geckodriver");
            System.setProperty("webdriver.chrome.driver", "/Users/admin/Documents/Drivers/chromedriver");
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

        @Test
        public void validateObsFormsWithoutMultiSelect(){

            validateObsFormsData(false);

        }

        @Test
        public void validateObsFormsWithMultiSelectAndAddMore(){

            validateObsFormsData(true);
        }


        public void validateObsFormsData(boolean isMultiSelectEnabled) {

            List<FormData> formDataList = FormDataJsonLoader.readFormDataFromJson();
            String formName;
            Map<String, String> columnNameType = new HashMap<>();
            List<FormData> FQDNDataList = FQDNJSONLoader.readFormDataFromJson();
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
                    //np.goToHomePage(driver);
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
                                //System.out.println("Column Name: "+ columnName + " data type is: "+ columnNameType.get(columnName));

                            }

                        }
                        System.out.print("Number of columns is: " + columnNameType.size());
                    }
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        @Test
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
            System.out.println("Patient Identifier for J0102056M is: " + patientId);



        }



}
