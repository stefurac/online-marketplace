package entities;

import java.util.UUID;

/**
 * Represents an account in the system; an abstract class that is extended by the User and Admin classes
 */
public abstract class Account {
    private String id;
    // private ArrayList<String> oldUsernames;
    //private String email;
    private String oldUsername;
    private String oldPassword;
    private String username;
    private String password;

    /**
     * Creates an Account
     * @param username the username of the account
     * @param password the password of the account
     */
    public Account(String username, String password) {
        UUID inputId = UUID.randomUUID();  // creates a UUID at instantiation
        id = String.valueOf(inputId);
        this.oldUsername = username; //oldUsername is set to current upon instantiation
        this.oldPassword = password;
        this.username = username;
        this.password = password;
    }
    // == GETTERS ==
    /**
     * Gets the account's ID
     * @return a String that represents the account's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the account's old username
     * @return the account's old username
     */
    public String getOldUsername() {
        return this.oldUsername;
    }

    /**
     * Gets the account's old password
     * @return the account's old password
     */
    public String getOldPassword() {
        return this.oldPassword;
    }

    /**
     * Gets the account's username
     * @return a String that represents the account's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the account's password
     * @return a String that represents the account's password
     */
    public String getPassword() {
        return password;
    }


    // == SETTERS ==

    /**
     * Changes this account's ID; for gateway use only
     * @param accountId the new ID of this account
     */
    public void setId(String accountId) {
        this.id = accountId;
    }

    /**
     * Sets this account's old username
     * @param username the old username
     */
    public void setOldUsername(String username) {
        this.oldUsername = username;
    }

    /**
     * Sets this account's old password
     * @param password the old password
     */
    public void setOldPassword(String password) {
        this.oldPassword = password;
    }

    /**
     * Changes this account's username
     * @param username the new username of this account
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Changes this account's password
     * @param password the new password of this account
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns a String that describes this account; abstract method to be implemented in the User and Admin classes
     * @return a String representation of the account
     */
    public abstract String toString();
}
