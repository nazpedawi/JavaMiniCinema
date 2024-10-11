package minicinemanaz.com.nazpedawi709378endassignment.data;
import minicinemanaz.com.nazpedawi709378endassignment.models.Showing;
import minicinemanaz.com.nazpedawi709378endassignment.models.User;
import minicinemanaz.com.nazpedawi709378endassignment.models.UserRole;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.io.*;

public class Database implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization
    private static final String FILE_NAME = "database.dat"; // File for serialization

    private List<User> users;
    private List<Showing> showings;

    public Database() {
        users = new ArrayList<>();
        users.add(new User("Alice", "Smith", "AliceSmith", "Alice$mith", UserRole.Management));
        users.add(new User("David", "Brooks", "DavidBrooks", "Dav!dbrooks", UserRole.Sales));

        showings = new ArrayList<>();
        load();
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Showing> getShowings() {
        return showings;
    }

    public User findUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new IllegalArgumentException("Invalid username/password combination, please try again.");
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
            this.showings = loadedDatabase.showings; // Load showings
            System.out.println("Database loaded successfully. Loaded " + showings.size() + " showings.");
        } catch (FileNotFoundException e) {
            System.out.println("No previous database found. A new one will be created.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading database: " + e.getMessage());
            e.printStackTrace();
        }
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
                save(); // Persist the updated data
                System.out.println("Showing updated successfully.");
                return;
            }
        }
        System.out.println("Showing not found. No update performed.");
    }
}