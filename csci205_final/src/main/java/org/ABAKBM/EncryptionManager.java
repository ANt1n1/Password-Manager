/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Andrew Bond
 * Date: 4/28/2025
 * Time: 1:23 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: EncryptionManager
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * A class to manage encryption and decryption of passwords using AES algorithm.
 */
public class EncryptionManager {
    private SecretKey encryptKey;
    private static final String ALGORITHM = "AES"; //Advanced Encryption Standard

    /**
     * Default constructor to create an EncryptionManager with a new key.
     */
    public EncryptionManager() {
        this.encryptKey = generateKey();
    }

    /**
     * Constructor to create an EncryptionManager with a specific key.
     *
     * @param base64Key Base64 encoded key
     */
    public EncryptionManager(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key); // Base64 into byte array
        this.encryptKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM); //Takes a byte array and creates a key
    }

    /**
     * Generates a new AES encryption key.
     *
     * @return the generated SecretKey
     */
    private SecretKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(128); // Sets the parameters for the key generator
            return keyGen.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Error generating encryption key", e);
        }
    }

    /**
     * Encrypts the given password using the AES algorithm.
     *
     * @param password the password to encrypt
     * @return the encrypted password as a Base64 encoded string
     */
    public String encrypt(String password) {
        if (password == null) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, encryptKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }

    /**
     * Decrypts the given encrypted password using the AES algorithm.
     *
     * @param encryptedPassword the encrypted password to decrypt
     * @return the decrypted password
     */
    public String decrypt(String encryptedPassword) {
        if (encryptedPassword == null) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, encryptKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password", e);
        }
    }

    /**
     * Returns the Base64 encoded key for storage or transmission.
     *
     * @return Base64 encoded key
     */
    public String getBase64Key() {
        return Base64.getEncoder().encodeToString(encryptKey.getEncoded());
    }
}