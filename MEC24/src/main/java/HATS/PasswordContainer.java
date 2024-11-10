import java.awt.image.BufferedImage;

public class PasswordContainer {
    private String displayName;
    private String password;
    private String sites;
    private String note;
    private BufferedImage storedImage;


    public PasswordContainer(String displayName, String password, String sites, String note, BufferedImage storedImage) {
        this.displayName = displayName;
        this.password = password;
        this.sites = sites;
        this.note = note;
        this.storedImage = storedImage;
    }


    public String getdisplayName() { return displayName; }
    public void setdisplayName(String displayName) { this.displayName = displayName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSites() { return sites; }
    public void setSites(String sites) { this.sites = sites; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public BufferedImage getStoredImage() { return storedImage; }
    public void setStoredImage(BufferedImage storedImage) { this.storedImage = storedImage; }
}
