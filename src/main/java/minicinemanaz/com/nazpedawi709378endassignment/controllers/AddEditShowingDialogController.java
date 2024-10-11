package minicinemanaz.com.nazpedawi709378endassignment.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import minicinemanaz.com.nazpedawi709378endassignment.data.Database;
import minicinemanaz.com.nazpedawi709378endassignment.models.Showing;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddEditShowingDialogController {
    @FXML
    private TextField titleField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField startTimeField;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField endTimeField;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label dialogTitleLabel;
    @FXML
    private Label  errorMessageLabel;

    Database database;
    public Showing getShowing() {
        return showing;
    }
    private Showing currentShowing;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    public Showing showing;
    public AddEditShowingDialogController(Database database){
        this.database = database;
    }

    public void showAddDialog(Database database) {
        this.database = database;
        dialogTitleLabel.setText("Add Showing");
        confirmButton.setText("Add");
        currentShowing = null;
        clearFields();
        Platform.runLater(() -> titleField.requestFocus());

        startDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (newValue != null) {
                    endDatePicker.setValue(newValue); // Set end date to start date
                }
            }
        });
    }

    private void clearFields() {
        titleField.clear();
        startDatePicker.setValue(null);
        startTimeField.clear();
        endDatePicker.setValue(null);
        endTimeField.clear();
    }

    public void showEditDialog(Showing showing, Database database) {
        this.database = database;
        dialogTitleLabel.setText("Edit Showing");
        confirmButton.setText("Save Changes");
        currentShowing = showing;
        populateFields(showing);
        Platform.runLater(() -> titleField.requestFocus());
    }

    private void populateFields(Showing showing) {
        titleField.setText(showing.getTitle());
        startDatePicker.setValue(showing.getStartDate().toLocalDate());
        startTimeField.setText(showing.getStartDate().toLocalTime().format(timeFormatter));
        endDatePicker.setValue(showing.getEndDate().toLocalDate());
        endTimeField.setText(showing.getEndDate().toLocalTime().format(timeFormatter));
    }

    @FXML
    private void onConfirmButtonClick(ActionEvent actionEvent) throws IOException {
        String title = titleField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = null;
        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = null;

        if (title.isEmpty()) {
            errorMessageLabel.setText("Validation failed: Title cannot be empty.");
        }

        if (startDate == null) {
            errorMessageLabel.setText("Validation failed: Please select a start date.");
        }

        try {
            startTime = LocalTime.parse(startTimeField.getText(), timeFormatter);
        } catch (DateTimeParseException e) {
            errorMessageLabel.setText("Invalid start time format. Use HH:mm.");
        }

        try {
            endTime = LocalTime.parse(endTimeField.getText(), timeFormatter);
        } catch (DateTimeParseException e) {
            errorMessageLabel.setText("Invalid end time format. Use HH:mm.");
        }

        if (startDate.atTime(startTime).isAfter(endDate.atTime(endTime))) {
            errorMessageLabel.setText("Validation failed: Start time must be before end time.");
        }

        if (currentShowing == null) {
            showing = new Showing(title, startDate.atTime(startTime), endDate.atTime(endTime));
            database.addShowing(showing);
        } else {

            System.out.println("Editing existing showing: " + currentShowing.getTitle());
            currentShowing.setTitle(title);
            currentShowing.setStartDate(startDate.atTime(startTime));
            currentShowing.setEndDate(endDate.atTime(endTime));
            database.updateShowing(currentShowing);
        }
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void handleKeyPress(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            if (titleField.isFocused()) {
                startDatePicker.requestFocus();
            } else if (startDatePicker.isFocused()) {
                startTimeField.requestFocus();
            } else if (startTimeField.isFocused()) {
                endDatePicker.requestFocus();
            } else if (endDatePicker.isFocused()) {
                endTimeField.requestFocus();
            } else if (endTimeField.isFocused()) {
                confirmButton.fire();
            }
        }
    }

    @FXML
    private void onCancelButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

}

