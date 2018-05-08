package org.bahmni.mart.automation.readers;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.bahmni.mart.automation.models.CustomTableDefinition;
import org.bahmni.mart.automation.models.CustomTables;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CustomTablesJsonReader {


        public CustomTables readJson() {

            Gson customtablessjsonobj = new Gson();
            JsonReader jr = new JsonReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/customTablesmetaData.json")));
            CustomTables inputdatajson = customtablessjsonobj.fromJson(jr, CustomTables.class);

            return inputdatajson;

        }

        public List<CustomTableDefinition> getCustomTablesfromJson () throws FileNotFoundException {

            CustomTables allcustomtablesjson = readJson();
            List<CustomTableDefinition> customTablesJsons = allcustomtablesjson.getTables();

            return customTablesJsons;
        }

        public ArrayList<String> getCustomTablesColumnsList(String form_name) throws FileNotFoundException{

            ArrayList<String> columnsList = new ArrayList<String>();

            CustomTables allcustomtablesjson = readJson();

            for (CustomTableDefinition table : allcustomtablesjson.getTables()) {

                if (table.getTableName().equals(form_name)) {

                    ArrayList<String> columns = table.getColumns();
                    for ( String column : columns ) {

                        if ( column.length() > 63 ) {
                            System.out.println("For form"+ form_name +"Sanitized string before: " + column);
                            column = column.substring(0,63);
                            System.out.println("Sanitized string after: " + column);
                        }

                        columnsList.add(column);
                    }

                    return columnsList;
                }

            }

            return null;
        }

        public static void main (String args[]) throws FileNotFoundException {
            CustomTablesJsonReader obsjr = new CustomTablesJsonReader();
            obsjr.readJson();
        }

}

