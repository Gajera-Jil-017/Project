package Hotel;
import java.util.Scanner;

public class main1 {
    public static void main(String[] args) throws Exception{
        Scanner sc = new Scanner(System.in);
        Customer cr=new Customer();
        BookingRoom b1=new BookingRoom(); 
        System.out.println();
        System.out.println("=============================================");
        System.out.println("       WELCOME TO THE HOTEL MANAGEMENT");
        System.out.println("=============================================");
        int x;
        do{
            System.out.println();
            System.out.println("1.  ADD CUSTOMER");
            System.out.println("2.  REMOVE CUSTOMER");
            System.out.println("3.  DISPLAY CUSTOMER");
            System.out.println("4.  BOOK YOUR ROOM ");
            System.out.println("5.  DELETE ROOM BOOK ");
            System.out.println("6.  DISPLAY ROOM BOOKING DETAILS ");
            System.out.println("7.  EXIT");
            System.out.println();
            System.out.print("Choose Number for Option : ");
            x = sc.nextInt();
            switch(x){
                case 1:
                    cr.addCustomer();
                    break;
                case 2: 
                    System.out.println("enter name that you want to remove");
                    String nm=sc.next();
                    cr.RemoveCustomer(nm);
                    break ;
                case 3:
                    System.out.println("customer list:");
                    cr.displayCustomersDetails();
                    break;
                case 4:
                    b1.addBooking();
                    break;
                case 5:
                    b1.DeleteBooking();
                    break;
                case 6:
                    b1.displayBookings();
                    break;
                case 7:
                    System.out.println();
                    System.out.println("Thank you for Visiting ");
                    break;    
                default:
                    System.out.println();
                    System.out.println("INVALID CHOISE ");
                    System.out.println("PLEASE ENTER A VALID NUMBER ");
                    break;
            }
        }while (x != 7);
    }
}