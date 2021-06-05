package fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientmain.fxml"));
        Parent parent = loader.load();
        ClientMainController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setOnCloseRequest(event -> controller.exitAction());
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
}
