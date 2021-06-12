package client.controllers;

import client.network.Network;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;
import transport.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
public class ClientController implements Initializable {
    public ListView lvFiles;
    private Network net;

    private String serverHost;
    private int serverPort;
    private Stage mainPrimaryStage;
    private String userName;
    private String userPassword;
    private boolean isLogged;

    public void setMainPrimaryStage(Stage mainPrimaryStage) {
        this.mainPrimaryStage = mainPrimaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userName = "user";
        userPassword = "password";
        serverHost = "localhost";
        serverPort = 8189;



        net = new Network(serverHost, serverPort, this::receiveMessage);
    }

    public void onbtnLoginAction(ActionEvent actionEvent) {
        login();
    }

    public void onCloseAction(WindowEvent windowEvent) {
        net.close();
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
                lvFiles.getItems().clear();
                lvFiles.getItems().addAll(
                        ((ListRequestMessage) message).getFiles()
                        .stream()
                        .map(f -> f.toString())
                        .collect(Collectors.toList())
                );
                break;
        }
    }
}
