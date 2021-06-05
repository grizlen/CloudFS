package fx;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import service.Pref;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientMainController implements Initializable {

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void btnLoadAction(ActionEvent actionEvent) {
        System.out.println("Загрузить");
    }

    public void btnSaveAction(ActionEvent actionEvent) {
        System.out.println("Скачать");
    }

    public void btnDeleteAction(ActionEvent actionEvent) {
        System.out.println("Удалить");
    }

    public void btnLoginAction(ActionEvent actionEvent) {
        System.out.println("Авторизация");
        try {
            LoginDialog loginDialog = new LoginDialog(primaryStage);
            loginDialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnUpAction(ActionEvent actionEvent) {
        System.out.println("Вверх");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String userName = Pref.getPref().getValue("userName");
        String userPassword = Pref.getPref().getValue("userPassword");
        System.out.println("User = " + userName);
        System.out.println("Password = " + userPassword);

    }

    public void exitAction() {
        Pref.close();
    }
}
