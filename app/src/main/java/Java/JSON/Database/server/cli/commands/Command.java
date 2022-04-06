package Java.JSON.Database.server.cli.commands;

import com.google.gson.JsonElement;

public interface Command {
    void execute();

    default JsonElement getResult() {
        return null;
    }
}
