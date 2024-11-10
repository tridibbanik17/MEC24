package HATS;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUploader extends JFrame {
    private JLabel imageLabel;
    private JButton uploadButton;
    private JTextField appNameField;
    private JTextField urlField;
    private JCheckBox addToVaultCheckbox;

    public ImageUploader() {
        setTitle("Image Uploader");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for inputs and upload button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Application Name field
        inputPanel.add(new JLabel("Application Name:"));
        appNameField = new JTextField();
        inputPanel.add(appNameField);

        // URL field
        inputPanel.add(new JLabel("URL:"));
        urlField = new JTextField();
        inputPanel.add(urlField);

        // Add to Vault checkbox
        addToVaultCheckbox = new JCheckBox("Add to vault");
        inputPanel.add(addToVaultCheckbox);

        // Upload button
        uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    displayImage(selectedFile);
                }
            }
        });
        inputPanel.add(uploadButton);

        // Image display area
        imageLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Add components to the main frame
        add(inputPanel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
    }

    private void displayImage(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            ImageIcon icon = new ImageIcon(img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH));
            imageLabel.setIcon(icon);
            imageLabel.setText(null);  // Clear the "No Image Selected" text
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ImageUploader().setVisible(true);
            }
        });
    }
}
