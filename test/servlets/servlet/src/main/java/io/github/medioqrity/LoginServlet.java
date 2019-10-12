package io.github.medioqrity;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import io.github.medioqrity.Authenticator;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = -8798269308271545789L;
    private Authenticator authenticator;

    public void init() {
        try {
            authenticator = new Authenticator();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (authenticator.login(username, password)) {
            request.getServletContext().log("User " + username + " logged in successfully");
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("admin", authenticator.admin(username));
            if (request.getAttribute("chain") != null) {
                FilterChain chain = (FilterChain) request.getAttribute("chain");
                try {
                    chain.doFilter((ServletRequest) request, (ServletResponse) response);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            request.getServletContext().log("User " + username + " failed to authentify");
            try {
                request.getRequestDispatcher("login.html").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}