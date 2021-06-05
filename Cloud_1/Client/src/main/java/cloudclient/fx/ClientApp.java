package cloudclient.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {

    private Stage mainPrimaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getMainPrimaryStage() {
        return mainPrimaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        mainPrimaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientmain.fxml"));
        Parent parent = loader.load();
        ClientMainController controller = loader.getController();
        controller.setMainPrimaryStage(primaryStage);
        primaryStage.setOnCloseRequest(event -> controller.exitAction());
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
}
