package cloudclient.fx;

import cloudclient.network.Net;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import cloudclient.preferences.Preferences;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import transport.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
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
    private boolean isLogged = false;

    public void setMainPrimaryStage(Stage mainPrimaryStage) {
        this.mainPrimaryStage = mainPrimaryStage;
    }

    public void btnLoadAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        loadFile(file);
    }

    public void btnSaveAction(ActionEvent actionEvent) {
        String fileName = lvFiles.getSelectionModel().getSelectedItem();
        log.debug("Скачать: " + fileName);
        net.send(new GetFileMessage(Paths.get(currentServerPath).resolve(fileName).toString()));
    }

    public void btnDeleteAction(ActionEvent actionEvent) {
        String fileName = lvFiles.getSelectionModel().getSelectedItem();
        log.debug("Удалить: " + fileName);
        net.send(new DeleteFileMessage(Paths.get(currentServerPath).resolve(fileName).toString()));
    }

    public void btnMdAction(ActionEvent actionEvent) {
        TextInputDialog textInputDialog = new TextInputDialog("new");
        textInputDialog.setTitle("Make directory");
        textInputDialog.setHeaderText(null);
        textInputDialog.setContentText("enter directory name:");
        Optional<String> result = textInputDialog.showAndWait();
        result.ifPresent(s -> net.send(new MakeDirMessage(s)));
    }

    public void btnLoginAction(ActionEvent actionEvent) {
        logIn();
    }

    public void btnUpAction(ActionEvent actionEvent) {
        log.debug("Вверх");
    }

    public void lvFilesDragOverAction(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()){
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
    }

    public void lvFilesDragDoneAction(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.getDragboard().getFiles().forEach(this::loadFile);
        }
        log.debug("Drag dropped.");
    }

    public void exitAction() {
        net.close();
        Preferences.close();
        Platform.exit();
    }

    private void logIn() {
        log.debug("Авторизация");
        try {
            LoginDialog loginDialog = new LoginDialog(mainPrimaryStage);
            loginDialog.setUserName(userName);
            loginDialog.setUserPassword(userPassword);
            if (loginDialog.execute()) {
                if (isLogged) {
                    net.send(new AuthCloseMessage());
                    isLogged = false;
                }
                userName = loginDialog.getUserName();
                userPassword = loginDialog.getUserPassword();
                Preferences.getPreferences().put("userName", userName);
                Preferences.getPreferences().put("userPassword", userPassword);
                if (loginDialog.isNewUser()) {
                    net.send(new AuthNewMessage(userName, userPassword));
                } else {
                    net.send(new AuthMessage(userName, userPassword));
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void receiveMessage(Message message){
        log.debug("Receive: " + message.getMsg());

        switch (message.getMsg()){
            case AUTH_OK:
                log.debug("Login success.");
                isLogged = true;
                Platform.runLater(() -> net.send(new ListMessage(currentServerPath)));
                break;
            case AUTH_FAIL:
                log.debug("Login fail, try again.");
                isLogged = false;
                Platform.runLater(() -> {
                    logIn();
                });
                break;
            case LIST_REQUEST:
                currentServerPath = ((ListRequestMessage) message).getPath();
                Platform.runLater(() -> {
                    tfPath.setText(String.format("@%s:/%s", userName, currentServerPath));
                    lvFiles.getItems().clear();
                    lvFiles.getItems().addAll(((ListRequestMessage) message).getFiles());
                });
                break;
            case SEND_FILE:
                Platform.runLater(() -> saveFile(((SendFileMessage) message).getPath(), ((SendFileMessage) message).getData()));
                break;
            default:
                log.error("Incorrect message.");
        }
    }

    private void loadFile(File file) {
        log.debug("Загрузить: " + file.getName());
        try {
            Path path = Paths.get(currentServerPath).resolve(file.getName());
            byte[] data = Files.readAllBytes(file.toPath());
            net.send(new SendFileMessage(path.toString(), data));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void saveFile(String pathName, byte[] data) {
        Path path = Paths.get(localPathName).resolve(currentServerPath);
        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        path = path.resolve(pathName);
        try {
            Files.createFile(path);
            Files.write(path, data);
        } catch (IOException e) {
            log.error(e.getMessage());
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
