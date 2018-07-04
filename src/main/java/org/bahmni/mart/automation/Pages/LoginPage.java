package org.bahmni.mart.automation.Pages;

import org.bahmni.mart.automation.helpers.Utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

public class LoginPage {

    private WebDriver driver;

    Properties connprops = Utils.getExternalizedParameters();
    private String PAGE_URL= connprops.getProperty("PAGE_URL");

    @FindBy(how= How.CSS, using = "#username")
    public WebElement txtUserName;

    @FindBy(how= How.CSS, using = "#password")
    public WebElement txtPassword;

    @FindBy(how= How.CSS, using = "#location")
    public WebElement cmbLocation;

    @FindBy(how= How.CSS, using = "#locale")
    public WebElement cmbLocale;

    @FindBy(how= How.CSS, using = ".confirm")
    public WebElement btnLogin;

        public LoginPage(WebDriver driver){

            this.driver = driver;
            driver.get(PAGE_URL);
            PageFactory.initElements(driver,this);

        }


        public void login(String username, String password){
            login(username,password,System.getenv("BAHMNI_GAUGE_APP_LOCATION"),"en");
        }

        public void login(String username, String password, String location){
            login(username,password,location,"en");
        }

        public void login(String username, String password, String location, String locale){
            txtUserName.sendKeys(username);
            txtPassword.sendKeys(password);
            new Select(cmbLocation).selectByVisibleText(location);
            new Select(cmbLocale).selectByVisibleText(locale);
            btnLogin.click();
        }

}
