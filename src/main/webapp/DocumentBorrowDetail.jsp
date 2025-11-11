<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>

<% request.setAttribute("today", new Date()); %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Danh sách tài liệu mượn</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
      max-width: 900px;
      margin: 20px auto;
    }
    .header-info {
      margin-bottom: 20px;
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
      background-color: #28a745; /* Màu xanh lá */
      color: white;
      text-decoration: none;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      display: inline-block;
    }
    .btn:hover {
      background-color: #218838;
    }
    .overdue {
      color: #d9534f; /* Màu đỏ */
      font-weight: bold;
    }
    .ontime {
      color: #5cb85c; /* Màu xanh lá */
    }
    .text-center {
      text-align: center;
    }
    /* Căn giữa cho cột checkbox */
    td.checkbox-cell {
      text-align: center;
    }
  </style>
</head>
<body>

<div class="user-info">
  <%-- Giả sử bạn đã lưu Librarian trong session khi đăng nhập --%>
  <c:if test="${not empty sessionScope.librarian}">
    <div>${sessionScope.librarian.username}</div>
  </c:if>
  <div><fmt:formatDate value="${today}" pattern="dd/MM/yyyy" /></div>
</div>

<h2>Danh sách tài liệu mượn</h2>

<c:if test="${not empty reader}">
  <div class="header-info">
    <div><strong>Họ và tên:</strong> ${reader.fullName}</div>
    <div><strong>Mã thẻ:</strong> ${reader.username}</div>
    <div><strong>Ngày sinh:</strong> ${reader.birthDate}</div>
  </div>
</c:if>

<%--
  FORM: Bao bọc toàn bộ bảng
  - action trỏ đến URL của DocumentServlet ("/process-return")
  - method="POST" để kích hoạt hàm doPost
--%>
<form action="process-return" method="POST">

  <%-- Gửi kèm readerId (ẩn) để doPost biết đang xử lý cho ai --%>
  <input type="hidden" name="readerId" value="${reader.id}" />

  <table>
    <thead>
    <tr>
      <th>STT</th>
      <th>Mã tài liệu</th>
      <th>Tên tài liệu</th>
      <th>Hạn trả</th>
      <th>Quá hạn</th>
      <th>Phí tạm tính (vnd)</th>
      <th>Chọn (Trả)</th>
      <th>Báo Hỏng?</th>
    </tr>
    </thead>
    <tbody>
    <%-- Lặp qua danh sách 'borrowedList' mà servlet gửi sang --%>
    <c:forEach var="item" items="${requestScope.borrowedList}" varStatus="loop">
      <tr>
        <td>${loop.count}</td>
        <td>${item.copyCode}</td>
        <td>${item.title}</td>
        <td>
          <fmt:formatDate value="${item.dueDate}" pattern="dd/MM/yyyy" />
        </td>
        <td>
                            <span class="${item.overdueStatus == 'Quá hạn' ? 'overdue' : 'ontime'}">
                                ${item.overdueStatus}
                            </span>
        </td>
        <td>
          <fmt:formatNumber value="${item.provisionalFine}" type="number" />
        </td>

        <td class="checkbox-cell">
            <%-- Checkbox để CHỌN SÁCH MUỐN TRẢ --%>
            <%-- Tên (name) phải khớp với servlet: "selected_item_id" --%>
          <input type="checkbox" name="selected_item_id" value="${item.borrowId}">
        </td>

        <td class="checkbox-cell">
            <%-- Checkbox để BÁO SÁCH BỊ HỎNG --%>
            <%-- Tên (name) phải khớp với servlet: "damaged_item_id" --%>
          <input type="checkbox" name="damaged_item_id" value="${item.borrowId}">
        </td>
      </tr>
    </c:forEach>

    <%-- Xử lý khi bạn đọc không mượn sách nào --%>
    <c:if test="${empty requestScope.borrowedList}">
      <tr>
        <td colspan="8" class="text-center">Bạn đọc này không mượn tài liệu nào.</td>
      </tr>
    </c:if>
    </tbody>
  </table>

  <br>

  <div class="text-center">
    <button type="submit" class="btn">
      Xác nhận trả sách
    </button>
  </div>

</form>

</body>
</html>