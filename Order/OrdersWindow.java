package Order;

import ServiceClients.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.text.MessageFormat;

public class OrdersWindow {
    private JFrame frame;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private int tableNumber;

    public OrdersWindow(int tableNumber) {
        this.tableNumber = tableNumber;

        frame = new JFrame("Orders Management - Table " + tableNumber);
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table columns
        String[] columnNames = {"Order Detail ID", "Item Name", "Quantity", "Price"};

        // Fetch order details from the database for this table
        ArrayList<Object[]> orderDetails = DBConnection.getOrderDetails(tableNumber);
        Object[][] data = orderDetails.toArray(new Object[0][]); // Convert ArrayList to Object[][]

        // Set up JTable
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing in the table directly
            }
        };
        orderTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(orderTable);

        // Buttons
        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton calculateButton = new JButton("Calculate Total");
        JButton printButton = new JButton("Print Receipt");

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(printButton);

        // Add components to frame
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Button functionality
        addButton.addActionListener(this::handleAddItem);
        editButton.addActionListener(this::handleEditItem);
        deleteButton.addActionListener(this::handleDeleteItem);
        calculateButton.addActionListener(this::handleCalculateTotal);
        printButton.addActionListener(this::handlePrintReceipt);

        frame.setVisible(true);
    }

    // Add Item to Order
    private void handleAddItem(ActionEvent e) {
        JTextField itemIdField = new JTextField();
        JTextField quantityField = new JTextField();

        Object[] fields = {
            "Item ID:", itemIdField,
            "Quantity:", quantityField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Add Item to Order", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int itemId = Integer.parseInt(itemIdField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                if (DBConnection.addOrderItem(tableNumber, itemId, quantity)) {
                    refreshOrderTable();
                    JOptionPane.showMessageDialog(frame, "Item added successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add item.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Edit Item in Order
    private void handleEditItem(ActionEvent e) {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an item to edit.");
            return;
        }

        int orderDetailId = (int) tableModel.getValueAt(selectedRow, 0);
        int currentQuantity = (int) tableModel.getValueAt(selectedRow, 2);

        JTextField quantityField = new JTextField(String.valueOf(currentQuantity));

        Object[] fields = {
            "New Quantity:", quantityField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Edit Item Quantity", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int newQuantity = Integer.parseInt(quantityField.getText());

                if (DBConnection.updateOrderItem(orderDetailId, newQuantity)) {
                    refreshOrderTable();
                    JOptionPane.showMessageDialog(frame, "Item updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update item.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Delete Item from Order
    private void handleDeleteItem(ActionEvent e) {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an item to delete.");
            return;
        }

        int orderDetailId = (int) tableModel.getValueAt(selectedRow, 0);

        int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this item?", "Delete Item", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            if (DBConnection.deleteOrderItem(orderDetailId)) {
                refreshOrderTable();
                JOptionPane.showMessageDialog(frame, "Item deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to delete item.");
            }
        }
    }

    // Calculate Total
    private void handleCalculateTotal(ActionEvent e) {
        double total = DBConnection.calculateTotal(tableNumber);
        JOptionPane.showMessageDialog(frame, "Total for Table " + tableNumber + ": $" + total);
    }

    // Print Receipt
    private void handlePrintReceipt(ActionEvent e) {
        try {
            boolean complete = orderTable.print(JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("Receipt - Table " + tableNumber),
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

    // Refresh the JTable data
    private void refreshOrderTable() {
        ArrayList<Object[]> orderDetails = DBConnection.getOrderDetails(tableNumber);
        updateOrderTable(orderDetails);
    }

    // Update JTable with new data
    private void updateOrderTable(ArrayList<Object[]> data) {
        tableModel.setRowCount(0); // Clear existing data
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
}