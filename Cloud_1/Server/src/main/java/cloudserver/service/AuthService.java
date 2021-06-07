package cloudserver.service;

import io.netty.channel.ChannelHandlerContext;
import cloudserver.service.db.DbService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class AuthService {

    private final ConcurrentHashMap<ChannelHandlerContext, UserEntry> users;
    private final DbService dbService;

    public AuthService() throws Exception {
        users = new ConcurrentHashMap<ChannelHandlerContext, UserEntry>();
        dbService = new DbService();
    }

    public void close() throws Exception {
        dbService.close();
    }

    public UserEntry subscribe(ChannelHandlerContext channelHandlerContext, String userName, String userPassword) {
        if (isLoggedIn(userName)) {
            log.debug("user {} is already logged.", userName);
            return null;
        }
        int userId = dbService.getUserId(userName, userPassword);
        if (userId == 0) {
            log.debug("user {} not found.", userName);
            return null;
        }
        UserEntry userEntry = new UserEntry(userId, userName);
        users.put(channelHandlerContext, userEntry);
        log.debug("user {} logged in Id = {}", userName, userId);
        return userEntry;
    }

    public UserEntry newEntry(ChannelHandlerContext channelHandlerContext, String userName, String userPassword) {
        int userId = dbService.getUserId(userName);
        if (userId == 0 && dbService.addUser(userName, userPassword)) {
            return subscribe(channelHandlerContext, userName, userPassword);
        }
        return null;
    }

    public void unSubscribe(ChannelHandlerContext channelHandlerContext) {
        users.remove(channelHandlerContext);
    }

    private boolean isLoggedIn(String userName){
        for(UserEntry entry: users.values()) {
            if (entry.getName().equalsIgnoreCase(userName)){
                return true;
            }
        }
        return false;
    }

    public UserEntry getEntry(ChannelHandlerContext channelHandlerContext) {
        return users.get(channelHandlerContext);
    }
}
