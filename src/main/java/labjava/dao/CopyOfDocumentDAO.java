package labjava.dao;

import labjava.model.CopyOfDocument;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CopyOfDocumentDAO extends DAO {

    public CopyOfDocumentDAO() {
        super();
    }
    public boolean updateStatus(CopyOfDocument copyd) throws SQLException {
        int id = 0;
        String sql1 = "SELECT c.id " +
                "FROM tbl_copy_of_document c " +
                "JOIN tbl_document_borrow db ON c.id = db.copy_of_document_id " +
                "WHERE db.id = ?"; // Lọc theo ID của lần mượn

        PreparedStatement ps = con.prepareStatement(sql1);
            ps.setInt(1, copyd.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                     rs.getInt(1);
                     id = rs.getInt(1);
                }
            }
        String sql = "UPDATE tbl_copy_of_document SET copy_condition = ? WHERE id = ?";
        try (PreparedStatement ps1 = con.prepareStatement(sql)) {
            ps1.setString(1, copyd.getStatus());
            ps1.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}