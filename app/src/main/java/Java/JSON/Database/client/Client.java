package Java.JSON.Database.client;

import Java.JSON.Database.server.cli.CommandLineArgs;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void start(CommandLineArgs cla) {
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
