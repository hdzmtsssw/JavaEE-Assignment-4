<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset = "UTF-8">
</head>
<body>
    <% 
    if (session.getAttribute("username") != null) 
        out.println("<p>Hello, " + (String) (session.getAttribute("username")) + "! <a href='logout'>Log out</a></p>");
    else
        out.println("<p><a href='login.html'>Sign in</a></p>");
    %>


    <form action = 'query'>
        Enter name or something else: <input type = 'text' name = 'keyword' />
        <input type = 'submit' value = 'search' />
    </form>
    <br>
    <form action = 'ImageSearch' method='post' enctype='multipart/form-data'>
        Or search by image: <input type = 'file' name = 'file' />
        <input type = 'submit' value = 'search' />
    </form>

</body>
</html>