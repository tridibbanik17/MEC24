package HATS;


import java.security.MessageDigest;
import java.util.Base64;

public class PasswordGenerator {

    public String generatePassword(String rgbString) throws Exception {
        // Hash the RGB string using SHA-256 to get a deterministic value
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(rgbString.getBytes());

        // Convert the hash to a string with letters, numbers, and special characters
        StringBuilder password = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";

        for (byte b : hashBytes) {
            int index = (b & 0xFF) % chars.length();
            password.append(chars.charAt(index));
        }

        return password.toString();
    }
}
