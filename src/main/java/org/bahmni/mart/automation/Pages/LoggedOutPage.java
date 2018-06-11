package org.bahmni.mart.automation.Pages;

import org.openqa.selenium.WebDriver;

public class LoggedOutPage {

    private WebDriver driver;

    private String PAGE_URL="https://qa-reporting.ehealthunit.org/bahmni/home/index.html";

    public LoggedOutPage(WebDriver driver){

        this.driver = driver;
        driver.get(PAGE_URL);
    }
}
