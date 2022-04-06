package Java.JSON.Database.server.cli;

import com.beust.jcommander.Parameter;
import com.google.gson.annotations.Expose;

public class CommandLineArgs {

    @Expose
    @Parameter(
            names = {"-t", "--type"},
            description = "Type of request",
            order = 0
    )
    public String type;

    @Expose
    @Parameter(
            names = {"-k", "--key"},
            description = "Index of the cell",
            order = 1
    )
    public String key;

    @Expose
    @Parameter(
            names = {"-v", "--value"},
            description = "Value to save in the database",
            order = 2
    )
    public String value;

    @Expose
    @Parameter(
            names = {"-in", "--input"},
            description = "Database file to be read from",
            order = 3
    )
    public String fileName;
}
