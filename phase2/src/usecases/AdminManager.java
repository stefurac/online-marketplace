package usecases;

import entities.Admin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the system of Admin
 */
public class AdminManager {
    //private ArrayList<Admin> adminList;
    private final HashMap<String, Admin> adminIdToAdmin = new HashMap<>();


    /**
     * Creates an instance of AdminManager with a list of admin
     * @param allAdmin an ArrayList of admin in the system
     */
    public AdminManager(ArrayList<Admin> allAdmin) {
        for (Admin admin: allAdmin) {
            this.adminIdToAdmin.put(admin.getId(), admin);
        }
        //this.adminList = allAdmin;
    }

    /**
     * Gets the HashMap of all admin, where admin id maps to admin object
     * @return HashMap<String, Admin> of all Admin in the system
     */
    public HashMap<String, Admin> getAdminIdToAdmin() {
        return this.adminIdToAdmin;
    }

    /**
     * Adds an admin to the system
     * @param newUsername the username of a new Admin account
     * @param newPassword the password of a new Admin account
     */
    public void addAdmin(String newUsername, String newPassword) {
        if (availableUsername(newUsername)) {
            Admin newAdmin = new Admin(newUsername, newPassword);
            getAdminIdToAdmin().put(newAdmin.getId(), newAdmin);
        }
    }

    /**
     * Checks if a username is available
     * @param username the username of an Admin account
     * @return true iff username is available
     */
    public boolean availableUsername(String username) {
        return !getUsernames().contains(username);
    }

    /**
     * Checks if the inputted password is incorrect
     * @param username the username of an Admin account
     * @param password the password input received from an Admin
     * @return true iff the password input does not match the username's password
     */
    public boolean incorrectPassword(String username, String password) {
        Admin admin = adminByUsername(username);
        assert admin != null;
        return !admin.getPassword().equals(password);
    }

    /**
     * Returns the admin object with this admin id
     * @param adminId the admin id of an admin
     * @return the Admin with this admin id
     */
    private Admin adminByAdminId(String adminId) {
        return adminIdToAdmin.get(adminId);
    }

    /**
     * Returns the admin object with this username
     * @param username the username of an admin
     * @return the Admin with this username
     */
    private Admin adminByUsername(String username) {
        HashMap<String, String> usernameToId = new HashMap<>();
        for (Map.Entry<String, Admin> entry: getAdminIdToAdmin().entrySet()) {
            usernameToId.put(entry.getValue().getUsername(), entry.getKey());
        }
        String adminId = usernameToId.get(username);
        return adminByAdminId(adminId);
    }

    /**
     * Returns the username of the Admin associated with a given admin id.
     * @param userId the admin ID to look up
     * @return the associated username
     */
    public String usernameByAdminId(String userId) {
        return adminByAdminId(userId).getUsername();
    }

    /**
     * Gets admin as a list of usernames
     * @return a list of admin usernames in this system
     */
    public ArrayList<String> getUsernames() {
        ArrayList<String> usernamesList = new ArrayList<>();
        for (Map.Entry <String, Admin> entry : getAdminIdToAdmin().entrySet()) {
            usernamesList.add(entry.getValue().getUsername());
        }
        return usernamesList;
    }

    /**
     * Changes the username of an admin with this admin id
     * @param adminId the user id of a admin
     * @param username the new username to change to
     */
    public void changeUsername(String adminId, String username) {
        adminByAdminId(adminId).setUsername(username);
    }
    /**
     * Changes the password of an admin with this admin id
     * @param adminId the admin id of an admin
     * @param password the new username to change to
     */
    public void changePassword(String adminId, String password) {
        adminByAdminId(adminId).setPassword(password);
    }

    /**
     * Represents all admin by their username and account type in the system
     * @return a String of admin usernames and their account type, separated by newline.
     */
    @Override
    public String toString() {
        StringBuilder allAdmin = new StringBuilder();
        for (Map.Entry <String, Admin> entry : getAdminIdToAdmin().entrySet()) {
            allAdmin.append(entry.getValue().toString());
            allAdmin.append("\n");
            allAdmin.append("\n");
        }
        return allAdmin.toString();

    }
}

