package server.service;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

public class AuthService {

    private final ConcurrentHashMap<ChannelHandlerContext, UserEntry> users;
    private final DbService dbService;

    public AuthService() {
        users = new ConcurrentHashMap<ChannelHandlerContext, UserEntry>();
        dbService = new DbService();
    }

    public UserEntry newEntry(ChannelHandlerContext channelHandlerContext, String userName, String userPassword) {
        if (isLoggedIn(userName)) {
            return null;
        }
        UserEntry userEntry = dbService.getUserEntry(userName, userPassword);
        if (userEntry != null) {
            users.put(channelHandlerContext, userEntry);
        }
        return userEntry;
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
