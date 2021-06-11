package server.services;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import server.handlers.UserHandler;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class AuthService {

    private final ConcurrentHashMap<ChannelHandlerContext, UserHandler> users;
    private final DbService dbService;

    public AuthService() throws Exception {
        users = new ConcurrentHashMap<>();
        dbService = new DbService();
    }

    public void close() {
        dbService.close();
    }

    public UserHandler subscribe(ChannelHandlerContext channelHandlerContext, String userName, String userPassword) {
        if (isLoggedIn(userName)){
            log.debug("user {} is already logged.", userName);
            return null;
        }
        int userId = DbService.getUserId(userName, userPassword);
        if (userId == 0) {
            log.error("User {} not found.", userName);
            return null;
        }
        UserHandler userHandler = new UserHandler(userId, userName);
        users.put(channelHandlerContext, userHandler);
        log.debug("user {} logged in Id = {}", userName, userId);
        return userHandler;
    }

    private boolean isLoggedIn(String userName) {
        for(UserHandler user: users.values()) {
            if (user.getName().equalsIgnoreCase(userName)){
                return true;
            }
        }
        return false;
    }

    public UserHandler newUser(ChannelHandlerContext channelHandlerContext, String userName, String userPassword) {
        if (dbService.addUser(userName, userPassword)) {
            return subscribe(channelHandlerContext, userName, userPassword);
        } else {
            return null;
        }
    }
}
