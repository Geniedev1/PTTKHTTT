package labjava.servlet;

import labjava.dao.PaymentDAO;
import labjava.model.Payment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "PaymentServlet", urlPatterns = {"/process-payment"})
public class PaymentServlet extends HttpServlet {

    private PaymentDAO paymentDAO;

    @Override
    public void init() throws ServletException {
        this.paymentDAO = new PaymentDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            // Lấy dữ liệu từ form (payment.jsp)
            int slipId = Integer.parseInt(request.getParameter("slipId"));
            String method = request.getParameter("paymentMethod");
            String status = "paid"; // Giả định thanh toán thành công ngay lập tức

            // Gọi DAO để lưu
            Payment payment = new Payment();
//            private int id;
//            private String transactionCode; // maGiaoDich
//            private String method; // phuongThuc
//            private String status; // trangThai (pending, paid, failed)
//            private Date paidAt; // ngayThanhToan
//            private int returnSlipId; // Khóa ngoại đến ReturnSlip
            payment.setMethod(method);
            payment.setStatus(status);
            payment.setId(slipId);
            boolean success = paymentDAO.createPayment(payment);

            if (success) {
                // Thanh toán xong, về trang chủ
                response.sendRedirect("librarian_home.jsp?payment=success"); // Sửa thành trang của bạn
            } else {
                // Lỗi, quay lại trang thanh toán
                String amount = request.getParameter("amount"); // Lấy lại amount
                response.sendRedirect("payment.jsp?slipId=" + slipId + "&amount=" + amount + "&error=PaymentFailed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Lỗi xử lý thanh toán", e);
        }
    }
}