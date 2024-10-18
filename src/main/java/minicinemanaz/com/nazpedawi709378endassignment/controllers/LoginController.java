package minicinemanaz.com.nazpedawi709378endassignment.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import minicinemanaz.com.nazpedawi709378endassignment.MiniCinemaApplication;
import minicinemanaz.com.nazpedawi709378endassignment.data.Database;
import minicinemanaz.com.nazpedawi709378endassignment.models.User;

public class LoginController {
    // gives warnings saying they are never assigned, but they are assigned in FXML, not in code here.
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;

    private final Database database;

    public LoginController(Database database) {
        this.database = database;
    }

    @FXML
    private void onLoginButtonClick() {
        // get user's input
        String username = usernameField.getText();
        String password = passwordField.getText();

        handleLogin(username, password);
    }

    private void handleLogin(String username, String password) {
        try {
            // Validate username and password
            User user = database.findUser(username, password);
            errorLabel.setOpacity(0); // hide error message if the username and password are correct
            loadMainScreen(user); // load the main screen with a greeting of the logged-in user
            closeLoginStage();
        } catch (IllegalArgumentException e) {
                errorLabel.setText(e.getMessage());  // if user and password don't match, display error message
                errorLabel.setOpacity(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void loadMainScreen(User user) throws Exception {
        FXMLLoader loader = new FXMLLoader(MiniCinemaApplication.class.getResource("MainScreen-view.fxml"));
        MainScreenController mainScreenController = new MainScreenController(database);
        loader.setController(mainScreenController);
        Parent mainScreenRoot = loader.load();
        mainScreenController.initializeMainScreen(user);

        Stage mainStage = new Stage();
        Scene mainscreenScene = new Scene(mainScreenRoot);
        mainStage.setScene(mainscreenScene);
        mainStage.show();
    }

    private void closeLoginStage() {
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();
    }
}