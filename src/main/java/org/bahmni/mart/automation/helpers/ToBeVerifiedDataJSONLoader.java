package org.bahmni.mart.automation.helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bahmni.mart.automation.models.FormData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class FQDNJSONLoader {

    public static List<FormData> readFormDataFromJson()  {
        JsonObject json = null;
        try {
            json = (JsonObject) new JsonParser().
                    parse(new FileReader("src/main/resources/configurations/formDataToBeVerified.json"));
        } catch (FileNotFoundException e) {
            e.getMessage();
        }

        Set<String> allFormNames = json.keySet();
        List<FormData> formDataList = new ArrayList<>();

        for(String formName : allFormNames){
            FormData formData = new FormData();
            formData.setFormName(formName);
            JsonObject formDataJson = json.getAsJsonObject(formName);
            Set<String> fields = formDataJson.keySet();
            Map<String,String> fieldValues = new LinkedHashMap<>();
            for(String fieldName : fields){
                if (!formDataJson.get(fieldName).isJsonNull()) {
                    fieldValues.put(fieldName, formDataJson.get(fieldName).getAsString());
                }
                else {
                    fieldValues.put(fieldName, null);
                }
            }
            formData.setFieldValueMap(fieldValues);
            formDataList.add(formData);
        }
        return formDataList;
    }
}
