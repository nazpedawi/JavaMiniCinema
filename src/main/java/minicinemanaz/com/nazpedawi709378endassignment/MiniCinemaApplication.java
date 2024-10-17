package minicinemanaz.com.nazpedawi709378endassignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import minicinemanaz.com.nazpedawi709378endassignment.controllers.LoginController;
import minicinemanaz.com.nazpedawi709378endassignment.data.Database;

import java.io.IOException;

public class MiniCinemaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Database database = new Database();
        FXMLLoader fxmlLoader = new FXMLLoader(MiniCinemaApplication.class.getResource("Login-view.fxml"));
        fxmlLoader.setController(new LoginController(database));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}