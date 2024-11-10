package HATS;

import javax.crypto.SecretKey;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class VaultEntry implements Serializable {
    private String application;
    private byte[] storedImage;
    private String displayName;
    private String encryptedPassword;
    private String note;

    public VaultEntry(String application, byte[] storedImage, String displayName, String encryptedPassword, String note) {
        this.application = application;
        this.storedImage = storedImage;
        this.displayName = displayName;
        this.encryptedPassword = encryptedPassword;
        this.note  = note;
    }

    // Getters
    public String getApplication() { return application; }
    public byte[] getStoredImage() { return storedImage; }
    public String getDisplayName() { return displayName; }
    public String getEncryptedPassword() { return encryptedPassword; }
    public  String getNote() { return note; }
}