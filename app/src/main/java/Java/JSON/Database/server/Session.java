package Java.JSON.Database.server;

import Java.JSON.Database.server.cli.commands.Command;
import Java.JSON.Database.server.cli.commands.DeleteCommand;
import Java.JSON.Database.server.cli.commands.GetCommand;
import Java.JSON.Database.server.cli.commands.SetCommand;
import Java.JSON.Database.server.exceptions.NoSuchKeyException;
import Java.JSON.Database.server.requests.Request;
import Java.JSON.Database.server.requests.RequestBuilder;
import Java.JSON.Database.server.requests.Response;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable {

    private final Socket socket;
    private Command command; // Do not convert to local variable atm
    private final Request request;
    private final Response response;

    private final DataInputStream input;
    private final DataOutputStream output;

    // Constructor
    public Session(Socket socket) throws IOException {
        this.socket = socket;

        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        this.request = RequestBuilder.buildFromCommandLine(input.readUTF());
        this.response = new Response();

        if (request.getType().equalsIgnoreCase("exit")) Server.stopServer();
    }

    // Process commands
    @Override
    public void run() {
        try (// Send and receive data
             socket;
             input;
             output
        ) {
            try {
                // Process user input
                switch (request.getType()) {
                    case "exit":
                        response.setResponse(Response.STATUS_OK);
                        System.exit(0);
                        break;
                    case "get":
                        command = new GetCommand(request.getKey());
                        command.execute();
                        response.setValue(command.getResult());
                        break;
                    case "set":
                        command = new SetCommand(request.getKey(), request.getValue());
                        command.execute();
                        break;
                    case "delete":
                        command = new DeleteCommand(request.getKey());
                        command.execute();
                        break;
                        // TODO default can throw new exception
                }
                response.setResponse(Response.STATUS_OK);
            } catch (NoSuchKeyException nsk) {
                response.setResponse(Response.STATUS_ERROR);
                response.setReason(nsk.getMessage());
            } catch (Exception e) {
                response.setResponse(e.getMessage());
                e.printStackTrace();
            } finally {
                output.writeUTF(new GsonBuilder()
                        .setPrettyPrinting() // May be optional depending on how you want to receive your json
                        .create()
                        .toJson(response));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
