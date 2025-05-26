/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Bennett McKeon
 * Date: 4/24/2025
 * Time: 10:38 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: PasswordGenerator
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = UPPER.toLowerCase();
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*_=+-/";

    @Test
    void testPasswordLength() {
        String password = PasswordGenerator.generatePassword();
        assertEquals(12, password.length(), "Password should be 12 characters long");
    }

    @Test
    void testPasswordContainsUppercase() {
        String password = PasswordGenerator.generatePassword();
        assertTrue(password.chars().anyMatch(c -> UPPER.indexOf(c) >= 0), "Password should contain at least one uppercase letter");
    }

    @Test
    void testPasswordContainsLowercase() {
        String password = PasswordGenerator.generatePassword();
        assertTrue(password.chars().anyMatch(c -> LOWER.indexOf(c) >= 0), "Password should contain at least one lowercase letter");
    }

    @Test
    void testPasswordContainsNumber() {
        String password = PasswordGenerator.generatePassword();
        assertTrue(password.chars().anyMatch(c -> NUMBERS.indexOf(c) >= 0), "Password should contain at least one digit");
    }

    @Test
    void testPasswordContainsSymbol() {
        String password = PasswordGenerator.generatePassword();
        assertTrue(password.chars().anyMatch(c -> SYMBOLS.indexOf(c) >= 0), "Password should contain at least one symbol");
    }

    @Test
    void testPasswordRandomness() {
        String password1 = PasswordGenerator.generatePassword();
        String password2 = PasswordGenerator.generatePassword();
        assertNotEquals(password1, password2, "Generated passwords should not be the same");
    }

    @Test
    void testPasswordHasOnlyValidCharacters() {
        String password = PasswordGenerator.generatePassword();
        String allValidChars = UPPER + LOWER + NUMBERS + SYMBOLS;

        for (char c : password.toCharArray()) {
            assertTrue(allValidChars.indexOf(c) >= 0, "Password contains invalid character: " + c);
        }
    }
}