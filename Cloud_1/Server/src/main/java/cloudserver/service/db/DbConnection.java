package cloudserver.service.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
public class DbConnection {

    private static final String DRIVER_NAME = "org.sqlite.JDBC";

    private static DbConnection dbConnection;
    private final Connection connection;

    public static DbConnection getConnection(String dbUrl) throws Exception {
        if (dbConnection == null) {
            dbConnection = new DbConnection(dbUrl);
        }
        return dbConnection;
    }

    public static void closeConnection() throws Exception {
        if (dbConnection != null) {
            dbConnection.connection.close();
            dbConnection = null;
        }
    }

    private DbConnection(String dbUrl) throws Exception {
        Class.forName(DRIVER_NAME);
        connection = DriverManager.getConnection(dbUrl);
        log.debug("Data base connected driver: {} URL: {}." ,DRIVER_NAME, dbUrl);
    }

    public void initTable(String query) throws Exception {
        Statement statement = connection.createStatement();
        statement.execute(query);
    }

    public int updateQuery(String query) {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        } catch (Exception e) {
            log.error("SQL update error: " + e.getMessage());
        }
        return 0;
    }

    public ResultSet selectQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            log.error("SQL select error: " + e.getMessage());
        }
        return null;
    }
}
