package main.java.HATS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainGUI extends JFrame {

    public MainGUI() {
        // Set up the JFrame
        setTitle("Password Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create the "Convert Image to Password" tab
        JPanel convertImagePanel = new JPanel(new BorderLayout());
        JLabel imageLabel = new JLabel("No Image Loaded", SwingConstants.CENTER);
        JButton loadImageButton = new JButton("Load Image");
        JTextField passwordField = new JTextField("Converted Password");

        // Load image button action
        loadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        BufferedImage image = ImageIO.read(file);
                        imageLabel.setIcon(new ImageIcon(image));
                        passwordField.setText("GeneratedPassword123"); // Placeholder for actual password logic
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error loading image.");
                    }
                }
            }
        });

        // Add components to the "Convert Image to Password" tab
        convertImagePanel.add(loadImageButton, BorderLayout.NORTH);
        convertImagePanel.add(imageLabel, BorderLayout.CENTER);
        convertImagePanel.add(passwordField, BorderLayout.SOUTH);

        // Create the "Password Vault" tab
        JPanel passwordVaultPanel = new JPanel();
        passwordVaultPanel.add(new JLabel("This is the Password Vault."));

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Convert Image to Password", convertImagePanel);
        tabbedPane.addTab("Password Vault", passwordVaultPanel);

        // Add the tabbed pane to the frame
        add(tabbedPane);
    }
}

