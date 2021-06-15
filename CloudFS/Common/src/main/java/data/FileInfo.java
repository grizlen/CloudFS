package data;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileInfo implements Serializable {

    private final long size;
    private String pathName;
    private final boolean isDir;

    public FileInfo(Path path) throws IOException {
        pathName = path.toString();
        isDir = Files.isDirectory(path);
        if (isDir) {
            size = -1;
        } else {
            size = Files.size(path);
        }
    }

    public void relativize(Path rootPath) {
        pathName = rootPath.relativize(Paths.get(pathName)).toString();
    }

    @Override
    public String toString() {
        return String.format("%s %s (%d)", isDir ? "D" : "F", pathName, size);
    }
}
