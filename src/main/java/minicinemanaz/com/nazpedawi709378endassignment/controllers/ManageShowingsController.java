package minicinemanaz.com.nazpedawi709378endassignment.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import minicinemanaz.com.nazpedawi709378endassignment.MiniCinemaApplication;
import minicinemanaz.com.nazpedawi709378endassignment.data.Database;
import minicinemanaz.com.nazpedawi709378endassignment.models.Showing;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ManageShowingsController implements Initializable {
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
    private Button editShowingButton;
    @FXML
    private Button deleteShowingButton;
    @FXML
    private Label errorMessageLabel;

    protected ObservableList<Showing> showingsData;
    protected Database database;
    protected final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editShowingButton.setDisable(true);
        deleteShowingButton.setDisable(true);
        database = new Database();
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
    public void onAddShowingClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(MiniCinemaApplication.class.getResource("AddEditShowingDialog.fxml")); // Ensure this path is correct
            AddEditShowingDialogController addEditController = new AddEditShowingDialogController(database);
            loader.setController(addEditController);
            Scene scene = new Scene(loader.load());
            addEditController.showAddDialog(database);
            Stage dialog = new Stage();
            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
            if (addEditController.getShowing() != null) {
                showingsData.add(addEditController.getShowing());
            }
        } catch (IOException e) {
            System.out.println("Error loading AddEditShowingDialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditShowingClick() {
        Showing selectedShowing = showingsTableView.getSelectionModel().getSelectedItem();
        if (selectedShowing != null) {
            try {
                FXMLLoader loader = new FXMLLoader(MiniCinemaApplication.class.getResource("AddEditShowingDialog.fxml"));
                AddEditShowingDialogController addEditController = new AddEditShowingDialogController(database);
                loader.setController(addEditController);
                Scene scene = new Scene(loader.load());
                addEditController.showEditDialog(selectedShowing,database);
                Stage dialog = new Stage();
                dialog.setScene(scene);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
                loadShowings();
            } catch (IOException e) {
                System.out.println("Error loading AddEditShowingDialog: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onDeleteShowingClick() {
        Showing selectedShowing = showingsTableView.getSelectionModel().getSelectedItem();
        if (selectedShowing != null) {
            if (selectedShowing.hasTicketsSold()) {
                errorMessageLabel.setText("Cannot delete, Tickets have already been sold for this showing.");
            } else {
               database.removeShowing(selectedShowing);
                loadShowings(); // Refresh the table
                errorMessageLabel.setText("");
            }
        }
    }

    @FXML
    public void handleTableClick(MouseEvent event) {
        Showing selectedShowing = showingsTableView.getSelectionModel().getSelectedItem();
        if (selectedShowing != null) {
            editShowingButton.setDisable(false);
            deleteShowingButton.setDisable(false);
        }
    }
}
