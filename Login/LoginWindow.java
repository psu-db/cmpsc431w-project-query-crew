package Login;

import ServiceClients.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import ServiceClients.MainAppWindow;


public class LoginWindow {
    private JFrame frame;

    public LoginWindow() {
        // Set up the login frame
        frame = new JFrame("Login");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // Add fields for username and password
        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordField);

        // Add login button
        JButton loginButton = new JButton("Login");
        panel.add(loginButton);

        frame.add(panel);

        // Add action listener for login
        loginButton.addActionListener((ActionEvent e) -> {
            String inputUsername = usernameField.getText();
            String inputPassword = new String(passwordField.getPassword());

            // Authenticate user
            if (authenticate(inputUsername, inputPassword)) {
                JOptionPane.showMessageDialog(frame, "Login Successful");
                frame.dispose(); // Close the login window
                new MainAppWindow(); // Launch the main application

            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.");
            }
        });

        frame.setVisible(true);
    }

    private boolean authenticate(String username, String password) {
        // Use DBConnection to validate username and password
        String[] credentials = DBConnection.getDynamicCredentials(username);
        if (credentials != null && password.equals(credentials[1])) {
            return true;
        }
        return false;
    }
}