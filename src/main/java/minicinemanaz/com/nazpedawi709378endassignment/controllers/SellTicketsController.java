package minicinemanaz.com.nazpedawi709378endassignment.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import minicinemanaz.com.nazpedawi709378endassignment.MiniCinemaApplication;
import minicinemanaz.com.nazpedawi709378endassignment.data.Database;
import minicinemanaz.com.nazpedawi709378endassignment.models.Showing;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SellTicketsController implements Initializable {

    @FXML
    private TableView<Showing> showingsTableView;
    @FXML
    private TableColumn<Showing, String> startDateColumn;
    @FXML
    private TableColumn<Showing, String> endDateColumn;
    @FXML
    private TableColumn<Showing, String> titleColumn;
    @FXML
    private TableColumn<Showing, String> seatsLeftColumn;
    @FXML
    private Button selectSeatsButton;
    @FXML
    private Label selectedShowingLabel;
    @FXML
    VBox layout;
    protected ObservableList<Showing> showingsData;
    protected Database database;
    protected final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        database = new Database();
        selectSeatsButton.setDisable(true);
        showingsData = FXCollections.observableArrayList(database.getShowings());
        showingsTableView.setItems(showingsData);

        setupColumns();
        loadShowings();
    }

    protected void setupColumns() {
        startDateColumn.setCellValueFactory(cellData -> {
            Showing showing = cellData.getValue();
            return new SimpleStringProperty(showing.getStartDate().format(dateFormatter));
        });

        endDateColumn.setCellValueFactory(cellData -> {
            Showing showing = cellData.getValue();
            return new SimpleStringProperty(showing.getEndDate().format(dateFormatter));
        });

        seatsLeftColumn.setCellValueFactory(cellData -> {
            Showing showing = cellData.getValue();
            String seatsInfo = showing.getSeatsLeft() + "/" + showing.getMaxSeats();
            return new SimpleStringProperty(seatsInfo);
        });
    }
    public void loadShowings() {
        LocalDateTime now = LocalDateTime.now();
        List<Showing> allShowings = database.getShowings();
        List<Showing> upcomingShowings = new ArrayList<>();

        for (Showing showing : allShowings) {
            if (showing.getStartDate().isAfter(now)) {
                upcomingShowings.add(showing);
            }
        }
        Collections.sort(upcomingShowings);
        showingsData.setAll(upcomingShowings);
    }

    @FXML
    public void handleTableClick(MouseEvent event) {
        Showing selectedShowing = showingsTableView.getSelectionModel().getSelectedItem();
        if (selectedShowing != null) {
            selectSeatsButton.setDisable(false);
        }
        selectedShowingLabel.setText("Selected: " + selectedShowing.getStartDate().format(dateFormatter) + " " + selectedShowing.getTitle());
    }

    @FXML
    public void onSelectSeatsClick()
    {
        try {
            FXMLLoader loader = new FXMLLoader(MiniCinemaApplication.class.getResource("selectseats-view.fxml"));
            SelectSeatsController selectSeatsController = new SelectSeatsController();
            loader.setController(selectSeatsController);
            Scene newScene = new Scene(loader.load());
            layout.getChildren().clear();
            layout.getChildren().add(newScene.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
