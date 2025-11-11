<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Thêm 2 thư viện taglib của JSTL --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="labjava.model.Librarian" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
    Librarian l = (Librarian) session.getAttribute("librarian");
    String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
%>
<html>
<head>
    <div class="user-box">
        <div><%= (l != null) ? l.getUsername() : "Guest" %></div>
        <div><%= today %></div>
    </div>
    <title>Trả tài liệu</title>
    <%-- CSS đơn giản để bảng trông giống ảnh --%>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        .btn { padding: 5px 10px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; }
        .status-locked { color: red; font-weight: bold; }
        .status-active { color: green; }
    </style>
</head>
<body>

<h2>Trả tài liệu</h2>

<%--
  FORM TÌM KIẾM
  - action trỏ đến URL của Servlet (@WebServlet)
  - method="GET" để việc tìm kiếm hiển thị trên URL (tốt cho bookmark)
--%>
<form action="return-search" method="GET">
    Tìm bạn đọc:
    <input type="text" name="searchCode" value="${requestScope.lastSearch}" placeholder="Nhập mã bạn đọc"/>
    <button type="submit">Tìm</button>
</form>

<hr>

<h3>Quẹt thẻ bạn đọc</h3>
<table>
    <thead>
    <tr>
        <th>STT</th>
        <th>Mã bạn đọc</th>
        <th>Họ và tên</th>
        <th>Số tài liệu đang mượn</th>
        <th>Nợ phạt (vnd)</th>
        <th>Trạng thái thẻ</th>
        <th>Chức năng</th>
    </tr>
    </thead>
    <tbody>
    <%--
      Dùng JSTL <c:forEach> để lặp qua "readerList"
      mà Servlet đã gửi sang
    --%>
    <c:forEach var="readerMap" items="${requestScope.readerList}" varStatus="loop">
        <tr>
            <td>${loop.count}</td>

                <%--
                  Vì dùng Map, chúng ta truy cập bằng key
                  Ví dụ: readerMap.readerCode
                --%>
            <td>${readerMap.readerCode}</td>
            <td>${readerMap.fullName}</td>
            <td>${readerMap.borrowedCount}</td>

                <%-- Dùng fmt:formatNumber để định dạng số tiền --%>
            <td>
                <fmt:formatNumber value="${readerMap.fineDebt}" type="number" />
            </td>

                <%--
                  Xử lý logic "Trạng thái thẻ" dựa trên nợ phạt
                  (Giả sử nợ > 100.000 thì "Khóa")
                --%>
            <td>
                <c:choose>
                    <c:when test="${readerMap.fineDebt > 100000}">
                        <span class="status-locked">Khóa</span>
                    </c:when>
                    <c:otherwise>
                        <span class="status-active">Còn hạn</span>
                    </c:otherwise>
                </c:choose>
            </td>

            <td>
                    <%-- Nút "Trả" sẽ link đến một servlet khác, mang theo id của bạn đọc --%>
                <a href="process-return?readerId=${readerMap.readerId}" class="btn">Trả</a>
            </td>
        </tr>
    </c:forEach>

    <%-- Xử lý trường hợp không tìm thấy kết quả --%>
    <c:if test="${empty requestScope.readerList}">
        <tr>
            <td colspan="7" style="text-align: center;">Không tìm thấy bạn đọc nào.</td>
        </tr>
    </c:if>

    </tbody>
</table>

</body>
</html>