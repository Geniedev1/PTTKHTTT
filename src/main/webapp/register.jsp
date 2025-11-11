<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký tài khoản</title>
    <style>
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background: linear-gradient(135deg, #4f46e5, #3b82f6);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .register-container {
            background: #fff;
            padding: 40px 50px;
            border-radius: 16px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.15);
            width: 380px;
            text-align: center;
        }

        .register-container h2 {
            margin-bottom: 24px;
            color: #1e3a8a;
        }

        .form-group {
            text-align: left;
            margin-bottom: 18px;
        }

        label {
            display: block;
            margin-bottom: 6px;
            color: #374151;
            font-weight: 500;
        }

        input {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            outline: none;
            font-size: 14px;
            transition: all 0.2s;
        }

        input:focus {
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37,99,235,0.2);
        }

        button {
            width: 100%;
            background-color: #2563eb;
            border: none;
            color: white;
            font-size: 16px;
            font-weight: 600;
            padding: 12px 0;
            border-radius: 8px;
            cursor: pointer;
            transition: background 0.3s;
        }

        button:hover {
            background-color: #1d4ed8;
        }

        .footer-text {
            margin-top: 16px;
            font-size: 13px;
            color: #6b7280;
        }

        .footer-text a {
            color: #2563eb;
            text-decoration: none;
        }

        .footer-text a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="register-container">
    <h2>Tạo tài khoản mới</h2>
    <form action="register" method="post">
        <div class="form-group">
            <label>Tên đăng nhập</label>
            <input name="username" placeholder="Nhập tên người dùng">
        </div>
        <div class="form-group">
            <label>Mật khẩu</label>
            <input type="password" name="password" placeholder="Nhập mật khẩu">
        </div>
        <div class="form-group">
            <label>Email</label>
            <input name="email" type="email" placeholder="Nhập email">
        </div>
        <div class="form-group">
            <label>Ngày sinh</label>
            <input name="ngày sinh" type="text" placeholder="Nhập ngày sinh">
        </div>
        <div class="form-group">
            <label>Điện thoại</label>
            <input name="phone" type="text" placeholder="Nhập phone">
        </div>
        <button>Đăng ký</button>
    </form>
    <div class="footer-text">
        Đã có tài khoản? <a href="login.jsp">Đăng nhập ngay</a>
    </div>
</div>
</body>
</html>
