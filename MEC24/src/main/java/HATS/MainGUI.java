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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainGUI extends JFrame {
    private JLabel imageLabel;
    private JLabel noPasswordsLabel;
    private JButton loadImageButton;
    private JTextField passwordField;
    private JButton addToVaultButton;
    private ArrayList<PasswordContainer> vault = new ArrayList<>();
    private JTabbedPane tabbedPane;
    private JPanel passwordVaultPanel;

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

        setupListeners();

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



        // Create the "Test" tab
        JPanel testPanel = new JPanel();
        testPanel.add(new JLabel("This is the test tab."));

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Convert Image to Password", convertImagePanel);
        tabbedPane.addTab("Password Vault", passwordVaultPanel);
        tabbedPane.addTab("Test", testPanel);

        add(tabbedPane);
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

    private JPanel lastSelectedPanel = null; // Add this as a class field
    private final Color SELECTED_COLOR = new Color(200, 200, 200); // Light grey
    private final Color DEFAULT_COLOR = new Color(245, 245, 245); // Original color

    private void refreshVaultPanel() {
        passwordVaultPanel.removeAll();

        if (vault.isEmpty()) {
            // No passwords message handling (keep existing code)
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
            passwordVaultPanel.setLayout(new GridLayout(0, 3, 5, 10));

            for (PasswordContainer container : vault) {
                JPanel entryPanel = new JPanel();
                entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
                entryPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                entryPanel.setBackground(DEFAULT_COLOR);

                // Store the container reference in the panel's client properties
                entryPanel.putClientProperty("container", container);

                // Add mouse listeners for selection and double-click
                entryPanel.addMouseListener(new MouseAdapter() {
                    private long lastClickTime = 0;
                    private static final long DOUBLE_CLICK_TIME = 500; // milliseconds

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastClickTime < DOUBLE_CLICK_TIME) {
                            // Double click - show details popup
                            showDetailsDialog(container);
                        } else {
                            // Single click - handle selection
                            if (lastSelectedPanel != null) {
                                lastSelectedPanel.setBackground(DEFAULT_COLOR);
                            }
                            entryPanel.setBackground(SELECTED_COLOR);
                            lastSelectedPanel = entryPanel;
                        }
                        lastClickTime = currentTime;
                    }
                });

                // Add image and label (keep existing code)
                BufferedImage originalImage = container.getStoredImage();
                if (originalImage != null) {
                    Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    JLabel imageLabel = new JLabel(scaledIcon);
                    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    entryPanel.add(imageLabel);
                }

                JLabel displayNameLabel = new JLabel(container.getDisplayName());
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
            PasswordContainer newPassword = new PasswordContainer(displayName, username, password, url, "", image);
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

    private void showDetailsDialog(PasswordContainer container) {
        JDialog dialog = new JDialog(this, "Password Details", true);
        dialog.setLayout(new BorderLayout());
        
        // Main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username field
        gbc.gridy = 0;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(container.getUsername(), 20);
        contentPanel.add(usernameField, gbc);

        // Password field (non-editable)
        gbc.gridy = 1;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JTextField passwordField = new JTextField(container.getPassword(), 20);
        passwordField.setEditable(false);
        passwordField.setBackground(Color.WHITE);
        contentPanel.add(passwordField, gbc);

        // Sites field
        gbc.gridy = 2;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Sites:"), gbc);
        gbc.gridx = 1;
        JTextField sitesField = new JTextField(container.getSites(), 20);
        contentPanel.add(sitesField, gbc);

        // Notes field
        gbc.gridy = 3;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1;
        JTextArea notesArea = new JTextArea(container.getNote(), 4, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        contentPanel.add(notesScroll, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            container.setUsername(usernameField.getText());
            container.setSites(sitesField.getText());
            container.setNote(notesArea.getText());
            dialog.dispose();
            refreshVaultPanel();
        });

        // Delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(255, 99, 71)); // Tomato red
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                dialog,
                "Are you sure you want to delete this password?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                vault.remove(container);
                dialog.dispose();
                refreshVaultPanel();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        // Add panels to dialog
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog properties
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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
}