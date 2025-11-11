package labjava.servlet;

import labjava.dao.ReaderCardDAO;
import labjava.dao.ReaderDAO;
import labjava.model.Reader;
import labjava.model.ReaderCard;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
public class ReaderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("reader") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        // ✅ Lấy reader hiện tại trong session
        Reader current = (Reader) session.getAttribute("reader");

        // ✅ Gọi DAO để lấy đầy đủ thông tin (JOIN name, address, ...)
        Reader fullInfo = new ReaderDAO().getFullInfoReader(current.getId());
        session.setAttribute("readerFull", fullInfo);

        System.out.println(fullInfo);
        // ✅ Truyền sang JSP hiển thị
        req.setAttribute("readerFull", fullInfo);
        req.getRequestDispatcher("/ReaderCard.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        System.out.println("[ReaderServlet] POST /reader (tạo thẻ)");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("reader") == null) {
            resp.sendRedirect("login.jsp?error=expired");
            return;
        }

        Reader current = (Reader) session.getAttribute("reader");
        int readerId = current.getId();

        // ✅ Gọi DAO để thêm thẻ bạn đọc
        ReaderCardDAO cardDAO = new ReaderCardDAO();
        ReaderCard readerCard = new ReaderCard();
        readerCard.setId(readerId);
        readerCard.setStatus("active");
        boolean ok = cardDAO.CreateReaderCard(readerCard);

        if (ok) {
            req.setAttribute("message", "✅ Tạo thẻ bạn đọc thành công!");
        } else {
            req.setAttribute("message", "❌ Lỗi khi tạo thẻ bạn đọc!");
        }

        // ✅ Gọi lại doGet() để hiển thị lại thông tin và thông báo
        doGet(req, resp);
    }
}
