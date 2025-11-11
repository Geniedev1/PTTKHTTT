<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán Phí</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 500px; margin: 40px auto; border: 1px solid #ccc; padding: 20px; border-radius: 8px; }
        .info { margin-bottom: 20px; }
        .amount { font-size: 1.5rem; font-weight: bold; color: #d9534f; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: 600; }
        select, button { width: 100%; padding: 10px; border-radius: 5px; border: 1px solid #ccc; }
        button { background-color: #007bff; color: white; cursor: pointer; font-size: 1.1rem; }
    </style>
</head>
<body>

<h2>Thanh toán Phí</h2>

<div class="info">
    <div>Tổng số tiền cần thanh toán:</div>
    <div class="amount">
        <%-- Đọc "amount" từ URL (do ReturnSlipServlet gửi qua) --%>
        <fmt:formatNumber value="${param.amount}" type="number" /> vnd
    </div>
</div>

<%-- Thông báo lỗi (nếu có) --%>
<c:if test="${not empty param.error}">
    <div style="color: red; margin-bottom: 15px;">
        Thanh toán thất bại. Vui lòng thử lại!
    </div>
</c:if>

<form action="process-payment" method="POST">

    <%-- Gửi ẩn 2 giá trị này để Servlet nhận được --%>
    <input type="hidden" name="slipId" value="${param.slipId}">
    <input type="hidden" name="amount" value="${param.amount}">

    <div class="form-group">
        <label for="paymentMethod">Phương thức thanh toán:</label>
        <select id="paymentMethod" name="paymentMethod">
            <option value="Cash">Tiền mặt (Cash)</option>
            <option value="Card">Thẻ (Card)</option>
            <option value="Transfer">Chuyển khoản (Transfer)</option>
        </select>
    </div>

    <button type="submit">Xác nhận Thanh toán</button>
</form>

</body>
</html>