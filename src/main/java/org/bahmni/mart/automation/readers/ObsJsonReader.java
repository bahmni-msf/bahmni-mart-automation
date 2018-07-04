package org.bahmni.mart.automation.readers;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.bahmni.mart.automation.models.AllObsForms;
import org.bahmni.mart.automation.models.ObsForm;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ObsJsonReader {



    public AllObsForms readJson(boolean readForMultiSelect) {

        Gson obsformsjsonobj = new Gson();
        JsonReader jr;

        if (readForMultiSelect) {
            jr = new JsonReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/ObsFormsWithMultiSelect.json")));
        }
        else {
            jr = new JsonReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/ObsFormsWithoutMultiSelect.json")));
        }
        AllObsForms inputdatajson = obsformsjsonobj.fromJson(jr, AllObsForms.class);

        return inputdatajson;

    }

    public List<ObsForm> getObsFormsfromJson (boolean readForMultiSelect) throws FileNotFoundException {

        AllObsForms allformsjson = readJson(readForMultiSelect);
        List<ObsForm> obsFormsJsons = allformsjson.getForms();

        return obsFormsJsons;
    }

    public ArrayList<String> getObsFormConceptList(String form_name, boolean readForMultiSelect) throws FileNotFoundException{

        ArrayList<String> concetpsList = new ArrayList<String>();

        AllObsForms allformsjson = readJson(readForMultiSelect);

        for (ObsForm form: allformsjson.getForms()) {

            if (form.getFormName().equals(form_name)) {

                ArrayList<String> concepts = form.getConceptsList();
                for ( String concept : concepts ) {

                    if ( concept.length() > 63 ) {
                        System.out.println("For form"+ form_name +"Sanitized string before: " + concept);
                        concept = concept.substring(0,63);
                        System.out.println("Sanitized string after: " + concept);
                    }

                    concetpsList.add(concept);
                }

                return concetpsList;
            }

        }

        return null;
    }

}
