<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList, java.util.List"%>

<% List<String> ids = (LinkedList<String>) session.getAttribute("ids"); %>
<% List<Double> dists = (LinkedList<Double>) session.getAttribute("dists"); %>
<% List<String> imgs = (LinkedList<String>) session.getAttribute("imgs"); %>

<table>
    <tr>
        <th>ID</th>
        <th>照片</th>
        <th>Distance</th>
    </tr>
    <% for (int i = 0; i < ids.size(); ++i) { %>
        <tr>
            <td><%= ids.get(i) %></td>
            <td><%= imgs.get(i) %></td>
            <td><%= dists.get(i) %></td>
        </tr>
    <% } %>
</table>

<p><a href="index.jsp">Back to homepage</a></p>