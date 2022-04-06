package Java.JSON.Database.server.cli.commands;

import Java.JSON.Database.server.Database;
import com.google.gson.JsonElement;

public class SetCommand implements Command {

    private final JsonElement key;
    private final JsonElement message;

    public SetCommand(JsonElement key, JsonElement message) {
        this.key = key;
        this.message = message;
    }

    @Override
    public void execute() {
        Database.set(key, message);
    }
}
