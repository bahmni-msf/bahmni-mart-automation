package org.bahmni.mart.automation.helpers;

import org.bahmni.mart.automation.Pages.*;
import org.bahmni.mart.automation.models.FormData;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class SeleniumHelper {

//    public static void main(String args[]) {
//        System.setProperty("webdriver.gecko.driver", "/Users/lakshmip/Documents/chromedriver");
//        System.setProperty("webdriver.chrome.driver", "/Users/lakshmip/Documents/chromedriver");
//        WebDriver driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
//
//        login(driver);
//        goToProgramDashBoard(driver);
//        goToConsultation(driver);
//        //fillFormData(driver, "test", );
//    }

    public static void fillFormData(WebDriver driver, String formName, FormData formData) {
        //List<FormData> formDataList = FormDataJsonLoader.readFormDataFromJson();

        ObservationsPage obspage = PageFactory.initElements(driver, ObservationsPage.class);

            obspage.selectTemplate(formName, driver);
            PageWaits.waitForSpinner(driver);

            obspage.fillTemplateData(formData, driver);
            PageWaits.waitForSpinner(driver);

            obspage.verifyTemplateData(formData, driver);
            PageWaits.waitForSpinner(driver);
    }

    public static void goToConsultation(WebDriver driver, String patientIQ) {
        ProgramManagementPage pmp = PageFactory.initElements(driver, ProgramManagementPage.class);
//        pmp.clickonProgram();
//        waitForSpinner(driver);
        pmp.goToBackPage();
        PageWaits.waitForSpinner(driver);

        IntermediateSearchPage isp = PageFactory.initElements(driver, IntermediateSearchPage.class);
        isp.goToMainHome();
        PageWaits.waitForSpinner(driver);
        HomePage home = new HomePage(driver);
        home.clickRegistrationApp();
        waitForSpinner(driver);
        RegistrationSearch rs = new RegistrationSearch(driver);
        rs.searchUsingIdentifier(patientIQ);
        waitForSpinner(driver);
        RegistrationFirstPage rfp = PageFactory.initElements(driver, RegistrationFirstPage.class);
        rfp.enterProgramDashboardPage();
        //rfp.enterVisitDetailsPage();
        waitForSpinner(driver);

        pmp.clickonProgram();
        waitForSpinner(driver);

        ConsultationPage clp = PageFactory.initElements(driver, ConsultationPage.class);
        clp.clickOnConsultation();
        waitForSpinner(driver);
    }

    public static void goToProgramDashBoard(WebDriver driver, String patientIQ) {
//        HomePage home = new HomePage(driver);
//        home.clickRegistrationApp();
//        waitForSpinner(driver);
//        RegistrationSearch rs = new RegistrationSearch(driver);
//        rs.searchUsingIdentifier(patientIQ);
//        waitForSpinner(driver);
        RegistrationFirstPage rfp = PageFactory.initElements(driver, RegistrationFirstPage.class);
        rfp.enterVisitDetailsPage();
        waitForSpinner(driver);
        ProgramManagementPage pmp = PageFactory.initElements(driver, ProgramManagementPage.class);
        // We are hardcoding the program name and program start date here. These can be generalized and externalized when needed.
        pmp.enrollToProgram("Reconstructive Surgery", "04/13/2018");
        PageWaits.waitForSpinner(driver);
    }

    public static void goToNewPatientForm(WebDriver driver) {
        HomePage home = new HomePage(driver);
        home.clickRegistrationApp();
        waitForSpinner(driver);
        RegistrationSearch rs = new RegistrationSearch(driver);
        rs.clickCreateNew();
        waitForSpinner(driver);
    }

    public static void login(WebDriver driver) {
        LoginPage lp = new LoginPage(driver);
        waitForSpinner(driver);
        lp.login("superman", "P@ssw0rd", "Admission");
        waitForSpinner(driver);
    }

    public static void selectTemplate(String templateName, WebDriver driver) {
        int formSelected = 0;
        List<WebElement> templateList = driver.findElements(By.cssSelector("section.concept-set-panel-left  li .concept-set-name"));
        if (templateList.size() > 0) {
            for (WebElement form : templateList) {
                if (form.getText().contains(templateName)) {
                        form.click();
                        waitForSpinner(driver);
                        formSelected = 1;
                        break;
                }
            }
        }
        if (formSelected == 0) {
            clickTemplateButton(driver);
            WebElement templatePanel = driver.findElement(By.cssSelector(".template-control-panel"));
            List<WebElement> allForms = templatePanel.findElements(By.tagName("button"));

            for (WebElement form : allForms) {
                if (form.getText().contains(templateName)) {
                    form.click();
                    waitForSpinner(driver);
                    formSelected = 1;
                    break;
                }
            }
        }
        if (formSelected == 0) {
            Assert.fail("Form " + templateName + " not found");
        }
    }

    private static void fillTemplateData(FormData formData, WebDriver driver) {
        Map<String, String> fieldValueMap = formData.getFieldValueMap();
        List<WebElement> observationNodes = driver.findElements(By.cssSelector(".leaf-observation-node"));
        for (String fieldName : fieldValueMap.keySet()) {
            boolean fieldFound = false;
            String value = fieldValueMap.get(fieldName);
            for (WebElement observationNode : observationNodes) {
                String observLabel = observationNode.findElement(By.tagName("label")).getText().trim();
                if (observLabel.isEmpty()) {
                        waitForElement(driver, ExpectedConditions.textToBePresentInElementValue(By.tagName("label"), fieldName));
                        //waitForElement(driver, ExpectedConditions.visibilityOfElementLocated(By.tagName("label").equals(fieldName)));
                        observLabel = observationNode.findElement(By.tagName("label")).getText().trim();
                }
                if (observLabel.equals(fieldName)) {
                    fieldFound = true;
                    if (hasTag(observationNode, "input")) {
                        WebElement autoComplete = observationNode.findElement(By.tagName("input"));
                        if (autoComplete.getAttribute("class").contains("ui-autocomplete-input")) {
                            fillAutocomplete(autoComplete, value , driver);
                            break;
                        } else if (autoComplete.getAttribute("class").contains("input ng-pristine ng-untouched ng-valid")) {
                            fillAutocomplete(autoComplete, value, driver);
                            break;
                        } else {
                            WebElement inputField = observationNode.findElement(By.tagName("input"));
                            if (inputField.getText().isEmpty()) {
                                inputField.sendKeys(value, Keys.TAB);
                                break;
                            }
                        }
                    } else if (hasTag(observationNode, "textarea")) {
                        WebElement textField = observationNode.findElement(By.tagName("textarea"));
                        if (textField.getText().isEmpty()) {
                            textField.sendKeys(value);
                            break;
                        }
                    } else if (hasTag(observationNode, "select")) {
                        new Select(observationNode.findElement(By.tagName("select"))).selectByVisibleText(value);
                        break;
                    } else if (hasTag(observationNode, "button")) {
                        List<WebElement> buttons = observationNode.findElements(By.tagName("button"));
                        List<WebElement> activeButtons = observationNode.findElements(By.cssSelector(".grid-row-element.active"));
                        String[] multiSelect = value.split(";");
                        for (String val : multiSelect) {
                            for (WebElement button : buttons) {
                                if (button.getText().contains(val) && !activeButtons.contains(button)) {
                                    try {
                                        button.click();
                                        break;
                                    } catch (WebDriverException e) {
                                        JavascriptExecutor js = ((JavascriptExecutor) driver);
                                        js.executeScript("scrollBy(0,1000)");
                                        Actions actions = new Actions(driver);
                                        actions.moveToElement(button).click().build().perform();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                observationNodes = driver.findElements(By.cssSelector(".leaf-observation-node"));

            }
            if (!fieldFound) {
                Assert.fail("Field " + fieldName + " not found or disabled");
            }
        }
        driver.findElement(By.cssSelector(".save-consultation")).click();
    }

    private static void verifyTemplateData(FormData formData, WebDriver driver) {
        Map<String, String> fieldValueMap = formData.getFieldValueMap();
        List<WebElement> observationNodes = driver.findElements(By.cssSelector(".leaf-observation-node"));
        for ( String fieldName : fieldValueMap.keySet()) {
            boolean fieldFound = false;
            String value = fieldValueMap.get(fieldName);
            for (WebElement observationNode : observationNodes) {
                String observLabel = observationNode.findElement(By.tagName("label")).getText().trim();
                if (observLabel.equals(fieldName)) {
                    fieldFound = true;
                    if (hasTag(observationNode, "input")) {
                        WebElement autoComplete = observationNode.findElement(By.tagName("input"));
                        Assert.assertTrue(String.format("%s Data does not match", fieldName), autoComplete.getAttribute("value").equalsIgnoreCase(value));
                        break;
                    } else if (hasTag(observationNode, "textarea")) {
                        WebElement textField = observationNode.findElement(By.tagName("textarea"));
                        Assert.assertTrue(String.format("%s Data does not match", fieldName), textField.getAttribute("value").equalsIgnoreCase(value));
                        break;

                    } else if (hasTag(observationNode, "select")) {
                        Assert.assertTrue("Data does not match", new Select(observationNode.findElement(By.tagName("select"))).getFirstSelectedOption().getText().equalsIgnoreCase(value));
                        break;
                    } else if (hasTag(observationNode, "button")) {
                        List<WebElement> buttons = observationNode.findElements(By.cssSelector(".grid-row-element.active"));
                        String[] multiSelect = value.split(";");
                        List<String> activeButtonNames = buttons.stream().map(WebElement::getText).collect(Collectors.toList());
                        for (String val : multiSelect) {
                            Assert.assertTrue("Button is not selected", activeButtonNames.contains(val) );
                        }
                        break;
                    }
                }
            }
            if (!fieldFound) {
                Assert.fail("Field " + fieldName + " not found or disabled");
            }
        }
    }

    private static void fillAutocomplete(WebElement autoComplete, String value, WebDriver driver) {
        autoComplete.sendKeys(value);
        autoComplete.sendKeys(Keys.DOWN);
//        waitForElementOnPage(By.xpath(".//a[text()=\"" + value + "\"]"));
        driver.findElement(By.xpath(".//a[text()=\"" + value + "\"]")).click();
    }

    private static boolean hasTag(WebElement answer, String input) {
        boolean val = true;
        try {
            answer.findElement(By.tagName(input));
        } catch (NoSuchElementException e) {
            val = false;
        }
        return val;
    }

    private static void clickTemplateButton(WebDriver driver) {
        WebElement addFormButton = driver.findElement(By.cssSelector("#template-control-panel-button"));
        addFormButton.click();
    }


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

