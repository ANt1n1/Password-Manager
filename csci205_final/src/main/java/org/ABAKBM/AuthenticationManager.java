/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Bennett McKeon
 * Date: 4/24/2025
 * Time: 11:01 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: AuthenticationManager
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to manage user authentication: login, logout, and session control.
 * Now supports loading and saving user data to files.
 */
public class AuthenticationManager {

    private Map<String, User> userDatabase;
    private User loggedInUser = null;
    private final FileStorageManager fileManager;
    private final EncryptionManager encryptionManager;

    /**
     * Constructor that initializes a new authentication manager and loads user data
     * from storage if available.
     *
     * @param encryptionManager The encryption manager to use
     */
    public AuthenticationManager(EncryptionManager encryptionManager) {
        this.encryptionManager = encryptionManager;
        this.fileManager = new FileStorageManager(encryptionManager);

        // Initialize an empty database first
        this.userDatabase = new HashMap<>();

        try {
            // Attempt to load existing users
            Map<String, User> loadedUsers = fileManager.loadUserData();
            if (loadedUsers != null && !loadedUsers.isEmpty()) {
                this.userDatabase = loadedUsers;
            }
        } catch (Exception e) {
            // If loading fails, we already have an empty database
            System.err.println("Failed to load user data: " + e.getMessage());
        }
    }

    /**
     * Check if a user is registered in the system.
     *
     * @param username the username to check
     * @return true if the user exists
     */
    public boolean isUserRegistered(String username) {
        return userDatabase.containsKey(username);
    }

    /**
     * Register a new user into the system.
     * @param username username for the new user
     * @param masterPassword password for the new user
     * @return true if registered successfully, false if username already exists
     */
    public boolean registerUser(String username, String masterPassword) {
        if (userDatabase.containsKey(username)) {
            return false; // user already exists
        }

        User newUser = new User(username, masterPassword);
        userDatabase.put(username, newUser);

        // Save updated user database to storage
        try {
            boolean saved = fileManager.saveUserData(userDatabase);
            return saved; // Return true only if saving was successful
        } catch (Exception e) {
            System.err.println("Failed to save user data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Attempt to login a user with their credentials.
     * @param username input username
     * @param password input password
     * @return true if login is successful
     */
    public boolean login(String username, String password) {
        User user = userDatabase.get(username);
        if (user != null && user.verifyMasterPassword(password)) {
            loggedInUser = user;

            // Load the user's password entries from storage
            if (loggedInUser != null) {
                try {
                    loadUserPasswordEntries();
                } catch (Exception e) {
                    System.err.println("Failed to load password entries: " + e.getMessage());
                    // Continue with login even if loading entries fails
                }
            }

            return true;
        }
        return false;
    }

    /**
     * Load password entries for the logged-in user from storage
     */
    private void loadUserPasswordEntries() {
        if (loggedInUser != null) {
            try {
                // Load password entries from file
                var passwordEntries = fileManager.loadPasswordEntries(loggedInUser.getUsername());

                // Add each entry to the user's collection
                for (PasswordEntry entry : passwordEntries) {
                    loggedInUser.storePasswordEntry(entry);
                }
            } catch (Exception e) {
                System.err.println("Error loading password entries: " + e.getMessage());
            }
        }
    }

    /**
     * Save the logged-in user's password entries to storage
     */
    public void saveUserPasswordEntries() {
        if (loggedInUser != null) {
            try {
                fileManager.savePasswordEntries(
                        loggedInUser.getUsername(),
                        loggedInUser.getAllPasswords()
                );
            } catch (Exception e) {
                System.err.println("Error saving password entries: " + e.getMessage());
            }
        }
    }

    /**
     * Log out the current user and save their data.
     */
    public void logout() {
        if (loggedInUser != null) {
            // Save the user's password entries before logging out
            saveUserPasswordEntries();
            loggedInUser = null;
        }
    }

    /**
     * Check if a user is currently logged in.
     * @return true if a user is logged in
     */
    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    /**
     * Get the currently logged-in user.
     * @return User object or null if not logged in
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Get the encryption manager used by this authentication manager.
     * @return the encryption manager
     */
    public EncryptionManager getEncryptionManager() {
        return encryptionManager;
    }

    /**
     * Save all user data to storage.
     * This should be called when the application is closing.
     */
    public void saveAllData() {
        try {
            // Save user database first
            fileManager.saveUserData(userDatabase);

            // If a user is logged in, save their password entries
            if (loggedInUser != null) {
                saveUserPasswordEntries();
            }
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
}