package minicinemanaz.com.nazpedawi709378endassignment.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import minicinemanaz.com.nazpedawi709378endassignment.data.Database;
import minicinemanaz.com.nazpedawi709378endassignment.models.Showing;
import minicinemanaz.com.nazpedawi709378endassignment.models.Seat;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddEditShowingDialogController implements Initializable {
    // gives warnings saying they are never assigned but, they are assigned in FXML not in code here
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
    private Label dialogTitleLabel;
    @FXML
    private Label  errorMessageLabel;

    private final Database database;
    private Showing currentShowing;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public Showing showing;
    public Showing getShowing() {
        return showing;
    }

    public AddEditShowingDialogController(Database database){
        this.database = database;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                endDatePicker.setValue(newValue); // Set end date to start date
            }
        });
    }

    public void showAddDialog() {
        dialogTitleLabel.setText("Add Showing");
        confirmButton.setText("Add");
        clearFields();
        Platform.runLater(() -> titleField.requestFocus());
    }

    private void clearFields() {
        titleField.clear();
        startDatePicker.setValue(null);
        startTimeField.clear();
        endDatePicker.setValue(null);
        endTimeField.clear();
    }

    public void showEditDialog(Showing showing) {
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
    private void onConfirmButtonClick(ActionEvent actionEvent){
        if (!validateInput()) return;
        List<Seat> seats = initializeSeats();

        if (currentShowing == null) {
            createNewShowing(seats);
        } else {
            updateExistingShowing();
        }

       closeStage(actionEvent);
    }

    private void closeStage(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        setErrorMessage("");

        if (!validateTitle()) return false;
        LocalDate startDate = getValidatedStartDate();
        if (startDate == null) return false;
        LocalTime startTime = getValidatedStartTime();
        if (startTime == null) return false;
        LocalDate endDate = getValidatedEndDate();
        if (endDate == null) return false;
        LocalTime endTime = getValidatedEndTime();
        if (endTime == null) return false;

        if (!validateTimeOrder(startDate, startTime, endDate, endTime)) return false;

        return validateShowingUniqueness(startDate, startTime, endDate, endTime);
    }

    private boolean validateTitle() {
        String title = titleField.getText();
        if (title.isEmpty()) {
            setErrorMessage("Error: Title cannot be empty.");
            return false;
        }
        return true;
    }

    private LocalDate getValidatedStartDate() {
        return parseDate(startDatePicker, "Invalid start date format. Use DD/MM/YYYY.");
    }

    private LocalTime getValidatedStartTime() {
        return parseTime(startTimeField.getText(), "Invalid start time format. Use HH:MM.");
    }

    private LocalDate getValidatedEndDate() {
        return parseDate(endDatePicker, "Invalid end date format. Use DD/MM/YYYY.");
    }

    private LocalTime getValidatedEndTime() {
        return parseTime(endTimeField.getText(), "Invalid end time format. Use HH:MM.");
    }

    private boolean validateTimeOrder(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        if (startDate.atTime(startTime).isAfter(endDate.atTime(endTime))) {
            setErrorMessage("Error: End time must be after Start time.");
            return false;
        }
        return true;
    }

    private boolean validateShowingUniqueness(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        if (database.showingExists(titleField.getText(), startDate.atTime(startTime), endDate.atTime(endTime))) {
            setErrorMessage("Error: This showing already exists.");
            return false;
        }
        return true;
    }

    private LocalDate parseDate(DatePicker datePicker, String errorMessage) {
        String dateText = datePicker.getEditor().getText();
        try {
            return LocalDate.parse(dateText, dateFormatter);
        } catch (DateTimeParseException e) {
            setErrorMessage(errorMessage);
            return null;
        }
    }

    private LocalTime parseTime(String timeString, String errorMessage) {
        try {
            return LocalTime.parse(timeString, timeFormatter);
        } catch (DateTimeParseException e) {
            setErrorMessage(errorMessage);
            return null;
        }
    }

    private void createNewShowing(List<Seat> seats) {
        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = LocalTime.parse(startTimeField.getText(), timeFormatter);
        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = LocalTime.parse(endTimeField.getText(), timeFormatter);

            showing = new Showing(titleField.getText(), startDate.atTime(startTime), endDate.atTime(endTime), seats);
            database.addShowing(showing);
    }

    private void updateExistingShowing() {
        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = LocalTime.parse(startTimeField.getText(), timeFormatter);
        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = LocalTime.parse(endTimeField.getText(), timeFormatter);

            currentShowing.setTitle(titleField.getText());
            currentShowing.setStartDate(startDate.atTime(startTime));
            currentShowing.setEndDate(endDate.atTime(endTime));
            database.updateShowing(currentShowing);
    }

    private void setErrorMessage(String message) {
        errorMessageLabel.setText(message);
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
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
    private void onCancelButtonClick(ActionEvent actionEvent){
        closeStage(actionEvent);
    }

    private List<Seat> initializeSeats() {
        List<Seat> seats = new ArrayList<>();
        int rows = 6;
        int cols = 12;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Seat seat = new Seat(row + 1, col + 1, false);
                seats.add(seat);
            }
        }
        return seats;
    }
}

