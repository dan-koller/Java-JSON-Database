package Java.JSON.Database.server.requests;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

public class Request {

    @Expose
    private String type;

    @Expose
    private JsonElement key;

    @Expose
    private JsonElement value;

    @Expose
    private String fileName;

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonElement getKey() {
        return key;
    }

    public void setKey(JsonElement key) {
        this.key = key;
    }

    public JsonElement getValue() {
        return value;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return type + " " + key + " " + value;
    }
}
