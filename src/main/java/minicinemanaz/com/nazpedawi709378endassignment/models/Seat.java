package minicinemanaz.com.nazpedawi709378endassignment.models;

import java.io.Serializable;

public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int row;
    private final int seatNumber;
    private boolean reserved;

    public Seat(int row, int seatNumber, boolean reserved) {
        this.row = row;
        this.seatNumber = seatNumber;
        this.reserved = reserved;
    }


    public int getRow() {
        return row;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }
}
