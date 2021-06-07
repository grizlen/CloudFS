package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileInfo {

    private Path path;
    private final String fileName;
    private final String dirName;
    private final boolean isDir;
    private final long size;
    private final LocalDateTime lastModifiedTime;

    public FileInfo(Path path) throws IOException {
        this.path = path;
        fileName = path.getFileName().toString();
        dirName = path.getParent().toString();
        isDir = Files.isDirectory(path);
        if (isDir) {
            size = -1;
        } else {
            size = Files.size(path);
        }
        lastModifiedTime = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(3));
    }

    public FileInfo(String pathName) throws IOException {
        this(Paths.get(pathName));
    }

    public void relativize(Path root){
        path = path.relativize(root);
    }
}
