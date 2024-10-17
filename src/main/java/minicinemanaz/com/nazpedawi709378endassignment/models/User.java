package minicinemanaz.com.nazpedawi709378endassignment.models;

import java.io.Serializable;
public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    // added 'final' to them because otherwise it was giving warnings
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final UserRole role;

    public User(String firstName, String lastName,String username, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName;}

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }
}
