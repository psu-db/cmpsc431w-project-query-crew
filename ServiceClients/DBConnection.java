package ServiceClients;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.sql.Statement;

public class DBConnection {

    private static final String BOOTSTRAP_URL = "jdbc:postgresql://localhost:5432/restaurantDb";
    private static final String BOOTSTRAP_USER = "bootstrap_user";
    private static final String BOOTSTRAP_PASSWORD = "bootstrap_password";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(BOOTSTRAP_URL, BOOTSTRAP_USER, BOOTSTRAP_PASSWORD);
            System.out.println("Connected to database using bootstrap user.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
        return conn;
    }

    public static String[] getDynamicCredentials(String inputUsername) {
        try (Connection conn = connect()) {
            String query = "SELECT username, password FROM employees WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, inputUsername);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[]{rs.getString("username"), rs.getString("password")};
            } else {
                System.out.println("No credentials found for username: " + inputUsername);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching credentials: " + e.getMessage());
        }
        return null;
    }


    //menu
    public static ArrayList<Object[]> getMenuItems() {
        ArrayList<Object[]> menuItems = new ArrayList<>();
        String query = "SELECT itemid, name, price, allergens FROM menuitems";
    
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                menuItems.add(new Object[]{
                    rs.getInt("itemid"),        
                    rs.getString("name"),      
                    rs.getDouble("price"),    
                    rs.getString("allergens")   
                });
            }
    
        } catch (SQLException e) {
            System.out.println("Error fetching menu items: " + e.getMessage());
        }
        return menuItems;
    }
    
    public static boolean addMenuItem(String name, double price, String allergens) {
        String query = "INSERT INTO menuitems (name, price, allergens) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, allergens);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding menu item: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean updateMenuItem(int itemId, String name, double price, String allergens) {
        String query = "UPDATE menuitems SET name = ?, price = ?, allergens = ? WHERE itemid = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, allergens);
            stmt.setInt(4, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating menu item: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean deleteMenuItem(int itemId) {
        String query = "DELETE FROM menuitems WHERE itemid = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting menu item: " + e.getMessage());
            return false;
        }
    }
    
    public static ArrayList<Object[]> filterMenuItemsByAllergen(String allergen) {
        ArrayList<Object[]> menuItems = new ArrayList<>();
        String query = "SELECT * FROM menuitems WHERE allergens ILIKE ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + allergen + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                menuItems.add(new Object[]{
                    rs.getInt("itemid"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getString("allergens")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error filtering menu items: " + e.getMessage());
        }
        return menuItems;
    }
    
    public static ArrayList<Object[]> sortMenuItemsByPrice() {
        ArrayList<Object[]> menuItems = new ArrayList<>();
        String query = "SELECT * FROM menuitems ORDER BY price";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                menuItems.add(new Object[]{
                    rs.getInt("itemid"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getString("allergens")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error sorting menu items: " + e.getMessage());
        }
        return menuItems;
    }

    //Reservation 
    public static ArrayList<Object[]> getReservations() {
        ArrayList<Object[]> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";
    
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                reservations.add(new Object[]{
                    rs.getInt("reservationId"),
                    rs.getString("customerName"),
                    rs.getInt("partySize"),
                    rs.getTimestamp("reservationTime"),
                    rs.getString("contactInfo")
                });
            }
    
        } catch (SQLException e) {
            System.out.println("Error fetching reservations: " + e.getMessage());
        }
        return reservations;
    }

    public static boolean addReservation(String customerName, int partySize, Timestamp reservationTime, String contactInfo) {
        String query = "INSERT INTO reservations (customerName, partySize, reservationTime, contactInfo) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerName);
            stmt.setInt(2, partySize);
            stmt.setTimestamp(3, reservationTime);
            stmt.setString(4, contactInfo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding reservation: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateReservation(int reservationId, String customerName, int partySize, Timestamp reservationTime, String contactInfo) {
        String query = "UPDATE reservations SET customerName = ?, partySize = ?, reservationTime = ?, contactInfo = ? WHERE reservationId = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerName);
            stmt.setInt(2, partySize);
            stmt.setTimestamp(3, reservationTime);
            stmt.setString(4, contactInfo);
            stmt.setInt(5, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating reservation: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteReservation(int reservationId) {
        String query = "DELETE FROM reservations WHERE reservationId = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting reservation: " + e.getMessage());
            return false;
        }
    }

    //Employyes
    public static ArrayList<Object[]> getEmployees() {
        ArrayList<Object[]> employees = new ArrayList<>();
        String query = "SELECT employeeId, username, name, role, contactInfo FROM employees";
    
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                employees.add(new Object[]{
                    rs.getInt("employeeId"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("role"),
                    rs.getString("contactInfo")
                });
            }
    
        } catch (SQLException e) {
            System.out.println("Error fetching employees: " + e.getMessage());
        }
        return employees;
    }

    public static boolean addEmployee(String username, String password, String name, String role, String contactInfo) {
        String query = "INSERT INTO employees (username, password, name, role, contactInfo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, role);
            stmt.setString(5, contactInfo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateEmployee(int employeeId, String username, String name, String role, String contactInfo) {
        String query = "UPDATE employees SET username = ?, name = ?, role = ?, contactInfo = ? WHERE employeeId = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, name);
            stmt.setString(3, role);
            stmt.setString(4, contactInfo);
            stmt.setInt(5, employeeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteEmployee(int employeeId) {
        String query = "DELETE FROM employees WHERE employeeId = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }
    //scedhules 
    public static ArrayList<Object[]> getAllSchedules() {
        ArrayList<Object[]> allSchedules = new ArrayList<>();
        String query = """
            SELECT e.name AS employeeName, s.shiftTime, s.jobRole
            FROM schedules s
            INNER JOIN employees e ON s.employeeId = e.employeeId
            ORDER BY s.shiftTime
        """;
    
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                allSchedules.add(new Object[]{
                    rs.getString("employeeName"),
                    rs.getTimestamp("shiftTime"),
                    rs.getString("jobRole")
                });
            }
    
        } catch (SQLException e) {
            System.out.println("Error fetching all schedules: " + e.getMessage());
        }
        return allSchedules;
    }

    public static boolean deleteSchedule(int scheduleId) {
        String query = "DELETE FROM schedules WHERE scheduleId = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, scheduleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting schedule: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateSchedule(int scheduleId, Timestamp shiftTime, String jobRole) {
        String query = "UPDATE schedules SET shiftTime = ?, jobRole = ? WHERE scheduleId = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, shiftTime);
            stmt.setString(2, jobRole);
            stmt.setInt(3, scheduleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating schedule: " + e.getMessage());
            return false;
        }
    }

    public static boolean addSchedule(int employeeId, Timestamp shiftTime, String jobRole) {
        String query = "INSERT INTO schedules (employeeId, shiftTime, jobRole) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            stmt.setTimestamp(2, shiftTime);
            stmt.setString(3, jobRole);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding schedule: " + e.getMessage());
            return false;
        }
    }

    public static ArrayList<Object[]> getSchedulesForEmployee(int employeeId) {
        ArrayList<Object[]> schedules = new ArrayList<>();
        String query = "SELECT scheduleId, shiftTime, jobRole FROM schedules WHERE employeeId = ?";
    
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                schedules.add(new Object[]{
                    rs.getInt("scheduleId"),
                    rs.getTimestamp("shiftTime"),
                    rs.getString("jobRole")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error fetching schedules: " + e.getMessage());
        }
        return schedules;
    }
    
    //orders
    public static boolean addOrderItem(int tableNumber, int itemId, int quantity) {
        String orderQuery = "SELECT orderId FROM orders WHERE tableNumber = ? AND status = 'active'";
        String detailQuery = """
            INSERT INTO orderdetails (orderId, itemId, quantity, price)
            VALUES (?, ?, ?, (SELECT price FROM menuitems WHERE itemid = ?) * ?)
        """;
    
        try (Connection conn = connect();
             PreparedStatement orderStmt = conn.prepareStatement(orderQuery);
             PreparedStatement detailStmt = conn.prepareStatement(detailQuery)) {
    
            orderStmt.setInt(1, tableNumber);
            ResultSet rs = orderStmt.executeQuery();
    
            if (rs.next()) {
                int orderId = rs.getInt("orderId");
    
                detailStmt.setInt(1, orderId);
                detailStmt.setInt(2, itemId);
                detailStmt.setInt(3, quantity);
                detailStmt.setInt(4, itemId);
                detailStmt.setInt(5, quantity);
    
                return detailStmt.executeUpdate() > 0;
            } else {
                System.out.println("No active order found for table " + tableNumber);
            }
        } catch (SQLException e) {
            System.out.println("Error adding order item: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateOrderItem(int orderDetailId, int quantity) {
        String query = """
            UPDATE orderdetails
            SET quantity = ?, price = (SELECT price FROM menuitems WHERE itemid = (SELECT itemId FROM orderdetails WHERE orderDetailId = ?)) * ?
            WHERE orderDetailId = ?
        """;
    
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, quantity);
            stmt.setInt(2, orderDetailId);
            stmt.setInt(3, quantity);
            stmt.setInt(4, orderDetailId);
    
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating order item: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteOrderItem(int orderDetailId) {
        String query = "DELETE FROM orderdetails WHERE orderDetailId = ?";
    
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, orderDetailId);
    
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting order item: " + e.getMessage());
            return false;
        }
    }

    public static double calculateTotal(int tableNumber) {
        String query = """
            SELECT SUM(od.price) AS total
            FROM orderdetails od
            JOIN orders o ON od.orderId = o.orderId
            WHERE o.tableNumber = ? AND o.status = 'active'
        """;
    
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, tableNumber);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                double total = rs.getDouble("total");
                double tax = total * 0.07; // 7% tax
                double gratuity = total * 0.15; // 15% gratuity
                return total + tax + gratuity;
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total: " + e.getMessage());
        }
        return 0.0;
    }

    public static ArrayList<Object[]> getOrderDetails(int tableNumber) {
        ArrayList<Object[]> orderDetails = new ArrayList<>();
        String query = """
            SELECT od.orderDetailId, mi.name AS itemName, od.quantity, od.price
            FROM orderdetails od
            JOIN orders o ON od.orderId = o.orderId
            JOIN menuitems mi ON od.itemId = mi.itemid
            WHERE o.tableNumber = ? AND o.status = 'active'
        """;
    
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, tableNumber);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                orderDetails.add(new Object[]{
                    rs.getInt("orderDetailId"),
                    rs.getString("itemName"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error fetching order details: " + e.getMessage());
        }
        return orderDetails;
    }
}