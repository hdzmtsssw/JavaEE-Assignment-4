package io.github.medioqrity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.http.HttpSession;

import static io.github.medioqrity.Settings.*;

@WebServlet("/ImageSearch")
@MultipartConfig
public class ImageSearchServlet extends HttpServlet {

    private static final long serialVersionUID = 5445154266585224261L;
    private Process python;

    @Override
    public void init() {
        System.out.println("PATH: " + getServletContext().getRealPath(IMAGE_DIRECTORY));
        try {
            python = Runtime.getRuntime().exec(
                "python " + 
                getServletContext().getRealPath(RECOGNIZOR_DIRECTORY + RECOGNIZOR_NAME) + " " +
                getServletContext().getRealPath(IMAGE_DIRECTORY)
            );
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("python: " + python);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        Part filePart = request.getPart("file"); // comes from <input type="file" name="file">
        InputStream fileContent = filePart.getInputStream();
        byte[] buffer = new byte[fileContent.available()];
        fileContent.read(buffer);
        FileOutputStream out = new FileOutputStream(getServletContext().getRealPath(IMAGE_DIRECTORY + TEMP + ".jpg"));
        out.write(buffer);
        out.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(python.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(python.getOutputStream()));

        System.out.println("reader, writer: " + reader + " " + writer);

        writer.write(getServletContext().getRealPath(IMAGE_DIRECTORY + TEMP + ".jpg") + "\n");
        writer.flush();

        HttpSession session = request.getSession();

        List<String> ids = new LinkedList<>();
        List<Double> dists = new LinkedList<>();

        // System.out.println(reader.readLine());
        // response.sendRedirect("/index.jsp");
        String temp;
        while ((temp = reader.readLine()) == null);
        System.out.println(temp);
        int n = Integer.parseInt(temp);
        for (int i = 0; i < n; ++i) {
            ids.add(reader.readLine());
            dists.add(Double.parseDouble(reader.readLine()));
        }

        session.setAttribute("ids", ids);
        session.setAttribute("dists", dists);

        response.sendRedirect(getServletContext().getContextPath() + "/ImageSearchResult.jsp");
    }
}