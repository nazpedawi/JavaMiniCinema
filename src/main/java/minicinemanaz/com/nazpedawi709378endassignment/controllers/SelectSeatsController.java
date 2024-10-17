package minicinemanaz.com.nazpedawi709378endassignment.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import minicinemanaz.com.nazpedawi709378endassignment.MiniCinemaApplication;
import minicinemanaz.com.nazpedawi709378endassignment.data.Database;
import minicinemanaz.com.nazpedawi709378endassignment.models.Sale;
import minicinemanaz.com.nazpedawi709378endassignment.models.Seat;
import minicinemanaz.com.nazpedawi709378endassignment.models.Showing;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class SelectSeatsController implements Initializable {
    // gives warnings saying they are never assigned but, they are assigned in FXML not in code here
    @FXML
    private Button cancelButton;

    @FXML
    private Label selectedShowingLabel;

    @FXML
    private GridPane seatsGrid;

    @FXML
    private ListView<String> selectedSeatsListView;

    @FXML
    private Button sellTicketsButton;

    @FXML
    private TextField customerNameField;

    @FXML
    private VBox layout;

    private final Showing selectedShowing;
    private final Set<Seat> selectedSeats = new HashSet<>();
    private final Database database;

    public SelectSeatsController(Showing selectedShowing, Database database) {
        this.selectedShowing = selectedShowing;
        this.database = database;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (selectedShowing != null) {
            selectedShowingLabel.setText("Selected Showing: " + selectedShowing.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + " " + selectedShowing.getTitle());
        }
        sellTicketsButton.setText("Sell 0 tickets");
        initializeSeatButtons();
        updateSellTicketsButtonState();
    }

    private void initializeSeatButtons() {
        int rows = 6;
        int cols = 12;
        List<Seat> showingSeats = selectedShowing.getSeats();

        for (int row = 0; row < rows; row++) {
            Label rowLabel = new Label("Row " + (row + 1));
            seatsGrid.add(rowLabel, 0, row);

            for (int col = 0; col < cols; col++) {
                int seatIndex = row * cols + col;
                Seat seat = showingSeats.get(seatIndex);  // Get the seat from the showing

                Button seatButton = new Button(String.valueOf(col + 1));

                if (seat.isReserved()) {
                    seatButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
                } else {
                    seatButton.setStyle("-fx-background-color: #808080; -fx-text-fill: white;");
                    seatButton.setOnAction(event -> onSeatSelection(seat, seatButton));
                    seatButton.setCursor(Cursor.HAND);
                }
                seatsGrid.add(seatButton, col + 1, row);
            }
        }

        customerNameField.textProperty().addListener((observable, oldValue, newValue) -> updateSellTicketsButtonState());
    }

    private void onSeatSelection(Seat seat, Button seatButton) {
        String seatInfo = "Row " + seat.getRow() + " / Seat " + seat.getSeatNumber();
        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
            selectedSeatsListView.getItems().remove(seatInfo);
            seatButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
        } else {
            selectedSeats.add(seat);
            selectedSeatsListView.getItems().add(seatInfo);
            seatButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        }

        int numberOfSeatsSelected = selectedSeats.size();
        sellTicketsButton.setText("Sell " + numberOfSeatsSelected + " ticket" + (numberOfSeatsSelected == 1 ? "" : "s"));
        updateSellTicketsButtonState();
    }

    @FXML
    private void onSellTicketsClick() throws IOException{
        String customerName = customerNameField.getText();
        int numberOfTickets = selectedSeats.size();

        Sale sale = new Sale(LocalDateTime.now(), customerName, numberOfTickets, selectedShowing);
        database.addSale(sale);
        database.reserveSeats(selectedShowing, new ArrayList<Seat>(selectedSeats));

        selectedSeats.clear();
        selectedSeatsListView.getItems().clear();
        customerNameField.clear();
        returnToSellTickets();
    }

    @FXML
    private void onCancelClick() throws IOException {
        returnToSellTickets();
    }

    private void updateSellTicketsButtonState() {
        boolean hasNoSelectedSeats = selectedSeats.isEmpty();
        boolean hasNoCustomerName = customerNameField.getText().isEmpty();
        sellTicketsButton.setDisable(hasNoSelectedSeats || hasNoCustomerName);
    }

    private void returnToSellTickets () throws IOException {
        FXMLLoader loader = new FXMLLoader(MiniCinemaApplication.class.getResource("SellTickets-view.fxml"));
        SellTicketsController controller = new SellTicketsController(database);
        loader.setController(controller);
        Scene newScene = new Scene(loader.load());
        if (!layout.getChildren().isEmpty()){
            layout.getChildren().removeAll(layout.getChildren());
        }
        layout.getChildren().add(newScene.getRoot());
    }
}