package io.github.medioqrity;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedList;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.medioqrity.Paginator;
import static io.github.medioqrity.Settings.*;

// @WebServlet("/query")
public class StudentQueryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int COUNT_PER_PAGE_DEFAULT = 10;
    private StudentDAO studentDAO = null;

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

    private int getIntParameter(HttpServletRequest req, String name, int defaultValue) {
        String temp = null;
        if ((temp = req.getParameter(name)) != null) {
            try {
                return Integer.parseInt(temp);
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private void addRow(int i, Student student, PrintWriter out, boolean admin, String keyword, int currentPage) {
        out.println("<tr>");
        out.println("<td><img src='" + getServletContext().getContextPath() + IMAGE_DIRECTORY + student.getId() + ".jpg" + "'></td>");
        if (admin) {
            out.println("<form action='upload' method='post' enctype='multipart/form-data'>" + 
                        "<input type='hidden' name='id' value='" + student.getId() + "'>" + 
                        "<input type='hidden' name='page' value='" + currentPage + "'>" +
                        "<td><input type='file' name='file'><input type='submit'></td>" + 
                        "</form>");
            out.println("<form action='update' method='post'>" +
                        "<input type='hidden' name='id' value='" + student.getId() + "'>" +
                        "<input type='hidden' name='page' value='" + currentPage + "'>");
        }
        out.println("<td>" + i + "</td>");
        out.println((admin ? "<td><input type='text' name='n' value='" : "<td>") + student.getName()  + (admin ? "' required>" : "") + "</td>");
        out.println("<td>" + student.getId() + "</td>");
        out.println((admin ? "<td><input type='text' name='p' value='" : "<td>") + student.getPhone() + (admin ? "' required>" : "") + "</td>");
        out.println((admin ? "<td><input type='text' name='q' value='" : "<td>") + student.getQq()    + (admin ? "'>" : "") + "</td>");
        out.println((admin ? "<td><input type='text' name='m' value='" : "<td>") + student.getMail()  + (admin ? "'>" : "") + "</td>");
        if (admin) out.println("<td><input type='submit' value='update'></td></form>");
        if (admin) out.println("<td><form action='delete'><input type='submit' value='delete'><input type='hidden' name='id' value='" + student.getId() + "'></form></td>");
        out.println("</tr>");
    }

    private void generatePage(String keyword, List<Student> result, HttpServletRequest req, HttpServletResponse res, 
                              int currentPage, int rowPerPage, int resultCount, String username, boolean admin) 
                              throws IOException {
        // TODO: Replace this function by a JSP file.
        PrintWriter out = res.getWriter();
        out.println("<body>");

        if (username != null) {
            out.println("<p>Hello, " + username + ". Click <a href='logout'>here</a> to log out.</p>");
            req.getSession().setAttribute("next", "index.jsp");
        }

        out.println("<table>");
        out.println("<tr>");
        out.println("<th>照片</th>");
        if (admin) out.println("<th></th>");
        out.println("<th>结果编号</th>");
        out.println("<th>姓名</th>");
        out.println("<th>学号</th>");
        out.println("<th>电话</th>");
        out.println("<th>QQ</th>");
        out.println("<th>邮箱</th>");
        if (admin) out.println("<th></th>\n<th></th>");
        out.println("</tr>");

        int totalPageCount = resultCount / rowPerPage;
        if (resultCount % rowPerPage != 0) ++totalPageCount; // [0, totalPageCount)

        int actualRecordCount = 0;
        if (result != null) {
            for (Student student : result) {
                addRow(++actualRecordCount, student, out, admin, keyword, currentPage);
            }
        }

        if (admin) {
            out.println("<tr>");
            out.println("<form action='insert'><input type='hidden' name='page' value='" + currentPage + "'>");
            out.println("<td>添加新学生</td>");
            out.println("<td><input type='text' name='n' placeholder='姓名' required></td>");
            out.println("<td><input type='text' name='id' placeholder='学号' required></td>");
            out.println("<td><input type='text' name='p' placeholder='电话' required></td>");
            out.println("<td><input type='text' name='q' placeholder='QQ'></td>");
            out.println("<td><input type='text' name='m' placeholder='邮箱'></td>");
            out.println("<td><input type='submit' value='insert'></td>");
            out.println("<td></td>");
            out.println("</form>");
            out.println("</tr>");
        }

        out.println("</table>");

        out.println(actualRecordCount + " records on this page, and " + resultCount + " records at all found.");

        // generate link
        Set<Integer> pages = new TreeSet<>();
        int cur = currentPage, step = 1;
        while (cur >= 0) {
            pages.add(cur);
            cur -= step;
            step <<= 1;
        }
        pages.add(0);

        cur = currentPage; step = 1;
        while (cur < totalPageCount) {
            pages.add(cur);
            cur += step;
            step <<= 1;
        }
        pages.add(totalPageCount - 1);

        out.println("<p>");
        for (int i : pages) {
            if (i != currentPage) {
                out.println("<span><a href=\"?page=" + i + "&countPerPage=" + rowPerPage + "&keyword=" 
                             + keyword + "\">" + i + "</a></span>");
            } else {
                out.println("<span>" + i + "</span>");
            }
        }
        out.println("</p>");

        out.println("<p><a href=\"" + getServletContext().getContextPath() + "/index.jsp\">Back to homepage</a></p>");

        out.println("<form action='query'>Change the record count per page: <input type='hidden' name='keyword' value=" + 
                    keyword + "><input type='text' name='countPerPage' value=" + rowPerPage + " pattern=\"[0-9]+\"><input type='submit'></form>");
        
        out.println("</body>");
    }

    private int verify(int a, int b) {
        if (a <= 0) return b;
        return a;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) 
    throws ServletException, IOException {
        doPost(req, res);
    }

    private List<Student> query(String keyword) {
        List<Student> result = null;
        try {
            result = studentDAO.queryStudent(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result == null) result = new LinkedList<>();
        return result;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) 
    throws ServletException, IOException {
        res.setContentType("text/html; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        String keyword = req.getParameter("keyword");
        HttpSession session = req.getSession();

        int currentPage = getIntParameter(req, "page", 0);
        int resultCountPerPage = COUNT_PER_PAGE_DEFAULT;

        // for security issues. e.g.: negative count per page
        if (session.getAttribute("resultCountPerPage") != null) {
            resultCountPerPage = (Integer) session.getAttribute("resultCountPerPage");
        }
        if (req.getParameter("countPerPage") != null) {
            try {
                resultCountPerPage = verify(Integer.parseInt(req.getParameter("countPerPage")), resultCountPerPage);
                session.setAttribute("resultCountPerPage", resultCountPerPage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (session.getAttribute("resultCountPerPage") != null) {
                resultCountPerPage = (Integer) session.getAttribute("resultCountPerPage");
            } else {
                resultCountPerPage = COUNT_PER_PAGE_DEFAULT;
            }
        }

        Paginator paginator = null;
        if (session.getAttribute("paginator") != null) {
            paginator = (Paginator) session.getAttribute("paginator");
        } else {
            paginator = new Paginator();
        }

        assert paginator != null;
        session.setAttribute("paginator", paginator);
        paginator.setRowPerPage(resultCountPerPage);
        
        String lastKeyWord = (String) session.getAttribute("keyword");
        if (lastKeyWord == null || !lastKeyWord.equals(keyword) || 
            session.getAttribute("updated") != null) {

            // the first query or the keyword has changed or some data updated
            session.setAttribute("keyword", keyword);
            paginator.setData(query(keyword));

            if (session.getAttribute("updated") != null) {
                session.removeAttribute("updated");
            }
        }

        String username = null;
        boolean admin = false;
        if (session.getAttribute("username") != null) username = (String) session.getAttribute("username");
        if (session.getAttribute("admin") != null) admin = (boolean) session.getAttribute("admin");
        
        generatePage(keyword, paginator.getData(currentPage), req, res, currentPage, resultCountPerPage, 
                     paginator.getDataSize(), username, admin);
    }
}
