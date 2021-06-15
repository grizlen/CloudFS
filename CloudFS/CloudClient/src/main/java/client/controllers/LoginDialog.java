package client.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginDialog {

    LoginDialogController dialogController;
    private final Stage dialogStage;

    public LoginDialog(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/logindialog.fxml"));
        Parent parent = loader.load();
        dialogController = loader.getController();
        dialogStage = new Stage();
        dialogStage.setScene(new Scene(parent));
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        dialogController.setDialogStage(dialogStage);
    }

    public boolean execute() {
        return dialogController.show();
    }

    public void setUserName(String userName) {
        dialogController.tfUserName.setText(userName);
    }

    public void setUserPassword(String userPassword) {
        dialogController.tfUserPassword.setText(userPassword);
    }

    public String getUserName() {
        return dialogController.tfUserName.getText();
    }

    public String getUserPassword() {
        return dialogController.tfUserPassword.getText();
    }

    public boolean isNewUser() {
        return dialogController.cbNewUser.isSelected();
    }
}
