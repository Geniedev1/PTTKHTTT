package labjava.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FineDetailDAO extends DAO {

    // Constructor mặc định
    public FineDetailDAO() {
        super();
    }

    /**
     * Constructor để dùng chung Connection
     */
    public FineDetailDAO(Connection con) {
        this.con = con;
    }

    /**
     * Lấy ID của một loại phạt dựa trên tên (fine_type)
     */
    public int getFineIdByType(String fineType) throws SQLException {
        String sql = "SELECT id FROM tbl_fine WHERE fine_type = ? LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fineType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        // Ném lỗi nếu không tìm thấy, để transaction có thể rollback
        throw new SQLException("Không tìm thấy fine_type trong CSDL: " + fineType);
    }

    /**
     * Lưu một chi tiết phạt vào CSDL
     */
    public boolean saveFineDetail(int returnSlipId, int fineId, int quantity) throws SQLException {
        String sql = "INSERT INTO tbl_fine_detail (quantity, return_slip_id, fine_id) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, returnSlipId);
            ps.setInt(3, fineId);
            return ps.executeUpdate() > 0;
        }
    }
}