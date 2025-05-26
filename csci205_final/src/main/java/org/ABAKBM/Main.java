package org.ABAKBM;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final String STORAGE_DIR = System.getProperty("user.home") +
            File.separator + ".passwordmanager";
    private static final String KEY_FILENAME = "encryption.key";

    public static void main(String[] args) {
        try {
            // Check if this is first run by seeing if the storage directory exists
            Path storagePath = Paths.get(STORAGE_DIR);
            boolean isFirstRun = !Files.exists(storagePath);

            // Create the storage directory if it doesn't exist
            if (isFirstRun) {
                Files.createDirectories(storagePath);
                System.out.println("First run - created storage directory");
            }

            // Check for existing encryption key file
            Path keyPath = storagePath.resolve(KEY_FILENAME);
            String encryptionKey;
            EncryptionManager encryptionManager;

            if (Files.exists(keyPath)) {
                // Read the existing key
                encryptionKey = Files.readString(keyPath).trim();
                System.out.println("Using existing encryption key");
                encryptionManager = new EncryptionManager(encryptionKey);
            } else {
                // First time - create a new key and save it
                System.out.println("Creating new encryption key");
                encryptionManager = new EncryptionManager();
                encryptionKey = encryptionManager.getBase64Key();
                Files.writeString(keyPath, encryptionKey);
            }

            System.out.println("Encryption Key: " + encryptionKey);

            // Create managers with the encryption manager
            AuthenticationManager authManager = new AuthenticationManager(encryptionManager);
            PasswordManager passwordManager = new PasswordManager(authManager, encryptionManager);

            // Register a new user (if not already registered)
            String username = "testuser";
            String masterPassword = "master123";

            if (isFirstRun || !authManager.isUserRegistered(username)) {
                if (authManager.registerUser(username, masterPassword)) {
                    System.out.println("User registered: " + username);
                } else {
                    System.out.println("Failed to register user: " + username);
                    return;
                }
            } else {
                System.out.println("User already exists: " + username);
            }

            // Login
            if (authManager.login(username, masterPassword)) {
                System.out.println("Logged in as: " + authManager.getLoggedInUser().getUsername());

                // Add some passwords if this is first run
                if (isFirstRun) {
                    passwordManager.addPassword("example.com", "user@example.com", "password123");
                    passwordManager.addPassword("github.com", "devuser", "gitpass456");
                    passwordManager.addPassword("gmail.com", "user@gmail.com", "gmailpass789");
                    System.out.println("Added initial passwords");
                }

                // Retrieve a password
                String password = passwordManager.getPassword("example.com");
                if (password != null) {
                    System.out.println("Retrieved password for example.com: " + password);
                } else {
                    System.out.println("No password found for example.com");
                }

                // Update a password
                if (passwordManager.hasPassword("github.com")) {
                    passwordManager.updatePassword("github.com", "newgitpass789");
                    System.out.println("Updated password for github.com");

                    // Verify the update
                    password = passwordManager.getPassword("github.com");
                    System.out.println("New password for github.com: " + password);
                }

                // Logout (this will automatically save user data)
                authManager.logout();
                System.out.println("Logged out successfully");
            } else {
                System.out.println("Login failed");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}