package io.github.medioqrity;

import java.net.URLEncoder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteImage")
public class DeleteImageServlet extends HttpServlet {

    private static final long serialVersionUID = -4306021838659005877L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            response.sendRedirect(
                getServletContext().getContextPath() + 
                "/query?keyword=" + 
                URLEncoder.encode(
                    (String) request.getSession().getAttribute("keyword"), "UTF-8"
                ) +
                "&page=" + (request.getParameter("page") == null ? 0 : Integer.parseInt(request.getParameter("page")))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}