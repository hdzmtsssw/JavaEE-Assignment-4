package io.github.medioqrity;

import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StudentUpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 8818368179247928448L;
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
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String studentID = request.getParameter("id");
        String name = request.getParameter("n");
        String phone = request.getParameter("p");
        String qq = request.getParameter("q");
        String mail = request.getParameter("m");
        try {
            studentDAO.update(studentID, new Student(name, phone, studentID, qq, mail));
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