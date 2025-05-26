/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Bennett McKeon, Andrew Bond
 * Date: 4/28/2025
 * Time: 1:53 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: EncrpytionManagerTest
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EncrpytionManagerTest {
    private EncryptionManager encryptionManager;

    @BeforeEach
    void setUp() {
        encryptionManager = new EncryptionManager();
    }

    @Test
    void testEncryptDecrypt() {
        String originalPassword = "mySecretPassword";
        String encryptedPassword = encryptionManager.encrypt(originalPassword);
        String decryptedPassword = encryptionManager.decrypt(encryptedPassword);

        assertNotEquals(originalPassword, encryptedPassword);
        assertEquals(originalPassword, decryptedPassword);
    }

    @Test
    void testEncryptDifferentValuesProduceDifferentResults() {
        String password1 = "password123";
        String password2 = "password124";

        String encrypted1 = encryptionManager.encrypt(password1);
        String encrypted2 = encryptionManager.encrypt(password2);

        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    void testEncryptSameValueTwiceProducesSameResult() {
        String password = "samePassword";

        String encrypted1 = encryptionManager.encrypt(password);
        String encrypted2 = encryptionManager.encrypt(password);

        assertEquals(encrypted1, encrypted2);
    }

    @Test
    void testBase64KeyNonEmpty() {
        String base64Key = encryptionManager.getBase64Key();
        assertNotNull(base64Key);
        assertFalse(base64Key.isEmpty());
    }

    @Test
    void testCreateEncryptionManagerWithKey() {
        String base64Key = encryptionManager.getBase64Key();
        EncryptionManager encryptionManager2 = new EncryptionManager(base64Key);

        String password = "testPassword";
        String encrypted1 = encryptionManager.encrypt(password);
        String encrypted2 = encryptionManager2.encrypt(password);

        assertEquals(encrypted1, encrypted2);
        assertEquals(password, encryptionManager2.decrypt(encrypted1));
    }
}