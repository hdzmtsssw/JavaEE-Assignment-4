package io.github.medioqrity;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter")
public class AuthFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpSession session = hRequest.getSession();
        try {
            System.out.println("AuthFilter: " + session.getAttribute("username"));
            if (session.getAttribute("username") == null || (boolean) session.getAttribute("admin") != true) {
                request.setAttribute("chain", chain);
                request.getRequestDispatcher("login.html").forward(request, response);
            }
            chain.doFilter(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}