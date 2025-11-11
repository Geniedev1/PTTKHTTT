package labjava.servlet;
import labjava.dao.ReaderDAO;
import labjava.model.Reader;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Reader r = new Reader();
        r.setUsername(req.getParameter("username"));
        r.setEmail(req.getParameter("email"));
        r.setBirthDate(req.getParameter("birth_date"));
        r.setPhone(req.getParameter("phone"));
        boolean ok = new ReaderDAO().insert(r);
        resp.sendRedirect(ok ? "login.jsp" : "register.jsp?error=1");
    }
}
