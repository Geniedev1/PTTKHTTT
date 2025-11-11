package labjava.dao;

import labjava.model.BorrowSlip;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowSlipDAO extends DAO {

    public BorrowSlipDAO() { super(); }

    /**
     * Lấy các phiếu mượn (slips) đang hoạt động (status = 'borrowing')
     */
    public List<BorrowSlip> getActiveBorrowSlips(int readerId) {
        List<BorrowSlip> list = new ArrayList<>();

        // SỬA Ở ĐÂY: Đã xóa "librarian_id" khỏi câu SELECT
        String sql = "SELECT id, created_at, status, reader_id " +
                "FROM tbl_borrow_slip " +
                "WHERE reader_id = ? AND status = 'borrowing'";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, readerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BorrowSlip slip = new BorrowSlip();
                    slip.setId(rs.getInt("id"));
                    slip.setCreatedAt(rs.getTimestamp("created_at"));
                    slip.setStatus(rs.getString("status"));
                    slip.setReaderId(rs.getInt("reader_id"));

                    // SỬA Ở ĐÂY: Đã xóa dòng "slip.setLibrarianId(...)"

                    list.add(slip);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}