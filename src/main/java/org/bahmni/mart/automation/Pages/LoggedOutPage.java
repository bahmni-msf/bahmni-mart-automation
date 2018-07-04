package org.bahmni.mart.automation.Pages;

import org.bahmni.mart.automation.helpers.Utils;
import org.openqa.selenium.WebDriver;

import java.util.Properties;

public class LoggedOutPage {

    private WebDriver driver;

    Properties connprops = Utils.getExternalizedParameters();

    private String PAGE_URL= connprops.getProperty("PAGE_URL");

    public LoggedOutPage(WebDriver driver){

        this.driver = driver;
        driver.get(PAGE_URL);
    }
}
