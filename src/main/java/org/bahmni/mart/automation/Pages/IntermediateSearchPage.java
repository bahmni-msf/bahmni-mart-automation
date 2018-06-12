package org.bahmni.mart.automation.Pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class IntermediateSearchPage {

    @FindBy(how= How.CSS, using = ".back-btn")
    public WebElement iconHome;

    public void goToMainHome(){

        iconHome.click();
    }
}
