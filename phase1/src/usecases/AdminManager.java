package usecases;

import entities.Admin;

import java.util.ArrayList;

/**
 * Represents the system of Admin
 */
public class AdminManager {
    private ArrayList<Admin> adminList;

    /**
     * Creates an instance of AdminManager with a list of Admin and a list of usernames that wish to be unfrozen
     */
    public AdminManager(ArrayList<Admin> allAdmin) {
        this.adminList = allAdmin;
    }

    /**
     * Gets the ArrayList of all admin
     *
     * @return ArrayList<Admin> of all Admin in the system
     */
    public ArrayList<Admin> getAdminList() {
        return adminList;
    }

    /**
     * Adds an admin to the system
     *
     * @param newUsername the username of a new Admin account
     * @param newPassword the password of a new Admin account
     */
    public void addAdmin(String newUsername, String newPassword) {
        if (availableUsername(newUsername)) {
            Admin newAdmin = new Admin(newUsername, newPassword);
            adminList.add(newAdmin);
        }
    }

    /**
     * Checks if a username is available
     *
     * @param username the username of an Admin account
     * @return true iff username is available
     */
    public boolean availableUsername(String username) {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the inputted password is incorrect
     *
     * @param username the username of an Admin account
     * @param pass the password input received from an Admin
     * @return true iff the password input does not match the username's password
     */
    public boolean incorrectPassword(String username, String pass) {
        Admin admin = adminByUsername(username);
        assert admin != null;
        return !admin.getPassword().equals(pass);
    }

    /**
     * Returns the admin with this admin id
     *
     * @param adminId the admin id of an admin
     * @return the Admin with this admin id
     */
    private Admin adminByAdminId(String adminId) {
        for (Admin admin : adminList) {
            if (admin.getId().equals(adminId)) {
                return admin;
            }
        }
        return null;
    }

    /**
     * Returns the Admin object with this username
     *
     * @param username the username of an admin
     * @return the Admin with this username
     */
    private Admin adminByUsername(String username) {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username)) {
                return admin;
            }
        }
        return null;
    }

    /**
     * Returns the username of the Admin associated with a given admin id.
     *
     * @param userId the admin ID to look up
     * @return the associated username
     */
    public String usernameByAdminId(String userId) {
        return adminByAdminId(userId).getUsername();
    }

    /**
     * Returns the adminID of the Admin associated with a given username.
     *
     * @param username the username to look up
     * @return the associated admin ID
     */
    public String adminIdByUsername(String username) {
        return adminByUsername(username).getId();
    }

    /**
     * Gets admin as a list of usernames
     *
     * @return a list of admin usernames in this system
     */
    public ArrayList<String> getUsernames() {
        ArrayList<String> usernamesList = new ArrayList<>();
        for (Admin admin : adminList) {
            usernamesList.add(admin.getUsername());
        }
        return usernamesList;

    }

    /**
     * Changes the username of an admin with this admin id
     *
     * @param adminId the user id of a admin
     * @param username the new username to change to
     */
    public void changeUsername(String adminId, String username) {
        adminByAdminId(adminId).setUsername(username);
    }
    /**
     * Changes the password of an admin with this admin id
     *
     * @param adminId the admin id of an admin
     * @param password the new username to change to
     */
    public void changePassword(String adminId, String password) {
        adminByAdminId(adminId).setPassword(password);
    }

    /**
     * Represents all admin by their username and account type in the system
     *
     * @return a String of admin usernames and their account type, separated by newline.
     */
    @Override
    public String toString() {
        StringBuilder allAdmin = new StringBuilder();
        for (Admin admin : adminList) {
            allAdmin.append(admin.toString());
            allAdmin.append("\n");
            allAdmin.append("\n");
        }
        return allAdmin.toString();

    }
}

