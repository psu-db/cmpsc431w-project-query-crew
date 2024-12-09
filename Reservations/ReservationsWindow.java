package Reservations;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import ServiceClients.*;

public class ReservationsWindow {
    private JFrame frame;
    private JTable reservationsTable;
    private DefaultTableModel tableModel;

    public ReservationsWindow() {
        frame = new JFrame("Reservations Management");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Reservation ID", "Customer Name", "Party Size", "Reservation Time", "Contact Info"};

        ArrayList<Object[]> reservations = DBConnection.getReservations();
        Object[][] data = reservations.toArray(new Object[0][]);

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(reservationsTable);

        JButton addButton = new JButton("Add Reservation");
        JButton editButton = new JButton("Edit Reservation");
        JButton deleteButton = new JButton("Delete Reservation");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(this::handleAddReservation);
        editButton.addActionListener(this::handleEditReservation);
        deleteButton.addActionListener(this::handleDeleteReservation);

        frame.setVisible(true);
    }

    private void handleAddReservation(ActionEvent e) {
        JTextField customerNameField = new JTextField();
        JTextField partySizeField = new JTextField();
        JTextField reservationTimeField = new JTextField();
        JTextField contactInfoField = new JTextField();

        Object[] fields = {
            "Customer Name:", customerNameField,
            "Party Size (number):", partySizeField,
            "Reservation Time (yyyy-MM-dd HH:mm):", reservationTimeField,
            "Contact Info:", contactInfoField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Add New Reservation", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String customerName = customerNameField.getText();
            String partySizeInput = partySizeField.getText();
            String reservationTimeInput = reservationTimeField.getText();
            String contactInfo = contactInfoField.getText();

            try {
                int partySize = Integer.parseInt(partySizeInput); 
                Timestamp reservationTime = parseTimestamp(reservationTimeInput); 
                if (DBConnection.addReservation(customerName, partySize, reservationTime, contactInfo)) {
                    refreshReservationsTable();
                    JOptionPane.showMessageDialog(frame, "Reservation added successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add reservation.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid party size. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid reservation time. Please use the format yyyy-MM-dd HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEditReservation(ActionEvent e) {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a reservation to edit.");
            return;
        }

        int reservationId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentCustomerName = (String) tableModel.getValueAt(selectedRow, 1);
        int currentPartySize = (int) tableModel.getValueAt(selectedRow, 2);
        Timestamp currentReservationTime = (Timestamp) tableModel.getValueAt(selectedRow, 3);
        String currentContactInfo = (String) tableModel.getValueAt(selectedRow, 4);

        JTextField customerNameField = new JTextField(currentCustomerName);
        JTextField partySizeField = new JTextField(String.valueOf(currentPartySize));
        JTextField reservationTimeField = new JTextField(currentReservationTime.toString());
        JTextField contactInfoField = new JTextField(currentContactInfo);

        Object[] fields = {
            "Customer Name:", customerNameField,
            "Party Size (number):", partySizeField,
            "Reservation Time (yyyy-MM-dd HH:mm):", reservationTimeField,
            "Contact Info:", contactInfoField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Edit Reservation", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String customerName = customerNameField.getText();
            String partySizeInput = partySizeField.getText();
            String reservationTimeInput = reservationTimeField.getText();
            String contactInfo = contactInfoField.getText();

            try {
                int partySize = Integer.parseInt(partySizeInput);
                Timestamp reservationTime = parseTimestamp(reservationTimeInput);

                if (DBConnection.updateReservation(reservationId, customerName, partySize, reservationTime, contactInfo)) {
                    refreshReservationsTable();
                    JOptionPane.showMessageDialog(frame, "Reservation updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update reservation.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid party size. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid reservation time. Please use the format yyyy-MM-dd HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleDeleteReservation(ActionEvent e) {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a reservation to delete.");
            return;
        }

        int reservationId = (int) tableModel.getValueAt(selectedRow, 0);

        int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this reservation?", "Delete Reservation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            if (DBConnection.deleteReservation(reservationId)) {
                refreshReservationsTable();
                JOptionPane.showMessageDialog(frame, "Reservation deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to delete reservation.");
            }
        }
    }

    private Timestamp parseTimestamp(String timestampInput) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return new Timestamp(dateFormat.parse(timestampInput).getTime());
    }

    private void refreshReservationsTable() {
        ArrayList<Object[]> reservations = DBConnection.getReservations();
        updateReservationsTable(reservations);
    }

    private void updateReservationsTable(ArrayList<Object[]> data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
}