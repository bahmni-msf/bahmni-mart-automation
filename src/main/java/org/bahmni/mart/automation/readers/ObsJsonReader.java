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



    public AllObsForms readJson() {

        Gson obsformsjsonobj = new Gson();
        JsonReader jr = new JsonReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/ObsFormsOthersfinal.json")));
        AllObsForms inputdatajson = obsformsjsonobj.fromJson(jr, AllObsForms.class);

        return inputdatajson;

    }

    public List<ObsForm> getObsFormsfromJson () throws FileNotFoundException {

        AllObsForms allformsjson = readJson();
        List<ObsForm> obsFormsJsons = allformsjson.getForms();

        return obsFormsJsons;
    }

    public ArrayList<String> getObsFormConceptList(String form_name) throws FileNotFoundException{

        ArrayList<String> concetpsList = new ArrayList<String>();

        AllObsForms allformsjson = readJson();

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

    public static void main (String args[]) throws FileNotFoundException {
        ObsJsonReader obsjr = new ObsJsonReader();
        obsjr.readJson();
    }

}
