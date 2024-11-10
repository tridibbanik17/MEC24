package HATS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainGUI extends JFrame {

    private JLabel imageLabel;
    private JButton loadImageButton;
    private JTextField passwordField;
    private JButton addToVaultButton;

    public MainGUI() {
        // Set up the JFrame
        setTitle("Password Manager");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // This maximizes the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create the "Convert Image to Password" tab
        JPanel convertImagePanel = new JPanel(new BorderLayout());
        imageLabel = new JLabel("No Image Loaded", SwingConstants.CENTER);
        loadImageButton = new JButton("Load Image");
        passwordField = new JTextField("Converted Password");
        addToVaultButton = new JButton("Add to Vault");

        // Set up the listener for the load image button
        setupListeners();

        // Add components to the "Convert Image to Password" tab
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(passwordField, BorderLayout.CENTER);
        southPanel.add(addToVaultButton, BorderLayout.SOUTH);
        convertImagePanel.add(loadImageButton, BorderLayout.NORTH);
        convertImagePanel.add(imageLabel, BorderLayout.CENTER);
        convertImagePanel.add(southPanel, BorderLayout.SOUTH);

        // Create the "Password Vault" tab
        JPanel passwordVaultPanel = new JPanel();
        passwordVaultPanel.add(new JLabel("This is the Password Vault."));

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Convert Image to Password", convertImagePanel);
        tabbedPane.addTab("Password Vault", passwordVaultPanel);

        // Add the tabbed pane to the frame
        add(tabbedPane);
    }

    private void setupListeners() {
        // Load image button listener
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

        // "Add to Vault" button listener
        addToVaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddToVaultDialog();
            }
        });
    }

    private void openAddToVaultDialog() {
        // Create the dialog window
        JDialog dialog = new JDialog(this, "Add to Vault", true);
        dialog.setLayout(new GridLayout(5, 2));

        // Application Name
        dialog.add(new JLabel("Application Name:"));
        JTextField appNameField = new JTextField();
        dialog.add(appNameField);

        // Username
        dialog.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        dialog.add(usernameField);

        // URL
        dialog.add(new JLabel("URL:"));
        JTextField urlField = new JTextField();
        dialog.add(urlField);

        // Note
        dialog.add(new JLabel("Note:"));
        JTextArea noteArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(noteArea);
        dialog.add(scrollPane);

        // Add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Close the dialog when the "Add" button is clicked
            }
        });
        dialog.add(addButton);

        // Set dialog properties
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this); // Center on the main window
        dialog.setVisible(true);
    }
}
