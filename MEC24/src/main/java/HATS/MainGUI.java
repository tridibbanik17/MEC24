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

public class MainGUI extends JFrame {
    private JLabel imageLabel;
    private JButton loadImageButton;
    private JTextField passwordField;
    private JButton addToVaultButton;
    private ArrayList<PasswordContainer> vault = new ArrayList<>();
    private JTabbedPane tabbedPane;
    private JPanel passwordVaultPanel;  // Make this a class field

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
        passwordVaultPanel = new JPanel();
        passwordVaultPanel.add(new JLabel("No Passwords Saved"));
        passwordVaultPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 20, 20));  // Use WrapLayout instead of GridLayout
        JScrollPane scrollPane = new JScrollPane(passwordVaultPanel);
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

    private void refreshVaultPanel() {
        passwordVaultPanel.removeAll();  // Clear existing components

        for (PasswordContainer container : vault) {
            JPanel entryPanel = new JPanel();
            entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
            entryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            entryPanel.setBackground(new Color(245, 245, 245));  // Light gray background

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


        // Add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String displayName = appNameField.getText();
            String username = usernameField.getText();
            String url = urlField.getText();
            String password = passwordField.getText();
            
            // Get the image from the imageLabel
            Icon icon = imageLabel.getIcon();
            BufferedImage image = null;
            if (icon instanceof ImageIcon) {
                Image img = ((ImageIcon) icon).getImage();
                image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.drawImage(img, 0, 0, null);
                g.dispose();
            }

            PasswordContainer newPassword = new PasswordContainer(displayName, password, url, "", image);
            vault.add(newPassword);
            refreshVaultPanel();  // Refresh the vault display
            dialog.dispose();
        });

        contentPanel.add(new JLabel());
        contentPanel.add(addButton);


        // Add the content panel to the dialog
        dialog.add(contentPanel);

        // Set dialog properties
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this); // Center on the main window
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
    }
}