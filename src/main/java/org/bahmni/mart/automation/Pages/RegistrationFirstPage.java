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

//    public void registerPatient(Patient patient) throws InterruptedException {
//        waitForSpinner();
//        try{
//            if(enterID_checkbox.isDisplayed() & patient.getIdNumber()!= null) {
//                enterID_checkbox.click();
//                txtRegistrationNumber.sendKeys(patient.getIdNumber());
//            }
//        } catch (NoSuchElementException ex){
//
//        }
//        txtPatientName.clear();
//        txtPatientName.sendKeys(patient.getFirstName());
//
//        familyName.clear();
//        familyName.sendKeys(patient.getLastName());
//
//        new Select(gender).selectByVisibleText(patient.getGender());
//
//        ageYears.clear();
//        ageYears.sendKeys(patient.getAge());
//
//        new Select(cohortType).selectByVisibleText(patient.getCohortType());
//        new Select(hivStatus).selectByVisibleText(patient.getHivStatus());
//
//        try {
//            commune.clear();
//            commune.sendKeys(patient.getCommune());
//            commune.sendKeys(Keys.DOWN);
//            waitForElementOnPage(By.xpath(".//a[text()=\"" + patient.getCommune() + "\"]"));
//            findElement(By.xpath(".//a[text()=\"" + patient.getCommune() + "\"]")).click();
//            WebDriverWait wait = new WebDriverWait(driver, 3);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal-refill-button")));
//
//            List<WebElement> elements = driver.findElements(By.cssSelector("#modal-refill-button"));
//
//            if (elements.size() != 0) {
//                elements.get(0).click();
//            }
//        } catch (Exception e) {
//        }
//
//        new Select(arvs).selectByVisibleText(patient.getarvNaifNonNaif());
//
//        clickSave();
//
//    }

//    private void editPatient(Patient patient) throws InterruptedException {
//        waitForSpinner();
//        try{
//            if(enterID_checkbox.isDisplayed() & patient.getIdNumber()!= null) {
//                enterID_checkbox.click();
//                txtRegistrationNumber.sendKeys(patient.getIdNumber());
//            }
//        } catch (NoSuchElementException ex){
//
//        }
//
//        familyName.clear();
//        familyName.sendKeys(patient.getLastName());
//
//        tel1.clear();
//        tel1.sendKeys(patient.getTel1());
//
//        occupation.clear();
//        occupation.sendKeys(patient.getOccupation());
//
//        new Select(etatCivil).selectByVisibleText(patient.getEtatCivil());
//        new Select(relegion).selectByVisibleText(patient.getReligion());
//        new Select(residentKinshasa).selectByVisibleText(patient.getResidentKinshasa());
//        new Select(provinceDeprovenance).selectByVisibleText(patient.getProvinceDeprovenance());
//
//        centreDePrenance.clear();
//        centreDePrenance.sendKeys(patient.getCentreDeprovenance());
//
//        clickSave();
//    }


//    public void registerPatientWithID(Patient patient) throws InterruptedException {
//        enterID_checkbox.click();
//        txtRegistrationNumber.sendKeys(patient.getIdNumber());
//        txtPatientName.sendKeys(patient.getFirstName());
//        familyName.sendKeys(patient.getLastName());
//        new Select(gender).selectByVisibleText(patient.getGender());
//        ageYears.sendKeys(patient.getAge());
//        clickSave();
//
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, 3);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal-refill-button")));
//
//            List<WebElement> elements = driver.findElements(By.cssSelector("#modal-refill-button"));
//
//            if (elements.size() != 0) {
//                elements.get(0).click();
//            }
//        } catch (Exception e) {
//        }
//    }


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

//    public Patient transformTableRowToPatient(TableRow row, List<String> headers) throws Exception {
//        Patient patient = new Patient();
//        return (Patient)transform(row,patient,headers);
//    }


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
//    public void createPatientUsingApi(Table table) throws Exception {
//        Patient patient = TableTransformer.asEntity(table,Patient.class);
//        BahmniRestClient.get().createPatient(patient,"patient_create.ftl");
//        patient.setIdNumber(patient.getIdentifier().substring(3));
//        storePatientInSpecStore(patient);
//    }

//    public void createPatients(Table table) throws Exception {
//        Patient patient = transformTableToPatient(table);
//        registerPatient(patient);
//
//        waitForSpinner();
//        String path = driver.getCurrentUrl();
//        String uuid = path.substring(path.lastIndexOf('/') + 1);
//        if (!Objects.equals(uuid, "new")) {
//            patient.setUuid(uuid);
//            patient.setIdentifier(_patientIdentifierValue.getText());
//            storePatientInSpecStore(patient);
//        }
//    }

//    public void editPatient(Table table) throws Exception {
//        Patient patient = transformTableToPatient(table);
//        editPatient(patient);
//        waitForSpinner();
//    }

//    public void createPatientWithId(Table table) throws Exception {
//        Patient patient = transformTableToPatient(table);
//        registerPatientWithID(patient);
//
//        waitForSpinner();
//        String path = driver.getCurrentUrl();
//        String uuid = path.substring(path.lastIndexOf('/') + 1);
//        if (!Objects.equals(uuid, "new")) {
//            patient.setUuid(uuid);
//            patient.setIdentifier(_patientIdentifierValue.getText());
//            storePatientInSpecStore(patient);
//        }
//    }

//    protected Patient transformTableToPatient(Table table) throws Exception {
//        List<TableRow> rows = table.getTableRows();
//        List<String> columnNames = table.getColumnNames();
//
//        if (rows.size() != 1) {
//            throw new TestSpecException("Only one patient should be provided in the table");
//        }
//
//        return transformTableRowToPatient(rows.get(0), columnNames);
//    }

//    public void enterVisitDetailsFromTable(Table table) {
//        waitForSpinner();
//        List<WebElement> webElements = driver.findElements(By.cssSelector(".field-value>div>div>input"));
//        WebElement webElement = driver.findElement(By.cssSelector(".field-value>div>div>textarea"));
//        webElement.clear();
//        for (WebElement element:webElements)
//            element.clear();
//
//        webElements.get(0).sendKeys(table.getColumnValues("height").get(0));
//        webElements.get(1).sendKeys(table.getColumnValues("weight").get(0));
//        webElements.get(2).sendKeys(table.getColumnValues("fees").get(0));
//        if(table.getColumnNames().contains("comments"))
//            webElement.sendKeys(table.getColumnValues("comments").get(0));
//
//        driver.findElement(By.cssSelector(".confirm")).click();
//
//    }

//    public String getDisplayControlText(String displayControl) {
//        if(displayControl.toLowerCase().contains("bmi"))
//            return driver.findElement(By.cssSelector(".concept-set-container-view")).getText().trim().replace("\n", "");
//        return null;
//    }

//    public void verifyPatientDetails(Patient patient) {
//        try{
//            if(enterID_checkbox.isDisplayed() && patient.getIdNumber()!= null) {
//                Assert.assertEquals("Identifier dont match",patient.getIdentifier(),txtRegistrationNumber.getAttribute("value"));
//            }} catch (NoSuchElementException ex){
//
//        }
//
//        Assert.assertEquals("First Name dont match",patient.getFirstName(),txtPatientName.getAttribute("value"));
//        Assert.assertEquals("Last Name dont match",patient.getLastName(),familyName.getAttribute("value"));
//
//        Assert.assertEquals("Gender dont match",patient.getGender(),new Select(gender).getFirstSelectedOption().getText());
//        Assert.assertEquals("Age dont match",patient.getAge(),ageYears.getAttribute("value"));
//
//    }

//    public void verifyRequiredField() {
//        Assert.assertEquals("","true",driver.findElement(By.id("givenName")).getAttribute("required"));
//        Assert.assertEquals("","true",driver.findElement(By.id("familyName")).getAttribute("required"));
//        Assert.assertEquals("","true",driver.findElement(By.id("gender")).getAttribute("required"));
//        Assert.assertEquals("","true",driver.findElement(By.id("ageYears")).getAttribute("required"));
//        Assert.assertEquals("","true",driver.findElement(By.id("ARV Naif/ Non Naif")).getAttribute("required"));
//        Assert.assertEquals("","[0-9]{10}",driver.findElement(By.id("Tel 1")).getAttribute("pattern"));
//        Assert.assertEquals("","[0-9]{10}",driver.findElement(By.id("Tel 2")).getAttribute("pattern"));
//    }

//    public void verifyFrenchInCreateNewPage() {
//        Assert.assertEquals("","Nom du patient*",driver.findElement(By.xpath("//*[@id=\"view-content\"]/div[3]/div/div/form/div[2]/div/div[1]/section[1]/article/div[1]/label")).getText());
//        Assert.assertEquals("","Sexe *",driver.findElement(By.xpath("//*[@id=\"view-content\"]/div[3]/div/div/form/div[2]/div/div[1]/section[2]/article/div[1]/label")).getText());
//        Assert.assertEquals("","Age*",driver.findElement(By.xpath("//*[@id=\"age\"]/div[1]/label")).getText());
//    }

//    public void createPatientandStartVisit(Table table) throws Exception {
//        Patient patient = transformTableToPatient(table);
//        createPatients(table);
//
//        waitForSpinner();
//
//        if(opdVisitBtn.getText().contains(patient.getVisitType())){
//            opdVisitBtn.click();
//        }
//        else if (ipdVisitBtn.getText().contains(patient.getVisitType())){
//            ipdVisitBtn.click();
//        }
//
//    }

    public void getInfantRegisterFields(){

    }

    public void verifyBabyFieldsInRegistrationPage() {

        Assert.assertNotNull(babyExposecheckbox);

        Select status_HIV = new Select(hivStatus);
        Assert.assertNotNull(status_HIV);

        List<WebElement> status_options = status_HIV.getOptions();
        List<String> options_text = new ArrayList<String>();
        for (WebElement option: status_options) {
            options_text.add(option.getText());
        }
        Assert.assertTrue(options_text.contains("Indeterminé"));

        //WebElement relation_toggle = driver.findElement(By.cssSelector("[toggle=\"patient.hasRelationships\"]"));
        relation_toggle.click();

        //Select relation_dropdown = new Select(driver.findElement(By.cssSelector("[ng-model=\"newRelationship.relationshipType.uuid\"]")));
        Select relation_select = new Select(relation_dropdown);
        Assert.assertNotNull(relation_select);
        List<WebElement> relation_options = relation_select.getOptions();
        List<String> relation_options_text = new ArrayList<String>();
        for (WebElement option: relation_options) {
            relation_options_text.add(option.getText());
        }

        for (String relation_text: relation_options_text){
            Assert.assertNotNull(relation_text);
        }

        Assert.assertNotNull(relation_editText);

    }


//    public void registerBaby(Patient patient, String relation_id) throws InterruptedException {
//        waitForSpinner();
//        try{
//            if(enterID_checkbox.isDisplayed() & patient.getIdNumber()!= null) {
//                enterID_checkbox.click();
//                txtRegistrationNumber.sendKeys(patient.getIdNumber());
//            }
//        } catch (NoSuchElementException ex){
//
//        }
//        txtPatientName.clear();
//        txtPatientName.sendKeys(patient.getFirstName());
//
//        familyName.clear();
//        familyName.sendKeys(patient.getLastName());
//
//        new Select(gender).selectByVisibleText(patient.getGender());
//
//        ageDays.clear();
//        ageDays.sendKeys(patient.getAge());
//        patient.setAge("0");
//
//        new Select(cohortType).selectByVisibleText(patient.getCohortType());
//        new Select(hivStatus).selectByVisibleText(patient.getHivStatus());
//
//        babyExposecheckbox.click();
//
//        new Select(relation_dropdown).selectByVisibleText("Partenaire");
//
//        relation_editText.clear();
//        relation_editText.sendKeys(relation_id);
//
//        try {
//            commune.clear();
//            commune.sendKeys(patient.getCommune());
//            commune.sendKeys(Keys.DOWN);
//            waitForElementOnPage(By.xpath(".//a[text()=\"" + patient.getCommune() + "\"]"));
//            findElement(By.xpath(".//a[text()=\"" + patient.getCommune() + "\"]")).click();
//            WebDriverWait wait = new WebDriverWait(driver, 3);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal-refill-button")));
//
//            List<WebElement> elements = driver.findElements(By.cssSelector("#modal-refill-button"));
//
//            if (elements.size() != 0) {
//                elements.get(0).click();
//            }
//        } catch (Exception e) {
//        }
//
//        new Select(arvs).selectByVisibleText(patient.getarvNaifNonNaif());
//
//        clickSave();
//
//    }

//    public void createBabyRecord(Table table , String relationID) throws Exception {
//        Patient patient = transformTableToPatient(table);
//        registerBaby(patient, relationID);
//
//        waitForSpinner();
//        String path = driver.getCurrentUrl();
//        String uuid = path.substring(path.lastIndexOf('/') + 1);
//        if (!Objects.equals(uuid, "new")) {
//            patient.setUuid(uuid);
//            patient.setIdentifier(_patientIdentifierValue.getText());
//            storePatientInSpecStore(patient);
//        }
//    }

}
