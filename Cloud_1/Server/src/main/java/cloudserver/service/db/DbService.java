package cloudserver.service.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;

@Slf4j
public class DbService {

    private static final String DB_URL = "jdbc:sqlite:cloud_db";
    private DbConnection connection;

    public DbService() throws Exception {
        connection = DbConnection.getConnection(DB_URL);
        initTables();
    }

    private void initTables() throws Exception {
        connection.initTable(
                "CREATE TABLE if not exists 'users' (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "login TEXT NOT NULL UNIQUE, " +
                        "password TEXT NOT NULL);"
        );
        log.debug("Table: users(id, login, password)");
        connection.initTable(
                "CREATE TABLE if not exists 'files' (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "user_id INTEGER NOT NULL, " +
                        "file_name TEXT NOT NULL, " +
                        "storage_path TEXT NOT NULL, " +
                        "client_path TEXT NOT NULL);"
        );
        log.debug("Table: files(id, user_id, file_name, storage_path, client_path)");
    }

    public void close() throws Exception {
        DbConnection.closeConnection();
        connection = null;
    }

    public int getUserId(String login) {
        String query = String.format("SELECT id FROM users WHERE login = '%s';", login);
        ResultSet rs = connection.selectQuery(query);
        try {
            if (rs != null && rs.next()) {
                int id = rs.getInt(1);
                rs.getStatement().close();
                return id;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    public int getUserId(String login, String password) {
        String query = String.format("SELECT id FROM users WHERE (login = '%s') AND (password = '%s');", login, password);
        ResultSet rs = connection.selectQuery(query);
        try {
            if (rs != null && rs.next()) {
                int id = rs.getInt(1);
                rs.getStatement().close();
                return id;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    public boolean addUser(String login, String password) {
        String query = String.format(
                "INSERT INTO 'users' (login, password) VALUES('%s', '%s');", login, password);
        return connection.updateQuery(query) == 1;
    }

}
