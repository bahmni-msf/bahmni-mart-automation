package org.bahmni.mart.automation.Pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class RegistrationSearch {

    @FindBy(how= How.CSS, using = ".fa-home")
    public WebElement iconHome;

    @FindBy(how= How.CSS, using = ".fa-search")
    public WebElement iconSearch;

    @FindBy(how= How.CSS, using = "i.fa-plus")
    public WebElement iconCreateNew;

    @FindBy(how= How.CSS, using = "#registrationNumber")
    public WebElement txtRegistration;

    @FindBy(how= How.CSS, using = "#name")
    public WebElement txtName;

    @FindBy(how= How.CSS, using = "#addressFieldValue")
    public WebElement txtGramPanchayat;

    @FindBy(how= How.CSS, using = "#identifierPrefix")
    public List<WebElement> txtIdentifier;

    @FindBy(how= How.CSS, using = ".search-patient-id .reg-srch-btn")
    public WebElement btnIdentifierSearch;

    @FindBy(how = How.CSS, using = ".search-seperator-r .reg-srch-btn")
    public WebElement btnRegSearch;

    @FindBy(how= How.CSS, using = ".registraition-search-results-container > table")
    public WebElement gridSearchResults;

    @FindBy(how= How.CSS, using = "#programAttribute")
    public WebElement prgm_attribute;

    private WebDriver driver;

    public RegistrationSearch(WebDriver driver) {

        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    public void clickSearch() {
        iconSearch.click();
    }

    public void clickHome() {
        iconHome.click();
    }

    public void clickCreateNew() {
        iconCreateNew.click();
    }

    public void enterName(String name) {
        txtName.sendKeys(name,Keys.ENTER);
    }

    public void searchByExactIdentifier(String prefix, String id){
        selectPrefix(prefix);

        txtRegistration.sendKeys(id);
        btnIdentifierSearch.click();
    }

    private void selectPrefix(String prefix) {
        if(txtIdentifier.size()>0){
            new Select(txtIdentifier.get(0)).selectByVisibleText(prefix);
        }
    }

    public void searchByIdentifier(String prefix, String id){
        selectPrefix(prefix);

        txtRegistration.sendKeys(id);
        btnIdentifierSearch.click();
    }

    public void searchUsingIdentifier ( String Id){
        txtRegistration.sendKeys(Id);
        btnIdentifierSearch.click();
    }

    public void searchByName(String name){
        txtName.sendKeys(name);
        btnRegSearch.click();
    }

    public void searchByProgramAttribute(String attribute){
        prgm_attribute.sendKeys(attribute);
        btnRegSearch.click();
    }

}
