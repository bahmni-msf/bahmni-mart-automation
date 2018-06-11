package org.bahmni.mart.automation.Pages;


import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.openqa.selenium.By.*;

public class ProgramManagementPage  {

    @FindBy(how = How.CSS, using = "select.ng-pristine")
    public WebElement program;

    @FindBy(how = How.CSS, using = ".fa-plus-square")
    public WebElement btnPlus;

    @FindBy(how = How.CSS, using = "input.ng-valid-max")
    public WebElement start_date;

    @FindBy(how = How.XPATH, using = "//input[@value='Enroll' and @type='submit']")
    public WebElement btnEnroll;

    @FindBy(how = How.CSS, using = ".active-program-tiles")
    public List<WebElement> allActivePrograms;

    @FindBy(how = How.CSS, using = ".inactive-program-tiles")
    public List<WebElement> allInactivePrograms;

    @FindBy(how = How.CSS, using = ".inactive-program-tiles")
    public List<WebElement> inactive_progs;

    @FindBy(how = How.CSS, using = "[ng-model='patientProgram.outcomeData']")
    public WebElement treatment_status;

    @FindBy(how = How.CSS, using = "#dashboard-link")
    public WebElement treatment_dashboard;

    @FindBy(how = How.CSS, using = ".active-program-container")
    public WebElement activeProgramContainer;

    @FindBy(how = How.CSS, using = ".inactive-program-container")
    public WebElement inactiveProgramContainer;

    @FindBy(how = How.CSS, using = "[id='Facility Name']")
    public WebElement facility_name;

    @FindBy(how = How.CSS, using = "[id='Sample attribute3']")
    public WebElement facility_id;

    @FindBy(how = How.CSS, using = "#Doctor")
    public WebElement doctor;

    @FindBy(how = How.CSS, using = "#Id_Number")
    public WebElement registration_id;


    public void clickonProgram() {
        treatment_dashboard.click();
    }

    public ProgramManagementPage editProgram(String registration, String facility) {
        facility_name.sendKeys(facility);
        registration_id.sendKeys(registration);
        return this;
    }



}
