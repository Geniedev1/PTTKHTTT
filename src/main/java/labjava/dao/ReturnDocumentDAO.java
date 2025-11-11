package labjava.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReturnDocumentDAO extends DAO {

    public ReturnDocumentDAO() {
        super();
    }


    public ReturnDocumentDAO(Connection con) {
        this.con = con;
    }


    public boolean saveDocumentReturn(int returnSlipId, int documentBorrowId) throws SQLException {

        String sql = "INSERT INTO tbl_document_return (document_borrow_id, returned_at, return_slip_id) " +
                "VALUES (?, NOW(), ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, documentBorrowId);
            ps.setInt(2, returnSlipId);
            return ps.executeUpdate() > 0;
        }
    }
}