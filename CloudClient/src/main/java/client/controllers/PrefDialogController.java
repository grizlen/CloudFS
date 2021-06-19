package client.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PrefDialogController {

    public TextField tfHost;
    public TextField tfPort;
    public TextField tfPath;
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

    public void onbtnOkAction(ActionEvent actionEvent) {
        isOkClicked = true;
        dialogStage.close();
    }

    public void onbtnCancelAction(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
