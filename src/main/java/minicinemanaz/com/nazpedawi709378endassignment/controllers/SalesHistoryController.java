package minicinemanaz.com.nazpedawi709378endassignment.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import minicinemanaz.com.nazpedawi709378endassignment.models.Sale;
import minicinemanaz.com.nazpedawi709378endassignment.models.Showing;
import minicinemanaz.com.nazpedawi709378endassignment.data.Database;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import javafx.beans.property.SimpleStringProperty;
import java.util.ResourceBundle;
import java.net.URL;

public class SalesHistoryController implements Initializable {
    // gives warnings saying they are never assigned, but they are assigned in FXML, not in code here.
    @FXML
    private TableView<Sale> salesTableView;
    @FXML
    private TableColumn<Sale, String> saleDateTimeColumn;
    @FXML
    private TableColumn<Sale, String> showingColumn;

    protected ObservableList<Sale> salesData;
    protected Database database;
    protected final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public SalesHistoryController(Database database) {
        this.database = database;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        salesData = FXCollections.observableArrayList(database.getSales());
        salesTableView.setItems(salesData);

        setupColumns();
        Collections.sort(salesData);
    }

    protected void setupColumns() {
        saleDateTimeColumn.setCellValueFactory(cellData -> {
            Sale sale = cellData.getValue();
            return new SimpleStringProperty(sale.getSaleDate().format(dateFormatter));
        });

        showingColumn.setCellValueFactory(cellData -> {
            Sale sale = cellData.getValue();
            Showing showing = sale.getShowing();
            return new SimpleStringProperty(showing.getStartDate().format(dateFormatter) + "  " + showing.getTitle());
        });

    }
}
