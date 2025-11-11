package labjava.servlet;

// Import các DAO
import labjava.dao.ReaderDAO;
import labjava.dao.BorrowSlipDAO;
import labjava.dao.DocumentBorrowDAO;
import labjava.dao.CopyOfDocumentDAO; // <-- THÊM DAO NÀY

// Import các Model
import labjava.model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays; // <-- THÊM IMPORT NÀY
import java.util.List;

/**
 * Servlet này xử lý 2 việc:
 * 1. doGet: Hiển thị danh sách sách MÀ MỘT BẠN ĐỌC đang mượn.
 * 2. doPost: Xử lý khi thủ thư nhấn "Xác nhận trả sách", cập nhật CSDL,
 * và chuyển tiếp đến trang xác nhận (Phiếu Trả).
 */
@WebServlet(name = "DocumentServlet", urlPatterns = {"/process-return"})
public class DocumentServlet extends HttpServlet {

    private ReaderDAO readerDAO;
    private BorrowSlipDAO borrowSlipDAO;
    private DocumentBorrowDAO documentBorrowDAO;
    private CopyOfDocumentDAO copyOfDocumentDAO; // <-- THÊM DAO NÀY

    @Override
    public void init() throws ServletException {
        super.init();
        this.readerDAO = new ReaderDAO();
        this.borrowSlipDAO = new BorrowSlipDAO();
        this.documentBorrowDAO = new DocumentBorrowDAO();
        this.copyOfDocumentDAO = new CopyOfDocumentDAO(); // <-- KHỞI TẠO DAO
    }

    /**
     * HIỂN THỊ TRANG CHI TIẾT SÁCH ĐANG MƯỢN
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            // 1. Lấy readerId từ URL (vd: /process-return?readerId=1)
            int readerId = Integer.parseInt(request.getParameter("readerId"));

            // 2. Lấy thông tin bạn đọc (để hiển thị header)
            Reader reader = readerDAO.getFullInfoReader(readerId);

            // 3. Lấy danh sách các phiếu đang mượn (status='borrowing')
            List<BorrowSlip> activeSlips = borrowSlipDAO.getActiveBorrowSlips(readerId);

            // 4. Tạo danh sách (phẳng) TẤT CẢ tài liệu
            List<DocumentBorrow> allBorrowedItems = new ArrayList<>();

            // 5. Lặp qua từng phiếu mượn (Logic N+1)
            for (BorrowSlip slip : activeSlips) {
                // Lấy các sách con của phiếu này
                List<DocumentBorrow> itemsInThisSlip =
                        documentBorrowDAO.getListBorrowedDocument(slip.getId());
                allBorrowedItems.addAll(itemsInThisSlip);
            }

            // 6. Đặt 2 thuộc tính riêng biệt vào request cho JSP
            request.setAttribute("reader", reader); // <-- Thông tin Bạn đọc
            request.setAttribute("borrowedList", allBorrowedItems); // <-- Danh sách sách

            // 7. Chuyển tiếp đến trang JSP chi tiết
            // (Sử dụng tên file JSP mà bạn đã cung cấp)
            RequestDispatcher dispatcher = request.getRequestDispatcher("DocumentBorrowDetail.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Reader ID không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    /**
     * XỬ LÝ KHI NHẤN NÚT "XÁC NHẬN TRẢ SÁCH"
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            // 1. Lấy dữ liệu từ form
            int readerId = Integer.parseInt(request.getParameter("readerId"));
            Librarian librarian = (Librarian) request.getSession().getAttribute("librarian");

            // ... (Kiểm tra librarian != null như cũ) ...

            String[] selectedBorrowIds = request.getParameterValues("selected_item_id");
            String[] damagedBorrowIds = request.getParameterValues("damaged_item_id");
            List<String> damagedList = (damagedBorrowIds != null) ? Arrays.asList(damagedBorrowIds) : new ArrayList<>();

            if (selectedBorrowIds == null || selectedBorrowIds.length == 0) {
                response.sendRedirect("process-return?readerId=" + readerId + "&error=NoItemsSelected");
                return;
            }

            List<DocumentBorrow> itemsForConfirmPage = new ArrayList<>();
            double totalFine = 0; // Tổng tiền phạt (bắt đầu bằng 0)

            for (String borrowIdStr : selectedBorrowIds) {
                int borrowId = Integer.parseInt(borrowIdStr);

                DocumentBorrow item = documentBorrowDAO.getBorrowedDocument(borrowId);
                if (item == null) continue;

                totalFine += item.getProvisionalFine();
                itemsForConfirmPage.add(item);

                String newStatus = "good";
                if (damagedList.contains(borrowIdStr)) {
                    newStatus = "damaged";

                    double damageFine = this.documentBorrowDAO.getDamageFineAmount(); // <--- MỚI

                    // 2. Cộng vào TỔNG PHẠT
                    totalFine += damageFine; // <--- MỚI

                    // 3. Cộng vào "Phí tạm tính" CỦA RIÊNG MỤC NÀY
                    //    để JSP mới hiển thị đúng
                    item.setProvisionalFine(item.getProvisionalFine() + damageFine); // <--- MỚI
                    // === KẾT THÚC LOGIC MỚI ===
                }

                // Cập nhật CSDL
                CopyOfDocument copyd = new CopyOfDocument();
                copyd.setId(borrowId);
                copyd.setStatus(newStatus);
                copyOfDocumentDAO.updateStatus(copyd);
            }

            // 3. Lấy thông tin bạn đọc
            Reader reader = readerDAO.getFullInfoReader(readerId);

            // 4. Đặt thuộc tính và chuyển tiếp sang trang JSP "Phiếu Trả"
            // (totalFine và returnedItemsList giờ đã chứa tiền phạt hỏng)
            request.setAttribute("reader", reader);
            request.setAttribute("librarian", librarian);
            request.setAttribute("returnedItemsList", itemsForConfirmPage);
            request.setAttribute("totalFine", totalFine); // <--- Đã cập nhật

            RequestDispatcher dispatcher = request.getRequestDispatcher("ReturnSlip.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Lỗi khi xử lý trả sách", e);
        }
    }}