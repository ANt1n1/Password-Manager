/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2025
 *
 * Name: Aiden Kim
 * Date: 4/22/25
 * Time: 1:35 AM
 *
 * Project: csci205_final
 * Package: org.ABAKBM
 * Class: PasswordHealthAnalyzer
 *
 * Description:
 *
 * ****************************************
 */

package org.ABAKBM;

import java.util.*;
import java.util.regex.Pattern;

public class PasswordHealthAnalyzer {
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_AGE_DAYS = 90;

    private static final Pattern HAS_UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern HAS_LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern HAS_DIGIT = Pattern.compile("\\d");
    private static final Pattern HAS_SPECIAL = Pattern.compile("[^A-Za-z0-9]");

    public List<PasswordEntry> identifyWeakPasswords(User user) {
        List<PasswordEntry> weakPasswords = new ArrayList<>();
        List<PasswordEntry> allPasswords = user.getAllPasswords();

        for (PasswordEntry entry : allPasswords) {
            String password = entry.getEncryptedPassword();

            if (!isStrongPassword(password)) {
                weakPasswords.add(entry);
            }
        }
        return weakPasswords;
    }

    private boolean isStrongPassword(String password) {
        //At least 8 chars, contains uppercase and lowercase, at least
        //one digit, at least one special character
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }

        boolean hasUppercase = HAS_UPPERCASE.matcher(password).find();
        boolean hasLowercase = HAS_LOWERCASE.matcher(password).find();
        boolean hasDigit = HAS_DIGIT.matcher(password).find();
        boolean hasSpecial = HAS_SPECIAL.matcher(password).find();

        //Strong password should have all these
        return hasUppercase && hasLowercase && hasDigit && hasSpecial;
    }

    public Map<String, List<PasswordEntry>> detectDuplicatePasswords(User user) {
        Map<String, List<PasswordEntry>> passwordMap = new HashMap<>();
        List<PasswordEntry> allPasswords = user.getAllPasswords();

        for (PasswordEntry entry : allPasswords) {
            String password = entry.getEncryptedPassword();

            if (!passwordMap.containsKey(password)) {
                passwordMap.put(password, new ArrayList<>());
            }

            passwordMap.get(password).add(entry);
        }

        //filter out unique passwords
        Map<String, List<PasswordEntry>> duplicatePasswords = new HashMap<>();
        for (Map.Entry<String, List<PasswordEntry>> mapEntry : passwordMap.entrySet()) {
            if (mapEntry.getValue().size() > 1) {
                duplicatePasswords.put(mapEntry.getKey(), mapEntry.getValue());
            }
        }

        return duplicatePasswords;
    }

    public List<PasswordEntry> trackPasswordAge(User user, int maxAgeDays) {
        List<PasswordEntry> oldPasswords = new ArrayList<>();
        List<PasswordEntry> allPasswords = user.getAllPasswords();

        for (PasswordEntry entry : allPasswords) {
            if (entry.getPasswordAge() > maxAgeDays) {
                oldPasswords.add(entry);
            }
        }

        return oldPasswords;
    }

    public List<PasswordEntry> trackPasswordAge(User user) {
        return trackPasswordAge(user, MAX_PASSWORD_AGE_DAYS);
    }
}