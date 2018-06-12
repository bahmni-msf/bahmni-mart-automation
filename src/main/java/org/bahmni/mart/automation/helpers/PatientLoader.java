package org.bahmni.mart.automation.helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bahmni.mart.automation.models.FormData;
import org.bahmni.mart.automation.models.PatientData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class PatientLoader {

        public static List<PatientData> readFormDataFromJson()  {
            JsonObject json = null;
            try {
                json = (JsonObject) new JsonParser().
                        parse(new FileReader("src/main/resources/configurations/patientData.json"));
            } catch (FileNotFoundException e) {
                e.getMessage();
            }

            Set<String> allPatients = json.keySet();
            List<PatientData> patientDetailsList = new ArrayList<>();

            for(String patient : allPatients){
                PatientData patientData = new PatientData();
                patientData.setPatient(patient);
                JsonObject patientDetailsJson = json.getAsJsonObject(patient);
                Set<String> patientDetails = patientDetailsJson.keySet();
                Map<String,String> patientValues = new LinkedHashMap<>();
                for(String patientdetail : patientDetails){
                    patientValues.put(patientdetail, patientDetailsJson.get(patientdetail).getAsString());
                }
                patientData.setFieldValueMap(patientValues);
                patientDetailsList.add(patientData);
            }
            return patientDetailsList;
        }
}
