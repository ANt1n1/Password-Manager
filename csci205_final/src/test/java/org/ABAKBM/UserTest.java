/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Bennett McKeon
 * Date: 4/24/2025
 * Time: 10:49 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: UserTest
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;
    private PasswordEntry entry1;
    private PasswordEntry entry2;
    private static final String USERNAME = "testUser";
    private static final String MASTER_PASS = "SecurePass123!";

    @BeforeEach
    void setUp() {
        user = new User(USERNAME, MASTER_PASS);

        entry1 = new PasswordEntry("github.com", "user123", "ghPass123",
                "https://github.com", "Work account", "Work");
        entry2 = new PasswordEntry("gmail.com", "user@gmail.com", "emailPass456",
                "https://mail.google.com", "", "Personal");
    }

    @Test
    void testStorePasswordEntry() {
        assertTrue(user.storePasswordEntry(entry1));
        assertTrue(user.getAllPasswords().contains(entry1));
    }

    @Test
    void testStoreNullPasswordEntry() {
        assertFalse(user.storePasswordEntry(null));
    }

    @Test
    void testRetrievePasswordEntryExists() {
        user.storePasswordEntry(entry1);
        PasswordEntry result = user.retrievePasswordEntry("github.com");
        assertEquals(entry1, result);
    }

    @Test
    void testRetrieveNonExistentPasswordEntry() {
        assertNull(user.retrievePasswordEntry("nonexistent.com"));
    }

    @Test
    void testRemovePasswordEntrySuccess() {
        user.storePasswordEntry(entry1);
        assertTrue(user.removePasswordEntry(entry1));
        assertFalse(user.getAllPasswords().contains(entry1));
    }

    @Test
    void testRemoveNonExistentPasswordEntry() {
        assertFalse(user.removePasswordEntry(entry1));
    }

    @Test
    void testRemoveNullPasswordEntry() {
        assertFalse(user.removePasswordEntry(null));
    }

    @Test
    void testGetAllPasswordsImmutable() {
        user.storePasswordEntry(entry1);
        List<PasswordEntry> passwords = user.getAllPasswords();
        passwords.clear(); // Should not affect internal list
        assertTrue(user.getAllPasswords().contains(entry1));
    }

    @Test
    void testCategoryManagement() {
        user.storePasswordEntry(entry1);
        user.storePasswordEntry(entry2);

        // Verify category creation
        assertTrue(user.getAllCategories().contains("Work"));
        assertTrue(user.getAllCategories().contains("Personal"));

        // Verify entries in categories
        assertTrue(user.getPasswordsByCategory("Work").contains(entry1));
        assertTrue(user.getPasswordsByCategory("Personal").contains(entry2));
    }

    @Test
    void testGetPasswordsByNonExistentCategory() {
        List<PasswordEntry> result = user.getPasswordsByCategory("Unknown");
        assertTrue(result.isEmpty());
    }

    @Test
    void testVerifyMasterPasswordCorrect() {
        assertTrue(user.verifyMasterPassword(MASTER_PASS));
    }

    @Test
    void testVerifyMasterPasswordIncorrect() {
        assertFalse(user.verifyMasterPassword("WrongPassword123"));
    }

    @Test
    void testUpdateMasterPassword() {
        String newPass = "NewSecurePass456!";
        user.updateMasterPassword(newPass);
        assertTrue(user.verifyMasterPassword(newPass));
    }

    @Test
    void testGetUsername() {
        assertEquals(USERNAME, user.getUsername());
    }

    @Test
    void testSetUsername() {
        String newName = "newUsername";
        user.setUsername(newName);
        assertEquals(newName, user.getUsername());
    }

    @Test
    void testDefaultCategoryExists() {
        PasswordEntry defaultEntry = new PasswordEntry("example.com", "user", "pass");
        user.storePasswordEntry(defaultEntry);
        assertTrue(user.getPasswordsByCategory("Default").contains(defaultEntry));
    }

    @Test
    void testOrganizePasswordsIntoCategory() {
        user.storePasswordEntry(entry1);
        List<PasswordEntry> entries = List.of(entry1);
        assertTrue(user.organizePasswordsIntoCategory("Finance", entries));
        assertTrue(user.getPasswordsByCategory("Finance").contains(entry1));
    }

    @Test
    void testSearchThroughStoredPasswords() {
        user.storePasswordEntry(entry1);
        user.storePasswordEntry(entry2);
        List<PasswordEntry> results = user.searchThroughStoredPasswords("Work");
        assertTrue(results.contains(entry1));
        assertFalse(results.contains(entry2));
    }
}