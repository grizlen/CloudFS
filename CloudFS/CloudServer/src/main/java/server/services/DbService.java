package server.services;

import lombok.extern.slf4j.Slf4j;
import server.services.db.DbConnection;

import java.sql.ResultSet;

@Slf4j
public class DbService {

    private static final String DB_URL = "jdbc:sqlite:cloud_db";

    public DbService() throws Exception {
        DbConnection.getConnection(DB_URL);
        init();
    }

    public void close() {
        try {
            DbConnection.closeConnection();
        } catch (Exception e) {
            log.error("DbService exception: ", e);
        }
    }

    private void init() {
        DbConnection.Execute(
                "CREATE TABLE if not exists 'users' (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "login TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL);");
    }

    public static int getUserId(String userName, String userPassword) {
        int result = 0;
        try {
            ResultSet resultSet = DbConnection.ExecuteQuery(String.format(
                    "SELECT id FROM users WHERE (login = '%s') AND (password = '%s');",
                    userName,
                    userPassword));
            if (resultSet != null && resultSet.next()) {
                result = resultSet.getInt("id");
                resultSet.getStatement().close();
            }
        } catch (Exception e) {
            log.error("DbService exception: ", e);
        }
        return result;
    }

    public boolean addUser(String userName, String userPassword) {
        return DbConnection.ExecuteUpdate(String.format(
                "INSERT INTO users (login, password) VALUES('%s', '%s');", userName, userPassword)
        ) == 1;
    }
}
