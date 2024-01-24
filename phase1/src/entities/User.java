package entities;

import java.util.ArrayList;

/**
 * Represents a user account in the system
 */
public class User extends Account {
    private ArrayList<String> inventory = new ArrayList<>();  // a list of item IDs; each item ID must be unique
    private ArrayList<String> wishlist = new ArrayList<>();  // a list of item IDs; each item ID must be unique
    private UserStatus status;  // an enum indicating the current status of the User

    /**
     * Creates an instance of User
     * @param username the user's username
     * @param password the user's password
     */
    public User(String username, String password) {
        super(username, password);
        status = UserStatus.GOOD;
    }

    /**
     * Returns the user's inventory of items, represented by unique IDs
     * @return an ArrayList of Strings representing the user's inventory of items
     */
    public ArrayList<String> getInventory() {
        return inventory;
    }

    /**
     * Returns the user's wishlist of items, represented by unique IDs
     * @return an Arraylist of Strings representing the user's wishlist of items
     */
    public ArrayList<String> getWishlist() {
        return wishlist;
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
     * Sets the user's wishlist list to a given ArrayList of item IDs
     * @param wishlist an ArrayList of item IDs that this user's wishlist will be set to
     */
    public void setWishlist(ArrayList<String> wishlist) {
        this.wishlist = wishlist;
    }

    /**
     * Adds an item ID to the user's inventory if it doesn't already exist
     * @param itemId the item ID of an Item that this user is willing to lend
     * @return true iff the item ID is added to the user's inventory
     */
    public boolean addToInventory(String itemId) {
        if (inventory.contains(itemId)) {
            return false;
        } else {
            inventory.add(itemId);
            return true;
        }
    }

    /**
     * Removes an item ID from the user's inventory if it exists
     * @param itemId the item ID of an Item that this user would like to remove from their inventory
     * @return true iff the item ID is present in and removed from the user's inventory
     */
    public boolean removeFromInventory(String itemId) {
        if (!inventory.contains(itemId)) {
            return false;
        } else {
            inventory.remove(itemId);
            return true;
        }
    }

    /**
     * Adds an item ID to the user's wishlist if it doesn't already exist
     * @param itemId the item ID of an Item that this user is wishing to borrow
     * @return true iff the item ID is added to the user's wishlist
     */
    public boolean addToWishlist(String itemId) {
        if (wishlist.contains(itemId)) {
            return false;
        } else {
            wishlist.add(itemId);
            return true;
        }
    }

    /**
     * Removes an item ID from the user's wishlist if it exists
     * @param itemId the item ID of an Item that this user would like to remove from their wishlist
     * @return true iff the item ID is present in and removed from the user's wishlist
     */
    public boolean removeFromWishlist(String itemId) {
        if (!wishlist.contains(itemId)) {
            return false;
        } else {
            wishlist.remove(itemId);
            return true;
        }
    }

    /**
     * Sets the user's status
     * @param status the new status of the user
     */
    public void setStatus(UserStatus status) {
        this.status = status;
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
