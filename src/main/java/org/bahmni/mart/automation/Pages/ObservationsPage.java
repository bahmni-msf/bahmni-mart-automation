package org.bahmni.mart.automation.Pages;

import org.bahmni.mart.automation.models.FormData;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bahmni.mart.automation.helpers.PageWaits.waitForElement;
import static org.bahmni.mart.automation.helpers.PageWaits.waitForSpinner;

public class ObservationsPage {

    @FindBy(how = How.CSS, using = ".leaf-observation-node")
    public List<WebElement> observationNodes;


    public void fillTemplateData(FormData formData, WebDriver driver) {
        Map<String, String> fieldValueMap = formData.getFieldValueMap();
        //List<WebElement> observationNodes = driver.findElements(By.cssSelector(".leaf-observation-node"));
        for (String fieldName : fieldValueMap.keySet()) {
            boolean fieldFound = false;
            String value = fieldValueMap.get(fieldName);
            for (WebElement observationNode : observationNodes) {
                String observLabel = observationNode.findElement(By.tagName("label")).getText().trim();
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
//                            if (inputField.getText().isEmpty()) {
//                                inputField.sendKeys(value, Keys.TAB);
//                                break;
//                            }
                            if (!inputField.getAttribute("value").isEmpty() && !inputField.getAttribute("type").equalsIgnoreCase("date")){
                                inputField.clear();
                                inputField.sendKeys(value, Keys.TAB);
                                break;
                            }
                            else {
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
                                if ( !activeButtons.contains(button) && button.getText().equals(val)) {
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
                                else if (button.getText().contains(val) && !activeButtons.contains(button)) {
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
                //observationNodes = driver.findElements(By.cssSelector(".leaf-observation-node"));

            }
            if (!fieldFound) {
                Assert.fail("Field " + fieldName + " not found or disabled");
            }
        }
        driver.findElement(By.cssSelector(".save-consultation")).click();
    }

    public void verifyTemplateData(FormData formData, WebDriver driver) {
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
                        //System.out.println("Data from json: "+ value + ". Data from obs form: "+ autoComplete.getAttribute("value").equalsIgnoreCase(value));
                        if (!autoComplete.getAttribute("type").equalsIgnoreCase("date")) {
                            Assert.assertTrue(String.format("%s Data does not match", fieldName), autoComplete.getAttribute("value").equalsIgnoreCase(value));
                            break;
                        }
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
                            Assert.assertTrue("Button " + "for field name " + fieldName+ " " + val +" is not selected", activeButtonNames.contains(val) );
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

    public void selectTemplate(String templateName, WebDriver driver) {
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

    private static boolean hasTag(WebElement answer, String input) {
        boolean val = true;
        try {
            answer.findElement(By.tagName(input));
        } catch (NoSuchElementException e) {
            val = false;
        }
        return val;
    }

    private static void fillAutocomplete(WebElement autoComplete, String value, WebDriver driver) {
        if (!autoComplete.getAttribute("value").isEmpty()) {
            autoComplete.clear();
        }
        autoComplete.sendKeys(value);
        autoComplete.sendKeys(Keys.DOWN);
//        waitForElementOnPage(By.xpath(".//a[text()=\"" + value + "\"]"));
        driver.findElement(By.xpath(".//a[text()=\"" + value + "\"]")).click();
    }

    private static void clickTemplateButton(WebDriver driver) {
        WebElement addFormButton = driver.findElement(By.cssSelector("#template-control-panel-button"));
        addFormButton.click();
    }

}
