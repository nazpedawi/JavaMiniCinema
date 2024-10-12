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
    private final int maxSeats = 72;
    private String id;
    private List<Seat> seats;

    public Showing(String title, LocalDateTime startDate, LocalDateTime endDate, List<Seat> seats) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.seatsLeft = maxSeats;
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
        return maxSeats;
    }
    public boolean hasTicketsSold() {
        return seatsLeft < maxSeats;
    }
    public void setSeatsLeft(int seatsLeft) {
        this.seatsLeft = seatsLeft;
    }
    public List<Seat> getSeats() {
        return seats;
    }
    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public int compareTo(Showing other) {
        return this.startDate.compareTo(other.startDate);
    }
}
