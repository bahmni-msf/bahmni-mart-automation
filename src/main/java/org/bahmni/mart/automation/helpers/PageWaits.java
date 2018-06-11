package org.bahmni.mart.automation.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageWaits {

    public static void waitForSpinner(WebDriver driver) {
        try {
            waitForElement(driver, ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#overlay")));
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <T> T waitForElement(WebDriver driver, ExpectedCondition<T> expectedCondition) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 180);
            return wait.until(expectedCondition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
