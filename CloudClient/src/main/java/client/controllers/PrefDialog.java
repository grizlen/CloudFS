package client.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrefDialog {

    private PrefDialogController dialogController;

    public PrefDialog(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/prefdialog.fxml"));
        Parent parent = loader.load();
        dialogController = loader.getController();
        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(parent));
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        dialogController.setDialogStage(dialogStage);
    }

    public boolean execute() {
        return dialogController.show();
    }

    public void setServerHost(String serverHost) {
        dialogController.tfHost.setText(serverHost);
    }

    public void setServerPort(int serverPort) {
        dialogController.tfPort.setText(String.valueOf(serverPort));
    }

    public String getServerHost() {
        return dialogController.tfHost.getText();
    }

    public int getServerPort() {
        return Integer.parseInt(dialogController.tfPort.getText());
    }

    public void setLocalPath(String localPathName) {
        dialogController.tfPath.setText(localPathName);
    }

    public String getLocalPath() {
        return dialogController.tfPath.getText();
    }
}
