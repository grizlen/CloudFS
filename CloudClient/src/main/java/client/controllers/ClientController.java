package client.controllers;

import client.network.Network;
import client.preferences.Preferences;
import data.FileInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import transport.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
public class ClientController implements Initializable {
    public ListView<FileInfo> lvFiles;
    public TextField tfPath;
    private Network net;

    private String serverHost;
    private int serverPort;
    private Stage mainPrimaryStage;
    private String userName;
    private String userPassword;
    private boolean isLogged;
    private String currentSerrverPath;
    private String localPathName;

    public void setMainPrimaryStage(Stage mainPrimaryStage) {
        this.mainPrimaryStage = mainPrimaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences pref = Preferences.getPreferences();
        userName = pref.get("userName");
        userPassword = pref.get("userPassword");
        serverHost = pref.get("serverHost");
        serverPort = Integer.parseInt(pref.get("serverPort"));
        localPathName = pref.get("localPathName");

        lvFiles.setCellFactory(new Callback<ListView<FileInfo>, ListCell<FileInfo>>() {
            @Override
            public ListCell<FileInfo> call(ListView<FileInfo> param) {
                ListCell<FileInfo> cell = new ListCell<FileInfo>(){
                    @Override
                    protected void updateItem(FileInfo item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.toString());
                        }
                    }
                };
                return cell;
            }
        });

        net = new Network(serverHost, serverPort, this::receiveMessage);
    }

    public void onbtnLoginAction(ActionEvent actionEvent) {
        login();
    }

    public void onCloseAction(WindowEvent windowEvent) {
        net.close();
        Preferences.close();
        Platform.exit();
    }

    private void login() {
        log.debug("Login...");
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
        } catch (Exception e) {
            log.error("Login exception: ", e);
        }
    }

    public void onbtnPrefAction(ActionEvent actionEvent) {
        try {
            PrefDialog prefDialog = new PrefDialog(mainPrimaryStage);
            prefDialog.setServerHost(serverHost);
            prefDialog.setServerPort(serverPort);
            prefDialog.setLocalPath(localPathName);
            if (prefDialog.execute()) {
                Preferences pref = Preferences.getPreferences();
                serverHost = prefDialog.getServerHost();
                serverPort = prefDialog.getServerPort();
                localPathName = prefDialog.getLocalPath();
                pref.put("serverHost", serverHost);
                pref.put("serverPort", String.valueOf(serverPort));
                pref.put("localPathName", localPathName);
            }
        } catch (Exception e) {
            log.error("Pref dialog Exception", e);
        }
    }

    private void receiveMessage(Message message) {
        log.debug("Receive: " + message.toString());
        switch (message.getMsg()){
            case AUTH_OK:
                log.debug("Login success.");
                isLogged = true;
                Platform.runLater(() -> net.send(new ListMessage("")));
                break;
            case AUTH_FAIL:
                log.debug("Login fail, try again.");
                isLogged = false;
                Platform.runLater(this::login);
                break;
            case LIST_REQUEST:
                currentSerrverPath = ((ListRequestMessage) message).getPathName();
                Platform.runLater(() -> {
                    tfPath.setText(String.format("@%s:/%s", userName, currentSerrverPath));
                    lvFiles.getItems().clear();
                    lvFiles.getItems().addAll(((ListRequestMessage) message).getFiles());
//                    lvFiles.getItems().addAll(
//                            ((ListRequestMessage) message).getFiles()
//                                    .stream()
//                                    .map(f -> f.toString())
//                                    .collect(Collectors.toList())
//                    );
                });
                break;
        }
    }

    public void btnLoadAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null) {
            loadFiles(files);
        }
    }

    private void loadFiles(List<File> files) {
        int count = files.size();
        if (count == 1) {
            loadFile(files.get(0), 0, 1, 1);
        } else if (count > 1){
            int id = files.hashCode();
            int index = 1;
            for (File f: files) {
                loadFile(f, id, index++, count);
            }
        }
    }

    private void loadFile(File file, int id, int index, int count) {
        Path path = Paths.get(currentSerrverPath).resolve(file.getName());
        try {
            byte[] data = Files.readAllBytes(file.toPath());
            SendMessage msg = new SendMessage(path.toString(), data);
            if (id != 0) {
                msg.setId(id);
                msg.setIndex(index);
                msg.setCount(count);
            }
            net.send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
