package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static final String URL = "jdbc:postgresql://switchyard.proxy.rlwy.net:27334/railway";
    private static final String USER = "postgres";
    private static final String PASSWORD = "USteHzezLPAKcheQnuXFBclRlviigMpv";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
