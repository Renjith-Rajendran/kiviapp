package kivi.ugran.com.core;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import androidx.annotation.NonNull;

public class JSONUtils {
    public static String convertAsJsonString(@NonNull Object object){
        Gson gson = new Gson();
        String json = gson.toJson(object);
        if(json==null){
            json = "{}";
        }
        return json;
    }

    public static JsonElement convertAsJson(@NonNull String json){
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse("{}");
        if(json!=null && json.isEmpty()==false) {
            jsonElement = parser.parse(json);
        }
        return jsonElement;
    }

}
