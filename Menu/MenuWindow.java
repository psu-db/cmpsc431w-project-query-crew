package Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import ServiceClients.*;
import javax.swing.table.DefaultTableModel;


public class MenuWindow {
    private JFrame frame;
    private JTable menuTable;
    private DefaultTableModel tableModel;

    public MenuWindow() {
        frame = new JFrame("Menu Management");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Item ID", "Name", "Price", "Allergens"};

        ArrayList<Object[]> menuItems = DBConnection.getMenuItems();
        Object[][] data = menuItems.toArray(new Object[0][]); 

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        menuTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(menuTable);

        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton filterAllergensButton = new JButton("Filter by Allergens");
        JButton sortByPriceButton = new JButton("Sort by Price");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(filterAllergensButton);
        buttonPanel.add(sortByPriceButton);

        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(this::handleAddItem);
        editButton.addActionListener(this::handleEditItem);
        deleteButton.addActionListener(this::handleDeleteItem);
        filterAllergensButton.addActionListener(this::handleFilterByAllergens);
        sortByPriceButton.addActionListener(this::handleSortByPrice);

        frame.setVisible(true);
    }

    private void handleAddItem(ActionEvent e) {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField allergensField = new JTextField();

        Object[] fields = {
            "Name:", nameField,
            "Price:", priceField,
            "Allergens:", allergensField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Add New Item", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String priceInput = priceField.getText();
            String allergens = allergensField.getText();

            try {
                double price = Double.parseDouble(priceInput);
                if (DBConnection.addMenuItem(name, price, allergens)) {
                    refreshMenuTable();
                    JOptionPane.showMessageDialog(frame, "Item added successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add item.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid price. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEditItem(ActionEvent e) {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an item to edit.");
            return;
        }

        int itemId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        double currentPrice = (double) tableModel.getValueAt(selectedRow, 2);
        String currentAllergens = (String) tableModel.getValueAt(selectedRow, 3);

        JTextField nameField = new JTextField(currentName);
        JTextField priceField = new JTextField(String.valueOf(currentPrice));
        JTextField allergensField = new JTextField(currentAllergens);

        Object[] fields = {
            "Name:", nameField,
            "Price:", priceField,
            "Allergens:", allergensField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Edit Item", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String priceInput = priceField.getText();
            String allergens = allergensField.getText();

            try {
                double price = Double.parseDouble(priceInput); 
                if (DBConnection.updateMenuItem(itemId, name, price, allergens)) {
                    refreshMenuTable();
                    JOptionPane.showMessageDialog(frame, "Item updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update item.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid price. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleDeleteItem(ActionEvent e) {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an item to delete.");
            return;
        }

        int itemId = (int) tableModel.getValueAt(selectedRow, 0);

        int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this item?", "Delete Item", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            if (DBConnection.deleteMenuItem(itemId)) {
                refreshMenuTable();
                JOptionPane.showMessageDialog(frame, "Item deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to delete item.");
            }
        }
    }

    private void handleFilterByAllergens(ActionEvent e) {
        String allergen = JOptionPane.showInputDialog(frame, "Enter allergen to filter by:");
        if (allergen != null && !allergen.isEmpty()) {
            ArrayList<Object[]> filteredItems = DBConnection.filterMenuItemsByAllergen(allergen);
            updateMenuTable(filteredItems);
        }
    }
    
    private void handleSortByPrice(ActionEvent e) {
        ArrayList<Object[]> sortedItems = DBConnection.sortMenuItemsByPrice();
        updateMenuTable(sortedItems);
    }
    

    private void refreshMenuTable() {
        ArrayList<Object[]> menuItems = DBConnection.getMenuItems();
        updateMenuTable(menuItems);
    }

    private void updateMenuTable(ArrayList<Object[]> data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
}