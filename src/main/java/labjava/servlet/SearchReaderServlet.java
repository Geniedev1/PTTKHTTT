package labjava.servlet;

import labjava.dao.ReaderDAO; // Import DAO của bạn

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class SearchReaderServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String searchCode = request.getParameter("searchCode");

        ReaderDAO readerDAO = new ReaderDAO();
        List<Map<String, Object>> readerList;

        // 2. Gọi DAO
        if (searchCode == null || searchCode.trim().isEmpty()) {
            // Nếu không tìm kiếm gì (tải trang lần đầu), lấy tất cả
            readerList = readerDAO.getAllReader();
        } else {
            // Nếu có tìm kiếm, gọi hàm search
            readerList = readerDAO.searchReader(searchCode);
        }

        // 3. Đặt kết quả vào request để gửi sang JSP
        request.setAttribute("readerList", readerList);

        // Ghi lại từ khóa tìm kiếm để hiển thị lại trên ô input
        request.setAttribute("lastSearch", searchCode);

        // 4. Chuyển tiếp (forward) đến file JSP
        // Đổi "searchReader.jsp" thành tên file JSP của bạn
        RequestDispatcher dispatcher = request.getRequestDispatcher("ReaderSearch.jsp");
        dispatcher.forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển toàn bộ xử lý sang doGet
        doGet(request, response);
    }
}