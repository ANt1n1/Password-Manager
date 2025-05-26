/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Aiden Kim
 * Date: 4/29/25
 * Time: 10:14 PM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: FileStorageManager
 *
 * Description:
 *
 * ****************************************
 */


/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Aiden Kim (Updated)
 * Date: 4/30/25
 * Time: 1:25 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: FileStorageManager
 *
 * Description: Manages saving and loading encrypted data to/from files
 *
 * ****************************************
 */

package org.ABAKBM;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Base64;

/**
 * FileStorageManager handles saving encrypted password data to files and loading it back.
 * It works with the EncryptionManager to ensure data is stored securely.
 */
public class FileStorageManager {
    private static final String DEFAULT_STORAGE_DIR = System.getProperty("user.home") + File.separator + ".passwordmanager";
    private static final String USER_DATA_FILENAME = "userdata.dat";
    private static final String PASSWORD_DATA_FILENAME = "passwords.dat";
    private static final String ENCRYPTION_KEY_FILENAME = "encryption.key";

    private final Path storageDir;
    private final EncryptionManager encryptionManager;

    /**
     * Constructor that uses the default storage directory
     *
     * @param encryptionManager The encryption manager to use for encrypting/decrypting data
     */
    public FileStorageManager(EncryptionManager encryptionManager) {
        this(encryptionManager, DEFAULT_STORAGE_DIR);
    }

    /**
     * Constructor that allows specifying a custom storage directory
     *
     * @param encryptionManager The encryption manager to use for encrypting/decrypting data
     * @param storageDirPath The directory path where files will be stored
     */
    public FileStorageManager(EncryptionManager encryptionManager, String storageDirPath) {
        this.encryptionManager = encryptionManager;
        this.storageDir = Paths.get(storageDirPath);
        try {
            initializeStorageDirectory();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory", e);
        }
    }

    /**
     * Creates the storage directory if it doesn't exist
     */
    private void initializeStorageDirectory() throws IOException {
        if (!Files.exists(storageDir)) {
            Files.createDirectories(storageDir);
        }
    }

    /**
     * Saves user data (usernames and master passwords) to file
     *
     * @param users Map of username to User objects
     * @return true if save was successful
     */
    public boolean saveUserData(Map<String, User> users) {
        Path userDataFile = storageDir.resolve(USER_DATA_FILENAME);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userDataFile.toFile()))) {
            // For each user, write username and encrypted master password
            for (Map.Entry<String, User> entry : users.entrySet()) {
                String username = entry.getKey();
                User user = entry.getValue();

                // Store the plaintext username and the master password plaintext
                // In a real system with higher security requirements, you would want to
                // encrypt the username as well and use a more secure password storage method
                writer.write(username);
                writer.newLine();
                writer.write(user.getMasterPassword());
                writer.newLine();
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error saving user data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads user data from file
     *
     * @return Map of username to User objects, or empty map if file doesn't exist
     */
    public Map<String, User> loadUserData() {
        Path userDataFile = storageDir.resolve(USER_DATA_FILENAME);
        Map<String, User> users = new HashMap<>();

        if (!Files.exists(userDataFile)) {
            return users; // Return empty map if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userDataFile.toFile()))) {
            String username;
            String masterPassword;

            // Read username and password pairs
            while ((username = reader.readLine()) != null) {
                masterPassword = reader.readLine();
                if (masterPassword != null) {
                    // Create user with the actual password
                    users.put(username, new User(username, masterPassword));
                }
            }
            return users;
        } catch (IOException e) {
            System.err.println("Error loading user data: " + e.getMessage());
            return users;
        }
    }

    /**
     * Saves password entries for a specific user
     *
     * @param username The username of the user
     * @param entries The list of password entries to save
     * @return true if save was successful
     */
    public boolean savePasswordEntries(String username, List<PasswordEntry> entries) {
        Path userDir = storageDir.resolve(username);
        try {
            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }

            Path passwordFile = userDir.resolve(PASSWORD_DATA_FILENAME);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(passwordFile.toFile()))) {
                for (PasswordEntry entry : entries) {
                    // Format: website,username,encryptedPassword,url,notes,category,creationDate,modificationDate
                    StringBuilder line = new StringBuilder();
                    line.append(entry.getWebsiteOrApp()).append(",");
                    line.append(entry.getUsername()).append(",");
                    line.append(entry.getEncryptedPassword()).append(","); // Already encrypted
                    line.append(entry.getUrl()).append(",");
                    line.append(entry.getNotes()).append(",");
                    line.append(entry.getCategory()).append(",");
                    line.append(entry.getCreationDate().getTime()).append(",");
                    line.append(entry.getModificationDate().getTime());

                    writer.write(line.toString());
                    writer.newLine();
                }
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error saving password entries: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads password entries for a specific user
     *
     * @param username The username of the user
     * @return List of password entries, or empty list if file doesn't exist
     */
    public List<PasswordEntry> loadPasswordEntries(String username) {
        Path userDir = storageDir.resolve(username);
        Path passwordFile = userDir.resolve(PASSWORD_DATA_FILENAME);
        List<PasswordEntry> entries = new ArrayList<>();

        if (!Files.exists(passwordFile)) {
            return entries; // Return empty list if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(passwordFile.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) { // Ensure all fields are present
                    String website = parts[0];
                    String user = parts[1];
                    String encryptedPassword = parts[2]; // Keep encrypted
                    String url = parts[3];
                    String notes = parts[4];
                    String category = parts[5];

                    // Create PasswordEntry with the basic constructor
                    PasswordEntry entry = new PasswordEntry(website, user, encryptedPassword,
                            url, notes, category);

                    // Set dates if they exist in the file - not implemented yet
                    // In a real implementation, you'd need to add setters for these fields

                    entries.add(entry);
                }
            }
            return entries;
        } catch (IOException e) {
            System.err.println("Error loading password entries: " + e.getMessage());
            return entries;
        }
    }

    /**
     * Deletes all stored data for a user
     *
     * @param username The username of the user whose data should be deleted
     * @return true if deletion was successful
     */
    public boolean deleteUserData(String username) {
        Path userDir = storageDir.resolve(username);
        if (!Files.exists(userDir)) {
            return true; // Nothing to delete
        }

        try {
            Files.walk(userDir)
                    .sorted((p1, p2) -> -p1.compareTo(p2)) // Sort in reverse order to delete files first
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Failed to delete: " + path);
                        }
                    });
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting user data: " + e.getMessage());
            return false;
        }
    }
}