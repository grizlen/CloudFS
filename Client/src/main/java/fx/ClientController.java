package fx;

import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ClientController {
    public ListView lvFiles;
    public TextField tfInput;

    public void sendCommand(ActionEvent actionEvent) {
        String msg = tfInput.getText().trim();
        if (msg.isEmpty()) {
            return;
        }
        System.out.println("sendCommand: " + msg);
    }
}
