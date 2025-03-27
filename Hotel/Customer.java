package Hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

class DetailsOfCustomer {
    String Customer_Name, Phone_no, Email_id, Address;

    public DetailsOfCustomer(String Customer_Name, String phone_no, String Email_id, String address) {
        this.Customer_Name = Customer_Name;
        this.Phone_no = phone_no;
        this.Email_id = Email_id;
        this.Address = address;
    }
}

class Node {
    DetailsOfCustomer d1;
    Node Prev, Next;

    Node(DetailsOfCustomer d1) {
        this.d1 = d1;
        this.Prev = this.Next = this;
    }
}

public class Customer {
    private Node First = null;

    public void addCustomer() {
        DetailsOfCustomer d1 = getCustomerDetailsFromUser();
        addToCLL(d1);
        saveToDB(d1, "INSERT INTO Customer (Customer_Name, Phone_no, Email_id, Address) VALUES (?, ?, ?, ?)");
    }

    DetailsOfCustomer getCustomerDetailsFromUser() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter name: ");
        String Customer_Name = sc.nextLine();
        String phone_no = getValidPhoneNo(sc);
        String Email_id = getValidEmailId(sc);
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        return new DetailsOfCustomer(Customer_Name, phone_no, Email_id, address);
    }

    String getValidPhoneNo(Scanner sc) {
        while (true) {
            System.out.print("Enter phone number (10 digits): ");
            String phone_no = sc.nextLine();
            if (phone_no.matches("\\d{10}"))
                return phone_no;
            System.out.println("Invalid phone number! Try again.");
        }
    }

    String getValidEmailId(Scanner sc) {
        while (true) {
            System.out.print("Enter email (@gmail.com): ");
            String Email_id = sc.nextLine();
            if (Email_id.endsWith("@gmail.com"))
                return Email_id;
            System.out.println("Invalid email! Try again.");
        }
    }

    void addToCLL(DetailsOfCustomer d1) {
        Node newNode = new Node(d1);
        if (First == null) {
            First = newNode;
        } else {
            Node last = First.Prev;
            last.Next = newNode;
            newNode.Prev = last;
            newNode.Next = First;
            First.Prev = newNode;
        }
    }

    void saveToDB(DetailsOfCustomer d1, String query) {
        try (Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, d1.Customer_Name);
            stmt.setString(2, d1.Phone_no);
            stmt.setString(3, d1.Email_id);
            stmt.setString(4, d1.Address);
            stmt.executeUpdate();
            System.out.println("Customer saved to database.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void RemoveCustomer(String name) {
        loadCustomersFromDB();
        if (First == null) {
            System.out.println("List is empty, nothing to remove.");
            return;
        }

        Node temp = First;
        Node toDelete = null;

        // Check if the first node matches
        if (temp.d1.Customer_Name.equals(name)) {
            toDelete = temp;
        } else {
            do {
                if (temp.d1.Customer_Name.equals(name)) {
                    toDelete = temp;
                    break;
                }
                temp = temp.Next;
            } while (temp != First);
        }

        if (toDelete != null) {
            // If there is only one node in the list
            if (toDelete == First && toDelete.Next == First) {
                First = null;
            } else {
                // If it's the first node
                if (toDelete == First) {
                    Node last = First.Prev;
                    First = First.Next;
                    First.Prev = last;
                    last.Next = First;
                } else {
                    Node prevNode = toDelete.Prev;
                    Node nextNode = toDelete.Next;
                    prevNode.Next = nextNode;
                    nextNode.Prev = prevNode;
                }
            }
            System.out.println("Customer removed from list: " + name);
        } else {
            System.out.println("Customer not found.");
        }

        removeFromDatabase(name);
    }

    public void removeFromDatabase(String Customer_Name) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataBaseConnection.getConnection();
            if (conn != null) {
                stmt = conn.prepareStatement("DELETE FROM Customer WHERE Customer_Name = ?");
                stmt.setString(1, Customer_Name);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Customer removed from database: " + Customer_Name);
                } else {
                    System.out.println("Customer not found in database.");
                }
            } else {
                System.out.println("Connection is null, cannot remove customer from database.");
            }
        } catch (Exception e) {
            System.out.println(e);
        } 
    }

    public void displayCustomersDetails() {
        loadCustomersFromDB();
        if (First == null) {
            System.out.println("No customers available.");
            return;
        }
        Node temp = First;
        do {
            System.out.println("Customer Name: " + temp.d1.Customer_Name + ", Phone: " + temp.d1.Phone_no + ", Email_id: " + temp.d1.Email_id
                    + ", Address: " + temp.d1.Address);
            temp = temp.Next;
        } while (temp != First);
    }

    public void loadCustomersFromDB() {
        try (Connection conn = DataBaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Customer")) {

            while (rs.next()) {
                DetailsOfCustomer customerDetails = new DetailsOfCustomer(rs.getString("Customer_Name"), rs.getString("Phone_no"),
                        rs.getString("Email_id"), rs.getString("Address"));
                addToCLL(customerDetails);
            }
            System.out.println("Customers loaded from database.");
        } catch (Exception e) {
            System.out.println();
        }
    }
}