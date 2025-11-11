package labjava.dao;

import labjava.model.DocumentBorrow;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentBorrowDAO extends DAO {

    public DocumentBorrowDAO() { super(); }

    private double getOverdueFineRate() throws SQLException {
        String sql = "SELECT amount FROM tbl_fine WHERE fine_type = 'Late return' LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("amount");
            }
        }
        return 5000.0;
    }


    public List<DocumentBorrow> getListBorrowedDocument(int borrowSlipId) {
        List<DocumentBorrow> list = new ArrayList<>();
        String sql = "SELECT " +
                "  db.id AS borrow_id, c.copy_code, d.title, db.due_at, " +
                "  DATEDIFF(CURDATE(), db.due_at) AS days_overdue " +
                "FROM tbl_document_borrow db " +
                "JOIN tbl_copy_of_document c ON db.copy_of_document_id = c.id " +
                "JOIN tbl_document d ON c.document_id = d.id " +
                "WHERE db.borrow_slip_id = ?";

        try {
            double finePerDay = getOverdueFineRate();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, borrowSlipId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // ... (Logic map và tính phạt giữ nguyên như cũ)
                        DocumentBorrow item = new DocumentBorrow();
                        item.setBorrowId(rs.getInt("borrow_id"));
                        item.setCopyCode(rs.getString("copy_code"));
                        item.setTitle(rs.getString("title"));
                        item.setDueDate(rs.getDate("due_at"));

                        int daysOverdue = rs.getInt("days_overdue");
                        if (daysOverdue > 0) {
                            item.setOverdueStatus("Quá hạn");
                            item.setProvisionalFine(daysOverdue * finePerDay);
                        } else {
                            item.setOverdueStatus("Đúng hạn");
                            item.setProvisionalFine(0);
                        }
                        list.add(item);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public DocumentBorrow getBorrowedDocument(int documentBorrowId) {
        String sql = "SELECT " +
                "  db.id AS borrow_id, c.copy_code, d.title, db.due_at, " +
                "  DATEDIFF(CURDATE(), db.due_at) AS days_overdue " +
                "FROM tbl_document_borrow db " +
                "JOIN tbl_copy_of_document c ON db.copy_of_document_id = c.id " +
                "JOIN tbl_document d ON c.document_id = d.id " +
                "WHERE db.id = ?";

        try {
            double finePerDay = getOverdueFineRate();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, documentBorrowId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) { // Chỉ lấy 1
                        DocumentBorrow item = new DocumentBorrow();
                        item.setBorrowId(rs.getInt("borrow_id"));
                        item.setCopyCode(rs.getString("copy_code"));
                        item.setTitle(rs.getString("title"));
                        item.setDueDate(rs.getDate("due_at"));

                        int daysOverdue = rs.getInt("days_overdue");
                        if (daysOverdue > 0) {
                            item.setOverdueStatus("Quá hạn");
                            item.setProvisionalFine(daysOverdue * finePerDay);
                        } else {
                            item.setOverdueStatus("Đúng hạn");
                            item.setProvisionalFine(0);
                        }
                        return item;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public double getDamageFineAmount() throws SQLException {
        String sql = "SELECT amount FROM tbl_fine WHERE fine_type = 'Damaged copy' LIMIT 1";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("amount");
            }
        }

        return 50000.0;
    }

}