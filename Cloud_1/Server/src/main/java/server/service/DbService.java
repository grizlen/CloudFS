package server.service;

public class DbService {

    public DbService() {
    }

    public UserEntry getUserEntry(String userName, String userPassword) {
        UserEntry userEntry = null;
        if (userName.equalsIgnoreCase("user")) {
            userEntry = new UserEntry(userName);
            userEntry.setUserRootPath("Server/server-files");
        }
        return userEntry;
    }
}
