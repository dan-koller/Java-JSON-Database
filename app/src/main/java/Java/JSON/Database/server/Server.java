package Java.JSON.Database.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    final int threads = Runtime.getRuntime().availableProcessors();
    private final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(threads);

    private static boolean hasStopped = false;

    public void start() {
        try (
                // Establish connection
                ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))
        ) {
            System.out.println("Server started!");

            while (!hasStopped) {
                // Create new session
                Session session = new Session(server.accept());
                EXECUTOR_SERVICE.submit(session);
            }
            EXECUTOR_SERVICE.shutdown();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void stopServer() {
        hasStopped = true;
    }
}
