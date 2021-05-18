import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public ListView<String> listOutput;
    public TextField textInput;
    private Net net;

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        net.sendMessage(textInput.getText());
        textInput.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            net = Net.start(8189);
            Thread thread = new Thread(() -> {
                try {
                    while (true){
                        String msg = net.getMessage();
                        if (msg.equals("/takeFiles")){
                            String files = net.getMessage();
                            String[] names = files.split(",");
                            Platform.runLater(() -> {
                                listOutput.getItems().clear();
                                listOutput.getItems().addAll(names);
                            });
                        } else if (msg.equals("/sendFile")){
                            saveFile();
                        } else {
                            Platform.runLater(() -> listOutput.getItems().add(msg));
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Exception while reading.");
                }
            });
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            System.err.println("Connection was broken.");
        }
    }

    private void saveFile() throws IOException {
        File f = new File("CloudFS-Client/client-files/" + net.getMessage());
        long sz = Long.parseLong(net.getMessage());

    }
}
