package kivi.ugran.com.kds.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import kivi.ugran.com.kds.model.registration.Coordinates;

//https://www.javacodegeeks.com/2013/10/android-json-tutorial-create-and-parse-json-data.html#
//https://stackoverflow.com/questions/24860255/gson-property-order-in-android
//https://stackoverflow.com/questions/42570242/add-a-name-to-jsonarray-or-jsonobject-using-java-json-simple-jar-library

/**
 * This class is added to enforce the order of properties lng and lat .
 * This is required to cover for a server limitations
 */

public class CoordinatorSerializer implements JsonSerializer<Coordinates> {
    @Override public JsonElement serialize(Coordinates request, Type type, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        JsonObject object = new JsonObject();
        object.add("lng", context.serialize(request.getLng()));
        object.add("lat", context.serialize(request.getLat()));
        jsonArray.add(object);
        return object;
    }
}
