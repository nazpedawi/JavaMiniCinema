package minicinemanaz.com.nazpedawi709378endassignment.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Sale implements Serializable, Comparable<Sale> {
    private static final long serialVersionUID = 1L;
    private LocalDateTime saleDate;
    private String customerName;
    private int numberOfTickets;
    private Showing showing;

    public Sale(LocalDateTime saleDate, String customerName, int numberOfTickets, Showing showing) {
        this.saleDate = saleDate;
        this.customerName = customerName;
        this.numberOfTickets = numberOfTickets;
        this.showing = showing;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public Showing getShowing() {
        return showing;
    }

    @Override
    public int compareTo(Sale other) {
        return this.saleDate.compareTo(other.saleDate);
    }
}
