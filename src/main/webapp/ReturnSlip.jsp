<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>

<%--
  Đặt 'today' vào scope để sử dụng,
  vì JSTL không thể tạo new Date()
--%>
<% request.setAttribute("today", new Date()); %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xác nhận Phiếu Trả</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            max-width: 800px;
            margin: 20px auto;
            border: 1px solid #ccc;
            padding: 24px;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        }
        .header-info {
            margin-bottom: 20px;
            border-bottom: 1px dashed #ccc;
            padding-bottom: 15px;
        }
        .header-info div {
            margin-bottom: 5px;
            font-size: 1.1rem;
        }
        .user-info {
            float: right;
            text-align: right;
            color: #555;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
            font-weight: 600;
        }
        .btn {
            padding: 10px 25px;
            font-size: 1.1rem;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            display: inline-block;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .text-center {
            text-align: center;
        }
        .total-fine {
            font-weight: bold;
            color: #d9534f; /* Màu đỏ cho tiền phạt */
            font-size: 1.2rem;
        }
    </style>
</head>
<body>

<div class="user-info">
    <%-- Giả sử Librarian có getUsername() hoặc getFullName() --%>
    <div>${librarian.username}</div>
    <div><fmt:formatDate value="${today}" pattern="dd/MM/yyyy" /></div>
</div>

<h2>Phiếu Trả</h2>

<c:if test="${not empty reader}">
    <div class="header-info">
        <div><strong>Họ và tên:</strong> ${reader.fullName}</div>
        <div><strong>Mã thẻ:</strong> ${reader.username}</div>
        <div><strong>Ngày sinh:</strong> ${reader.birthDate}</div>
        <div><strong>Ngày trả:</strong> <fmt:formatDate value="${today}" pattern="dd/MM/yyyy" /></div>
        <div class="total-fine">
            <strong>Tổng phạt:</strong>
            <fmt:formatNumber value="${totalFine}" type="number" /> vnd
        </div>
        <div><strong>Thủ thư:</strong> ${librarian.username}</div>
    </div>
</c:if>

<%--
  FORM CUỐI CÙNG
  Form này sẽ gửi đến một Servlet MỚI (chưa tạo), ví dụ: 'CreateReturnSlipServlet'
  để lưu thông tin phiếu trả (tbl_return_slip) vào CSDL.
--%>
<form action="create-return-slip" method="POST">

    <%--
      Gửi các thông tin quan trọng này một cách "ẩn"
      để Servlet tiếp theo có thể nhận và lưu vào CSDL
    --%>
    <input type="hidden" name="readerId" value="${reader.id}" />
    <input type="hidden" name="librarianId" value="${librarian.id}" />
    <input type="hidden" name="totalFine" value="${totalFine}" />

    <%-- Gửi danh sách ID của các mục đã trả --%>
    <c:forEach var="item" items="${requestScope.returnedItemsList}">
        <input type="hidden" name="returned_borrow_id" value="${item.borrowId}" />
    </c:forEach>

    <table>
        <thead>
        <tr>
            <th>STT</th>
            <th>Mã tài liệu</th>
            <th>Tên tài liệu</th>
            <th>Quá hạn</th>
            <th>Phí tạm tính (vnd)</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${requestScope.returnedItemsList}" varStatus="loop">
            <tr>
                <td>${loop.count}</td>
                <td>${item.copyCode}</td>
                <td>${item.title}</td>
                <td>${item.overdueStatus}</td>
                <td>
                    <fmt:formatNumber value="${item.provisionalFine}" type="number" />
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <br>

    <div class="text-center">
        <button type="submit" class="btn">
            Xác nhận
        </button>
    </div>

</form>

</body>
</html>