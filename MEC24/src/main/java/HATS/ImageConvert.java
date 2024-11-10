package HATS;

import javax.imageio.ImageIO;

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
        BufferedImage resizedImage = resizeImage(img, 64, 64);

        StringBuilder pixelData = new StringBuilder();
        int width = resizedImage.getWidth();
        int height = resizedImage.getHeight();

        System.out.println("width: " + width);
        System.out.println("height: " + width);

        // Loop through each pixel to build a string based on RGB values
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = resizedImage.getRGB(x, y);
                pixelData.append(rgb);
            }
        }
        return pixelData.toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        System.out.println("Testing ImageHasher class");

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
        } catch (IOException e) {
            System.err.println("Error reading the image file: " + e.getMessage());
        }
    }
}
