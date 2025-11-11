<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.text.SimpleDateFormat, java.util.Date" %>
<%@ page import="labjava.model.Librarian" %>
<%
    Librarian l = (Librarian) session.getAttribute("librarian");
    String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Trang quản lý thư viện</title>
    <style>
        /* ... CSS của bạn ... */
    </style>
</head>
<body>

<div class="lib-card">
    <div class="user-box">
        <div><%= (l != null) ? l.getUsername() : "Guest" %></div>
        <div><%= today %></div>
    </div>

    <h1 class="title">Trang quản lý thư viện</h1>

    <div class="center-box">
        <%--
          SỬA Ở ĐÂY:
          - Đổi "LibraryManage.jsp" thành URL của servlet.
          - Dùng ${pageContext.request.contextPath} để đảm bảo link luôn đúng
            ngay cả khi bạn triển khai ứng dụng trên một đường dẫn phụ.
        --%>
        <a class="btn btn-primary btn-main"
           href="${pageContext.request.contextPath}/return-search">
            Trả tài liệu
        </a>
    </div>
</div>

</body>
</html>