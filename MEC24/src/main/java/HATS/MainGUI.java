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
import java.util.ArrayList;
import java.awt.event.*;
import java.io.*;

public class MainGUI extends JFrame {
    private JLabel imageLabel;
    private JLabel noPasswordsLabel;
    private JButton loadImageButton;
    private JTextField passwordField;
    private JButton addToVaultButton;
    private ArrayList<PasswordContainer> vault = new ArrayList<>();
    private JTabbedPane tabbedPane;
    private JPanel passwordVaultPanel;  // Make this a class field
    private JButton addImageButton;
    private JLabel imageLabelTest;  // To display the uploaded image
    private JPanel testPanel;


    public MainGUI() {
        setTitle("Password Manager");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        // Create the "Convert Image to Password" tab
        JPanel convertImagePanel = new JPanel(new BorderLayout());
        imageLabel = new JLabel("No Image Loaded", SwingConstants.CENTER);
        loadImageButton = new JButton("Load Image");
        passwordField = new JTextField("Converted Password");
        addToVaultButton = new JButton("Add to Vault");

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(passwordField, BorderLayout.CENTER);
        southPanel.add(addToVaultButton, BorderLayout.SOUTH);
        convertImagePanel.add(loadImageButton, BorderLayout.NORTH);
        convertImagePanel.add(imageLabel, BorderLayout.CENTER);
        convertImagePanel.add(southPanel, BorderLayout.SOUTH);

        // Initialize the Password Vault panel
        passwordVaultPanel = new JPanel(new GridBagLayout());  // For initial "No Passwords" message

        // Create the scroll pane for the password vault
        JScrollPane scrollPane = new JScrollPane(passwordVaultPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Create and configure the "No Passwords" label
        JLabel noPasswordsLabel = new JLabel("No Passwords Saved", SwingConstants.CENTER);
        noPasswordsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        noPasswordsLabel.setForeground(new Color(128, 128, 128));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        passwordVaultPanel.add(noPasswordsLabel, gbc);

        //--------------------------
        passwordVaultPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 20, 20));  // Use WrapLayout instead of GridLayout
        
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


  
        // Create and initialize the "Test" tab components
        JPanel testPanel = new JPanel();
        testPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(10, 10, 10, 10); // Add padding

        // Label for displaying image (in a fixed-size box)
        imageLabelTest = new JLabel("No Image Loaded", SwingConstants.CENTER);

        // Panel to contain the image with a fixed size
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(300, 300)); // Set fixed dimensions for the image box
        imagePanel.setMaximumSize(imagePanel.getPreferredSize());
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Optional: Add a border to the box
        imagePanel.setLayout(new BorderLayout());
        imagePanel.add(imageLabelTest, BorderLayout.CENTER);

        // Button to upload an image
        addImageButton = new JButton("Upload Image");

        // Text field for password input (with placeholder)
        JTextField passwordFieldTest = new JTextField("Enter your password here");
        passwordFieldTest.setPreferredSize(new Dimension(250, 30)); // Set a fixed width
        passwordFieldTest.setMaximumSize(passwordFieldTest.getPreferredSize());

        // Set the placeholder text color to a lighter gray
        passwordFieldTest.setForeground(Color.LIGHT_GRAY);

        // FocusListener to handle clearing placeholder text when the field is clicked
        passwordFieldTest.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (passwordFieldTest.getText().equals("Enter your password here")) {
                    passwordFieldTest.setText("");
                    passwordFieldTest.setForeground(Color.BLACK); // Set text color to black when focused
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (passwordFieldTest.getText().isEmpty()) {
                    passwordFieldTest.setText("Enter your password here");
                    passwordFieldTest.setForeground(Color.LIGHT_GRAY); // Revert to light gray when not focused
                }
            }
        });

        // Add components to the "Test" tab using GridBagLayout
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.gridwidth = 1;
        gbc2.anchor = GridBagConstraints.CENTER;

        gbc2.gridx = 0;
        gbc2.gridy = 1;
        testPanel.add(Box.createVerticalStrut(20), gbc2); // Spacer

        // Button
        gbc2.gridx = 0;
        gbc2.gridy = 2;
        testPanel.add(addImageButton, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy = 3;
        testPanel.add(Box.createVerticalStrut(10), gbc2); // Spacer

        // Add the image panel below the button
        gbc2.gridx = 0;
        gbc2.gridy = 4;
        testPanel.add(imagePanel, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy = 5;
        testPanel.add(Box.createVerticalStrut(10), gbc2); // Spacer

        // Password field
        gbc2.gridx = 0;
        gbc2.gridy = 6;
        testPanel.add(passwordFieldTest, gbc2);


        // Add tabs to the tabbed pane
        tabbedPane.addTab("Convert Image to Password", convertImagePanel);
        tabbedPane.addTab("Password Vault", passwordVaultPanel);
        tabbedPane.addTab("Test", testPanel);

        add(tabbedPane);

        // Setting up listeners
        setupListeners();
    }

    // Custom WrapLayout class for better item arrangement
    private class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getWidth();
                if (targetWidth == 0) {
                    targetWidth = Integer.MAX_VALUE;
                }

                int hgap = getHgap();
                int vgap = getVgap();
                Insets insets = target.getInsets();
                int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
                int maxWidth = targetWidth - horizontalInsetsAndGap;

                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0;
                int rowHeight = 0;

                int nmembers = target.getComponentCount();
                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            dim.width = Math.max(dim.width, rowWidth);
                            dim.height += rowHeight + vgap;
                            rowWidth = d.width;
                            rowHeight = d.height;
                        } else {
                            rowWidth += d.width + hgap;
                            rowHeight = Math.max(rowHeight, d.height);
                        }
                    }
                }
                dim.width = Math.max(dim.width, rowWidth);
                dim.height += rowHeight;

                dim.width += horizontalInsetsAndGap;
                dim.height += insets.top + insets.bottom + vgap * 2;

                return dim;
            }
        }
    }

    private void refreshVaultPanel() {
        passwordVaultPanel.removeAll();  // Clear existing components

        if (vault.isEmpty()) {
            // If no passwords, show the centered message
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            JLabel noPasswordsLabel = new JLabel("No Passwords Saved", SwingConstants.CENTER);
            noPasswordsLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noPasswordsLabel.setForeground(new Color(128, 128, 128));
            passwordVaultPanel.add(noPasswordsLabel, gbc);
        } else {
            // Reduce horizontal gap from 10 to 5 pixels
            // You can also increase number of columns if desired (currently 3)
            passwordVaultPanel.setLayout(new GridLayout(0, 3, 5, 10));  // 3 columns, 5px horizontal gap, 10px vertical gap

            for (PasswordContainer container : vault) {
                JPanel entryPanel = new JPanel();
                entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
                // Reduce the border padding if needed
                entryPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));  // Reduced from 10 to 5

                // Scale the image
                BufferedImage originalImage = container.getStoredImage();
                if (originalImage != null) {
                    Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    JLabel imageLabel = new JLabel(scaledIcon);
                    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    entryPanel.add(imageLabel);
                }

                // Add display name
                JLabel displayNameLabel = new JLabel(container.getdisplayName());
                displayNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                displayNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
                entryPanel.add(displayNameLabel);

                passwordVaultPanel.add(entryPanel);
            }
        }

        passwordVaultPanel.revalidate();
        passwordVaultPanel.repaint();
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

private void openAddToVaultDialog() {
    JDialog dialog = new JDialog(this, "Add to Vault", true);
    JPanel contentPanel = new JPanel(new GridLayout(4, 2, 10, 10));
    contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

    contentPanel.add(new JLabel("Application Name:"));
    PlaceholderTextField appNameField = new PlaceholderTextField("e.g. McMaster");
    contentPanel.add(appNameField);

    contentPanel.add(new JLabel("Username:"));
    PlaceholderTextField usernameField = new PlaceholderTextField("e.g. Nejat");
    contentPanel.add(usernameField);

    contentPanel.add(new JLabel("URL:"));
    PlaceholderTextField urlField = new PlaceholderTextField("e.g. mcmaster.ca");
    contentPanel.add(urlField);

    JButton addButton = new JButton("Add");
    addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String displayName = appNameField.getText();
            String username = usernameField.getText();
            String url = urlField.getText();
            String password = passwordField.getText();

            // Get the image from the imageLabel
            Icon icon = imageLabel.getIcon();
            BufferedImage image = null;
            if (icon instanceof ImageIcon) {
                Image img = ((ImageIcon) icon).getImage();
                image = new BufferedImage(
                    img.getWidth(null),
                    img.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.drawImage(img, 0, 0, null);
                g.dispose();
            }

            // Create and add the new password container
            PasswordContainer newPassword = new PasswordContainer(displayName, password, url, "", image);
            vault.add(newPassword);

            // Close the dialog first
            dialog.dispose();
            
            // Then refresh the vault panel
            refreshVaultPanel();
        }
    });

    contentPanel.add(new JLabel());
    contentPanel.add(addButton);

    dialog.add(contentPanel);
    dialog.setSize(300, 200);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

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

        // Image button for Test tab
        addImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open file chooser to select an image
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select an Image");

                // Show open dialog and check if user selected a file
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    try {
                        // Load the image and scale it to fit the panel size
                        Image image = ImageIO.read(selectedFile);
                        Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH); // Scale the image

                        // Set the scaled image to the JLabel
                        imageLabelTest.setIcon(new ImageIcon(scaledImage));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}