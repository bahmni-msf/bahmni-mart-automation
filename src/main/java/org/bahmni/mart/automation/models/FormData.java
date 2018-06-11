package org.bahmni.mart.automation.models;

import java.util.List;
import java.util.Map;

public class FormData {

    private  String formName;
    private Map<String, String> fieldValueMap;


    public Map<String, String> getFieldValueMap() {
        return fieldValueMap;
    }

    public void setFieldValueMap(Map<String, String> fieldValueMap) {
        this.fieldValueMap = fieldValueMap;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}
