<%@ page import="com.theah64.pigeon.database.tables.Users" %>
<%@ page import="com.theah64.pigeon.models.User" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: theapache64
  Date: 8/12/16
  Time: 6:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>API Status</title>
    <%@include file="common_headers.jsp" %>
</head>
<body>

<div class="container">

    <h2>Pigeon statistics</h2>
    <p>Live status</p>

    <table class="table table-striped table-bordered table-hover">
        <thead>
        <tr>
            <td>Name</td>
            <td>IMEI</td>
            <td></td>
        </tr>
        </thead>
        <tbody>

        <%
            final List<User> users = Users.getInstance().getAll(Users.COLUMN_IS_ACTIVE, Users.TRUE);
            if (users != null) {
                for (final User user : users) {
        %>
        <tr>
            <td><%=user.getName()%>
            </td>
            <td><%=user.getIMEI()%>
            </td>
            <td><%=user.hasFcm() ? "<a href=\"find/" + user.getIMEI() + "\"> Track location </a>" : "-"%>
            </td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>

</div>
</body>
</html>
