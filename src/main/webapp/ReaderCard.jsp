<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="labjava.model.Reader, labjava.model.Name, labjava.model.Address" %>
<%
    Reader r = (Reader) request.getAttribute("readerFull");
    if (r == null) r = (Reader) session.getAttribute("reader");
    if (r == null) {
        response.sendRedirect("login.jsp?error=expired");
        return;
    }
    Name n = r.getName();
    Address a = r.getAddress();
%>

<h2>Thông tin bạn đọc</h2>
<ul>
    <li>Username: <%= r.getUsername() %></li>
    <li>Email: <%= r.getEmail() %></li>
    <li>Phone: <%= r.getPhone() %></li>
    <li>Birth date: <%= r.getBirthDate() %></li> <!-- ✅ thêm dấu > -->

    <!-- Tên đầy đủ -->
    <li>Full name:
        <%= (n != null)
                ? ( (n.getSurname()!=null?n.getSurname():"") + " "
                + (n.getMiddleName()!=null?n.getMiddleName():"") + " "
                + (n.getGivenName()!=null?n.getGivenName():"") ).trim()
                : "(chưa cập nhật)" %>
    </li>

    <!-- Địa chỉ chi tiết -->
    <li>Địa chỉ:
        <%
            if (a != null) {
                String addr =
                        (a.getHouseNumber()!=null ? a.getHouseNumber() : "") +
                                (a.getBuilding()!=null ? ", " + a.getBuilding() : "") +
                                (a.getStreetNumber()!=null ? ", " + a.getStreetNumber() : "") +
                                (a.getWard()!=null ? ", " + a.getWard() : "") +
                                (a.getDistrict()!=null ? ", " + a.getDistrict() : "") +
                                (a.getProvince()!=null ? ", " + a.getProvince() : "");
                out.print(addr.replaceAll("^,\\s*|\\s*,\\s*$", "")); // loại dấu phẩy thừa đầu/cuối
            } else {
                out.print("(chưa cập nhật)");
            }
        %>
    </li>

</ul>
<form action="confirm.jsp" method="get">
    <!-- Ẩn ID để confirm.jsp biết người nào -->
    <input type="hidden" name="readerId" value="<%= r.getId() %>">
    <button type="submit">Xác nhận thông tin</button>
</form>