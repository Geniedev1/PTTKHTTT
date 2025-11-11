package labjava.dao;

import labjava.model.ReturnSlip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReturnSlipDAO extends DAO {

    public ReturnSlipDAO() {
        super();
    }

    /**
     * Hàm chính: Tạo một Phiếu Trả hoàn chỉnh trong một TRANSACTION
     *
     *
     */
    public int createReturnSlip(ReturnSlip returnSlip) {
//        readerId,
//                librarianId,
//                totalFine,
//                returnedBorrowIds,
//                damagedCount,
//                lateFineAmount
        int readerId = returnSlip.getReader().getId();
        int librarianId = returnSlip.getLibrarian().getId();
        double totalFine = returnSlip.getTotalFine();
        int damagedCount = returnSlip.getFineDetails().get(0).getQuantity();
        double lateFineAmount = returnSlip.getFineDetails().get(0).getFine().getFine();
        String[] returnedBorrowIds = new String[returnSlip.getDocumentReturns().size()];
        for (int i = 0; i < returnSlip.getDocumentReturns().size(); i++) {
            returnedBorrowIds[i] = returnSlip.getDocumentReturns().get(i).getDocumentBorrowId();
        }
        String sqlSlip = "INSERT INTO tbl_return_slip (total_fine, librarian_id, reader_id) " +
                "VALUES (?, ?, ?)";

        Connection conn = this.con; // Sử dụng connection chung của DAO

        try {
            // === BẮT ĐẦU TRANSACTION ===
            conn.setAutoCommit(false);

            // 1. Tạo Phiếu Trả (tbl_return_slip)
            int returnSlipId;
            try (PreparedStatement psSlip = conn.prepareStatement(sqlSlip, Statement.RETURN_GENERATED_KEYS)) {
                psSlip.setDouble(1, totalFine);
                psSlip.setInt(2, librarianId);
                psSlip.setInt(3, readerId);
                psSlip.executeUpdate();

                try (ResultSet rs = psSlip.getGeneratedKeys()) {
                    if (rs.next()) {
                        returnSlipId = rs.getInt(1); // Lấy ID của phiếu vừa tạo
                    } else {
                        throw new SQLException("Tạo phiếu trả thất bại, không lấy được ID.");
                    }
                }
            }

            // 2. Lưu Chi tiết Tài liệu trả (gọi ReturnDocumentDAO)
            ReturnDocumentDAO docReturnDAO = new ReturnDocumentDAO(conn); // Truyền connection
            for (String borrowIdStr : returnedBorrowIds) {
                int borrowId = Integer.parseInt(borrowIdStr);
                docReturnDAO.saveDocumentReturn(returnSlipId, borrowId);
            }

            // 3. Lưu Chi tiết Phạt (gọi FineDetailDAO)
            FineDetailDAO fineDetailDAO = new FineDetailDAO(conn); // Truyền connection

            // 3a. Lưu phạt trễ hạn (nếu có)
            if (lateFineAmount > 0) {
                int lateFineId = fineDetailDAO.getFineIdByType("Late return");
                fineDetailDAO.saveFineDetail(returnSlipId, lateFineId, 1); // Số lượng 1 (cho cả đợt)
            }

            // 3b. Lưu phạt hỏng (nếu có)
            if (damagedCount > 0) {
                int damageFineId = fineDetailDAO.getFineIdByType("Damaged copy");
                fineDetailDAO.saveFineDetail(returnSlipId, damageFineId, damagedCount); // Số lượng sách hỏng
            }

            // === KẾT THÚC TRANSACTION ===
            conn.commit(); // Lưu tất cả thay đổi
            return returnSlipId; // <-- SỬA: Trả về ID
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); // Hoàn tác nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1;
        } finally {
            try {
                conn.setAutoCommit(true); // Trả về chế độ auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}