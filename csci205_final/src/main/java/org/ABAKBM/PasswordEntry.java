/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Aiden Kim
 * Date: 4/20/25
 * Time: 1:44 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: PasswordEntry
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM;

import java.util.Date;

public class PasswordEntry {
    private String websiteOrApp;
    private String username;
    private String encryptedPassword;
    private String url;
    private String notes;
    private Date creationDate;
    private Date modificationDate;
    private String category;

    /**
     * Constructor for PasswordEntry.
     *
     * @param websiteOrApp      the name of the website or application
     * @param username          the username associated with the password
     * @param encryptedPassword the encrypted password
     * @param url               the URL of the website or application
     * @param notes             additional notes about the entry
     * @param category          category of the password entry
     */
    public PasswordEntry(String websiteOrApp, String username, String encryptedPassword,
                         String url, String notes, String category) {
        this.websiteOrApp = websiteOrApp;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.url = url;
        this.notes = notes;
        this.category = category;
        this.creationDate = new Date();
        this.modificationDate = new Date();
    }

    /**
     * Constructor for PasswordEntry with default values for URL and notes.
     *
     * @param websiteOrApp      the name of the website or application
     * @param username          the username associated with the password
     * @param encryptedPassword the encrypted password
     */
    public PasswordEntry(String websiteOrApp, String username, String encryptedPassword) {
        this(websiteOrApp, username, encryptedPassword, "", "", "Default");
    }


    public void updatePassword(String newEncryptedPassword) {
        this.encryptedPassword = newEncryptedPassword;
        this.modificationDate = new Date();
    }

    /**
     * Updates the details of the password entry.
     *
     * @param websiteOrApp the new name of the website or application
     * @param username     the new username associated with the password
     * @param url          the new URL of the website or application
     * @param notes        additional notes about the entry
     */
    public void updateDetails(String websiteOrApp, String username, String url, String notes) {
        if (websiteOrApp != null && !websiteOrApp.isEmpty()) {
            this.websiteOrApp = websiteOrApp;
        }
        if (username != null && !username.isEmpty()) {
            this.username = username;
        }
        if (url != null) {
            this.url = url;
        }
        if (notes != null) {
            this.notes = notes;
        }
        this.modificationDate = new Date();
    }

    /**
     * Gets the age of the password in days.
     *
     * @return the age of the password in days
     */
    public long getPasswordAge() {
        Date now = new Date();
        long diff = now.getTime() - modificationDate.getTime();
        return diff / (1000 * 60 * 60 * 24); // Convert milliseconds to days
    }


    public String getWebsiteOrApp() {
        return websiteOrApp;
    }

    /**
     * Sets the website or application name and updates the modification date.
     *
     * @param websiteOrApp the new name of the website or application
     */
    public void setWebsiteOrApp(String websiteOrApp) {
        if (websiteOrApp != null && !websiteOrApp.isEmpty()) {
            this.websiteOrApp = websiteOrApp;
            this.modificationDate = new Date();
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null && !username.isEmpty()) {
            this.username = username;
            this.modificationDate = new Date();
        }
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.modificationDate = new Date();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
        this.modificationDate = new Date();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "PasswordEntry{" +
                "websiteOrApp='" + websiteOrApp + '\'' +
                ", username='" + username + '\'' +
                ", url='" + url + '\'' +
                ", category='" + category + '\'' +
                ", created=" + creationDate +
                ", modified=" + modificationDate +
                '}';
    }


}