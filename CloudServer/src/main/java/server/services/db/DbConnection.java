package server.services.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class DbConnection {

    private static final String DRIVER_NAME = "org.sqlite.JDBC";
    private static DbConnection dbConnection;
    private final Connection connection;

    public static DbConnection getConnection(String url) throws Exception {
        if (dbConnection == null) {
            dbConnection = new DbConnection(url);
        }
        return dbConnection;
    }

    public static void closeConnection() throws Exception {
        if (dbConnection != null) {
            dbConnection.connection.close();
            dbConnection = null;
        }
    }

    private DbConnection(String url) throws Exception {
        Class.forName(DRIVER_NAME);
        connection = DriverManager.getConnection(url);
        log.debug("Data base connected driver: {} URL: {}." ,DRIVER_NAME, url);
    }

    public static boolean Execute(String query) {
        boolean result;
        try (Statement statement = dbConnection.connection.createStatement()) {
            result = statement.execute(query);
        } catch (Exception e) {
            log.error("SQL exception: ", e);
            result = false;
        }
        return result;
    }

    public static ResultSet ExecuteQuery(String query) {
        Statement statement;
        try {
            statement = dbConnection.connection.createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            log.error("SQL exception: ", e);
            return null;
        }
    }

    public static int ExecuteUpdate(String query) {
        int result;
        try (Statement statement = dbConnection.connection.createStatement()) {
            result = statement.executeUpdate(query);
        } catch (Exception e) {
            log.error("SQL exception: ", e);
            result = 0;
        }
        return result;
    }
}
