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

    private final int id;
    private final String name;
    private Path userRootPath;
    private Path userCurrentPath;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setUserCurrentPath(String currentPathName) {
        userCurrentPath = Paths.get(currentPathName);
    }

    public UserEntry(int id, String name) {
        this.id = id;
        this.name = name;
        userRootPath = FilesService.getServerRootPath().resolve("user_" + id);
        userCurrentPath = Paths.get("");
        if (!Files.exists(userRootPath)) {
            log.debug("md {}.", userRootPath.toString());
            try {
                Files.createDirectory(userRootPath);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        log.debug("UserEntry: id = {}; name = {}; path = {}.", id, name, userRootPath.toString());
    }

    public ListRequestMessage listFiles() {
        try {
            Path path = userRootPath.resolve(userCurrentPath);
            log.debug("send list of: " + path.toString());
            List<String> list = Files.list(path)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
            return new ListRequestMessage(userCurrentPath.toString(), list);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public SendFileMessage sendFile(String pathName) {
        Path path = userRootPath.resolve(pathName);
        log.debug("Send file: " + path.toString());
        if (Files.notExists(path) || Files.isDirectory(path)){
            log.error("file not exists: " + path.toString());
            return null;
        }
        try {
            byte[] data = Files.readAllBytes(path);
            return new SendFileMessage(pathName, data);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public void saveFile(String pathName, byte[] data) {
        Path path = userRootPath.resolve(pathName);
        log.debug("Save file: " + path.toString());
        try {
            Files.createFile(path);
            Files.write(path, data);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void deleteFile(String pathName) {
        Path path = userRootPath.resolve(pathName);
        log.debug("Delete file: " + path.toString());
        if (Files.isDirectory(path)) {
            if (path.equals(userRootPath)) {
                return;
            }
            if (path.equals(userRootPath.resolve(userCurrentPath))){
                userCurrentPath = path.getParent();
            }
            doDeleteDir(path);
        }
        doDeleteFile(path);
    }

    public void makeDir(String pathName) {
        Path path = userRootPath.resolve(userCurrentPath).resolve(pathName).normalize().toAbsolutePath();
        log.debug("md absolute: " + path);
        if (path.getNameCount() < userRootPath.toAbsolutePath().getNameCount()){
            log.error("invalid directory name: " + path);
        }
        path = userRootPath.toAbsolutePath().relativize(path);
        try {
            Files.createDirectory(userRootPath.resolve(path));
            userCurrentPath = path;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.debug("md: {} [{}]", path, path.getNameCount());
    }

    private void doDeleteDir(Path path) {
        try {
            Files.list(path).forEach(p -> {
                if (Files.isDirectory(p)){
                    doDeleteDir(p);
                }
                doDeleteFile(p);
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void doDeleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
