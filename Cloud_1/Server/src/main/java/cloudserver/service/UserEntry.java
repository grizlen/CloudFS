package cloudserver.service;

import lombok.extern.slf4j.Slf4j;
import transport.ListRequestMessage;
import transport.SendFileMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserEntry {

    private final String name;
    private final int id;
    private Path userRootPath;

    public String getName() {
        return name;
    }

    public Path getUserRootPath() {
        return userRootPath;
    }

    public UserEntry(int id, String name) {
        this.id = id;
        this.name = name;
        userRootPath = FilesService.getServerRootPath().resolve("user_" + id);
    }

    public ListRequestMessage listFiles(String pathName) {
        try {
            Path path = userRootPath.resolve(pathName);
            log.debug("list of: " + path.toString());
            List<String> list = Files.list(path)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
            return new ListRequestMessage(pathName, list);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public SendFileMessage sendFile(String pathName) {
        Path path = userRootPath.resolve(pathName);
        File file = path.toFile();
        if (!file.exists() || file.isDirectory()) {
            log.error("file not exists: " + path.toString());
            return null;
        }
        log.debug("send: " + path.toString());
        try (FileInputStream fs = new FileInputStream(file)) {
            int sz = (int) file.length();
            byte[] data = new byte[sz];
            fs.read(data);
            return new SendFileMessage(pathName, data);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
