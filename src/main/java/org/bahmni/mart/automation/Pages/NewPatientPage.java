package org.bahmni.mart.automation.Pages;

import org.bahmni.mart.automation.helpers.PageWaits;
import org.bahmni.mart.automation.models.FormData;
import org.bahmni.mart.automation.models.PatientData;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Map;

public class NewPatientPage {

    @FindBy(how = How.CSS, using = ".back-btn")
    public WebElement backButton;

    @FindBy(how = How.CSS, using = ".submit-btn")
    public WebElement save;


    @FindBy(how = How.CSS, using = "#givenName")
    public WebElement firstName;

    @FindBy(how = How.CSS, using = "#familyName")
    public WebElement lastName;

    @FindBy(how = How.CSS, using = "#gender")
    public WebElement gender;

    @FindBy(how = How.CSS, using = "#ageYears")
    public WebElement ageYears;

    @FindBy(how = How.CSS, using = "#stateProvince")
    public WebElement governorate;

    @FindBy(how = How.CSS, using = "#address3")
    public WebElement country;

    @FindBy(how = How.CSS, using = "#phoneNumber1")
    public WebElement phoneNumber;

    @FindBy(how = How.CSS, using = "#nationality1")
    public WebElement nationality;

    @FindBy(how = How.CSS, using = "#spokenLanguages")
    public WebElement spokenlanguage;

    @FindBy(how = How.CSS, using = "#patientIdentifierValue")
    public WebElement patientID;

    public String createPatient(PatientData patientData, WebDriver driver) {

        Map<String, String> patientDetailsMap = patientData.getFieldValueMap();

        firstName.sendKeys(patientDetailsMap.get("First Name"));

        lastName.sendKeys(patientDetailsMap.get("Last Name"));

        new Select(gender).selectByVisibleText(patientDetailsMap.get("gender"));

        ageYears.sendKeys(patientDetailsMap.get("age Years"));

        governorate.sendKeys(patientDetailsMap.get("governorate"));

        country.sendKeys(patientDetailsMap.get("country"));

        phoneNumber.sendKeys(patientDetailsMap.get("Phone Number"));

        new Select(nationality).selectByVisibleText(patientDetailsMap.get("Nationality"));

        spokenlanguage.sendKeys(patientDetailsMap.get("Spoken Languages"));

        save.click();

        PageWaits.waitForSpinner(driver);

        String patientIdentifier = patientID.getText();

        System.out.println("patient ID of newly created one is: " + patientIdentifier);

        return  patientIdentifier;
    }

    public void goToHomePage(WebDriver driver){

        backButton.click();
        PageWaits.waitForSpinner(driver);

    }


}
