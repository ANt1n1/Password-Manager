/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Aiden Kim
 * Date: 4/20/25
 * Time: 1:41 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: User
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String username;
    private String masterPassword;
    private Map<String, List<PasswordEntry>> categories;
    private List<PasswordEntry> allPasswords;

    public User(String username, String masterPassword) {
        this.username = username;
        this.masterPassword = masterPassword;
        this.categories = new HashMap<>();
        this.allPasswords = new ArrayList<>();
        categories.put("Default", new ArrayList<>());
    }

    public boolean storePasswordEntry(PasswordEntry entry) {
        if (entry == null) return false;

        allPasswords.add(entry);
        String category = entry.getCategory() != null ?
                entry.getCategory() : "Default";

        categories.computeIfAbsent(category, k -> new ArrayList<>())
                .add(entry);
        return true;
    }

    public PasswordEntry retrievePasswordEntry(String websiteOrApp) {
        for (PasswordEntry entry : allPasswords) {
            if (entry.getWebsiteOrApp().equals(websiteOrApp)) {
                return entry;
            }
        }
        return null;
    }

    public boolean organizePasswordsIntoCategory(String category, List<PasswordEntry> entries) {
        if (category == null || category.isEmpty() || entries == null) return false;

        List<PasswordEntry> categoryList = categories.computeIfAbsent(category, k -> new ArrayList<>());
        for (PasswordEntry entry : entries) {
            if (!categoryList.contains(entry)) {
                entry.setCategory(category);
                categoryList.add(entry);
            }
        }
        return true;
    }

    public List<PasswordEntry> searchThroughStoredPasswords(String keyword) {
        List<PasswordEntry> results = new ArrayList<>();
        if (keyword == null || keyword.isEmpty()) return results;

        for (PasswordEntry entry : allPasswords) {
            if (entry.getWebsiteOrApp().contains(keyword) ||
                    entry.getUsername().contains(keyword) ||
                    entry.getNotes().contains(keyword)) {
                results.add(entry);
            }
        }
        return results;
    }

    public List<PasswordEntry> getPasswordsByCategory(String category) {
        return new ArrayList<>(categories.getOrDefault(category, new ArrayList<>()));
    }

    public List<String> getAllCategories() {
        return new ArrayList<>(categories.keySet());
    }

    public List<PasswordEntry> getAllPasswords() {
        return new ArrayList<>(allPasswords);
    }

    public boolean removePasswordEntry(PasswordEntry entry) {
        if (entry == null) return false;
        boolean removed = allPasswords.remove(entry);
        if (removed) {
            categories.values().forEach(list -> list.remove(entry));
        }
        return removed;
    }

    // Authentication-agnostic methods
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the master password - added for file storage purposes.
     * In a production environment, this should have additional security measures.
     *
     * @return the master password
     */
    public String getMasterPassword() {
        return masterPassword;
    }

    public boolean verifyMasterPassword(String password) {
        return masterPassword.equals(password);
    }

    public void updateMasterPassword(String newPassword) {
        masterPassword = newPassword;
    }
}