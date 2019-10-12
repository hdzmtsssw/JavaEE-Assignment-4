package io.github.medioqrity;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = -7530856378200094282L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            session.removeAttribute("username");
            session.removeAttribute("admin");
        }
        assert session.getAttribute("username") == null;
        if (session.getAttribute("next") != null) {
            try {
                request.getRequestDispatcher((String) session.getAttribute("next")).forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}