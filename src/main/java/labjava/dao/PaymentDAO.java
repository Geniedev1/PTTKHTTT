package labjava.dao;

import labjava.model.Payment;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentDAO extends DAO {

    public PaymentDAO() {
        super();
    }

    /**
     * Tạo một giao dịch thanh toán mới trong tbl_payment

     * @return true nếu thành công
     */
    public boolean createPayment(Payment p) {
//        slipId, method, status
        int returnSlipId = p.getId();
        String method = p.getMethod();
        String status = p.getStatus();
        // CSDL của bạn có: txn_code, method, status, paid_at, return_slip_id
        String sql = "INSERT INTO tbl_payment (txn_code, method, status, paid_at, return_slip_id) " +
                "VALUES (?, ?, ?, NOW(), ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Tạo mã giao dịch đơn giản
            String txnCode = "TXN_" + System.currentTimeMillis();

            ps.setString(1, txnCode);
            ps.setString(2, method);
            ps.setString(3, status);
            ps.setInt(4, returnSlipId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}