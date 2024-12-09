package Schedules;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import ServiceClients.*;

public class ScheduleWindow {
    private JFrame frame;
    private JTable scheduleTable;
    private DefaultTableModel tableModel;
    private int employeeId;

    public ScheduleWindow(int employeeId) {
        this.employeeId = employeeId;

        frame = new JFrame("Schedule Management");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table columns
        String[] columnNames = {"Schedule ID", "Shift Time", "Job Role"};

        // Fetch schedules from the database for this employee
        ArrayList<Object[]> schedules = DBConnection.getSchedulesForEmployee(employeeId);
        Object[][] data = schedules.toArray(new Object[0][]); // Convert ArrayList to Object[][]

        // Set up JTable
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing in the table directly
            }
        };
        scheduleTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(scheduleTable);

        // Buttons
        JButton addButton = new JButton("Add Shift");
        JButton editButton = new JButton("Edit Shift");
        JButton deleteButton = new JButton("Delete Shift");

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Add components to frame
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Button functionality
        addButton.addActionListener(this::handleAddShift);
        editButton.addActionListener(this::handleEditShift);
        deleteButton.addActionListener(this::handleDeleteShift);

        frame.setVisible(true);
    }

    // Add Shift
    private void handleAddShift(ActionEvent e) {
        JTextField shiftTimeField = new JTextField();
        JTextField jobRoleField = new JTextField();

        Object[] fields = {
            "Shift Time (yyyy-MM-dd HH:mm):", shiftTimeField,
            "Job Role:", jobRoleField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Add New Shift", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String shiftTimeInput = shiftTimeField.getText();
            String jobRole = jobRoleField.getText();

            try {
                Timestamp shiftTime = parseTimestamp(shiftTimeInput); // Validate timestamp

                if (DBConnection.addSchedule(employeeId, shiftTime, jobRole)) {
                    refreshScheduleTable();
                    JOptionPane.showMessageDialog(frame, "Shift added successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add shift.");
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid shift time. Please use the format yyyy-MM-dd HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Edit Shift
    private void handleEditShift(ActionEvent e) {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a shift to edit.");
            return;
        }

        int scheduleId = (int) tableModel.getValueAt(selectedRow, 0);
        Timestamp currentShiftTime = (Timestamp) tableModel.getValueAt(selectedRow, 1);
        String currentJobRole = (String) tableModel.getValueAt(selectedRow, 2);

        JTextField shiftTimeField = new JTextField(currentShiftTime.toString());
        JTextField jobRoleField = new JTextField(currentJobRole);

        Object[] fields = {
            "Shift Time (yyyy-MM-dd HH:mm):", shiftTimeField,
            "Job Role:", jobRoleField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Edit Shift", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String shiftTimeInput = shiftTimeField.getText();
            String jobRole = jobRoleField.getText();

            try {
                Timestamp shiftTime = parseTimestamp(shiftTimeInput); // Validate timestamp

                if (DBConnection.updateSchedule(scheduleId, shiftTime, jobRole)) {
                    refreshScheduleTable();
                    JOptionPane.showMessageDialog(frame, "Shift updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update shift.");
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid shift time. Please use the format yyyy-MM-dd HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Delete Shift
    private void handleDeleteShift(ActionEvent e) {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a shift to delete.");
            return;
        }

        int scheduleId = (int) tableModel.getValueAt(selectedRow, 0);

        int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this shift?", "Delete Shift", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            if (DBConnection.deleteSchedule(scheduleId)) {
                refreshScheduleTable();
                JOptionPane.showMessageDialog(frame, "Shift deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to delete shift.");
            }
        }
    }

    // Helper method to parse a timestamp
    private Timestamp parseTimestamp(String timestampInput) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return new Timestamp(dateFormat.parse(timestampInput).getTime());
    }

    // Refresh the JTable data
    private void refreshScheduleTable() {
        ArrayList<Object[]> schedules = DBConnection.getSchedulesForEmployee(employeeId);
        updateScheduleTable(schedules);
    }

    // Update JTable with new data
    private void updateScheduleTable(ArrayList<Object[]> data) {
        tableModel.setRowCount(0); // Clear existing data
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
}