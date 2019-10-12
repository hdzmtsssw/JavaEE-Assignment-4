package io.github.medioqrity;

import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StudentInsertServlet extends HttpServlet {

    private static final long serialVersionUID = -7070743916429864363L;
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
        String name = request.getParameter("n");
        String id = request.getParameter("id");
        String phone = request.getParameter("p");
        String qq = request.getParameter("q");
        String mail = request.getParameter("m");
        try {
            studentDAO.insert(new Student(name, phone, id, qq, mail));
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