package labjava.dao;

import labjava.model.Librarian;
import java.sql.*;

public class LibrarianDAO extends DAO {

    public LibrarianDAO() {
        super();
    }


    public Librarian findByLogin(String username, String passHash) {
        String sql = """
            SELECT librarian_id, username, email, phone, birth_date
            FROM tbl_member
            WHERE username=? AND password_hash=? AND librarian_id IS NOT NULL
            LIMIT 1
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Librarian l = new Librarian();
                    l.setId(rs.getInt("librarian_id"));
                    l.setUsername(rs.getString("username"));
                    l.setEmail(rs.getString("email"));
                    l.setPhone(rs.getString("phone"));
                    java.sql.Date bd = rs.getDate("birth_date");
                    l.setBirthDate(bd != null ? bd.toString() : null);
                    return l;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
