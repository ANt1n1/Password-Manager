/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Bennett McKeon
 * Date: 4/24/2025
 * Time: 11:23 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: AuthenticationTest
 *
 * Description:
 *
 * ****************************************
 */
package org.ABAKBM;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationTest {
    private AuthenticationManager authManager;
    private PasswordManager pm;
    private static final String USER1 = "bennett";
    private static final String USER2 = "andrew";
    private static final String PASS1 = "pass123";
    private static final String PASS2 = "secure456";
    private EncryptionManager encryptionManager;

    private static final String FIXED_TEST_KEY = "h5qH+8d9YqG3bPfL3k0+5A==";

    @BeforeEach
    void setUp() {
        // Use fixed key for consistent encryption/decryption
        encryptionManager = new EncryptionManager(FIXED_TEST_KEY);
        authManager = new AuthenticationManager(encryptionManager);

        // Clear previous data and register users
        clearPasswordFiles();
        authManager.registerUser(USER1, PASS1);
        authManager.registerUser(USER2, PASS2);
        pm = new PasswordManager(authManager);
    }

    @AfterEach
    void tearDown() {
        // Clean up authentication state
        if (authManager.isLoggedIn()) {
            authManager.logout();
        }
        clearPasswordFiles();
    }

    private void clearPasswordFiles() {
        // Delete any existing password files
        new java.io.File("password_entries_" + USER1 + ".ser").delete();
        new java.io.File("password_entries_" + USER2 + ".ser").delete();
    }

    @Test
    void testLoginSuccess() {
        assertTrue(authManager.login(USER1, PASS1));
        assertTrue(authManager.isLoggedIn());
        assertEquals(USER1, authManager.getLoggedInUser().getUsername());
    }

    @Test
    void testLoginFailure() {
        assertFalse(authManager.login(USER2, "wrongpass"));
        assertFalse(authManager.isLoggedIn());
    }

    @Test
    void testAddPasswordWithoutLogin() {
        pm.addPassword("gmail.com", USER1, "pass123");
        assertNull(pm.getPassword("gmail.com"));
    }

    @Test
    void testAccessOtherUsersPasswords() {
        // User1 adds password
        authManager.login(USER1, PASS1);
        pm.addPassword("gmail.com", USER1, "gmailPass123");
        authManager.logout();

        // User2 tries to access
        authManager.login(USER2, PASS2);
        assertNull(pm.getPassword("gmail.com"));
    }

    @Test
    void testLogoutBlocksAccess() {
        authManager.login(USER1, PASS1);
        pm.addPassword("youtube.com", USER1, "ytPass123");
        authManager.logout();
        assertNull(pm.getPassword("youtube.com"));
    }

    @Test
    void testPasswordUpdateAfterLogin() {
        authManager.login(USER1, PASS1);
        pm.addPassword("instagram.com", USER1, "insta123");
        pm.updatePassword("instagram.com", "newInsta456");
        assertEquals("newInsta456", pm.getPassword("instagram.com"));
    }

    @Test
    void testRemovePasswordAfterLogin() {
        authManager.login(USER1, PASS1);
        pm.addPassword("snapchat.com", USER1, "snap123");
        assertTrue(pm.hasPassword("snapchat.com"));

        pm.removePassword("snapchat.com");
        assertFalse(pm.hasPassword("snapchat.com"));
    }

}