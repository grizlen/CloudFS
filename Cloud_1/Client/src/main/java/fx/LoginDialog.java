package fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginDialog {

    private LoginController controller;
    private Stage dialogStage;

    public LoginDialog(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/logindialog.fxml"));
        Parent parent = loader.load();
        controller = loader.getController();
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        dialogStage.setScene(new Scene(parent));
    }

    public void show(){
        dialogStage.showAndWait();
    }
}
