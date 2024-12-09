package ServiceClients;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import Reservations.*;
import Menu.*;
import Employees.*;
import Order.*;

public class MainAppWindow {
    public MainAppWindow() {
        JFrame frame = new JFrame("Restaurant Management System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton menuButton = new JButton("Menu Management");
        JButton reservationsButton = new JButton("Reservations Management");
        JButton EmployeesButton = new JButton("Employee Management");
        JButton ordersButton = new JButton("Manage Orders");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(menuButton);
        buttonPanel.add(reservationsButton);
        buttonPanel.add(EmployeesButton);
        buttonPanel.add(ordersButton);


        frame.add(buttonPanel, BorderLayout.CENTER);

        menuButton.addActionListener((ActionEvent e) -> {
            new MenuWindow();
        });

        reservationsButton.addActionListener((ActionEvent e) -> {
            new ReservationsWindow();
        });

        EmployeesButton.addActionListener((ActionEvent e) -> {
            new EmployeeWindow();
        });
        
        ordersButton.addActionListener(e -> {
                String input = JOptionPane.showInputDialog(frame, "Enter Table Number:");
            try {
                int tableNumber = Integer.parseInt(input);
                new OrdersWindow(tableNumber); 
            } 
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid table number. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        frame.setVisible(true);
    }
}