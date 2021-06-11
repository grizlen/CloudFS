package client.controllers;

import client.network.Network;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;
import transport.AuthMessage;
import transport.AuthNewMessage;
import transport.Message;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class ClientController implements Initializable {
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
                userName = loginDialog.getUserName();
                userPassword = loginDialog.getUserPassword();
                if (loginDialog.isNewUser()) {
                    net.send(new AuthNewMessage(userName, userPassword));
                }
                net.send(new AuthMessage(userName, userPassword));
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
                break;
            case AUTH_FAIL:
                log.debug("Login fail, try again.");
                isLogged = false;
                Platform.runLater(this::login);
                break;
        }
    }
}
