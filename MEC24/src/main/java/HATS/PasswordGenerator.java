package HATS;

import javax.crypto.SecretKey;
import javax.imageio.ImageIO;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PasswordGenerator {

    private static final String CURRENT_PASSWORD_PATH = "src/main/resources/current_password.txt";

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resizedImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    public static String imageToPixelString(BufferedImage img) {
        StringBuilder pixelData = new StringBuilder();
        int width = img.getWidth();
        int height = img.getHeight();

        // Loop through each pixel to build a string based on RGB values
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                pixelData.append(rgb);
            }
        }
        return pixelData.toString().replaceAll("-", "");
    }

    public static String hashPassword(String plainText) {
        try {
            // Use SHA-512 to hash the plain text
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(plainText.getBytes());

            // Convert the hash bytes to a hexadecimal string
            BigInteger number = new BigInteger(1, hashBytes);
            StringBuilder hexString = new StringBuilder(number.toString(16));

            // Pad with leading zeros to make the length 128 characters
            while (hexString.length() < 128) {
                hexString.insert(0, '0');
            }

            String cleanedHash = hexString.toString().replaceFirst("^0+", "");
            return cleanedHash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generatePasswordFromImage(BufferedImage img) {
        try {
            BufferedImage resizedImg = resizeImage(img, 64, 64);
            String pixelString = imageToPixelString(resizedImg);
            String hashPassword = hashPassword(pixelString);
            hashPassword += "!";

            return hashPassword;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException  
    {  
        /* MessageDigest instance for hashing using SHA512*/  
        MessageDigest md = MessageDigest.getInstance("SHA-512");  
  
        /* digest() method called to calculate message digest of an input and return array of byte */  
        return md.digest(input.getBytes(StandardCharsets.UTF_8));  
    }  
      
    public static String toHexString(byte[] hash)  
    {  
        /* Convert byte array of hash into digest */  
        BigInteger number = new BigInteger(1, hash);  
  
        /* Convert the digest into hex value */  
        StringBuilder hexString = new StringBuilder(number.toString(16));  
  
        /* Pad with leading zeros */  
        while (hexString.length() < 32)  
        {  
            hexString.insert(0, '0');  
        }  
  
        return hexString.toString();
    }

    public static String encryptPassword512(String password) {
        try {
            byte[] shaBytes = getSHA(password);
            String encryptedPassword = toHexString(shaBytes);
            return encryptedPassword;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void storeCurrentPassword(String password) {
        try {
            byte[] shaBytes = getSHA(password);
            String encryptedPassword = toHexString(shaBytes);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(CURRENT_PASSWORD_PATH, true))) {
                writer.write(encryptedPassword);
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    public static void main(String[] args) {
        System.out.println("Testing");
        
        String imagePath = "src/main/resources/test-file.jpg"; // Path to your image file
        File imageFile = new File(imagePath);

        System.out.println("Attempting to read from: " + imageFile.getAbsolutePath());

        if (!imageFile.exists()) {
            System.err.println("File not found: " + imageFile.getAbsolutePath());
            return;
        }

        try {
            // Load the image
            BufferedImage img = ImageIO.read(imageFile);

            // Convert image to a pixel data string
            String password = generatePasswordFromImage(img);
            System.out.println(password);

            storeCurrentPassword(password);

        } catch (IOException e) {
            System.err.println("Error reading the image file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error converting the image file: " + e.getMessage());
        }
    }
}
