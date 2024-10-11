package minicinemanaz.com.nazpedawi709378endassignment.controllers;

import minicinemanaz.com.nazpedawi709378endassignment.MiniCinemaApplication;
import minicinemanaz.com.nazpedawi709378endassignment.data.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import minicinemanaz.com.nazpedawi709378endassignment.models.User;
import minicinemanaz.com.nazpedawi709378endassignment.models.UserRole;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML
    private Button manageShowingsButton;
    @FXML
    private Button viewSalesHistoryButton;
    @FXML
    private Button sellTicketsButton;
    @FXML
    private HBox buttonContainer;

    @FXML
    VBox layout;

    private VBox labelsVBox;
    private Label usernameLabel;
    private Label roleLabel;
    private Label datetimeLabel;

    private Database database;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public void initialize(URL url, ResourceBundle resourceBundle) {
        database = new Database();
    }

    public void initializeMainScreen(User user) {
        labelsVBox = new VBox();
        labelsVBox.setAlignment(Pos.CENTER);
        labelsVBox.setPadding(new Insets(50, 0, 0, 0));

        String currentDateTime = LocalDateTime.now().format(formatter);

        usernameLabel = new Label();
        usernameLabel.setFont(Font.font("System", 20));
        usernameLabel.setStyle("-fx-font-size: 18;");
        usernameLabel.setText("Welcome " + user.getFullName() + "!");

        roleLabel = new Label();
        roleLabel.setFont(Font.font(14));
        roleLabel.setStyle("-fx-font-size: 18;");
        roleLabel.setText("You are logged in as " + user.getRole().name());

        datetimeLabel = new Label();
        datetimeLabel.setFont(Font.font(14));
        datetimeLabel.setText("The current date and time is " + currentDateTime);

        labelsVBox.getChildren().addAll(usernameLabel, roleLabel, datetimeLabel);

        layout.getChildren().add(labelsVBox);

        showButtonsBasedOnRole(user.getRole());
    }

    @FXML
    public void onManageShowingsClick(ActionEvent actionEvent) {
        manageShowingsButton.setStyle("-fx-background-color: white; -fx-text-fill: green;");
        loadView("manageshowings-view.fxml", new ManageShowingsController());
    }

    public void onSellTicketsClick(ActionEvent actionEvent) {
        sellTicketsButton.setStyle("-fx-background-color: white; -fx-text-fill: green;");
        loadView("selltickets-view.fxml", new SellTicketsController());
    }

    private void loadView(String fxmlFileName, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(MiniCinemaApplication.class.getResource(fxmlFileName));
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            if (layout.getChildren().size() > 1)
                layout.getChildren().remove(1);
            layout.getChildren().add(scene.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showButtonsBasedOnRole(UserRole role) {
        buttonContainer.getChildren().clear();
        if (role == UserRole.Management) {
            buttonContainer.getChildren().add(manageShowingsButton); //
            buttonContainer.getChildren().add(viewSalesHistoryButton);
        } else if (role == UserRole.Sales) {
            buttonContainer.getChildren().add(sellTicketsButton);
        }
    }
}
