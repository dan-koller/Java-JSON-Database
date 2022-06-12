package Java.JSON.Database.util;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

/*
 * This class handles the initial configuration of database
 * and its environment. It sets up file path, ip address
 * and port.
 */
public class Setup {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Properties PROPERTIES = new Properties();
    private static final String FILE_NAME = System.getProperty("user.dir") + File.separator + "app.config";

    public static String setUpServerAddress() {
        try {
            String property = readProperty("app.address");
            if (property.isBlank()) {
                throw new RuntimeException();
            } else {
                return property;
            }
        } catch (Exception e) {
            System.out.print("Please enter the IP address of your server or press enter for default (127.0.0.1): ");
            String address = scanner.nextLine();

            if (address.isBlank()) {
                address = "127.0.0.1"; // default
            }

            writeProperty("app.address", address);
            return address;
        }
    }

    public static int setUpServerPort() {
        try {
            String property = readProperty("app.port");
            if (property.isBlank()) {
                throw new RuntimeException();
            } else {
                return Integer.parseInt(property);
            }
        } catch (Exception e) {
            int portNumber;

            while (true) {
                System.out.print("Please enter Port your server should be listening or press enter for default (23456): ");
                String port = scanner.nextLine();

                if (port.isBlank()) {
                    portNumber = 23456; // default
                    break;
                } else {
                    portNumber = Integer.parseInt(port);
                }

                if (portNumber > 65535) {
                    System.err.println("Max port number reached. Please select a different port.");
                } else {
                    break;
                }
            }

            writeProperty("app.port", Integer.toString(portNumber));
            return portNumber;
        }
    }

    public static void setUpDatabase() {
        try {
            String property = readProperty("app.path");
            if (property.isBlank()) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            System.out.println("Database path is not set!");
            System.out.print("Please enter the path to the database file: ");

            String path = scanner.nextLine();
            writeProperty("app.path", path);
            // Database reads path from file because the static methods can't handle a passed value from here
        }
    }

    public static String readProperty(String property) throws IOException {
        String value;
        // Try to read configuration file
        try (FileInputStream fis = new FileInputStream(FILE_NAME)) {
            PROPERTIES.load(fis);
            value = PROPERTIES.getProperty(property);
        }

        // Check if property is set
        if (value == null || value.equals("")) {
            throw new IOException();
        } else {
            return value;
        }
    }

    private static void writeProperty(String property, String value) {
        /*
         * IOExceptions get checked here because if there is no file,
         * a new config gets written.
         */
        try (FileInputStream fis = new FileInputStream(FILE_NAME)) {
            PROPERTIES.load(fis);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + FILE_NAME + "\nCreating new file");
            try {
                PROPERTIES.setProperty(property, "");
                new FileOutputStream(FILE_NAME);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Create new config file
            if (PROPERTIES.getProperty(property) == null || PROPERTIES.getProperty(property).isEmpty()) {
                PROPERTIES.setProperty(property, value);
                try {
                    PROPERTIES.store(new FileOutputStream(FILE_NAME), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
