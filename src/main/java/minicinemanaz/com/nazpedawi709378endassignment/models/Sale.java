package minicinemanaz.com.nazpedawi709378endassignment.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Sale implements Serializable, Comparable<Sale> {
    private static final long serialVersionUID = 1L;
    private final LocalDateTime saleDate;
    private final String customerName;
    private final int numberOfTickets;
    private final Showing showing;

    public Sale(LocalDateTime saleDate, String customerName, int numberOfTickets, Showing showing) {
        this.saleDate = saleDate;
        this.customerName = customerName;
        this.numberOfTickets = numberOfTickets;
        this.showing = showing;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    // it is accessed by the sales history Table View in FXML but not in code, that's why it gives a warning
    public String getCustomerName() {
        return customerName;
    }

    // it is accessed by the sales history Table View in FXML but not in code, that's why it gives a warning
    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public Showing getShowing() {
        return showing;
    }

    // to see newly added sale at the top of the table view
    @Override
    public int compareTo(Sale other) {
        return other.getSaleDate().compareTo(this.getSaleDate());
    }
}
