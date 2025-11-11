package labjava.servlet;

import labjava.dao.ReturnSlipDAO;
import labjava.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BorrowReturnServlet extends HttpServlet {

    private ReturnSlipDAO returnSlipDAO;

    @Override
    public void init() throws ServletException {
        this.returnSlipDAO = new ReturnSlipDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int readerId = Integer.parseInt(request.getParameter("readerId"));
            int librarianId = Integer.parseInt(request.getParameter("librarianId"));
            double totalFine = Double.parseDouble(request.getParameter("totalFine"));
            String[] returnedBorrowIds = request.getParameterValues("returned_borrow_id");

            double lateFineAmount = 0; // Tạm
            double damageFineAmount = 0; // Tạm
            int damagedCount = 0; // Tạm

            if(totalFine > 0) lateFineAmount = totalFine;
//            private int id;
//            private float totalFine; // "tongPhat"
//            private Reader reader;
//            private Librarian librarian;
//            private List<DocumentReturn> documentReturns;
//            private List<FineDetail> fineDetails;
            FineDetail fineDetail = new FineDetail();
            Fine fine = new Fine();
            fine.setFine(lateFineAmount);
            fineDetail.setFine(fine);
            fineDetail.setQuantity(damagedCount);
            ReturnSlip returnSlip = new ReturnSlip();
            returnSlip.getFineDetails().add(fineDetail);
            for(int i =0;i < returnedBorrowIds.length;i++) {
                DocumentReturn dr = new DocumentReturn();
                dr.setDocumentBorrowId(returnedBorrowIds[i]);
                returnSlip.getDocumentReturns().add(dr);
            }
            Reader reader = new Reader();
            reader.setId(readerId);
            returnSlip.setReader(reader);
            Librarian librarian = new Librarian();
            librarian.setId(librarianId);
            returnSlip.setLibrarian(librarian);
            returnSlip.setTotalFine(totalFine);
            int success = returnSlipDAO.createReturnSlip(returnSlip);

            if (success != -1) {
                if (totalFine > 0) {

                    response.sendRedirect("payment.jsp?slipId=" + success + "&amount=" + totalFine);
                } else {

                    response.sendRedirect("librarian_home.jsp?return=success");
                }
            } else {
                response.sendRedirect("returnslip_confirm.jsp?error=CreationFailed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Lỗi khi tạo phiếu trả", e);
        }
    }
}