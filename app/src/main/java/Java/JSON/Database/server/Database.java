package Java.JSON.Database.server;

import Java.JSON.Database.server.exceptions.NoSuchKeyException;
import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {
    private static Path DB_FILE_PATH;

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock readLock = lock.readLock();
    private static final Lock writeLock = lock.writeLock();
    private static final JsonObject database = initDatabase();

    // Initialize new db file or read existing one
    private static JsonObject initDatabase() {
        // Try to read config file
        getConfig();

        // Initialize db file
        String dbContent = "";
        try {
            if (Files.exists(DB_FILE_PATH)) {
                dbContent = new String(Files.readAllBytes(DB_FILE_PATH));
            } else {
                Files.createFile(DB_FILE_PATH);
                write();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Converts db format to json
        return dbContent.isBlank() ? new JsonObject() : new Gson().fromJson(dbContent, JsonObject.class);
    }

    // Get app configuration
    private static void getConfig() {
        Properties props = new Properties();
        String fileName = System.getProperty("user.dir") + File.separator + "app.config";

        // Try to read configuration file
        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName + "\nCreating new file");
            try {
                props.setProperty("app.path", "");
                new FileOutputStream(fileName);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Create new config file
            if (props.getProperty("app.path").isEmpty()) {
                System.out.println("Database path is not set!");
                System.out.print("Please enter the path to the database file: ");

                Scanner scanner = new Scanner(System.in);
                String path = scanner.next();

                props.setProperty("app.path", path);
                try {
                    props.store(new FileOutputStream(fileName), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Set file path
                DB_FILE_PATH = Path.of(props.getProperty("app.path")).toAbsolutePath();
            }
        }
    }

    // Set command
    public static void set(JsonElement key, JsonElement value) {
        try {
            writeLock.lock();
            if (key.isJsonPrimitive()) {
                database.add(key.getAsString(), value);
            } else if (key.isJsonArray()) {
                JsonArray keys = key.getAsJsonArray();
                String addedKey = keys.remove(keys.size() - 1).getAsString();
                findKey(keys, true).getAsJsonObject().add(addedKey, value);
            } else {
                throw new NoSuchKeyException();
            }
            write();
        } finally {
            writeLock.unlock();
        }
    }

    // Get command
    public static JsonElement get(JsonElement key) {
        try {
            readLock.lock();
            if (key.isJsonPrimitive() && database.has(key.getAsString())) {
                return database.get(key.getAsString());
            } else if (key.isJsonArray()) {
                return findKey(key.getAsJsonArray(), false);
            } else {
                throw new NoSuchKeyException();
            }
        } finally {
            readLock.unlock();
        }
    }

    // Delete command
    public static void delete(JsonElement key) {
        try {
            writeLock.lock();
            if (key.isJsonPrimitive() && database.has(key.getAsString())) {
                database.remove(key.getAsString());
            } else if (key.isJsonArray()) {
                JsonArray keys = key.getAsJsonArray();
                String removedKey = keys.remove(keys.size() - 1).getAsString();
                findKey(keys, false).getAsJsonObject().remove(removedKey);
                write();
            } else {
                throw new NoSuchKeyException();
            }
        } finally {
            writeLock.unlock();
        }
    }

    // Find element in storage
    private static JsonElement findKey(JsonArray keys, boolean isCreated) {
        JsonElement searchedKey = database;
        if (isCreated) {
            for (JsonElement element : keys) {
                if (searchedKey.getAsJsonObject().has(element.getAsString())) {
                    searchedKey = searchedKey.getAsJsonObject().get(element.getAsString());
                } else {
                    searchedKey.getAsJsonObject().add(element.getAsString(), new JsonObject());
                }
            }
        } else {
            for (JsonElement element : keys) {
                if (!element.isJsonPrimitive() || !searchedKey.getAsJsonObject().has(element.getAsString())) {
                    throw new NoSuchKeyException();
                }
                searchedKey = searchedKey.getAsJsonObject().get(element.getAsString());
            }
        }
        return searchedKey;
    }

    // File writer to save updated content as json to database file
    private static void write() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        try (FileWriter writer = new FileWriter(DB_FILE_PATH.toString())) {
            writer.write(gson.toJson(database));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
