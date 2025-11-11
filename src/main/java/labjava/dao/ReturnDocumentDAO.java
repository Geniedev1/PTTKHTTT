package labjava.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReturnDocumentDAO extends DAO {

    // Constructor mặc định (nếu dùng ngoài transaction)
    public ReturnDocumentDAO() {
        super();
    }

    /**
     * Constructor này nhận Connection
     * để đảm bảo chạy bên trong một transaction
     */
    public ReturnDocumentDAO(Connection con) {
        this.con = con;
    }

    /**
     * Lưu một mục tài liệu đã trả vào CSDL
     * @param returnSlipId ID của phiếu trả (khóa ngoại)
     * @param documentBorrowId ID của lần mượn (tbl_document_borrow.id)
     */
    public boolean saveDocumentReturn(int returnSlipId, int documentBorrowId) throws SQLException {
        // Ghi vào tbl_document_return, đồng thời cập nhật returned_at
        String sql = "INSERT INTO tbl_document_return (document_borrow_id, returned_at, return_slip_id) " +
                "VALUES (?, NOW(), ?)";

        // Sử dụng 'con' (connection) đã được truyền vào
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, documentBorrowId);
            ps.setInt(2, returnSlipId);
            return ps.executeUpdate() > 0;
        }
        // Không đóng connection ở đây!
    }
}