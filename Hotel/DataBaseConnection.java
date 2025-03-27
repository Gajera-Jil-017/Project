package Hotel;
import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection {
    private static String DB_URL = "jdbc:mysql://localhost:3306/hotel";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "";

    public static Connection getConnection() {
        try {
            String drivername = "com.mysql.cj.jdbc.Driver";
            Class.forName(drivername);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}