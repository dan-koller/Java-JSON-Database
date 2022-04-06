package Java.JSON.Database.server.cli.commands;

import Java.JSON.Database.server.Database;
import com.google.gson.JsonElement;

public class GetCommand implements Command {

    private final JsonElement key;
    private JsonElement result;

    public GetCommand(JsonElement key) {
        this.key = key;
    }

    public JsonElement getResult() {
        return result;
    }

    @Override
    public void execute() {
        result = Database.get(key);
    }
}
