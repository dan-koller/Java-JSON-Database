package Java.JSON.Database.server.requests;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/*
 * This is a helper class that converts
 * cli argument strings into an actual request
 */
public class RequestBuilder {

    private static final Request request = new Request();

    public static Request buildFromCommandLine(String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);

        request.setType(jsonObject.get("type").getAsString());
        request.setKey(jsonObject.get("key"));
        request.setValue(jsonObject.get("value"));

        return request;
    }

}
