<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="labjava.dao.ReaderDAO, labjava.model.Reader" %>

<%
  int readerId = Integer.parseInt(request.getParameter("readerId"));
  ReaderDAO dao = new ReaderDAO();
  Reader r = dao.findFullByReaderId(readerId);
%>

<h2>Xác nhận thông tin bạn đọc</h2>
<ul>
  <li>Username: <%= r.getUsername() %></li>
  <li>Email: <%= r.getEmail() %></li>
  <li>Phone: <%= r.getPhone() %></li>
  <li>Birth date: <%= r.getBirthDate() %></li>
  <li>Full name: <%= r.getName().getSurname() %> <%= r.getName().getMiddleName() %> <%= r.getName().getGivenName() %></li>
  <li>Địa chỉ:
    <%= r.getAddress().getHouseNumber() %>,
    <%= r.getAddress().getStreetNumber() %>,
    <%= r.getAddress().getWard() %>,
    <%= r.getAddress().getDistrict() %>,
    <%= r.getAddress().getProvince() %>
  </li>
</ul>

<form action="submitConfirm" method="post">
  <input type="hidden" name="readerId" value="<%= r.getId() %>">
  <button type="submit">Xác nhận</button>
</form>
