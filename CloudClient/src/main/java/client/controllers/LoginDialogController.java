package client.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginDialogController {
    public TextField tfUserName;
    public TextField tfUserPassword;
    public CheckBox cbNewUser;
    private Stage dialogStage;
    private boolean isOkClicked;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean show() {
        isOkClicked = false;
        dialogStage.showAndWait();
        return isOkClicked;
    }

    public void btnOkAction(ActionEvent actionEvent) {
        isOkClicked = true;
        dialogStage.close();
    }

    public void btnCancelAction(ActionEvent actionEvent) {
        isOkClicked =false;
        dialogStage.close();
    }
}
