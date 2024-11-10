package HATS;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PasswordManager {
    private static final String VAULT_PATH = "/src/main/resources/vault.txt";
    private static final String AES_ALGORITHM = "AES";

    // Encrypts and saves the VaultEntry in the vault file
    public void saveEntry(VaultEntry entry, SecretKey key) throws Exception {
        String encryptedEntry = encryptObject(entry, key);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VAULT_PATH, true))) {
            writer.write(encryptedEntry);
            writer.newLine();
        }
    }

    // Encrypts the VaultEntry object with AES and serializes it to a string
    private String encryptObject(VaultEntry entry, SecretKey key) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objStream = new ObjectOutputStream(byteStream)) {
            objStream.writeObject(entry);
        }
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(byteStream.toByteArray());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypts a line from the vault file and returns the VaultEntry object
    public VaultEntry retrieveEntry(String application, SecretKey key) throws Exception {
        List<VaultEntry> entries = loadEntries(key);
        for (VaultEntry entry : entries) {
            if (entry.getApplication().equals(application)) {
                return entry;
            }
        }
        return null; // Return null if the application is not found
    }

    // Loads all entries from the vault
    private List<VaultEntry> loadEntries(SecretKey key) throws Exception {
        List<VaultEntry> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(VAULT_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                VaultEntry entry = decryptObject(line, key);
                entries.add(entry);
            }
        }
        return entries;
    }
    
    private VaultEntry decryptObject(String encryptedData, SecretKey key) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        try (ObjectInputStream objStream = new ObjectInputStream(new ByteArrayInputStream(decryptedBytes))) {
            return (VaultEntry) objStream.readObject();
        }
    }

    // Generates a new AES key
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGen.init(128); // AES-128
        return keyGen.generateKey();
    }
}
