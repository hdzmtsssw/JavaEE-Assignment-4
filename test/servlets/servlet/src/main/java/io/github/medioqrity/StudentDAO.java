package io.github.medioqrity;

import java.sql.*;
import java.util.List;
import java.util.LinkedList;
import java.io.*;

import com.microsoft.sqlserver.jdbc.*;

public class StudentDAO extends GeneralDAO {

    public StudentDAO() throws SQLException {
        super();
    }

    public List<Student> queryStudent(String keyword) throws SQLException {
        List<Student> result = new LinkedList<>();

        String queryString = "select * from students where id like ? or name like ? or phone like ? or qq like ? or mail like ? ";
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        for (int i = 1; i <= 5; ++i) preparedStatement.setString(i, "%" + keyword + "%");

        ResultSet resultSet = query(preparedStatement);
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String id = resultSet.getString("id");
            String phone = resultSet.getString("phone");
            String qq = resultSet.getString("qq");
            String mail = resultSet.getString("mail");
            Student student = new Student(name, phone, id, qq, mail);
            result.add(student);
        }

        connection.close();
        return result;
    }

    public void insert(Student student) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        if (student.getId() == null || student.getName() == null || student.getPhone() == null) return;
        statement.executeUpdate("insert into students values " + student);
        connection.close();
    }

    public void insert(List<Student> students) throws SQLException {
        for (Student student : students) {
            insert(student);
        }
    }

    public void update(String id, Student student) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "update students " + 
            "set name = ? , phone = ? , qq = ? , mail = ? " +
            "where id like ? "
        );
        preparedStatement.setString(1, student.getName());
        preparedStatement.setString(2, student.getPhone());
        preparedStatement.setString(3, student.getQq());
        preparedStatement.setString(4, student.getMail());
        preparedStatement.setString(5, student.getId());
        preparedStatement.executeUpdate();
        connection.close();
    }

    public void delete(String id) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "delete from students " +
            "where id like ? "
        );
        assert !id.contains("%");
        preparedStatement.setString(1, id);
        preparedStatement.executeUpdate();
        connection.close();
    }

    private static Student parse(String line) {
        String[] l = line.split(";");
        if (l.length == 3) return new Student(l[0], l[1], l[2], "", "");
        if (l.length == 4) return new Student(l[0], l[1], l[2], l[3], "");
        if (l.length == 5) return new Student(l[0], l[1], l[2], l[3], l[4]);
        return null;
    }

    private static void insert(Student student, Statement statement) throws SQLException {
        statement.executeUpdate("insert into students values " + student);
    }

    public static void main(String[] args) {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setIntegratedSecurity(true);
        dataSource.setServerName("DESKTOP-C8S8ARU");
        dataSource.setDatabaseName("master");

        String fileName = "C:\\Users\\qianx\\eclipse-workspace\\Test\\test\\servlets\\servlet\\src\\main\\webapp\\WEB-INF\\contact\\out";
        BufferedReader reader = null;

        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("drop table students;");
            statement.executeUpdate("create table students (ID char(9) NOT NULL, NAME nvarchar(10) NOT NULL, PHONE char(11) NOT NULL,QQ varchar(10) NULL, MAIL varchar(50) NULL, PRIMARY KEY (ID));");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                Student student = parse(temp);
                if (student != null) {
                    insert(student, statement);
                }
            }
            connection.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}