package io.github.medioqrity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;

import io.github.medioqrity.Encryptor;

public class Authenticator extends GeneralDAO {

    public Authenticator() throws SQLException {
        super();
    }

    public boolean login(String username, String password) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "select password from users where username like ? "
            );
            statement.setString(1, username);
            ResultSet result = query(statement);
            if (result.next()) {
                String encryptedPassword = result.getString("password");
                return encryptedPassword.equals(Encryptor.encode(password));
            } else {
                return false; // no such user
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean admin(String username) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "select admin from users where username like ? "
            );
            statement.setString(1, username);
            ResultSet result = query(statement);
            if (result.next()) {
                int isAdmin = result.getInt("admin");
                return isAdmin == 1;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}