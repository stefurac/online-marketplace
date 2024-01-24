package entities;

/**
 * Represents an administrative account in this system
 */
public class Admin extends Account {

    /**
     * Creates an instance of Admin
     * @param username the Admin's username
     * @param password the Admin's password
     */
    public Admin(String username, String password) {
        super(username, password);
    }

    /**
     * Represents the current admin by their username and account type
     * @return the username and account type separated by a newline
     */
    @Override
    public String toString() {
        return "username: " + getUsername() + "\naccount type: administrative";
    }
}