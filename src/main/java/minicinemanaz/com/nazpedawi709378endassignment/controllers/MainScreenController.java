package minicinemanaz.com.nazpedawi709378endassignment.controllers;

import minicinemanaz.com.nazpedawi709378endassignment.MiniCinemaApplication;
import minicinemanaz.com.nazpedawi709378endassignment.data.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainScreenController {
    // gives warnings saying they are never assigned, but they are assigned in FXML, not in code here.
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

    private final Database database;

    public MainScreenController(Database database) {
        this.database = database;
    }

    public void initializeMainScreen(User user) {
        layout.getChildren().add(createLabelsVBox(user)); // add labels to welcome the logged-in user
        showButtonsBasedOnRole(user.getRole()); // show specific buttons based on the logged-in user's role
    }

    private VBox createLabelsVBox(User user) {
        VBox labelsVBox = new VBox();
        labelsVBox.setAlignment(Pos.CENTER);
        labelsVBox.setPadding(new Insets(50, 0, 0, 0));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String currentDateTime = LocalDateTime.now().format(formatter);

        Label usernameLabel = createLabel("Welcome " + user.getFullName() + "!", 20);
        Label roleLabel = createLabel("You are logged in as " + user.getRole().name(), 14);
        Label datetimeLabel = createLabel("The current date and time is " + currentDateTime, 14);

        labelsVBox.getChildren().addAll(usernameLabel, roleLabel, datetimeLabel);
        return labelsVBox;
    }

    // for code reusability
    private Label createLabel(String text, int fontSize) {
        Label label = new Label(text);
        label.setFont(Font.font("System", fontSize));
        label.setStyle("-fx-font-size: " + fontSize + ";");
        return label;
    }

    @FXML
    public void onManageShowingsClick() {
        manageShowingsButton.setStyle("-fx-background-color: white; -fx-text-fill: green;");
        viewSalesHistoryButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        loadView("ManageShowings-view.fxml", new ManageShowingsController(database));
    }

    @FXML
    public void onSellTicketsClick() {
        sellTicketsButton.setStyle("-fx-background-color: white; -fx-text-fill: green;");
        loadView("SellTickets-view.fxml", new SellTicketsController(database));
    }

    @FXML
    public void onViewSalesHistoryClick() {
        viewSalesHistoryButton.setStyle("-fx-background-color: white; -fx-text-fill: green;");
        manageShowingsButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        loadView("SalesHistory-view.fxml", new SalesHistoryController(database));
    }

    public void loadView(String fxmlFileName, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(MiniCinemaApplication.class.getResource(fxmlFileName));
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            if (layout.getChildren().size() > 1)
                layout.getChildren().remove(1); // remove all the content of the bottom half of the screen
            layout.getChildren().add(scene.getRoot()); // add the new scene to the bottom half
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showButtonsBasedOnRole(UserRole role) {
        buttonContainer.getChildren().clear();
        if (role == UserRole.Management) {
            buttonContainer.getChildren().add(manageShowingsButton);
            buttonContainer.getChildren().add(viewSalesHistoryButton);
        } else if (role == UserRole.Sales) {
            buttonContainer.getChildren().add(sellTicketsButton);
        }
    }
}
