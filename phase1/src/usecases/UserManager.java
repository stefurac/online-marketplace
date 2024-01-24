package usecases;

import entities.User;
import entities.UserStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the system of Users
 */
public class UserManager {
    private HashMap<String, User> userIdToUser = new HashMap<>();  // a HashMap with user IDs as keys and User entities as values

    /**
     * Creates an instance of UserManager with a list of Users, with an initial list of Users contained in the input parameter
     * @param allUsers an ArrayList of Users that the UserManager instance will initially contain
     */
    public UserManager(ArrayList<User> allUsers) {
        for (User user: allUsers) {
            this.userIdToUser.put(user.getId(), user);
        }
    }

    /**
     * Return a HashMap of all Users, with user IDs as keys and User entities as values
     * @return HashMap<String, User> of all Users in the system
     */
    public HashMap<String, User> getUserIdToUser() {
        return this.userIdToUser;
    }

    /**
     * Adds a User to the system
     * @param newUsername the username of a new User account
     * @param newPassword the password of a new User account
     */
    public void addUser(String newUsername, String newPassword) {
        if (checkUsername(newUsername)) {
            User newUser = new User(newUsername, newPassword);
            getUserIdToUser().put(newUser.getId(), newUser);
        }
    }

    /**
     * Returns the User object with the given user ID
     * @param userId the ID of a User
     * @return the User with this user ID
     */
    private User findUserByUserId(String userId) {
            return userIdToUser.get(userId);
    }

    /**
     * Returns the User object with the given username
     * @param username the username of a User
     * @return the User with this username
     */
    private User findUserByUsername(String username) {
        HashMap<String, String> userUsernameToId = new HashMap<>();
        for (Map.Entry <String, User> entry : getUserIdToUser().entrySet()) {
            userUsernameToId.put(entry.getValue().getUsername(), entry.getKey());
        }
        String userId = userUsernameToId.get(username);
        return findUserByUserId(userId);
    }

    /**
     * Returns the username of the User associated with a given user ID
     * @param userId the user ID to look up
     * @return the associated username
     */
    public String usernameByUserId(String userId) {
        return findUserByUserId(userId).getUsername();
    }

    /**
     * Returns the userID of the User associated with a given username
     * @param username the username to look up
     * @return the associated user ID
     */
    public String userIdByUsername(String username) {
        return findUserByUsername(username).getId();
    }

    /**
     * Gets a list of user IDs by status
     * @param status the UserStatus enum to look up
     * @return a list of user IDs with this status
     */
    public ArrayList<String> userIdsByStatus(UserStatus status) {
        ArrayList<String> userIds = new ArrayList<>();
        for (Map.Entry <String, User> entry : getUserIdToUser().entrySet()) {
            if (entry.getValue().getStatus().equals(status)){
                userIds.add(entry.getKey());
            }
        }
        return userIds;
    }

    /**
     * Returns the user status of the user with this user ID
     *
     * @param userId the user ID of a User
     * @return the user status associated with this user ID
     */
    public UserStatus statusByUserId(String userId) {
        return findUserByUserId(userId).getStatus();
    }

    /**
     * Returns a inventory of item IDs associated with this user ID
     *
     * @param userId the user ID of a user
     * @return an ArrayList of ints representing inventory of items belonging to this user ID
     */
    public ArrayList<String> inventoryByUserId(String userId) {
        User user = findUserByUserId(userId);
        return user.getInventory();
    }

    /**
     * Returns true iff the item, represented by item ID is in the user's inventory
     *
     * @param userId the user ID of a user
     * @param itemId the item ID of an item
     * @return true iff item ID in the given user ID's User's inventory
     */
    public boolean itemIdInInventory(String userId, String itemId) {
        return inventoryByUserId(userId).contains(itemId);
    }

    /**
     * @param userId the user Id of a user
     * @param itemId the item Id of an item
     * @return true iff item Id in the given user Id's user's wishlist
     */
    public boolean itemIdInWishlist(String userId, String itemId) {
        return wishlistByUserId(userId).contains(itemId);
    }

    /**
     * Returns a wishlist item IDs associated with this user ID
     *
     * @param userId the user ID of a user
     * @return an ArrayList of ints representing wishlist of items belonging to this user ID
     */
    public ArrayList<String> wishlistByUserId(String userId) {
        User user = findUserByUserId(userId);
        return user.getWishlist();
    }

    /**
     * Sets a User's inventory to a given ArrayList of item IDs
     * @param userId the user ID of the User whose inventory is to be modified
     * @param inventory an ArrayList of item IDs that this User's inventory will be set to
     */
    public void setInventory(String userId, ArrayList<String> inventory) {
        findUserByUserId(userId).setInventory(inventory);
    }

    /**
     * Sets a User's wishlist to a given ArrayList of item IDs
     * @param userId the user ID of the User whose wishlist is to be modified
     * @param wishlist an ArrayList of item IDs that this User's wishlist will be set to
     */
    public void setWishlist(String userId, ArrayList<String> wishlist) {
        findUserByUserId(userId).setWishlist(wishlist);
    }

    /**
     * Adds an item ID to the user ID's User's inventory if it doesn't already exist
     * @param userId the user ID of the User
     * @param itemId the item ID of an Item to be added to the User's inventory
     * @return true iff the item ID is added to the user's inventory
     */
    public boolean addToInventory(String userId, String itemId) {
        return findUserByUserId(userId).addToInventory(itemId);
    }

    /**
     * Removes an item ID from the user ID's User's inventory if it exists
     * @param userId the user ID of the User
     * @param itemId the item ID of an Item to be removed from the User's inventory
     * @return true iff the item ID is removed from the user's inventory
     */
    public boolean removeFromInventory(String userId, String itemId) {
        return findUserByUserId(userId).removeFromInventory(itemId);
    }

    /**
     * Adds an item ID to the user ID's User's wishlist if it doesn't already exist
     * @param userId the user ID of the User
     * @param itemId the item ID of an Item to be added to the User's wishlist
     * @return true iff the item ID is added to the user's wishlist
     */
    public boolean addToWishlist(String userId, String itemId) {
        return findUserByUserId(userId).addToWishlist(itemId);
    }

    /**
     * Removes an item ID from the user ID's User's wishlist if it exists
     * @param userId the user ID of the User
     * @param itemId the item ID of an Item to be removed from the User's wishlist
     * @return true iff the item ID is removed from the user's wishlist
     */
    public boolean removeFromWishlist(String userId, String itemId) {
        return findUserByUserId(userId).removeFromWishlist(itemId);
    }

    /**
     * Changes the username of a user with this user ID
     *
     * @param userId the user ID of a user
     * @param username the new username to change to
     */
    public void changeUsername(String userId, String username) {
        findUserByUserId(userId).setUsername(username);
    }

    /**
     * Changes the password of a user with this user ID
     *
     * @param userId the user ID of a user
     * @param password the new username to change to
     */
    public void changePassword(String userId, String password) {
        findUserByUserId(userId).setPassword(password);
    }

    /**
     * Changes the status of a user with this user ID
     *
     * @param userId the user ID of a user
     * @param status the status of a user
     */
    public void changeStatus(String userId, UserStatus status) {
        findUserByUserId(userId).setStatus(status);
    }

    /**
     * Gets Users as a list of usernames
     * @return a list of usernames in this system
     */
    public ArrayList<String> getUsernames() {
        ArrayList<String> usernamesList = new ArrayList<>();
        for (Map.Entry <String, User> entry : getUserIdToUser().entrySet()) {
            usernamesList.add(entry.getValue().getUsername());
        }
        return usernamesList;
    }

    /**
     * Checks if the username exists in the UserManager
     * @param username the username of an User
     * @return true iff username is not in use
     */
    public boolean checkUsername(String username) {
        return !getUsernames().contains(username);
    }

    /**
     * Checks if the inputted password is correct
     * @param username the username of a user
     * @param password the password input received from a user
     * @return true iff the password input matches the username's password
     */
    public boolean checkPassword(String username, String password) {
        User user = findUserByUsername(username);
        return user.getPassword().equals(password);
    }

    /**
     * Adds the frozen User associated with the given user ID to the list in AdminManager of users that requested to be unfrozen
     * @param userId the user ID of the user to be added to the requested-to-be-unfrozen list
     * @return true iff the user's status is currently FROZEN, in which case the user will be successfully added to the list
     */
    public boolean unfreezeRequest(String userId) {
        if (findUserByUserId(userId).getStatus().equals(UserStatus.FROZEN)) {
            changeStatus(userId, UserStatus.REQUESTED);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns the toString() method of the User associated with the given user ID
     * @param userId the user ID
     * @return the string representation of the associated User
     */
    public String toStringByAccountId(String userId) {
        return findUserByUserId(userId).toString();
    }

    /**
     * Returns the toString() method of all Users in the system
     * @return a String consisting of the toString() returns of all Users in the system
     */
    @Override
    public String toString() {
        StringBuilder allUsers = new StringBuilder();
        for (Map.Entry <String, User> entry : getUserIdToUser().entrySet()) {
            allUsers.append(entry.getValue().toString());
            allUsers.append("\n");
            allUsers.append("\n");
        }
        return allUsers.toString();
    }

    /**
     * @param itemId Id of an item
     * @return the userId whose inventory contains the Item
     */
    public String findUserByItemInventory(String itemId) {
        for (String key: userIdToUser.keySet()) {
            if (itemIdInInventory(key, itemId)) {
                return key;
            }
        }
        return null;
    }

    /**
     * @param itemId Id of an item
     * @return the userId whose wishlist contains the Item
     */
    public String findUserByItemWishlist(String itemId) {
        for (String key: userIdToUser.keySet()) {
            if (itemIdInWishlist(key, itemId)) {
                return key;
            }
        }
        return null;
    }
}
