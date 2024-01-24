package entities;

import java.util.ArrayList;

/**
 * Represents a user account in the system
 */
public class User extends Account {
    private ArrayList<String> inventory = new ArrayList<>();  // a list of item IDs; each item ID must be unique
    private ArrayList<String> wishlist = new ArrayList<>();  // a list of item IDs; each item ID must be unique
    private ArrayList<String> inventoryHistory = new ArrayList<>();
    private ArrayList<String> wishlistHistory = new ArrayList<>();
    private UserStatus status;  // an enum indicating the current status of the User
    private int points; //private int points = 0;
    private int incompleteTradeOffset;
    /**
     * Creates an instance of User
     * @param username the user's username
     * @param password the user's password
     */
    public User(String username, String password) {
        super(username, password);
        status = UserStatus.GOOD;
        points = 0;
        incompleteTradeOffset = 0;  // the number to offset the number of incomplete trades by
    }

    /**
     * Returns the user's inventory of items, represented by unique IDs
     * @return an ArrayList of Strings representing the user's inventory of items
     */
    public ArrayList<String> getInventory() {
        return inventory;
    }

    /**
     * Returns the user's inventory history
     * @return an ArrayList of Strings representing the user's historical inventory
     */
    public ArrayList<String> getInventoryHistory() {
        return this.inventoryHistory;
    }
    /**
     * Returns the user's wishlist of items, represented by unique IDs
     * @return an Arraylist of Strings representing the user's wishlist of items
     */
    public ArrayList<String> getWishlist() {
        return wishlist;
    }

    /**
     * Returns the user's wishlist history
     * @return an ArrayList of Strings representing the user's historical wishlist
     */
    public ArrayList<String> getWishlistHistory() {
        return this.wishlistHistory;
    }

    /**
     * Gets the user's status
     * @return the UserStatus enum that is associated with this user's status
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Sets the user's inventory list to a given ArrayList of item IDs
     * @param inventory an ArrayList of item IDs that this user's inventory will be set to
     */
    public void setInventory(ArrayList<String> inventory) {
        this.inventory = inventory;
    }

    /**
     * Sets the user's historical inventory list to a given ArrayList of item IDs
     * @param inventoryHistory an ArrayList of item IDs that this user's inventory history will be set to
     */
    public void setInventoryHistory(ArrayList<String> inventoryHistory) {
        this.inventoryHistory = inventoryHistory;
    }

    /**
     * Sets the user's wishlist list to a given ArrayList of item IDs
     * @param wishlist an ArrayList of item IDs that this user's wishlist will be set to
     */
    public void setWishlist(ArrayList<String> wishlist) {
        this.wishlist = wishlist;
    }

    /**
     * Sets the user's historical wishlist list to a given ArrayList of item IDs
     * @param wishlistHistory an ArrayList of item IDs that this user's wishlist history will be set to
     */
    public void setWishlistHistory(ArrayList<String> wishlistHistory) {
        this.wishlistHistory = wishlistHistory;
    }

    /**
     * Sets the user's status
     * @param status the new status of the user
     */
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * Sets the user's points
     * @param points the new points value of the user
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the user's points
     * @return the points value of the user
     */
    public int getPoints() {
        return points;
    }

    /**
     * Adds a set amount of points to the user
     * @param points the points amount
     */
    public void addPoints(int points) {
        this.points = this.points + points;
        if (points >= 500 && getStatus() == UserStatus.GOOD) {setStatus(UserStatus.PREMIUM); }
    }

    /**
     * Deducts a set amount of points from the
     * @param points the points amount
     */
    public void removePoints(int points) {
        this.points = Math.max(this.points - points, 0);
        if (points < 500 && getStatus() == UserStatus.PREMIUM) {
            setStatus(UserStatus.GOOD);
        }
    }

    /**
     * Gets the incomplete trade offset of this user
     * @return the incomplete trade offset of this user
     */
    public int getIncompleteTradeOffset() {
        return this.incompleteTradeOffset;
    }

    /**
     * Sets the incomplete trade offset of this user
     * @param newOffset the new incomplete trade offset
     */
    public void setIncompleteTradeOffset(int newOffset) {
        this.incompleteTradeOffset = newOffset;
    }

    /**
     * Returns a String that describes this user by their username and current status
     * @return a String representation of the user
     */
    @Override
    public String toString() {
        return "username: " + getUsername() + "\naccount type: user \naccount status: " + status;
    }

}
