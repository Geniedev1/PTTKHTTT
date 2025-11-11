package labjava.servlet;

import labjava.dao.ReaderDAO;
import labjava.dao.BorrowSlipDAO;
import labjava.dao.DocumentBorrowDAO;
import labjava.dao.CopyOfDocumentDAO;

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


@WebServlet(name = "DocumentServlet", urlPatterns = {"/process-return"})
public class DocumentServlet extends HttpServlet {

    private ReaderDAO readerDAO;
    private BorrowSlipDAO borrowSlipDAO;
    private DocumentBorrowDAO documentBorrowDAO;
    private CopyOfDocumentDAO copyOfDocumentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.readerDAO = new ReaderDAO();
        this.borrowSlipDAO = new BorrowSlipDAO();
        this.documentBorrowDAO = new DocumentBorrowDAO();
        this.copyOfDocumentDAO = new CopyOfDocumentDAO();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int readerId = Integer.parseInt(request.getParameter("readerId"));

            Reader reader = readerDAO.getFullInfoReader(readerId);

            List<BorrowSlip> activeSlips = borrowSlipDAO.getActiveBorrowSlips(readerId);

            List<DocumentBorrow> allBorrowedItems = new ArrayList<>();

            for (BorrowSlip slip : activeSlips) {
                List<DocumentBorrow> itemsInThisSlip =
                        documentBorrowDAO.getListBorrowedDocument(slip.getId());
                allBorrowedItems.addAll(itemsInThisSlip);
            }

            request.setAttribute("reader", reader);
            request.setAttribute("borrowedList", allBorrowedItems);

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


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int readerId = Integer.parseInt(request.getParameter("readerId"));
            Librarian librarian = (Librarian) request.getSession().getAttribute("librarian");


            String[] selectedBorrowIds = request.getParameterValues("selected_item_id");
            String[] damagedBorrowIds = request.getParameterValues("damaged_item_id");
            List<String> damagedList = (damagedBorrowIds != null) ? Arrays.asList(damagedBorrowIds) : new ArrayList<>();

            if (selectedBorrowIds == null || selectedBorrowIds.length == 0) {
                response.sendRedirect("process-return?readerId=" + readerId + "&error=NoItemsSelected");
                return;
            }

            List<DocumentBorrow> itemsForConfirmPage = new ArrayList<>();
            double totalFine = 0;

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

                    totalFine += damageFine;


                    item.setProvisionalFine(item.getProvisionalFine() + damageFine); // <--- MỚI
                }

                CopyOfDocument copyd = new CopyOfDocument();
                copyd.setId(borrowId);
                copyd.setStatus(newStatus);
                copyOfDocumentDAO.updateStatus(copyd);
            }

            Reader reader = readerDAO.getFullInfoReader(readerId);

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