/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Bennett McKeon
 * Date: 4/24/2025
 * Time: 10:24 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: PasswordEntryTest
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEntryTest {
    private PasswordEntry entry;

    @BeforeEach
    void setUp() {
        entry = new PasswordEntry("Google", "user@gmail.com", "encrypted123", "https://google.com", "Personal email", "Email");
    }

    @Test
    void testConstructorWithFullParams() {
        assertEquals("Google", entry.getWebsiteOrApp());
        assertEquals("user@gmail.com", entry.getUsername());
        assertEquals("encrypted123", entry.getEncryptedPassword());
        assertEquals("https://google.com", entry.getUrl());
        assertEquals("Personal email", entry.getNotes());
        assertEquals("Email", entry.getCategory());
        assertNotNull(entry.getCreationDate());
        assertNotNull(entry.getModificationDate());
    }

    @Test
    void testConstructorWithMinimalParams() {
        PasswordEntry minimalEntry = new PasswordEntry("Facebook", "fb_user", "fb_encrypted");
        assertEquals("Facebook", minimalEntry.getWebsiteOrApp());
        assertEquals("fb_user", minimalEntry.getUsername());
        assertEquals("fb_encrypted", minimalEntry.getEncryptedPassword());
        assertEquals("", minimalEntry.getUrl());
        assertEquals("", minimalEntry.getNotes());
        assertEquals("Default", minimalEntry.getCategory());
    }

    @Test
    void testUpdatePasswordChangesPasswordAndDate() {
        Date beforeUpdate = entry.getModificationDate();
        entry.updatePassword("newEncrypted456");

        assertEquals("newEncrypted456", entry.getEncryptedPassword());
        assertTrue(entry.getModificationDate().compareTo(beforeUpdate) >= 0);
    }

    @Test
    void testUpdateDetailsUpdatesFieldsAndDate() {
        Date beforeUpdate = entry.getModificationDate();
        entry.updateDetails("Gmail", "newuser", "https://mail.google.com", "Updated account");

        assertEquals("Gmail", entry.getWebsiteOrApp());
        assertEquals("newuser", entry.getUsername());
        assertEquals("https://mail.google.com", entry.getUrl());
        assertEquals("Updated account", entry.getNotes());
        assertTrue(entry.getModificationDate().compareTo(beforeUpdate) >= 0);
    }

    @Test
    void testGetPasswordAgeReturnsZeroInitially() {
        long age = entry.getPasswordAge();
        assertEquals(0, age);
    }

    @Test
    void testSetWebsiteOrApp() {
        entry.setWebsiteOrApp("YouTube");
        assertEquals("YouTube", entry.getWebsiteOrApp());
    }

    @Test
    void testSetUsername() {
        entry.setUsername("newuser123");
        assertEquals("newuser123", entry.getUsername());
    }

    @Test
    void testSetUrl() {
        entry.setUrl("https://youtube.com");
        assertEquals("https://youtube.com", entry.getUrl());
    }

    @Test
    void testSetNotes() {
        entry.setNotes("New note");
        assertEquals("New note", entry.getNotes());
    }

    @Test
    void testSetCategory() {
        entry.setCategory("Social Media");
        assertEquals("Social Media", entry.getCategory());
    }

    @Test
    void testToStringIncludesRelevantInfo() {
        String str = entry.toString();
        assertTrue(str.contains("websiteOrApp='Google'"));
        assertTrue(str.contains("username='user@gmail.com'"));
        assertTrue(str.contains("category='Email'"));
    }

    @Test
    void testUpdateDetailsWithNullsDoesNotChangeFields() {
        String originalApp = entry.getWebsiteOrApp();
        String originalUser = entry.getUsername();
        entry.updateDetails(null, null, null, null);
        assertEquals(originalApp, entry.getWebsiteOrApp());
        assertEquals(originalUser, entry.getUsername());
    }

    @Test
    void testSettersRejectEmptyValues() {
        String originalApp = entry.getWebsiteOrApp();
        String originalUser = entry.getUsername();

        entry.setWebsiteOrApp("");
        entry.setUsername("");

        assertEquals(originalApp, entry.getWebsiteOrApp());
        assertEquals(originalUser, entry.getUsername());
    }
}
