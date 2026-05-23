package phase1;

import javax.swing.*;

public class MainClientGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameClientGUI gui = new GameClientGUI();
            new GameClient(gui);
            gui.setVisible(true);
        });
    }
}