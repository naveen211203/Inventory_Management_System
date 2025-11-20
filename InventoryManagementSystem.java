package Pro;

import java.sql.*;
import java.util.Scanner;

public class InventoryManagementSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/inventorydb";
    private static final String USER = "root";       // your MySQL username
    private static final String PASSWORD = "naveen"; // your MySQL password
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("✅ Connected to Database Successfully!");
            boolean running = true;

            while(running){
                String role = login(conn);
                if(role != null){
                    running = showMenu(conn, role);
                } else {
                    System.out.println("❌ Login failed! Try again.");
                }
            }
            System.out.println("👋 Program terminated.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // ----------------- Login returns role -----------------
    private static String login(Connection conn) throws SQLException {
        System.out.print("Enter username: ");
        String username = sc.next();
        System.out.print("Enter password: ");
        String password = sc.next();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String role = rs.getString("role");
            System.out.println("✅ Login successful! Role: " + role);
            return role;
        } else {
            System.out.println("❌ Invalid credentials!");
            return null;
        }
    }

    // ----------------- Menu based on role -----------------
    private static boolean showMenu(Connection conn, String role) throws SQLException {
        while (true) {
            System.out.println("\n--- INVENTORY MENU ---");
            if (role.equalsIgnoreCase("Admin")) {
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("3. Remove Products");
                System.out.println("4. Update Product Quantity");
                System.out.println("5. Make a Sale (Billing)");
                System.out.println("6. View Sales Report");
                System.out.println("7. Add Staff");
                System.out.println("8. View Staff");
                System.out.println("9. Remove Staff");
                System.out.println("10. Log Out");
                System.out.println("11. Exit");
            } else { // Staff
                System.out.println("1. View Products");
                System.out.println("2. Make a Sale (Billing)");
                System.out.println("3. Log Out");
                System.out.println("4. Exit");
            }

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            if (role.equalsIgnoreCase("Admin")) {
                switch (choice) {
                    case 1 -> addProduct(conn);
                    case 2 -> viewProducts(conn);
                    case 3 -> removeProducts(conn);
                    case 4 -> updateProduct(conn);
                    case 5 -> makeSale(conn);
                    case 6 -> viewSalesReport(conn);
                    case 7 -> addStaff(conn);
                    case 8 -> viewStaff(conn);
                    case 9 -> removeStaff(conn);
                    case 10 -> { 
                    	System.out.println("✅ Logged Out Successfully");
                    	return true;
                    }
                    case 11 -> {
                        System.out.println("👋 Exiting...");
                        return false;
                    }
                    default -> System.out.println("❌ Invalid choice!");
                }
            } else { // Staff menu
                switch (choice) {
                    case 1 -> viewProducts(conn);
                    case 2 -> makeSale(conn);
                    case 3 ->{ 
                    	System.out.println("✅ Logged Out Successfully");
                    	return true;
                    }
                    case 4 -> {
                        System.out.println("👋 Exiting...");
                        return false;
                    }
                    default -> System.out.println("❌ Invalid choice!");
                }
            }
        }
    }

    // ----------------- Admin Features -----------------
    private static void addStaff(Connection conn) throws SQLException {
        System.out.print("Enter new staff username: ");
        String username = sc.next();
        System.out.print("Enter new staff password: ");
        String password = sc.next();

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users(username, password, role) VALUES (?, ?, 'Staff')");
        ps.setString(1, username);
        ps.setString(2, password);
        ps.executeUpdate();
        System.out.println("✅ Staff added successfully!");
    }
    
    private static void viewStaff(Connection conn) throws SQLException{
    	Statement st =conn.createStatement();
    	ResultSet rs = st.executeQuery("SELECT * FROM users WHERE role='Staff'");
    	boolean found = false;
    	while(rs.next()) {
    		found=true;
    		System.out.printf("Username : %s | Password : %s\n",rs.getString("username"),rs.getString("password"));
   		}
    	if(!found) {
    		System.out.println("❌ Staff not found!");
    	}
    }

    private static void removeStaff(Connection conn) throws SQLException {
    	System.out.println("\n--- Existing Staff ---");
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT username FROM users WHERE role='Staff'");
        while (rs.next()) {
            System.out.printf("Username: %s\n", rs.getString("username"));
        }
        System.out.print("Enter staff username to remove: ");
        String username = sc.next();

        PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE username=? AND role='Staff'");
        ps.setString(1, username);
        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("✅ Staff removed successfully!");
        } else {
            System.out.println("❌ Staff not found!");
        }
    }

    private static void addProduct(Connection conn) throws SQLException {
        while(true){
            System.out.print("Enter product name (or type '0' to finish): ");
            String name = sc.next();
            if(name.equals("0")) break;

            System.out.print("Enter quantity: ");
            int qty = sc.nextInt();
            System.out.print("Enter price: ");
            double price = sc.nextDouble();

            PreparedStatement ps = conn.prepareStatement("INSERT INTO products(name, quantity, price) VALUES (?, ?, ?)");
            ps.setString(1, name);
            ps.setInt(2, qty);
            ps.setDouble(3, price);
            ps.executeUpdate();

            System.out.println("✅ Product '" + name + "' added successfully!\n");
        }
        System.out.println("✅ Finished adding products.");
    }


    private static void viewProducts(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM products");
        System.out.println("\n--- Product List ---");
        while (rs.next()) {
            System.out.printf("ID: %d | Name: %s | Qty: %d | Price: %.2f\n",
                    rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"), rs.getDouble("price"));
        }
    }
    
    private static void removeProducts(Connection conn) throws SQLException {
        while(true){
            System.out.println("\n--- Current Products ---");
            viewProducts(conn);

            System.out.print("Enter product ID to remove (or 0 to finish): ");
            int id = sc.nextInt();
            if(id==0) break;

            PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM products WHERE id=?");
            ps1.setInt(1,id);
            ResultSet rs = ps1.executeQuery();

            if(rs.next()){
                int available = rs.getInt("quantity");
                String name = rs.getString("name");

                System.out.print("Enter quantity to remove: ");
                int qty = sc.nextInt();

                if(qty >= available){
                    // Check if product exists in sales
                    PreparedStatement psSales = conn.prepareStatement("SELECT COUNT(*) FROM sales WHERE product_id=?");
                    psSales.setInt(1,id);
                    ResultSet rsSales = psSales.executeQuery();
                    rsSales.next();

                    if(rsSales.getInt(1) > 0){
                        System.out.println("❌ Cannot delete entire product as it exists in sales. Remove quantity only.");
                    } else {
                        PreparedStatement del = conn.prepareStatement("DELETE FROM products WHERE id=?");
                        del.setInt(1,id);
                        del.executeUpdate();
                        System.out.println("✅ Product '" + name + "' completely removed from inventory!");
                    }
                } else {
                    int newQty = available - qty;
                    PreparedStatement upd = conn.prepareStatement("UPDATE products SET quantity=? WHERE id=?");
                    upd.setInt(1,newQty);
                    upd.setInt(2,id);
                    upd.executeUpdate();
                    System.out.println("✅ Removed "+qty+" of '"+name+"'. New quantity: "+newQty);
                }
            } else {
                System.out.println("❌ Product not found!");
            }
        }
        System.out.println("✅ Finished removing products.");
    }



    private static void updateProduct(Connection conn) throws SQLException {
        while(true){
            System.out.println("\n--- Available Products ---");
            viewProducts(conn);

            System.out.print("Enter product ID to update (or 0 to finish): ");
            int id = sc.nextInt();
            if(id == 0) break;

            System.out.print("Enter quantity to add: ");
            int qtyToAdd = sc.nextInt();

            PreparedStatement ps1 = conn.prepareStatement("SELECT quantity FROM products WHERE id=?");
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();

            if(rs.next()){
                int currentQty = rs.getInt("quantity");
                int newQty = currentQty + qtyToAdd;

                PreparedStatement ps2 = conn.prepareStatement("UPDATE products SET quantity=? WHERE id=?");
                ps2.setInt(1, newQty);
                ps2.setInt(2, id);
                ps2.executeUpdate();

                System.out.println("✅ Product updated successfully! New quantity: " + newQty);
            } else {
                System.out.println("❌ Product not found!");
            }
        }
        System.out.println("✅ Finished updating products.");
    }



    private static void makeSale(Connection conn) throws SQLException {
        double grandTotal = 0.0;
        System.out.println("\n--- Start Sale ---");

        long billId = System.currentTimeMillis();

        while (true) {
            System.out.println("\nAvailable Products");
            viewProducts(conn);

            System.out.print("Enter product ID to buy (or 0 to finish): ");
            int id = sc.nextInt();
            if (id == 0) break;

            System.out.print("Enter quantity: ");
            int qty = sc.nextInt();

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int available = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String name = rs.getString("name");

                if (qty <= available) {
                    double total = qty * price;
                    grandTotal += total;

                    // Insert into sales table with billId
                    PreparedStatement salePs = conn.prepareStatement(
                            "INSERT INTO sales(product_id, quantity, total, bill_id) VALUES (?, ?, ?, ?)");
                    salePs.setInt(1, id);
                    salePs.setInt(2, qty);
                    salePs.setDouble(3, total);
                    salePs.setLong(4, billId);
                    salePs.executeUpdate();

                    // Update stock
                    PreparedStatement upd = conn.prepareStatement("UPDATE products SET quantity=? WHERE id=?");
                    upd.setInt(1, available - qty);
                    upd.setInt(2, id);
                    upd.executeUpdate();

                    System.out.println("✅ Added " + qty + " x " + name + " to bill (Total: " + total + ")");
                    if (available - qty < 5) {
                        System.out.println("⚠ Low Stock Alert for product: " + name);
                    }
                } else {
                    System.out.println("❌ Not enough stock for product: " + name);
                }
            } else {
                System.out.println("❌ Product not found!");
            }
        }

        if (grandTotal > 0) {
            System.out.println("\n💰 Final Bill Amount: " + grandTotal);
            System.out.println("✅ Sale Completed!");
        } else {
            System.out.println("❌ No products selected for sale!");
        }
    }


    private static void viewSalesReport(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(
            "SELECT s.bill_id, p.name, s.quantity, s.total, s.sale_date " +
            "FROM sales s JOIN products p ON s.product_id = p.id " +
            "ORDER BY s.bill_id, s.id"
        );

        long currentBill = -1;
        System.out.println("\n--- Sales Report ---");

        while (rs.next()) {
            long billId = rs.getLong("bill_id");
            if (billId != currentBill) {
                currentBill = billId;
                System.out.println("\n💳 Bill ID: " + billId);
            }
            System.out.printf("Product: %s | Qty: %d | Total: %.2f | Date: %s\n",
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDouble("total"),
                    rs.getTimestamp("sale_date"));
        }
    }

}
