import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/labjava";
        String user = "root";   // đổi nếu bạn có user khác
        String pass = "your_password";

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            System.out.println("✅ Kết nối MySQL thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
