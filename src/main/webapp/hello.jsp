<%--
  Created by IntelliJ IDEA.
  User: victoryw
  Date: 2019-03-08
  Time: 14:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><%= request.getAttribute("key") %></title>
</head>
<body>
    <h1><%= response.hashCode() %></h1>

    <jsp:include page="/servlet/com.victoryw.picc.HelloServlet" />
    <h1>
        <%= request.getAttribute("key") %>
        <%= request.getAttribute("username") %>
    </h1>
    <form action="/servlet/PostServlet" method="post">
        First name: <input type="text" name="fname"><br>
        Last name: <input type="text" name="lname"><br>
        <input type="submit" value="Submit">
    </form>

    <form action="/servlet/PostServlet" method="get">
        First name: <input type="text" name="fname"><br>
        Last name: <input type="text" name="lname"><br>
        <input type="submit" value="Submit">
    </form>


</body>
</html>
