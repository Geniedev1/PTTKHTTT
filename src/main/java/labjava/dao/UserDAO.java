package labjava.dao;

import labjava.model.User;
import java.sql.*;

/**
 * UserDAO kế thừa lớp DAO để dùng chung Connection
 */
public class UserDAO extends DAO {

    public UserDAO() {
        super(); // gọi constructor của DAO để mở kết nối
    }

    // Thêm user mới
    public boolean insert(User u) {
        String sql = "INSERT INTO users(username, password, email) VALUES(?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm user theo username + password
    public User findByLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
