package cloudserver.service;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesService {

    public static Path getServerRootPath() {
        return Paths.get("Server", "server-files");
    }
}
