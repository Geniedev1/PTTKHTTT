<%--
  Created by IntelliJ IDEA.
  User: dongocminh
  Date: 29/10/2025
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Thư Viện</title>

    <!-- Bootstrap để tạo nút & bố cục đẹp -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #f6f7fb;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        .hero-card {
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 8px 30px rgba(0,0,0,0.1);
            padding: 40px 60px;
            text-align: center;
        }
        .hero-card h1 {
            font-weight: 700;
            font-size: 32px;
            margin-bottom: 20px;
        }
        .arrow-down {
            font-size: 60px;
            color: #007bff;
            margin-top: 40px;
            animation: bounce 1.5s infinite;
        }
        @keyframes bounce {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(10px); }
        }
    </style>
</head>
<body>

<div class="hero-card">
    <h1>Trang Thư Viện</h1>
    <!-- Nút đăng ký -->
    <p>
        <a href="${pageContext.request.contextPath}/reader">
            Xem thông tin bạn đọc
        </a>
    </p>
</div>


</body>
</html>
