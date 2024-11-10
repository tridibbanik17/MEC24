package HATS;

import java.awt.image.BufferedImage;

public class PasswordContainer {
    private String displayName;
    private String username;
    private String password;
    private String sites;
    private String note;
    private BufferedImage storedImage;


    public PasswordContainer(String displayName, String username, String password, String sites, String note, BufferedImage storedImage) {
        this.displayName = displayName;
        this.username = username;
        this.password = password;
        this.sites = sites;
        this.note = note;
        this.storedImage = storedImage;
    }


    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSites() { return sites; }
    public void setSites(String sites) { this.sites = sites; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public BufferedImage getStoredImage() { return storedImage; }
    public void setStoredImage(BufferedImage storedImage) { this.storedImage = storedImage; }
}
