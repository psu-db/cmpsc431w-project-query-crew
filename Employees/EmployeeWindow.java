package Employees;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;

import ServiceClients.*;
import Schedules.*;

public class EmployeeWindow {
    private JFrame frame;
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeeWindow() {
        frame = new JFrame("Employee Management");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Employee ID", "Username", "Name", "Role", "Contact Info"};

        ArrayList<Object[]> employees = DBConnection.getEmployees();
        Object[][] data = employees.toArray(new Object[0][]); 

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        employeeTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);

        JButton addButton = new JButton("Add Employee");
        JButton editButton = new JButton("Edit Employee");
        JButton deleteButton = new JButton("Delete Employee");
        JButton manageSchedulesButton = new JButton("Manage Schedules");
        JButton printAllSchedulesButton = new JButton("Print All Schedules");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(manageSchedulesButton);
        buttonPanel.add(printAllSchedulesButton);

        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(this::handleAddEmployee);
        editButton.addActionListener(this::handleEditEmployee);
        deleteButton.addActionListener(this::handleDeleteEmployee);
        manageSchedulesButton.addActionListener(this::handleManageSchedules);
        printAllSchedulesButton.addActionListener(this::handlePrintAllSchedules);

        frame.setVisible(true);
    }

    private void handleAddEmployee(ActionEvent e) {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField roleField = new JTextField();
        JTextField contactInfoField = new JTextField();

        Object[] fields = {
            "Username:", usernameField,
            "Password:", passwordField,
            "Name:", nameField,
            "Role:", roleField,
            "Contact Info:", contactInfoField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Add New Employee", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String role = roleField.getText();
            String contactInfo = contactInfoField.getText();

            if (DBConnection.addEmployee(username, password, name, role, contactInfo)) {
                refreshEmployeeTable();
                JOptionPane.showMessageDialog(frame, "Employee added successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add employee.");
            }
        }
    }

    private void handleEditEmployee(ActionEvent e) {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an employee to edit.");
            return;
        }

        int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentUsername = (String) tableModel.getValueAt(selectedRow, 1);
        String currentName = (String) tableModel.getValueAt(selectedRow, 2);
        String currentRole = (String) tableModel.getValueAt(selectedRow, 3);
        String currentContactInfo = (String) tableModel.getValueAt(selectedRow, 4);

        JTextField usernameField = new JTextField(currentUsername);
        JTextField nameField = new JTextField(currentName);
        JTextField roleField = new JTextField(currentRole);
        JTextField contactInfoField = new JTextField(currentContactInfo);

        Object[] fields = {
            "Username:", usernameField,
            "Name:", nameField,
            "Role:", roleField,
            "Contact Info:", contactInfoField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Edit Employee", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String name = nameField.getText();
            String role = roleField.getText();
            String contactInfo = contactInfoField.getText();

            if (DBConnection.updateEmployee(employeeId, username, name, role, contactInfo)) {
                refreshEmployeeTable();
                JOptionPane.showMessageDialog(frame, "Employee updated successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update employee.");
            }
        }
    }

    private void handleDeleteEmployee(ActionEvent e) {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an employee to delete.");
            return;
        }

        int employeeId = (int) tableModel.getValueAt(selectedRow, 0);

        int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this employee?", "Delete Employee", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            if (DBConnection.deleteEmployee(employeeId)) {
                refreshEmployeeTable();
                JOptionPane.showMessageDialog(frame, "Employee deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to delete employee.");
            }
        }
    }

    private void handleManageSchedules(ActionEvent e) {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an employee to manage schedules.");
            return;
        }

        int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
        new ScheduleWindow(employeeId); // Opens a new window for managing schedules
    }

    private void refreshEmployeeTable() {
        ArrayList<Object[]> employees = DBConnection.getEmployees();
        updateEmployeeTable(employees);
    }

    private void updateEmployeeTable(ArrayList<Object[]> data) {
        tableModel.setRowCount(0); 
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    private void handlePrintAllSchedules(ActionEvent e) {
        ArrayList<Object[]> allSchedules = DBConnection.getAllSchedules();
        if (allSchedules.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No schedules found to print.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        allSchedules.sort(Comparator.comparing(o -> (Timestamp) ((Object[]) o)[1]));
        allSchedules.sort(Comparator.comparing(o -> (Timestamp) o[1]));
        String[] columnNames = {"Employee Name", "Shift Time", "Job Role"};
        DefaultTableModel printTableModel = new DefaultTableModel(columnNames, 0);
        for (Object[] schedule : allSchedules) {
            printTableModel.addRow(new Object[]{schedule[0], schedule[1], schedule[2]});
        }

        JTable printTable = new JTable(printTableModel);

        try {
            boolean complete = printTable.print(JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("All Employee Schedules"),
                    new MessageFormat("Page {0}"));
            if (complete) {
                JOptionPane.showMessageDialog(frame, "Printing Complete.");
            } else {
                JOptionPane.showMessageDialog(frame, "Printing Cancelled.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Printing Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}