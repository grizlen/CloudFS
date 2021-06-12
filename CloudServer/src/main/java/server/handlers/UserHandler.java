package server.handlers;

import data.FileInfo;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import transport.ListMessage;
import transport.ListRequestMessage;
import transport.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserHandler {

    private static final Path SERVER_ROOT_PATH = Paths.get("CloudServer", "ServerFiles");

    private final int id;
    private final String name;
    private final Path userRootPath;
    private Path userCurrentPath;

    public UserHandler(int id, String name) throws Exception {
        this.id = id;
        this.name = name;
        userRootPath = SERVER_ROOT_PATH.resolve("user_" + id);
        if (Files.notExists(userRootPath)) {
            Files.createDirectory(userRootPath);
        }
        userCurrentPath = Paths.get("");
    }

    public String getName() {
        return name;
    }

    public void setUserCurrentPath(String currentPathName) {
        userCurrentPath = Paths.get(currentPathName);
    }

    public void processMessage(ChannelHandlerContext channelHandlerContext, Message message) {
        switch (message.getMsg()){
            case LIST:
                setUserCurrentPath(((ListMessage) message).getPathName());
                channelHandlerContext.writeAndFlush(listFiles());
                break;
            default:
                log.warn("Unsupported message: " + message.toString());
        }
    }

    private ListRequestMessage listFiles() {
        try {
            Path path = userRootPath.resolve(userCurrentPath);
            log.debug("send list of: " + path.toString());
            List<FileInfo> list = Files.list(path)
                    .map(p -> {
                        try {
                            FileInfo f = new FileInfo(p);
                            f.relativize(userRootPath);
                            return f;
                        } catch (IOException e) {
                            log.error("List files exception: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(f -> f != null)
                    .collect(Collectors.toList());
            return new ListRequestMessage(userCurrentPath.toString(), list);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
