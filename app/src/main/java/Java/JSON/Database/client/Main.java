package Java.JSON.Database.client;

import Java.JSON.Database.server.cli.CommandLineArgs;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class Main {

    public static void main(String[] args) {
        // Process commandline arguments
        CommandLineArgs cla = new CommandLineArgs();
        JCommander jCommander = new JCommander(cla);
        jCommander.setProgramName("JSON Database");

        try {
            jCommander.parse(args);
            Client.start(cla);
        } catch (ParameterException pe) {
            System.out.println("Wrong parameter type or usage: " + pe.getMessage());
            pe.usage();
        }
    }
}
