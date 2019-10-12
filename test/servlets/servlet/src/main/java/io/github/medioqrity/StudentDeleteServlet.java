package io.github.medioqrity;

import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StudentDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 6213850251654820379L;
    private StudentDAO studentDAO;

    public void init() {
        studentDAO = (StudentDAO) getServletContext().getAttribute("studentDAO");
        if (studentDAO == null) {
            try {
                studentDAO = new StudentDAO();
                getServletContext().setAttribute("studentDAO", studentDAO);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            studentDAO.delete(request.getParameter("id"));
            request.getSession().setAttribute("updated", true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.sendRedirect(
                    getServletContext().getContextPath() + 
                    "/query?keyword=" + 
                    URLEncoder.encode(
                        (String) request.getSession().getAttribute("keyword"), "UTF-8"
                    ) +
                    "&page=" + request.getParameter("page")
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}