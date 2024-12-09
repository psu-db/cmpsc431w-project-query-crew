
import javax.swing.*;

import Login.LoginWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //initially showing login screen
            new LoginWindow();
        });
    }
}