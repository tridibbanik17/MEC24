package HATS;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


  
public class PlainTextToHash 
{  
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
  
      // Method to save hash to a text file
    public static void saveHashToFile(String hash, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(hash);
        writer.close();
    }

    /* Driver code */  
    /*public static void main(String args[]) {
        try {
            String string1 = "myPassword";

            // Corrected file path with double backslashes or forward slashes
            String filePath1 = "C:\\Users\\OWNER\\Downloads\\FilePath\\hashOutput1.txt";
            
            // Save hash of string1 to file
            saveHashToFile(toHexString(getSHA(string1)), filePath1);
            System.out.println("Hash saved to file: " + filePath1);

            // Example hash output to console for another string
            String string2 = "hashtrial";
            // Corrected file path with double backslashes or forward slashes
            String filePath2 = "C:\\Users\\OWNER\\Downloads\\FilePath\\hashOutput2.txt";
            
            // Save hash of string1 to file
            saveHashToFile(toHexString(getSHA(string2)), filePath2);
            System.out.println("Hash saved to file: " + filePath2);
            // System.out.println("\n" + string2 + " : " + toHexString(getSHA(string2)));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }*/
}
    

