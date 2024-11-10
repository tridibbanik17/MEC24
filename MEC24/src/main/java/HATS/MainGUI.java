package HATS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.border.EmptyBorder;

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

        // Create a panel to hold the form components, and add padding using EmptyBorder
        JPanel contentPanel = new JPanel(new GridLayout(4, 2, 10, 10)); // 10px padding between rows and columns
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // 15px padding around the edges

        // Application Name field with placeholder
        contentPanel.add(new JLabel("Application Name:"));
        PlaceholderTextField appNameField = new PlaceholderTextField("e.g. McMaster");
        contentPanel.add(appNameField);

        // Username field with placeholder
        contentPanel.add(new JLabel("Username:"));
        PlaceholderTextField usernameField = new PlaceholderTextField("e.g. Nejat");
        contentPanel.add(usernameField);

        // URL field with placeholder
        contentPanel.add(new JLabel("URL:"));
        PlaceholderTextField urlField = new PlaceholderTextField("e.g. mcmaster.ca");
        contentPanel.add(urlField);

        // Add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Close the dialog when the "Add" button is clicked
            }
        });
        contentPanel.add(new JLabel()); // Empty label to align the button
        contentPanel.add(addButton);

        // Add the content panel to the dialog
        dialog.add(contentPanel);

        // Set dialog properties
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this); // Center on the main window
        dialog.setVisible(true);
    }


    // Custom PlaceholderTextField class (The faded part of the input field "Enter Username" or can put examples)
    class PlaceholderTextField extends JTextField implements FocusListener {
        private String placeholder;

        public PlaceholderTextField(String placeholder) {
            super();
            this.placeholder = placeholder;
            addFocusListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw placeholder text if the field is empty and unfocused
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY); // Placeholder text color
                g2.setFont(getFont().deriveFont(Font.ITALIC)); // Placeholder font style
                g2.drawString(placeholder, 5, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
                g2.dispose();
            }
        }

        @Override
        public void focusGained(FocusEvent e) {
            repaint(); // Remove placeholder text when focused
        }

        @Override
        public void focusLost(FocusEvent e) {
            repaint(); // Show placeholder text when focus is lost
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}
