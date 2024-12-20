package com.zapp.mFTP;
import java.security.SecureRandom;

public class PasswordGenerator {

    // Safe characters for password generation (avoiding ambiguous characters)
    private static final String LOWERCASE = "abcdefghjkmnpqrstuvwxyz"; // Exclude 'i', 'l', 'o'
    private static final String UPPERCASE = "ABCDEFGHJKMNPQRSTVWXYZ"; // Exclude 'I', 'L', 'O'
    private static final String DIGITS = "23456789"; // Exclude '0' and '1'

    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);

        // Ensure the password includes at least one character from each category
        password.append(randomFrom(LOWERCASE));
        password.append(randomFrom(UPPERCASE));
        password.append(randomFrom(DIGITS));

        // Fill the rest of the password with random characters from all categories
        String allCharacters = LOWERCASE + UPPERCASE + DIGITS;
        for (int i = 3; i < length; i++) {
            password.append(randomFrom(allCharacters));
        }

        return shuffleString(password.toString());
    }

    private static char randomFrom(String characters) {
        return characters.charAt(random.nextInt(characters.length()));
    }

    private static String shuffleString(String input) {
        StringBuilder shuffled = new StringBuilder(input);
        for (int i = 0; i < shuffled.length(); i++) {
            int randomIndex = random.nextInt(shuffled.length());
            char temp = shuffled.charAt(i);
            shuffled.setCharAt(i, shuffled.charAt(randomIndex));
            shuffled.setCharAt(randomIndex, temp);
        }
        return shuffled.toString();
    }

}
