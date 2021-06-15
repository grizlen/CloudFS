package transport;

import data.FileInfo;

import java.util.ArrayList;
import java.util.List;

public class ListRequestMessage extends AbstractPathMessage {

    private List<FileInfo> files;

    public ListRequestMessage(String pathName, List<FileInfo> files) {
        super(MessageId.LIST_REQUEST, pathName);
        this.files = new ArrayList<>();
        this.files.addAll(files);
    }

    public List<FileInfo> getFiles() {
        return files;
    }
}
