package HATS;

import java.util.Base64;

import java.io.*;

import org.apache.commons.io.FileUtils;

public class ImageConvert {

    public String fileToBase64String(String filePath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(classLoader.getResource(filePath).getFile());

        byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);



        return encodedString;
    }


    public static void main(String[] args) {
        System.out.println("Testing ImageConvert class");

        ImageConvert imageConvert = new ImageConvert();

        try {
            // Call the method and convert the test image file to Base64
            String base64String = imageConvert.fileToBase64String("test-file.jpg");

            /* Check that the Base64 string is not null or empty
            assertNotNull(base64String, "Base64 string should not be null");
            assertTrue(base64String.length() > 0, "Base64 string should not be empty");
            */

            // Optionally, print the result to check if it looks like a Base64 string
            System.out.println("Base64 String: " + base64String);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
