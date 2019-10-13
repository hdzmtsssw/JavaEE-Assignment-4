package io.github.medioqrity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import static io.github.medioqrity.Settings.*;

@WebServlet("/upload")
@MultipartConfig
public class UploadImageServlet extends HttpServlet {

    private static final long serialVersionUID = -4603243291938105576L;
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        String studentID = request.getParameter("id");
        Part filePart = request.getPart("file"); // comes from <input type="file" name="file">
        InputStream fileContent = filePart.getInputStream();
        byte[] buffer = new byte[fileContent.available()];
        fileContent.read(buffer);
        FileOutputStream out = new FileOutputStream(getServletContext().getRealPath(IMAGE_DIRECTORY + studentID + ".jpg"));
        out.write(buffer);
        out.close();

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