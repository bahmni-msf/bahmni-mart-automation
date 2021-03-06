package org.bahmni.mart.automation.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage {


        //public static final String URL = BASE_URL.concat("/home#/dashboard");

        private WebDriver driver;

        @FindBy(how= How.ID, using = "bahmni.registration")
        public WebElement registration;

        @FindBy(how = How.ID, using = "bahmni.adt")
        public WebElement inpatient;

        @FindBy(how= How.CSS, using = "i.icon-bahmni-documents")
        public WebElement patientDocuments;

        @FindBy(how= How.CSS, using = "i.icon-bahmni-admin")
        public WebElement admin;

        @FindBy(how= How.CSS, using = "i.icon-bahmni-reports")
        public WebElement exports;

        @FindBy(how= How.CSS, using = "i.icon-bahmni-program")
        public WebElement programs;

        @FindBy(how = How.CSS, using = "i.fa-caret-down")
        public WebElement user_info;

        @FindBy(how= How.CSS, using = "i.fa-power-off")
        public WebElement logout;

        @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Clinical")
        public WebElement clinical;

        @FindBy(how = How.CSS, using = "#bahmni\\.orders")
        public WebElement orders;

        @FindBy(how = How.ID, using = "bahmni.implementer.interface")
        public WebElement implementerInterface;

        @FindBy(how = How.CSS, using = "#bahmni\\.radiology\\.document\\.upload")
        public WebElement radiologyUpload;

        @FindBy(how = How.CSS, using = "i.fa-user-secret")
        public WebElement formBuilder;

        public HomePage(WebDriver driver) {

            this.driver = driver;
            PageFactory.initElements(driver,this);
        }

        public void clickRegistrationApp(){
            registration.click();
        }

}
