/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 * Name: Aiden Kim, Bennett McKeon (Updated)
 * Date: 4/29/2025
 * Time: 11:00 PM
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: PasswordManager
 *
 * Description: This class manages passwords with encryption and file storage support.
 *
 * ****************************************
 */

package org.ABAKBM;

import java.util.HashMap;
import java.util.Map;

/**
 * PasswordManager class to manage password entries for a user.
 * It allows adding, retrieving, updating, and removing password entries with
 * encryption and file storage support.
 */
public class PasswordManager {
    private final AuthenticationManager authManager;
    private final EncryptionManager encryptionManager;
    private final Map<String, PasswordEntry> temporaryCache;

    /**
     * Constructor for PasswordManager.
     * @param authManager the AuthenticationManager instance to check user login status
     * @param encryptionManager the EncryptionManager for encrypting/decrypting passwords
     */
    public PasswordManager(AuthenticationManager authManager, EncryptionManager encryptionManager) {
        this.authManager = authManager;
        this.encryptionManager = encryptionManager;
        this.temporaryCache = new HashMap<>();
    }

    /**
     * Constructor for PasswordManager that creates a new EncryptionManager automatically.
     * @param authManager the AuthenticationManager instance to check user login status
     */
    public PasswordManager(AuthenticationManager authManager) {
        this.authManager = authManager;
        // Get the EncryptionManager from the AuthenticationManager
        // This assumes the AuthenticationManager has a method to access its EncryptionManager
        this.encryptionManager = authManager.getEncryptionManager();
        this.temporaryCache = new HashMap<>();
    }

    /**
     * Encrypts a password using the encryption manager.
     *
     * @param plainPassword The plain text password to encrypt
     * @return The encrypted password
     */
    public String encryptPassword(String plainPassword) {
        return encryptionManager.encrypt(plainPassword);
    }

    /**
     * Adds a password entry for the logged-in user.
     * @param website the website or application name
     * @param username the username associated with the password
     * @param password the plaintext password to encrypt and store
     */
    public void addPassword(String website, String username, String password) {
        if (!authManager.isLoggedIn()) {
            return;
        }

        // Encrypt the password
        String encryptedPassword = encryptionManager.encrypt(password);

        User currentUser = authManager.getLoggedInUser();
        PasswordEntry entry = new PasswordEntry(website, username, encryptedPassword);
        currentUser.storePasswordEntry(entry);
        temporaryCache.put(website, entry);

        // Save changes to storage
        authManager.saveUserPasswordEntries();
    }

    /**
     * Retrieves and decrypts the password for a specific website or application.
     * @param website the website or application name
     * @return the decrypted password if found, null otherwise
     */
    public String getPassword(String website) {
        if (!authManager.isLoggedIn()) {
            return null;
        }

        User currentUser = authManager.getLoggedInUser();
        PasswordEntry entry = currentUser.retrievePasswordEntry(website);

        if (entry != null) {
            // Decrypt the password before returning
            return encryptionManager.decrypt(entry.getEncryptedPassword());
        }
        return null;
    }

    /**
     * Retrieves the PasswordEntry object for a specific website.
     * @param website the website or application name
     * @return the PasswordEntry if found, null otherwise
     */
    public PasswordEntry getPasswordEntry(String website) {
        if (!authManager.isLoggedIn()) {
            return null;
        }

        User currentUser = authManager.getLoggedInUser();
        return currentUser.retrievePasswordEntry(website);
    }

    /**
     * Removes a password entry for the logged-in user.
     * @param website the website or application name
     * @return true if the entry was removed successfully, false otherwise
     */
    public boolean removePassword(String website) {
        if (!authManager.isLoggedIn()) {
            return false;
        }

        User currentUser = authManager.getLoggedInUser();
        PasswordEntry entry = currentUser.retrievePasswordEntry(website);

        if (entry == null) {
            return false;
        }

        boolean removed = currentUser.removePasswordEntry(entry);
        if (removed) {
            // Save changes to storage
            authManager.saveUserPasswordEntries();
            temporaryCache.remove(website);
        }
        return removed;
    }

    /**
     * Checks if a password entry exists for the specified website or application.
     * @param website the website or application name
     * @return true if the entry exists, false otherwise
     */
    public boolean hasPassword(String website) {
        if (!authManager.isLoggedIn()) {
            return false;
        }

        User currentUser = authManager.getLoggedInUser();
        PasswordEntry entry = currentUser.retrievePasswordEntry(website);
        return entry != null;
    }

    /**
     * Updates the password entry for a specific website or application.
     * @param website the website or application name
     * @param newPassword the new plaintext password to encrypt and store
     */
    public void updatePassword(String website, String newPassword) {
        if (!authManager.isLoggedIn()) {
            return;
        }

        // Encrypt the new password
        String encryptedPassword = encryptionManager.encrypt(newPassword);

        User currentUser = authManager.getLoggedInUser();
        PasswordEntry entry = currentUser.retrievePasswordEntry(website);

        if (entry != null) {
            entry.updatePassword(encryptedPassword);
            // Save changes to storage
            authManager.saveUserPasswordEntries();
        }
    }

    /**
     * Updates details for a password entry.
     * @param website the website or application name to identify the entry
     * @param newWebsite the new website name (or null to leave unchanged)
     * @param newUsername the new username (or null to leave unchanged)
     * @param newUrl the new URL (or null to leave unchanged)
     * @param newNotes the new notes (or null to leave unchanged)
     */
    public void updatePasswordDetails(String website, String newWebsite, String newUsername,
                                      String newUrl, String newNotes) {
        if (!authManager.isLoggedIn()) {
            return;
        }

        User currentUser = authManager.getLoggedInUser();
        PasswordEntry entry = currentUser.retrievePasswordEntry(website);

        if (entry != null) {
            entry.updateDetails(newWebsite, newUsername, newUrl, newNotes);

            // If the website name changed, update the cache key
            if (newWebsite != null && !newWebsite.isEmpty() && !newWebsite.equals(website)) {
                temporaryCache.remove(website);
                temporaryCache.put(newWebsite, entry);
            }

            // Save changes to storage
            authManager.saveUserPasswordEntries();
        }
    }

    /**
     * Clears the temporary cache of password entries.
     */
    public void clearCache() {
        temporaryCache.clear();
    }

    /**
     * Saves all password data to storage.
     * This should be called when making significant changes or when closing the application.
     */
    public void saveAllPasswords() {
        if (authManager.isLoggedIn()) {
            authManager.saveUserPasswordEntries();
        }
    }
}