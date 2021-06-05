package transport;

import java.util.ArrayList;
import java.util.List;

public class ListRequestMessage extends AbstractMessage{

    private String path;
    private List<String> files;

    public ListRequestMessage() {
        super("list_request");
        path = "";
        files = new ArrayList<>();
    }

    public void append(String name){
        files.add(name);
    }

    public List<String> getFiles() {
        return files;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
