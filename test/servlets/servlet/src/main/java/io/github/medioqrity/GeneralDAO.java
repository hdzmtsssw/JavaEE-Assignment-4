package io.github.medioqrity;

import java.sql.*;

import com.microsoft.sqlserver.jdbc.*;

public class GeneralDAO {

    protected static SQLServerDataSource dataSource = null;

    static {
        dataSource = new SQLServerDataSource();
        dataSource.setIntegratedSecurity(true);
        dataSource.setServerName("DESKTOP-C8S8ARU");
        dataSource.setDatabaseName("master");
    }

    public ResultSet query(String sql) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        connection.close();
        return result;
    }

    public ResultSet query(PreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        return result;
    }
}