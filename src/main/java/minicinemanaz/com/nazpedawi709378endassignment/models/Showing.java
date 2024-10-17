package minicinemanaz.com.nazpedawi709378endassignment.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Showing implements Serializable, Comparable<Showing> {
    private static final long serialVersionUID = 1L;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int seatsLeft;
    private final String id;
    private final List<Seat> seats;

    public Showing(String title, LocalDateTime startDate, LocalDateTime endDate, List<Seat> seats) {
        this.id = UUID.randomUUID().toString(); // generates a random id for every showing when it gets created,
                                                // this id is used later to edit the showing in 'manage showings'
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.seats = seats;
        this.seatsLeft = seats.size();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }

    public int getMaxSeats() {
        return seats.size();
    }

    public boolean hasTicketsSold() {
        return seatsLeft < seats.size();
    }  // if seats left are less than 72 that means tickets have already been sold

    public void setSeatsLeft(int seatsLeft) {
        this.seatsLeft = seatsLeft;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    @Override
    public int compareTo(Showing other) {
        return this.getStartDate().compareTo(other.getStartDate());
    }
}
