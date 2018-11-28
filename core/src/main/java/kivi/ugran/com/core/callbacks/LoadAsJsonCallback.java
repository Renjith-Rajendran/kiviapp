package kivi.ugran.com.core.callbacks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface LoadAsJsonCallback {
    void callback(JsonObject object);

    void callback(JsonElement object);
}

