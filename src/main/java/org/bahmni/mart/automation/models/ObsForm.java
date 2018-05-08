package org.bahmni.mart.automation.models;

import java.util.ArrayList;

public class ObsForm {

    private String form_name;
    private ArrayList<String> conceptsList;

    public String getFormName(){
        return form_name;
    }

    public ArrayList<String> getConceptsList() {
        return conceptsList;
    }
}
