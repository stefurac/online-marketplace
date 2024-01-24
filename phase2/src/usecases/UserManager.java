package usecases;

import entities.ItemStatus;
import entities.User;
import entities.UserStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the system of Users
 */
public class UserManager {
    private final HashMap<String, User> userIdToUser = new HashMap<>();  // a HashMap with user IDs as keys and User entities as values

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
    public User userByUserId(String userId) {
        return userIdToUser.get(userId);
    }

    /**
     * Returns the User object with the given username
     * @param username the username of a User
     * @return the User with this username
     */
    private User userByUsername(String username) {
        HashMap<String, String> userUsernameToId = new HashMap<>();
        for (Map.Entry <String, User> entry : getUserIdToUser().entrySet()) {
            userUsernameToId.put(entry.getValue().getUsername(), entry.getKey());
        }
        String userId = userUsernameToId.get(username);
        return userByUserId(userId);
    }

    /**
     * Returns the username of the User associated with a given user ID
     * @param userId the user ID to look up
     * @return the associated username
     */
    public String usernameByUserId(String userId) {
        return userByUserId(userId).getUsername();
    }

    /**
     * Returns the userID of the User associated with a given username
     * @param username the username to look up
     * @return the associated user ID
     */
    public String userIdByUsername(String username) {
        return userByUsername(username).getId();
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
     * Returns a list of all premium Users
     *  @return a list of all premium Users
     */
    public ArrayList<String> premiumUserIds() {
        ArrayList<String> userIds = new ArrayList<>();
        for (Map.Entry <String, User> entry : getUserIdToUser().entrySet()) {
            if (entry.getValue().getStatus()== UserStatus.PREMIUM){
                userIds.add(entry.getKey());
            }
        }
        return userIds;
    }

    /**
     * Returns a list of all Users whose point value is above or at 500
     * @return a list of all Users whose point value is above or at 500
     */
    public ArrayList<String> usersAbove500() {
        ArrayList<String> userIds = new ArrayList<>();
        for (Map.Entry <String, User> entry : getUserIdToUser().entrySet()) {
            if (entry.getValue().getPoints() >= 500){
                userIds.add(entry.getKey());
            }
        }
        return userIds;
    }

    /**
     * Returns the user status of the user with this user ID
     * @param userId the user ID of a User
     * @return the user status associated with this user ID
     */
    public UserStatus statusByUserId(String userId) {
        return userByUserId(userId).getStatus();
    }

    /**
     * Gets a user's points
     * @param userId the user ID
     * @return the points value of the user
     */
    public int pointsByUserId(String userId) {
        return userByUserId(userId).getPoints();
    }


    /**
     * Returns a inventory of item IDs associated with this user ID
     * @param userId the user ID of a user
     * @return an ArrayList of ints representing inventory of items belonging to this user ID
     */
    public ArrayList<String> inventoryByUserId(String userId) {
        User user = userByUserId(userId);
        return user.getInventory();
    }

    /**
     * Returns a list of this user's available items in their inventory
     * @param userId the user ID
     * @param im an instance of ItemManager
     * @return a list of this user's available items in their inventory
     */
    public ArrayList<String> availableInventoryByUserId(String userId, ItemManager im) {
        User user = userByUserId(userId);
        ArrayList<String> items = new ArrayList<>();
        for (String itemId : user.getInventory()) {
            if (im.statusByItemId(itemId) == ItemStatus.AVAILABLE) {
                items.add(itemId);
            }
        }
        return items;
    }

    /**
     * Returns true iff the item, represented by item ID is in the user's inventory
     * @param userId the user ID of a user
     * @param itemId the item ID of an item
     * @return true iff item ID in the given user ID's User's inventory
     */
    public boolean itemIdInInventory(String userId, String itemId) {
        return inventoryByUserId(userId).contains(itemId);
    }

    /**
     * Returns true iff the item, represented by item id is in the user's wishlist
     * @param userId the user Id of a user
     * @param itemId the item Id of an item
     * @return true iff item Id in the given user Id's user's wishlist
     */
    public boolean itemIdInWishlist(String userId, String itemId) {
        return wishlistByUserId(userId).contains(itemId);
    }

    /**
     * Returns a wishlist item IDs associated with this user ID
     * @param userId the user ID of a user
     * @return an ArrayList of ints representing wishlist of items belonging to this user ID
     */
    public ArrayList<String> wishlistByUserId(String userId) {
        return userByUserId(userId).getWishlist();
    }

    public ArrayList<String> wishlistHistoryByUserId(String userId) {
        return userByUserId(userId).getWishlistHistory();
    }

    /**
     * Gets a user's wishlist of available items
     * @param userId the user id of a user
     * @param im the item manager in this system
     * @return an array list of String that represent item ids
     */
    public ArrayList<String> availableWishlistByUserId(String userId, ItemManager im) {
        User user = userByUserId(userId);
        ArrayList<String> items = new ArrayList<>();
        for (String itemId : user.getWishlist()) {
            if (im.statusByItemId(itemId) == ItemStatus.AVAILABLE) {
                items.add(itemId);
            }
        }
        return items;
    }

    /**
     * Adds an item ID to the user ID's User's inventory if it doesn't already exist
     * @param userId the user ID of the User
     * @param itemId the item ID of an Item to be added to the User's inventory
     * @return true iff the item ID is added to the user's inventory
     */
    public boolean addToInventory(String userId, String itemId) {
        return inventoryByUserId(userId).add(itemId);
    }

    /**
     * Removes an item ID from the user ID's User's inventory if it exists
     * @param userId the user ID of the User
     * @param itemId the item ID of an Item to be removed from the User's inventory
     */
    public void systemRemoveFromInventory(String userId, String itemId) {
        inventoryByUserId(userId).remove(itemId);
    }

    /**
     * Removes an item ID from the user ID's User's inventory if it exists
     * @param userId the user ID of the User
     * @param itemId the item ID of an Item to be removed from the User's inventory
     */
    public void userRemoveFromInventory(String userId, String itemId, ItemManager im) {
        inventoryByUserId(userId).remove(itemId);
        userByUserId(userId).getInventoryHistory().add(itemId);
        // user.addInventoryHistory(itemId);
        im.changeStatus(itemId, ItemStatus.REMOVED);
    }

    /**
     * Removes an item ID from the user ID's User's wishlist if it exists
     * @param userId the user ID of the User
     * @param itemId the item ID of an Item to be removed from the User's wishlist
     * @return true iff the item ID is removed from the user's wishlist
     */
    public boolean systemRemoveFromWishlist(String userId, String itemId) {
        if (itemIdInWishlist(userId, itemId)) {
            wishlistByUserId(userId).remove(itemId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes an item ID from the user ID's User's wishlist if it exists
     * @param userId the user ID of the User
     * @param itemId the item ID of an Item to be removed from the User's wishlist
     */
    public void userRemoveFromWishList(String userId, String itemId) {
        wishlistByUserId(userId).remove(itemId);
        wishlistHistoryByUserId(userId).add(itemId);
    }

    /**
     * Adds an item ID to the user ID's User's wishlist if it doesn't already exist
     * @param userId the user ID of the User
     * @param itemId the item ID of an Item to be added to the User's wishlist
     * @return true iff the item ID is added to the user's wishlist
     */
    public boolean addToWishlist(String userId, String itemId) {
        if (!itemIdInWishlist(userId, itemId)) {
            wishlistByUserId(userId).add(itemId);
            //wishlist.add(itemId);
            //user.addToWishlist(itemId); //should we remove add/remove methods in User?
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds a set amount of points to the user
     * @param userId the user ID
     * @param points the points amount
     */
    public void addPoints(String userId, int points) {
        userByUserId(userId).addPoints(points);
    }

    /**
     * Removes a set amount of points to the user
     * @param userId the user ID
     * @param points the points amount
     */
    public void removePoints(String userId, int points) {
        userByUserId(userId).removePoints(points);
    }

    /**
     * Changes the username of a user with this user ID
     * @param userId the user ID of a user
     * @param username the new username to change to
     */
    public void changeUsername(String userId, String username) {
        User user = userByUserId(userId);
        String currUsername = user.getUsername();
        user.setOldUsername(currUsername);
        user.setUsername(username);
    }

    /**
     * Changes the password of a user with this user ID
     * @param userId the user ID of a user
     * @param password the new username to change to
     */
    public void changePassword(String userId, String password) {
        User user = userByUserId(userId);
        String currPassword = user.getPassword();
        user.setOldPassword(currPassword);
        user.setPassword(password);
    }

    /**
     * Changes the status of a user with this user ID, and changes the points accordingly
     * @param userId the user ID of a user
     * @param status the status of a user
     */
    public void changeStatus(String userId, UserStatus status) {
        User user = userByUserId(userId);
        UserStatus oldStatus = user.getStatus();

        if (oldStatus == UserStatus.PROBATION && status == UserStatus.FROZEN) {
            user.removePoints(40);
        }
        if (status == UserStatus.PROBATION) {
            user.removePoints(10);
        }
        userByUserId(userId).setStatus(status);

    }

    /**
     * Gets an array list of users
     * @return an array list of users
     */
    public ArrayList<User> userList() {
        ArrayList<User> users = new ArrayList<>();
        for (Map.Entry<String, User> idsToTrades: getUserIdToUser().entrySet()) {
            users.add(idsToTrades.getValue()); }
        return users; }


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
    public boolean incorrectPassword(String username, String password) {
        User user = userByUsername(username);
        return !user.getPassword().equals(password);
    }

    /**
     * Adds the frozen User associated with the given user ID to the list in AdminManager of users that requested to be unfrozen
     * @param userId the user ID of the user to be added to the requested-to-be-unfrozen list
     * @return true iff the user's status is currently FROZEN, in which case the user will be successfully added to the list
     */
    public boolean unfreezeRequest(String userId) {
        if (userByUserId(userId).getStatus().equals(UserStatus.FROZEN)) {
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
        return userByUserId(userId).toString();
    }

    /**
     * Returns the user ID whose inventory contains the specified item
     * @param itemId Id of an item
     * @return the userId whose inventory contains the Item
     */
    public String userIdByInventoryItem(String itemId) {
        String keyOut = "";
        for (String key: userIdToUser.keySet()) {
            if (itemIdInInventory(key, itemId)) {
                keyOut = key;
            }
        }
        return keyOut;
    }

    /**
     * Returns the potential borrowers in good-standing for a given user
     * @param userId the ID of the user in question
     * @param im an instance of the ItemManager
     * @return a list of user ID/item ID pairs with the user ID indicating a potential borrower and the item ID an Item in this user's inventory that are available and can be potentially lent out
     */
    public ArrayList<ArrayList<String>> findPotentialBorrowers(String userId, ItemManager im) {
        ArrayList<ArrayList<String>> pairs = new ArrayList<>();
        for (String itemId : availableInventoryByUserId(userId, im)) {
            for (Map.Entry<String, User> borrower : userIdToUser.entrySet()) {
                if (itemIdInWishlist(borrower.getKey(), itemId) && !borrower.getKey().equals(userId)) {
                    ArrayList<String> pair = new ArrayList<>();  // the user ID/item ID pair to be added to the list of pairs
                    pair.add(borrower.getKey());
                    pair.add(itemId);
                    pairs.add(pair);
                }
            }
        }
        return pairs;
    }

    /**
     * returns only the items in the inputted list owned by the specified owner
     * @param itemList a list of items
     * @param ownerId the specified owner
     * @return a list of items owned by the specified owner
     */
    public ArrayList<String> filterByOwner(ArrayList<String> itemList, String ownerId) {
        ArrayList<String> itemsListFiltered = new ArrayList<>();

        for (String itemId : itemList) {
            if (itemIdInInventory(ownerId, itemId)) {
                itemsListFiltered.add(itemId);
            }
        }
        return itemsListFiltered;
    }

    /**
     * Swaps a user's current username with their old username
     * @param userId the user id of a user
     * @return true iff a user's username has been successfully swapped with their old username
     */
    public boolean swapUsernames(String userId) {
        User user = userByUserId(userId);
        String oldUsername = user.getOldUsername();
        String currUsername = user.getUsername();
        if (!oldUsername.equals(currUsername)){ //if (!oldUsername.equals("null")){
            user.setUsername(oldUsername);
            user.setOldUsername(currUsername);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Swaps a user's current password with their old password
     * @param userId the user id of a user
     * @return true iff the user's password has been successfully swapped with their old password
     */
    public boolean swapPasswords(String userId) {
        User user = userByUserId(userId);
        String oldPassword = user.getOldPassword();
        String currPassword = user.getPassword();
        if (!oldPassword.equals(currPassword)){
            user.setPassword(oldPassword);
            user.setOldPassword(currPassword);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns the toString() method of all Users in the system
     * @return a String consisting of the toString() returns of all Users in the system
     */
    @Override
    public String toString() {
        StringBuilder allUsers = new StringBuilder();
        int i = 1;
        for (Map.Entry <String, User> entry : getUserIdToUser().entrySet()) {
            allUsers.append("\n").append(i).append(". ").append(entry.getValue().toString());
            allUsers.append("\n");
            //allUsers.append("\n");
            i++;
        }
        return allUsers.toString();
    }

    /**
     * Returns the user's inventory history
     * @param userId the user id of a user
     * @return an array list of String that represent item ids
     */
    public ArrayList<String> inventoryHistoryByUserId(String userId) {
        User user = userByUserId(userId);
        return user.getInventoryHistory();
    }

    /**
     * Returns true iff an item has been successfully removed from the user's inventory history
     * @param userId the user id of a user
     * @param itemId the item id of an item
     * @return true iff an item id has been successfully removed from the user's inventory history
     */
    public boolean removeFromInventoryHistory(String userId, String itemId) {
        ArrayList<String> ih = inventoryHistoryByUserId(userId);
        if (ih.contains(itemId)) {
            ih.remove(itemId);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns true iff an item id has been successfully removed from the user's wishlist history
     * @param userId the user id of a user
     * @param itemId the item id of an item
     * @return true iff an item id has been successfully removed from the user's wishlist history
     */
    public boolean removeFromWishlistHistory(String userId, String itemId) {
        ArrayList<String> wh = wishlistHistoryByUserId(userId);
        if (wh.contains(itemId)) {
            wh.remove(itemId);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Gets the incomplete trade offset of a user
     * @param userId the user ID
     * @return the incomplete trade offset
     */
    public int getIncompleteTradeOffset(String userId) {
        return userIdToUser.get(userId).getIncompleteTradeOffset();
    }

    /**
     * Sets the incomplete trade offset of a user
     * @param userId the user ID
     * @param newOffset the new incomplete trade offset
     */
    public void setIncompleteTradeOffset(String userId, int newOffset) {
        userIdToUser.get(userId).setIncompleteTradeOffset(newOffset);
    }

}
