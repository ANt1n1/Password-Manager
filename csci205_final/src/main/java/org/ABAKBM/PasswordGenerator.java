/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Aiden Kim, Bennett McKeon
 * Date: 4/20/25
 * Time: 1:48 AM
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

import java.security.SecureRandom;


/**
 * PasswordGenerator class to create secure random passwords.
 * The generated password will contain at least one uppercase letter,
 * one lowercase letter, one digit, and one special character.
 */
public class PasswordGenerator {
    private static final int PASSWORD_LENGTH = 12;
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = UPPER.toLowerCase();
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*_=+-/";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a random password with at least one uppercase letter, one lowercase letter,
     * one digit, and one special character.
     *
     * @return a randomly generated password
     */
    public static String generatePassword() {
        char[] password = new char[PASSWORD_LENGTH];
        String allChars = UPPER + LOWER + NUMBERS + SYMBOLS;

        // Ensure at least one character from each category
        password[0] = getRandomChar(UPPER);
        password[1] = getRandomChar(LOWER);
        password[2] = getRandomChar(NUMBERS);
        password[3] = getRandomChar(SYMBOLS);

        // Fill remaining positions with random characters from all categories
        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            password[i] = getRandomChar(allChars);
        }

        // Shuffle to avoid predictable positions
        for (int i = PASSWORD_LENGTH - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char temp = password[i];
            password[i] = password[j];
            password[j] = temp;
        }

        return new String(password);
    }

    /**
     * Generates a random character from the given character set.
     *
     * @param characterSet the set of characters to choose from
     * @return a random character from the character set
     */
    private static char getRandomChar(String characterSet) {
        return characterSet.charAt(RANDOM.nextInt(characterSet.length()));
    }
}