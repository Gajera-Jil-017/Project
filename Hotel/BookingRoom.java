package Hotel;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class BookingRoom {

    private boolean[] roomAvailability = {true, true, true, true, true};

    class BookingDetails {
        int roomNo;
        int customerId;
        Date checkInDate;
        Date checkOutDate;
        String roomType;
        double price;

        public BookingDetails(int roomNo, int customerId, Date checkInDate, Date checkOutDate, String roomType, double price) {
            this.roomNo = roomNo;
            this.customerId = customerId;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.roomType = roomType;
            this.price = price;
        }
    }

    class Node {
        BookingDetails booking;
        Node next;
        Node prev;

        Node(BookingDetails booking) {
            this.booking = booking;
            this.next = this;
            this.prev = this;
        }
    }

    private Node head = null;

    public void addBooking() {
        Scanner sc = new Scanner(System.in);
        int customerId;

        while (true) {
            System.out.println("Enter Customer ID: ");
            customerId = sc.nextInt();

            if (isCustomerIdValid(customerId)) {
                break;
            } else {
                System.out.println("Customer ID not found. Please enter a valid Customer ID.");
            }
        }

        System.out.println("Enter Check-In Date (yyyy-mm-dd): ");
        Date checkInDate = Date.valueOf(sc.next());
        System.out.println("Enter Check-Out Date (yyyy-mm-dd): ");
        Date checkOutDate = Date.valueOf(sc.next());

        String roomType;
        double price = 0;
        int roomNo ;

        while (true) {
            System.out.println("Enter Room Type (single, deluxe, family): ");
            roomType = sc.next().toLowerCase();

            if (roomType.equals("single") && (roomAvailability[0] || roomAvailability[1])) {
                roomNo = roomAvailability[0] ? 1 : 2;
                roomAvailability[roomNo - 1] = false;
                price = 5000;
                break;
            } else if (roomType.equals("deluxe") && (roomAvailability[2] || roomAvailability[3])) {
                roomNo = roomAvailability[2] ? 3 : 4;
                roomAvailability[roomNo - 1] = false;
                price = 10000;
                break;
            } else if (roomType.equals("family") && roomAvailability[4]) {
                roomNo = 5;
                roomAvailability[4] = false;
                price = 15000;
                break;
            } else {
                System.out.println("Invalid or unavailable room type! Please try again.");
            }
        }

        BookingDetails newBooking = new BookingDetails(roomNo, customerId, checkInDate, checkOutDate, roomType, price);
        Node newNode = new Node(newBooking);

        if (head == null) {
            head = newNode;
        } else {
            Node last = head.prev;
            last.next = newNode;
            newNode.prev = last;
            newNode.next = head;
            head.prev = newNode;
        }

        saveBookingToDB(newBooking);
        System.out.println("Room booking added successfully. Assigned Room Number: " + roomNo);

        generateBill(newBooking);
    }

    private boolean isCustomerIdValid(int customerId) {
        String sql = "SELECT COUNT(*) FROM customer WHERE customerId = ?";
        try (Connection con = DataBaseConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, customerId);
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false; 
    }

    private void saveBookingToDB(BookingDetails booking) {
        String sql = "{call addRoomBooking(?, ?, ?, ?, ?, ?)}";
        try (Connection con = DataBaseConnection.getConnection();
            CallableStatement cst = con.prepareCall(sql)) {
            cst.setInt(1, booking.roomNo);
            cst.setInt(2, booking.customerId);
            cst.setDate(3, booking.checkInDate);
            cst.setDate(4, booking.checkOutDate);
            cst.setString(5, booking.roomType);
            cst.setDouble(6, booking.price);
            cst.executeUpdate();
            System.out.println("Booking saved to database.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void generateBill(BookingDetails booking) {
        long daysStayed = (booking.checkOutDate.getTime() - booking.checkInDate.getTime()) / (1000 * 60 * 60 * 24) + 1;
        double totalBill = daysStayed * booking.price;
        System.out.println("Bill Summary:");
        System.out.println("Customer ID: " + booking.customerId);
        System.out.println("Room Number: " + booking.roomNo);
        System.out.println("Room Type: " + booking.roomType);
        System.out.println("Check-In Date: " + booking.checkInDate);
        System.out.println("Check-Out Date: " + booking.checkOutDate);
        System.out.println("Days Stayed: " + daysStayed);
        System.out.println("Total Bill: " + totalBill);
    }

    public void DeleteBooking() {
        loadBookingsFromDB();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Room Number to delete: ");
        int roomNumber = sc.nextInt();
        deleteBookingFromDB(roomNumber);
        if (head == null) {
            System.out.println("No bookings available.");
            return;
        }
        Node current = head;
        boolean bookingFound = false;

        do {
            if (current.booking.roomNo == roomNumber) {
                if (current.booking.checkOutDate.before(new Date(System.currentTimeMillis()))) {
                    roomAvailability[roomNumber - 1] = true;

                    deleteBookingFromDB(current.booking.roomNo);

                    if (current == head && head.next == head) {
                        head = null;
                    } else {
                        current.prev.next = current.next;
                        current.next.prev = current.prev;
                        if (current == head) {
                            head = current.next;
                        }
                    }

                    bookingFound = true;
                    System.out.println("Booking deleted successfully.");
                    break;
                } else {
                    System.out.println("Cannot delete booking before check-out date.");
                    return;
                }
            }
            current = current.next;
        } while (current != head);

        if (!bookingFound) {
            System.out.println("Booking not found for the given room number.");
        }
    }

    private void deleteBookingFromDB(int roomNumber) {
        String sql = "DELETE FROM RoomBooking WHERE RoomNumber = ?";

        try (Connection con = DataBaseConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, roomNumber);
            int rowsAffected = pst.executeUpdate();

            System.out.println(rowsAffected > 0 ? "Booking deleted from database." : "Failed to delete booking from database.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void displayBookings() {
        loadBookingsFromDB();
        if (head == null) {
            System.out.println("No bookings available.");
            return;
        }
        Node current = head;
        do {
            System.out.println("Room Number: " + current.booking.roomNo);
            System.out.println("Customer ID: " + current.booking.customerId);
            System.out.println("Check-In Date: " + current.booking.checkInDate);
            System.out.println("Check-Out Date: " + current.booking.checkOutDate);
            System.out.println("Room Type: " + current.booking.roomType);
            System.out.println("Price: " + current.booking.price);
            System.out.println("-----------------------------");
            current = current.next;
        } while (current != head);
    }

    public void loadBookingsFromDB() {
        String sql = "SELECT RoomNumber, CustomerId, CheckInDate, CheckOutDate, RoomType, Price FROM RoomBooking";
        
        try (Connection con = DataBaseConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int roomNo = rs.getInt("RoomNumber");
                int customerId = rs.getInt("CustomerId"); 
                Date checkInDate = rs.getDate("CheckInDate");
                Date checkOutDate = rs.getDate("CheckOutDate");
                String roomType = rs.getString("RoomType");
                double price = rs.getDouble("Price");
                
                BookingDetails booking = new BookingDetails(roomNo, customerId, checkInDate, checkOutDate, roomType, price);
                Node newNode = new Node(booking);
                
                if (head == null) {
                    head = newNode;
                    head.next = head; 
                    head.prev = head; 
                } else {
                    Node last = head.prev;
                    last.next = newNode;
                    newNode.prev = last;
                    newNode.next = head;
                    head.prev = newNode;
                }
                
                // Update room availability
                roomAvailability[roomNo - 1] = false;
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}