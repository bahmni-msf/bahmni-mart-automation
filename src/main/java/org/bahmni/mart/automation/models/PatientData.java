package org.bahmni.mart.automation.models;

import java.util.List;
import java.util.Map;

public class PatientData {

    private  String patient;
    private Map<String, String> fieldValueMap;


    public Map<String, String> getFieldValueMap() {
        return fieldValueMap;
    }

    public void setFieldValueMap(Map<String, String> fieldValueMap) {
        this.fieldValueMap = fieldValueMap;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }
}
