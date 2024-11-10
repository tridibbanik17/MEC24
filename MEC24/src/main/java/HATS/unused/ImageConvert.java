package HATS.unused;

import javax.crypto.SecretKey;
import javax.imageio.ImageIO;

import HATS.PasswordManager;
import HATS.VaultEntry;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageConvert {

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

    public static String generatePasswordFromImage(BufferedImage img) {
        try {
            BufferedImage resizedImg = resizeImage(img, 64, 64);
            String pixelString = imageToPixelString(resizedImg);
            
            // make password containing numbers, letters, special characters

            return pixelString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            PasswordManager vaultManager = new PasswordManager();
            SecretKey key = PasswordManager.generateKey();

            String imagePath = "src/main/resources/test-file.jpg"; // Path to your image file
            File imageFile = new File(imagePath);
            BufferedImage img = ImageIO.read(imageFile);

            // Example usage: Save a VaultEntry
            String application = "exampleApp";
            String displayName = "My Example App";
            BufferedImage image = img; // Assume you have an image here
            String password = "generatedPassword";
            //vaultManager.saveEntry(application, image, displayName, password, key);

            // Retrieve and display VaultEntry data
            VaultEntry retrievedEntry = vaultManager.retrieveEntry("exampleApp", key);
            if (retrievedEntry != null) {
                BufferedImage retrievedImage = vaultManager.bytesToImage(retrievedEntry.getStoredImage());
                System.out.println("Retrieved entry for: " + retrievedEntry.getApplication());
                System.out.println("Display name: " + retrievedEntry.getDisplayName());
            }
        } catch (Exception e) {
            System.out.println("Error! " + e.getMessage());
            e.printStackTrace();
        }
        
        /*System.out.println("Testing");

        String passwordFilePath = "./src/main/resources/passwords.txt";
        
        String imagePath = "./src/main/resources/test-file.jpg"; // Path to your image file
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
            String pixelString = imageToPixelString(img);
            System.out.println(pixelString);

            byte[] shaBytes = PlainTextToHash.getSHA(pixelString);
            String hashString = PlainTextToHash.toHexString(shaBytes);
            
            PlainTextToHash.saveHashToFile(hashString, passwordFilePath);
            System.out.println("Hash saved to file: " + passwordFilePath);

        } catch (IOException e) {
            System.err.println("Error reading the image file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error converting the image file: " + e.getMessage());
        }*/
    }
}
