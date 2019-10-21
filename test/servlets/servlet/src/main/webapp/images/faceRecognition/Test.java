import java.io.*;
import java.lang.ProcessBuilder;

public class Test {
    public static void main(String[] args) {
        try {
            Process python = Runtime.getRuntime().exec(
                "python C:\\Users\\qianx\\eclipse-workspace\\Test\\test\\servlets\\servlet\\src\\main\\webapp\\images\\faceRecognition\\test.py"
            );
            BufferedReader reader = new BufferedReader(new InputStreamReader(python.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(python.getOutputStream()));
            writer.write("123\n");
            writer.flush();
            writer.write("456\n");
            writer.flush();
            writer.write("aaa\n");
            writer.flush();
            reader.lines().forEach(line -> System.out.println("R: " + line));
            python.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}