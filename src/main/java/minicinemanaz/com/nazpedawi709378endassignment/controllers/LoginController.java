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
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    private Database database;

    public LoginController() {
        database = new Database();
    }

    @FXML
    private void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = database.findUser(username, password);

            errorLabel.setOpacity(0);

            FXMLLoader loader = new FXMLLoader(MiniCinemaApplication.class.getResource("mainscreen-view.fxml"));
            Parent mainscreenRoot = loader.load();

            MainScreenController mainScreenController = loader.getController();
            // Initialize with user information
            mainScreenController.initializeMainScreen(user);

            // Get the current stage and switch to the new scene
            Stage mainStage = new Stage();
            Scene mainscreenScene = new Scene(mainscreenRoot);
            mainStage.setScene(mainscreenScene);

            mainStage.show();

            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setOpacity(1); // Show error message
        } catch (Exception e) {
            e.printStackTrace(); // Handle other exceptions
        }
    }
}