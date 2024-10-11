package minicinemanaz.com.nazpedawi709378endassignment.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.time.format.DateTimeFormatter;

public class SelectSeatsController {

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

    private Showing selectedShowing;
    private Seat[][] seats;
    private Set<Seat> selectedSeats = new HashSet<>();
    private Database database;
    private VBox previousLayout;
    public SelectSeatsController(Showing selectedShowing, Database database, VBox previousLayout) {
        this.selectedShowing = selectedShowing;
        this.database = database;
        this.previousLayout = previousLayout;
    }

    @FXML
    public void initialize() {
        if (selectedShowing != null) {
            selectedShowingLabel.setText("Selected Showing: " + selectedShowing.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + " " + selectedShowing.getTitle());
        }
        sellTicketsButton.setText("Sell 0 tickets");
        initializeSeatButtons();
    }
    private void initializeSeatButtons() {
        int rows = 6;
        int cols = 12;
        seats = new Seat[rows][cols];

        for (int row = 0; row < rows; row++) {
            Label rowLabel = new Label("Row " + (row + 1));
            seatsGrid.add(rowLabel, 0, row);

            for (int col = 0; col < cols; col++) {
                Seat seat = new Seat(row + 1, col + 1, false);
                seats[row][col] = seat;

                Button seatButton = new Button(String.valueOf(col + 1));
                seatButton.setPrefWidth(30);
                seatButton.setPrefHeight(30);
                seatButton.setCursor(Cursor.HAND);

                if (seat.isReserved()) {
                    seatButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;"); // Reserved seat style (red)
                    seatButton.setDisable(true); // Disable button for reserved seats
                } else {
                    seatButton.setStyle("-fx-background-color: #808080; -fx-text-fill: white;"); // Available seat style (grey)
                }

                seatButton.setOnAction(event -> onSeatSelection(seat, seatButton));

                seatsGrid.add(seatButton, col + 1, row);
            }
        }
    }

    private void onSeatSelection(Seat seat, Button seatButton) {
        String seatInfo = "Row " + seat.getRow() + "/ Seat " + seat.getSeatNumber();
        if (selectedSeats.contains(seat)) {
            // Seat was already selected, so deselect it
            selectedSeats.remove(seat);
            selectedSeatsListView.getItems().remove(seatInfo);
            seatButton.setStyle("-fx-background-color: #808080; -fx-text-fill: white;"); // Set back to grey for unreserved seats
        } else {
            // Seat is being selected
            selectedSeats.add(seat);
            selectedSeatsListView.getItems().add(seatInfo);
            seatButton.setStyle("-fx-background-color: #00FF00; -fx-text-fill: white;"); // Set to green for selected seats
        }

        int numberOfSeatsSelected = selectedSeats.size();
        sellTicketsButton.setText("Sell " + numberOfSeatsSelected + " ticket" + (numberOfSeatsSelected == 1 ? "" : "s"));
    }

    @FXML
    private void onSellTicketsClick(ActionEvent event) {
        String customerName = customerNameField.getText();
        int numberOfTickets = selectedSeats.size();

        Sale sale = new Sale(LocalDateTime.now(), customerName, numberOfTickets, selectedShowing);
        database.addSale(sale);
        selectedShowing.reduceSeats(numberOfTickets);
        selectedSeats.clear();
        selectedSeatsListView.getItems().clear();
        customerNameField.clear();
        sellTicketsButton.setText("Sell 0 tickets");
    }

    @FXML
    private void onCancelClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(MiniCinemaApplication.class.getResource("selltickets-view.fxml"));
        SellTicketsController controller = new SellTicketsController();
        loader.setController(controller);
        Scene newScene = new Scene(loader.load());
        previousLayout.getChildren().clear();
        previousLayout.getChildren().add(newScene.getRoot());
    }
}