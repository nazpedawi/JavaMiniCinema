package minicinemanaz.com.nazpedawi709378endassignment.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import minicinemanaz.com.nazpedawi709378endassignment.MiniCinemaApplication;
import minicinemanaz.com.nazpedawi709378endassignment.data.Database;
import minicinemanaz.com.nazpedawi709378endassignment.models.Showing;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.ResourceBundle;

public class ManageShowingsController implements Initializable {
    // gives warnings saying they are never assigned, but they are assigned in FXML, not in code here.
    @FXML
    private TableView<Showing> showingsTableView;
    @FXML
    private TableColumn<Showing, String> startDateColumn;
    @FXML
    private TableColumn<Showing, String> endDateColumn;
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

    public ManageShowingsController(Database database) {
        this.database = database;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editShowingButton.setDisable(true);
        deleteShowingButton.setDisable(true);
        setupColumns();
        loadShowings();
    }

    // format the columns in a specific way, display dates in dd-mm-yyyy format and seats left in --/-- format
    protected void setupColumns() {
        startDateColumn.setCellValueFactory(cellData -> createStringProperty(cellData.getValue().getStartDate().format(dateFormatter)));
        endDateColumn.setCellValueFactory(cellData -> createStringProperty(cellData.getValue().getEndDate().format(dateFormatter)));
        seatsLeftColumn.setCellValueFactory(cellData -> {
            Showing showing = cellData.getValue();
            return createStringProperty(showing.getSeatsLeft() + "/" + showing.getMaxSeats());
        });
    }

    private SimpleStringProperty createStringProperty(String value) {
        return new SimpleStringProperty(value);
    }

    public void loadShowings() {
        showingsData = FXCollections.observableArrayList(database.getShowings());
        Collections.sort(showingsData); // sort the showings in ascending order
        showingsTableView.setItems(showingsData);
    }

    @FXML
    public void onAddShowingClick() {
        showDialog(null);
    }

    @FXML
    public void onEditShowingClick() {
        Showing selectedShowing = showingsTableView.getSelectionModel().getSelectedItem();
        if (selectedShowing != null) {
            showDialog(selectedShowing);
        }
    }

    private void showDialog(Showing showing) {
        try {
            FXMLLoader loader = new FXMLLoader(MiniCinemaApplication.class.getResource("AddEditShowingDialog.fxml"));
            AddEditShowingDialogController addEditController = new AddEditShowingDialogController(database);
            loader.setController(addEditController);
            Scene scene = new Scene(loader.load());

            if (showing == null) {
                addEditController.showAddDialog(); // display add new showing dialog if showing is null
            } else {
                addEditController.showEditDialog(showing); // display edit dialog if showing is provided
            }

            Stage dialog = new Stage();
            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
            loadShowings();
            showingsTableView.refresh();
            reset();
        } catch (IOException e) {
            System.out.println("Error loading Add/Edit Showing Dialog: " + e.getMessage());
        }
    }

    @FXML
    public void onDeleteShowingClick() {
        Showing selectedShowing = showingsTableView.getSelectionModel().getSelectedItem();
        if (selectedShowing != null) {
           if (selectedShowing.hasTicketsSold()) {
                errorMessageLabel.setText("Error: Cannot delete the selected showing, tickets have already been sold.");
            } else {
               database.removeShowing(selectedShowing);
                loadShowings();
               errorMessageLabel.setText("");
            }
            reset();
        }
    }

    // to enable the edit and delete buttons when a showing is selected
    @FXML
    public void handleTableClick() {
        Showing selectedShowing = showingsTableView.getSelectionModel().getSelectedItem();
        if (selectedShowing != null) {
            editShowingButton.setDisable(false);
            deleteShowingButton.setDisable(false);
            errorMessageLabel.setText("");
        }
    }

    private void reset(){
        editShowingButton.setDisable(true);
        deleteShowingButton.setDisable(true);
        showingsTableView.getSelectionModel().clearSelection();
    }
}
