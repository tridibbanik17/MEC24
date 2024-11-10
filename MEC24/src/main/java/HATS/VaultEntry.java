package HATS;

import javax.crypto.SecretKey;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class VaultEntry implements Serializable {
    private String application;
    private BufferedImage storedImage;
    private String displayName;
    private String encryptedPassword;

    public VaultEntry(String application, BufferedImage storedImage, String displayName, String encryptedPassword) {
        this.application = application;
        this.storedImage = storedImage;
        this.displayName = displayName;
        this.encryptedPassword = encryptedPassword;
    }

    // Getters
    public String getApplication() { return application; }
    public BufferedImage getStoredImage() { return storedImage; }
    public String getDisplayName() { return displayName; }
    public String getEncryptedPassword() { return encryptedPassword; }
}