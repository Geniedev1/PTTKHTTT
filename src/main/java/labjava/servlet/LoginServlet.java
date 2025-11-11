package labjava.servlet;

import labjava.dao.ReaderDAO;
import labjava.dao.LibrarianDAO;
import labjava.model.Member;
import labjava.model.Reader;
import labjava.model.Librarian;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Bước 1: Tìm trong member/reader
        Reader r = new ReaderDAO().findByLogin(username, password);

        if (r == null) {
            resp.sendRedirect("login.jsp?error=1"); // sai tài khoản hoặc mật khẩu
            return;
        }

        HttpSession session = req.getSession();

        // Bước 2: Kiểm tra role dựa trên reader_id trong member
        if (r.getId() != 0) { // Có reader_id → là bạn đọc
            session.setAttribute("reader", r);
            resp.sendRedirect("LibraryHome.jsp");

        } else  { // Không có reader_id → là thủ thư
            Librarian l = new LibrarianDAO().findByLogin(username, password);
            session.setAttribute("librarian", l);
            resp.sendRedirect("LibraryManage.jsp");

        }
    }
}
