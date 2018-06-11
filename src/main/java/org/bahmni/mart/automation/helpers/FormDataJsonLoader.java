package org.bahmni.mart.automation.helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bahmni.mart.automation.models.FormData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonLoader {

    public static List<FormData> readFormDataFromJson()  {
        JsonObject json = null;
        try {
            json = (JsonObject) new JsonParser().
                    parse(new FileReader("src/main/resources/configurations/formData.json"));
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
            Map<String,String> fieldValues = new HashMap<>();
            for(String fieldName : fields){
                fieldValues.put(fieldName, formDataJson.get(fieldName).getAsString());
            }
            formData.setFieldValueMap(fieldValues);
            formDataList.add(formData);
        }
        return formDataList;
    }
}
