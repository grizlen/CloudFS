package client.fx;

import client.network.Net;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import client.preferences.Preferences;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import transport.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ClientMainController implements Initializable {


    public ListView<String> lvFiles;
    public TextField tfPath;
    private Stage mainPrimaryStage;
    private String userName;
    private String userPassword;
    private String serverHost;
    private int serverPort;
    private Net net;
    private String currentServerPath;
    private String localPathName;

    public void setMainPrimaryStage(Stage mainPrimaryStage) {
        this.mainPrimaryStage = mainPrimaryStage;
    }

    public void btnLoadAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        System.out.println("Загрузить: " + file.getName());
    }

    public void btnSaveAction(ActionEvent actionEvent) {
        String fileName = lvFiles.getSelectionModel().getSelectedItem();
        System.out.println("Скачать: " + fileName);
        GetFileMessage message = new GetFileMessage();
        message.setPath(Paths.get(currentServerPath).resolve(fileName).toString());
        net.send(message);
    }

    public void btnDeleteAction(ActionEvent actionEvent) {
        System.out.println("Удалить");
    }

    public void btnLoginAction(ActionEvent actionEvent) {
        logIn();
    }

    public void btnUpAction(ActionEvent actionEvent) {
        System.out.println("Вверх");
    }

    public void exitAction() {
        net.colse();
        Platform.exit();
    }

    private void logIn() {
        System.out.println("Авторизация");
        try {
            LoginDialog loginDialog = new LoginDialog(mainPrimaryStage);
            loginDialog.setUserName(userName);
            loginDialog.setUserPassword(userPassword);
            if (loginDialog.execute()) {
                userName = loginDialog.getUserName();
                userPassword = loginDialog.getUserPassword();
                Preferences.getPreferences().put("userName", userName);
                Preferences.getPreferences().put("userPassword", userPassword);
                AuthMessage message = new AuthMessage();
                message.setUserName(userName);
                message.setUserPassword(userPassword);
                net.send(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage(Message message){
        System.out.println("Receive: " + message.getMsg());
        if (message instanceof AuthOkMessage) {
            System.out.println("Login success.");
            ListMessage out = new ListMessage();
            out.setPath(currentServerPath);
            Platform.runLater(() -> net.send(out));
        } else if (message instanceof AuthFailMessage) {
            System.out.println("Login fail, try again.");
            Platform.runLater(() -> {
                logIn();
            });
        } else if (message instanceof ListRequestMessage) {
            currentServerPath = ((ListRequestMessage) message).getPath();
            Platform.runLater(() -> {
                tfPath.setText(String.format("@%s:/%s", userName, currentServerPath));
                lvFiles.getItems().clear();
                lvFiles.getItems().addAll(((ListRequestMessage) message).getFiles());
            });
        } else if ((message instanceof SendFileMessage)) {
            Platform.runLater(() -> saveFile(((SendFileMessage) message).getPath(), ((SendFileMessage) message).getData()));
        }
    }

    private void saveFile(String pathName, byte[] data) {
        Path path = Paths.get(localPathName).resolve(currentServerPath).resolve(pathName);
        File file = path.toFile();
        try {
            try (FileOutputStream fs = new FileOutputStream(file)) {
                fs.write(data);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userName = Preferences.getPreferences().get("userName");
        userPassword = Preferences.getPreferences().get("userPassword");
        serverHost = Preferences.getPreferences().get("serverHost");
        serverPort = Integer.parseInt(Preferences.getPreferences().get("serverPort"));
        localPathName = Preferences.getPreferences().get("localPathName");
        currentServerPath = "";
        net = new Net(serverHost, serverPort, this::receiveMessage);
    }
}
