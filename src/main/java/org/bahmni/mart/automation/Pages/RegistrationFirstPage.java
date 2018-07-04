package org.bahmni.mart.automation.Pages;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegistrationFirstPage  {

    //public static final String URL = BASE_URL.concat("/registration/#/patient/new");

    @FindBy(how = How.CSS, using = "#givenName")
    public WebElement txtPatientName;

    @FindBy(how = How.CSS, using = "#registrationNumber")
    public WebElement txtRegistrationNumber;

    @FindBy(how = How.CSS, using = "#familyName")
    public WebElement familyName;

    @FindBy(how = How.CSS, using = "#gender")
    public WebElement gender;

    @FindBy(how = How.CSS, using = "#ageYears")
    public WebElement ageYears;

    @FindBy(how = How.ID, using = "ageDays")
    public WebElement ageDays;


    @FindBy(how = How.CSS, using = ".submit-btn")
    public WebElement save;

    @FindBy(how = How.CSS, using = "div[option-click=\"visitControl.startVisit\"] li.primaryOption  button")
    public WebElement enterVisitDetails;

    @FindBy(how = How.CSS, using = "button[ng-click=\"setSubmitSource('forwardAction')\"]")
    public WebElement enterVisitDetailButton;


    @FindBy(how = How.CSS, using = "#address6")
    public WebElement commune;


    //support for release-0.84
    @Deprecated
    @FindBy(how = How.CSS, using = ".registraion_legend strong > span")
    public WebElement _patientIdentifierValue;

    @FindBy(how = How.CSS, using = ".fa-search")
    public WebElement searchLink;

    @FindBy(how = How.CSS, using = "#hasOldIdentifier")
    public WebElement enterID_checkbox;

    @FindBy(how = How.ID, using = "identifierPrefix")
    public WebElement identifier_prefix;

    @FindBy(how = How.XPATH, using = "//*[@id=\"ARV Naif/ Non Naif\"]")
    public WebElement arvs;

    @FindBy(how = How.ID, using = "Tel 1")
    public WebElement tel1;

    @FindBy(how = How.ID, using = "Occupation")
    public WebElement occupation;

    @FindBy(how = How.ID, using = "Religion")
    public WebElement relegion;

    @FindBy(how = How.ID, using = "Resident à Kinshasa")
    public WebElement residentKinshasa;

    @FindBy(how = How.ID, using = "Centre de provenance")
    public WebElement centreDePrenance;

    @FindBy(how = How.ID, using = "Province de provenance")
    public WebElement provinceDeprovenance;

    @FindBy(how = How.ID, using = "Etat Civil")
    public WebElement etatCivil;

    @FindBy(how = How.ID, using = "Type de cohorte")
    public WebElement cohortType;

    @FindBy(how = How.ID, using = "Status VIH")
    public WebElement hivStatus;

    @FindBy(how= How.CSS, using = ".btn-user-info")
    public WebElement user_info;

    @FindBy(how= How.CSS, using = "i.fa-power-off")
    public WebElement logout;

    @FindBy(how = How.XPATH, using = "//*[@id=\"view-content\"]/div/div[3]/div/div/form/patient-action/div/span/span/div/div/ul/li/button")
    public WebElement opdVisitBtn;

    @FindBy(how = How.CLASS_NAME, using = "secondaryOption")
    public  WebElement ipdVisitBtn;

    @FindBy(how = How.ID, using = "Bébé exposé")
    public WebElement babyExposecheckbox;

    @FindBy(how = How.CSS, using = "[toggle=\"patient.hasRelationships\"]")
    public WebElement relation_toggle;

    @FindBy(how = How.CSS, using = "[ng-model=\"newRelationship.relationshipType.uuid\"]")
    public WebElement relation_dropdown;

    @FindBy(how = How.CSS, using = "[ng-model=\"newRelationship.patientIdentifier\"]")
    public WebElement relation_editText;


    public void clickSave() {
        save.click();
    }

    public void navigateToSearchPage() {
        searchLink.click();
    }

    public void verifyPatientWithIdentifierAndName() {
//		Patient patient = getPatientFromSpecStore();
        //TODO: Find a way to identify the identifier in edit mode with correct selector
        //TODO: write a correct assertion
//		Assert.assertTrue(familyName.getText().equals(lastName) && txtPatientName.getText().equals(firstName));
    }

    public void selectEnterPatientID(){
        enterID_checkbox.click();
    }


    public void logout(){
        user_info.click();
        logout.click();
    }

    public void enterVisitDetailsPage() {
        enterVisitDetails.click();
    }

    public void enterProgramDashboardPage() {
        enterVisitDetailButton.click();
    }

}
