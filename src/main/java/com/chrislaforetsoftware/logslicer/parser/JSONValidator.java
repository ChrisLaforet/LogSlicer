package com.chrislaforetsoftware.logslicer.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONValidator {

    private JSONValidator() {}

    // https://www.baeldung.com/java-validate-json-string

    public static boolean isValid(String possibleJson) {
        try {
            new JSONObject(possibleJson);
        } catch (JSONException e) {
            try {
                new JSONArray(possibleJson);
            } catch (JSONException ne) {
                return false;
            }
        }
        return true;
    }
}
