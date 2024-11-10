package HATS;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HATS.MainGUI gui = new HATS.MainGUI();
            gui.setVisible(true);
        });
    }
}


