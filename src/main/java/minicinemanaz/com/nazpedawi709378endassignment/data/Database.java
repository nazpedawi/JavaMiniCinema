package minicinemanaz.com.nazpedawi709378endassignment.data;
import minicinemanaz.com.nazpedawi709378endassignment.models.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Database implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization
    private static final String FILE_NAME = "database.dat"; // File for serialization
    private final List<User> users;
    private List<Showing> showings;
    private List<Sale> sales;

    public Database() {
        users = new ArrayList<>();
        users.add(new User("Alice", "Smith", "AliceSmith", "Alice$mith", UserRole.Management));
        users.add(new User("David", "Brooks", "DavidBrooks", "Dav!dbrooks", UserRole.Sales));

        showings = new ArrayList<>();
        sales = new ArrayList<>();
        load();
    }

    public List<User> getUsers() {
        return users;
    }
    public List<Showing> getShowings() {
        return showings;
    }
    public List<Sale> getSales() { return sales; }

    public User findUser(String username, String password) {
        for (User user : getUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new IllegalArgumentException("Invalid username/password combination, please try again.");
    }

    public void addShowing(Showing showing) {
        System.out.println("Adding showing: " + showing.getTitle());
        showings.add(showing);
        save();
    }

    public void removeShowing(Showing showing) {
        boolean removed = showings.remove(showing);
        if (removed) {
            save();
        }
    }

    public void updateShowing(Showing updatedShowing) {
        for (int i = 0; i < showings.size(); i++) {
            Showing existingShowing = showings.get(i);

            // Find the showing by its unique ID
            if (existingShowing.getId().equals(updatedShowing.getId())) {
                // Update the existing showing's details
                existingShowing.setTitle(updatedShowing.getTitle());
                existingShowing.setStartDate(updatedShowing.getStartDate());
                existingShowing.setEndDate(updatedShowing.getEndDate());
                existingShowing.setSeatsLeft(updatedShowing.getSeatsLeft());

                showings.set(i, existingShowing); // Replace the old showing with the updated one
                save();
                System.out.println("Showing updated successfully.");
                return;
            }
        }
        System.out.println("Showing not found. No update performed.");
    }

    public boolean showingExists(String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        for (Showing existingShowing : getShowings()) {
            if (existingShowing.getTitle().equalsIgnoreCase(title) &&
                    existingShowing.getStartDate().equals(startDateTime) &&
                    existingShowing.getEndDate().equals(endDateTime)) {
                return true; // Conflict found
            }
        }
        return false; // No conflict
    }

    public void addSale(Sale sale) {
        sales.add(sale);
        save();
    }

    public void reserveSeats(Showing showing, List<Seat> seatsToReserve) {
        // Update the reserved status of seats
        for (Seat seat : seatsToReserve) {
            seat.setReserved(true); // Mark seat as reserved
        }
        int newSeatsLeft = showing.getSeatsLeft() - seatsToReserve.size();
        showing.setSeatsLeft(newSeatsLeft); // Update seats left
        updateShowing(showing);
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(this);
            System.out.println("Database saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving database: " + e.getMessage());
        }
    }

    private void load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Database loadedDatabase = (Database) ois.readObject();
            this.showings = loadedDatabase.showings;
            this.sales = loadedDatabase.sales;
            System.out.println("Database loaded successfully. Loaded " + showings.size() + " showings, and " + sales.size() + " sales.");
        } catch (FileNotFoundException e) {
            System.out.println("No previous database found. A new one will be created.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading database: " + e.getMessage());
        }
    }
}