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
    <h1>
        <%= request.getAttribute("key") %>
    </h1>
</body>
</html>
