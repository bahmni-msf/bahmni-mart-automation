package org.bahmni.mart.automation.Pages;



import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ConsultationPage {

    @FindBy(how = How.CSS, using = ".btn--success")
    public WebElement clinical;

    @FindBy(how = How.CSS, using = ".consultation-tabs .tab-item a")
    public List<WebElement> tabs;

    @FindBy(how = How.CSS, using = ".patient-info")
    public WebElement patient_profile;

    @FindBy(how = How.CSS, using = ".save-consultation")
    public WebElement save;

    @FindBy(how = How.CSS, using = ".tab-selected")
    public WebElement activeTab;

    @FindBy(how = How.CSS, using = ".retro-date-widget-header.fr")
    public WebElement registrationDeskButton;

    public void saveConsultation() {
        save.click();
    }

    public void clickPatientProfile() {
        patient_profile.click();
    }

    public void clickOnTab(String tab) {
        for (WebElement eachTab : tabs) {
            if (eachTab.getText().contains(tab)) {
                eachTab.click();
                return;
            }
        }
    }

    public String getActiveTab() {
        return activeTab.getText();
    }

    public void clickOnConsultation(){
        clinical.click();
    }


}
