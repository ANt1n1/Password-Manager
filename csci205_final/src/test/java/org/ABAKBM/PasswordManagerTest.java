/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Bennett McKeon
 * Date: 4/24/2025
 * Time: 10:43 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: PasswordManagerTest
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

class PasswordManagerTest {
    private AuthenticationManager authManager;
    private PasswordManager passwordManager;
    private static final String TEST_USER = "testUser";
    private static final String TEST_PASS = "testPass";
    private EncryptionManager encryptionManager;
    private static final String TEST_STORAGE_DIR = System.getProperty("java.io.tmpdir") + File.separator + "test_passwordmanager";

    @BeforeEach
    void setUp() {
        // Use a test-specific storage directory
        encryptionManager = new EncryptionManager();

        // Create a clean test directory
        try {
            Path testDir = Paths.get(TEST_STORAGE_DIR);
            if (Files.exists(testDir)) {
                Files.walk(testDir)
                        .sorted((p1, p2) -> -p1.compareTo(p2))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (Exception e) {
                                // Ignore errors
                            }
                        });
            }
        } catch (Exception e) {
            System.err.println("Error cleaning test directory: " + e.getMessage());
        }

        // Create authentication manager with test storage
        authManager = new AuthenticationManager(encryptionManager);
        authManager.registerUser(TEST_USER, TEST_PASS);
        authManager.login(TEST_USER, TEST_PASS);
        passwordManager = new PasswordManager(authManager);
    }

    @AfterEach
    void tearDown() {
        // Make sure we're logged out
        if (authManager.isLoggedIn()) {
            authManager.logout();
        }

        // Clean up test directory
        try {
            Path testDir = Paths.get(TEST_STORAGE_DIR);
            if (Files.exists(testDir)) {
                Files.walk(testDir)
                        .sorted((p1, p2) -> -p1.compareTo(p2))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (Exception e) {
                                // Ignore errors
                            }
                        });
            }
        } catch (Exception e) {
            System.err.println("Error cleaning test directory: " + e.getMessage());
        }
    }

    @Test
    void testRemovePassword() {
        passwordManager.addPassword("delete.com", TEST_USER, "deleteMe");
        assertTrue(passwordManager.removePassword("delete.com"));
        assertFalse(passwordManager.hasPassword("delete.com"));
        assertNull(passwordManager.getPassword("delete.com"));
    }

    @Test
    void testHasPasswordWhenPresent() {
        passwordManager.addPassword("check.com", TEST_USER, "check123");
        assertTrue(passwordManager.hasPassword("check.com"));
    }

    @Test
    void testHasPasswordWhenAbsent() {
        assertFalse(passwordManager.hasPassword("nonexistent.com"));
    }

    @Test
    void testUpdatePasswordWhenPresent() {
        passwordManager.addPassword("update.com", TEST_USER, "oldPass");
        passwordManager.updatePassword("update.com", "newPass");
        assertEquals("newPass", passwordManager.getPassword("update.com"));
    }

    @Test
    void testUpdatePasswordWhenAbsent() {
        passwordManager.updatePassword("missing.com", "somePass");
        assertFalse(passwordManager.hasPassword("missing.com"),
                "Should not create new entry when updating nonexistent key");
    }

    @Test
    void testOverwritePassword() {
        passwordManager.addPassword("site.com", TEST_USER, "firstPass");
        passwordManager.updatePassword("site.com", "secondPass");
        assertEquals("secondPass", passwordManager.getPassword("site.com"));
    }

    @Test
    void testAccessWithoutLogin() {
        authManager.logout();
        passwordManager.addPassword("locked.com", TEST_USER, "shouldNotAdd");
        assertNull(passwordManager.getPassword("locked.com"));
        assertFalse(passwordManager.hasPassword("locked.com"));
    }

    @Test
    void testPasswordEntryDetails() {
        // Add a password with details
        passwordManager.addPassword("details.com", TEST_USER, "detailPass");

        // Update the details
        passwordManager.updatePasswordDetails(
                "details.com",
                "newdetails.com",
                "newUsername",
                "https://newdetails.com",
                "Important account"
        );

        // Password entry should now be under the new website name
        assertFalse(passwordManager.hasPassword("details.com"));
        assertTrue(passwordManager.hasPassword("newdetails.com"));

        // Get the entry and verify details
        PasswordEntry entry = passwordManager.getPasswordEntry("newdetails.com");
        assertNotNull(entry);
        assertEquals("newdetails.com", entry.getWebsiteOrApp());
        assertEquals("newUsername", entry.getUsername());
        assertEquals("https://newdetails.com", entry.getUrl());
        assertEquals("Important account", entry.getNotes());
    }


}