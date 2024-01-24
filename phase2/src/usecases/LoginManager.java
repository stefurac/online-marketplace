package usecases;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * a class that temporarily stores all Users' or Admins' login information and IDs; must be instantiated anew every time at login
 */
public class LoginManager {
    HashMap<String, ArrayList<String>> usernameToPasswordsIds;  // a dictionary where each key is an Account's username, and each value is a (current password, user ID) pair

    /**
     * Constructs a LoginManager instance
     * @param usernameToPasswordsIds a HashMap where each key is a User's username, and each value is a (current password, user ID) pair
     */
    public LoginManager(HashMap<String, ArrayList<String>> usernameToPasswordsIds) {
        this.usernameToPasswordsIds = usernameToPasswordsIds;
    }


    /**
     * Checks if a username exists on the system
     * @param username the username
     * @return true iff the username exists on the system
     */
    public boolean containsUsername(String username) {
        return usernameToPasswordsIds.containsKey(username);
    }

    /**
     * Checks if a password matches that of a username
     * @param username the username
     * @param password the password
     * @return true iff the password matches that of the username
     */
    public boolean checkPassword(String username, String password) {
        return usernameToPasswordsIds.get(username).get(0).equals(password);
    }

    /**
     * Returns the user ID associated with the give username
     * @param username the username
     * @return the user ID associated with the give username
     */
    public String getUserId(String username) {
        return usernameToPasswordsIds.get(username).get(1);
    }


}
