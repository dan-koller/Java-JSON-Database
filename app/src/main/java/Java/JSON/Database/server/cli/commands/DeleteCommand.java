package Java.JSON.Database.server.cli.commands;

import Java.JSON.Database.server.Database;
import com.google.gson.JsonElement;

public class DeleteCommand implements Command {

    private final JsonElement key;

    public DeleteCommand(JsonElement key) {
        this.key = key;
    }

    @Override
    public void execute() {
        Database.delete(key);
    }
}
