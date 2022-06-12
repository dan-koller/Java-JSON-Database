package Java.JSON.Database.client;

import Java.JSON.Database.server.cli.CommandLineArgs;
import Java.JSON.Database.util.Setup;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client {

    private static String ADDRESS;
    private static int PORT;

    private static void initClient() {
        try {
            // Read config file
            ADDRESS = Setup.readProperty("app.address");
            PORT = Integer.parseInt(Setup.readProperty("app.port"));
        } catch (IOException e) {
            System.out.println("No config file found. Please run server setup first.");
        }
    }

    public static void start(CommandLineArgs cla) {
        // Initialize client
        initClient();
        // Establish connection
        try (
                Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            String jsonRequest = cla.fileName != null
                    ? new String(Files.readAllBytes(Path.of(cla.fileName)))
                    : new GsonBuilder()
//                    .setPrettyPrinting()
                    .create()
                    .toJson(cla);

            // Send message
            output.writeUTF(jsonRequest);
            System.out.println("Sent: " + jsonRequest);
            // Response from server
            System.out.println("Received: " + input.readUTF());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
