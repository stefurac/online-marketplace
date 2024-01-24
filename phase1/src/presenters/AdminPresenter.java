package presenters;

import usecases.AdminManager;
import usecases.ItemManager;
import usecases.UserManager;

/**
 * A presenter that displays the functionalities of AdminSystem
 */
public class AdminPresenter {
    private String cancelString = "C";  // the string that the user enters when they wish to cancel the current action at every non-menu-option input prompt
    private final AdminManager adm;
    private final UserManager um;

    /**
     * Creates an instance of AdminPresenter
     *
     * @param adm an instance of admin manager
     * @param um an instance of user manager
     */
    public AdminPresenter(AdminManager adm, UserManager um){
        this.adm = adm;
        this.um = um;
    }

    // General
    /**
     * Returns the cancel string
     *
     * @return the String that represents cancelling a system prompt
     */
    public String getCancelString() {
        return cancelString;
    }

    /**
     * Prints the select option prompt
     */
    public void selectOptionPrompt() {
        System.out.println("Please Select an Option By Entering the Corresponding Number: ");
    }

    /**
     * Prints the invalid input message
     */
    public void invalidInputMessage() {System.out.println("Please Enter a Valid Option");}

    /**
     * Prints the update successful message
     */
    public void updateSuccessMessage() {
        System.out.println("Update Successful! \n");
    }

    /**
     * Prints the update not completed message
     */
    public void noUpdateMsg() {
        System.out.println("Update Not Completed.");
    }

    // Manage Profile
    /**
     * Prints the enter current password prompt
     */
    public void currentPasswordPrompt() {
        System.out.println("Please Enter Current Password, or\"" + cancelString + "\"to Cancel:");
    }

    /**
     * Prints the incorrect password prompt
     */
    public void incorrectPasswordPrompt() { // also in LoginPresenter
        System.out.println("Incorrect Password.");
    }

    /**
     * Prints the new password prompt
     */
    public void newPasswordPrompt() {
        System.out.println("Please Enter New Password, or  \"" + cancelString + "\"  to Cancel: ");
    }

    /**
     * Prints the invalid password message/prompt
     */
    public void invalidNewPasswordMessage() {
        System.out.println("Invalid Password. Please Enter Password:");
    }

    /**
     * Prints the change username prompt
     *
     * @param username the user's current username
     */
    public void changeUsernamePrompt(String username) {
        System.out.println("Your Username is " + username + ". Please Enter New Username, or \"" + cancelString + "\" to Cancel: ");
    }

    /**
     * Prints the new username prompt during admin registration
     */
    public void newUsernamePrompt() {
        System.out.println("Enter New Username, or \"" + cancelString + "\" to Cancel: ");
    }

    // Menu Options
    /**
     * Prints the admin main menu
     */
    public void adminMainMenu() {
        System.out.println("== Main Menu ==");
        System.out.println("1. Manage Profile");
        System.out.println("2. Manage User Accounts");
        System.out.println("3. Manage Admin Accounts");
        System.out.println("4. Manage Items");
        System.out.println("5. Manage Thresholds");
        System.out.println("0. Log Out");
    }

    /**
     * Prints the manage profile menu
     */
    public void adminManageProfileMenu() {
        System.out.println("== Manage Profile ==");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("0. Main Menu");
    }

    /**
     * Prints the manage user menu
     */
    public void adminManageUserMenu() {
        System.out.println("== Manage User Accounts ==");
        System.out.println("1. View Users");
        System.out.println("2. Manage Flagged Users");
        System.out.println("3. Manage Frozen Users");
        System.out.println("4. Manage Unfreeze Requests");
        System.out.println("0. Main Menu");
    }

    /**
     * Prints the manage flagged users menu
     */
    public void flagMenu(){
        System.out.println("== Manage Flagged Users: ==");
        System.out.println("1. Freeze Flagged User");
        System.out.println("2. Unflag Flagged User");
        System.out.println("0. Go Back");
    }


    /**
     * Prints the manage admin menu
     */
    public void adminManageAdminMenu() {
        System.out.println("== Manage Admin Accounts ==");
        System.out.println("1. View Admin");
        System.out.println("2. Add Admin");
        System.out.println("0. Main Menu");
    }

    /**
     * Prints the manage items menu
     */
    public void adminManageItemMenu() {
        System.out.println("== Manage Items ==");
        System.out.println("1. Manage Items");
        System.out.println("2. Manage Item Requests");
        System.out.println("0. Main Menu");
    }

    /**
     * Prints the manage threshold menu
     */
    public void adminManageThresholdMenu() {
        System.out.println("== Manage Thresholds ==");
        System.out.println("1. Edit Meeting Cancellation Threshold");
        System.out.println("2. Edit Weekly Trade Limit");
        System.out.println("3. Edit Borrowed-Lent Difference Threshold");
        System.out.println("4. Edit Meeting Edits Threshold");
        System.out.println("5. Edit Minimum Password Length");
        System.out.println("0. Main Menu");
    }

    // Manage Admin
    /**
     * Prints all admin registered in the system
     */
    public void viewAdmin() {
        System.out.println("== All Admin ==");
        System.out.println(adm.toString());
    }

    /**
     * @param username the username of a new admin
     */
    public void adminUsernameMessage(String username) {
        System.out.println("Admin Username: " + username);
    }

    /**
     * Prints whether admin registration is successful or terminated
     *
     * @param isSuccessful is true iff admin registration is successful
     */
    public void registrationResultMsg(boolean isSuccessful) {
        if (isSuccessful) {
            System.out.println("Registration Successful.");
        } else {
            System.out.println("Registration Terminated");
        }
    }

    /**
     * Prints the admin registration prompt
     */
    public void adminRegisterPrompt() {
        System.out.println("== Admin Registration ==");
        System.out.println("Register Admin Username:");
    }

    /**
     * Prints the passcode generated by the system
     *
     * @param username the username of a new admin
     * @param pass the system generated passcode for the new admin
     */
    public void generatePassMsg(String username, String pass) {
        adminUsernameMessage(username);
        System.out.println("Generated Admin Password: " + pass + "\n");
    }

    /**
     * Prints the username unavailable message/prompt
     */
    public void usernameUnavailablePrompt() {
        System.out.println("Admin Username Unavailable. Enter New Username:");
    }

    /**
     * Prints the registration confirmation prompt
     */
    public void proceedRegistrationPrompt() {
        System.out.println("Register Admin? (Y/N)");
    }

    // Manage Users

    /**
     * Prints all user registered in the system
     */
    public void viewUsers() {
        System.out.println("== All Users ==");
        System.out.println(um.toString());
    }

    /**
     * Prints the user profile of a user in the system with this user id
     *
     * @param userId the user id of a user in the system
     */
    public void userProfile(String userId) {
        System.out.println(um.toStringByAccountId(userId) + "\n");
    }

    /**
     * Prints username not found message
     */
    public void usernameNotFound() {
        System.out.println("Username Not Found");
    }

    /**
     * Prints the frozen users title
     */
    public void frozenUsers() {
        System.out.println("== Frozen Users ==");
    }

    /**
     * Prints the requested users title and description
     */
    public void requestedUsers() {
        System.out.println("== Requested Users ==");
        System.out.println("The following users have requested to be Unfrozen:");
    }

    /**
     * Prints the flagged users title
     */
    public void flaggedUsers() {
        System.out.println("== Flagged Users ==");
    }

    /**
     * Prints the change username's status prompt
     *
     * @param status the new status of a user
     */
    public void statusPrompt(String status) {
        System.out.println("Enter Username to " + status + " Account, or \"" + cancelString + "\" to Cancel:" );
    }

    // Manage Thresholds

    /**
     * Prints the threshold edit prompt
     *
     * @param thresholdName the name of the threshold that is being updated
     */
    public void thresholdEditPrompt(String thresholdName) {
        System.out.println("Please Enter New " + thresholdName + ", or \"" + cancelString + "\" to Cancel: ");
    }

    /**
     * Prints the invalid threshold message/prompt
     *
     * @param minValue the minimum acceptable value of a new threshold
     */
    public void badThresholdMessage(int minValue) {
        System.out.println("Invalid Input; Enter Value Greater Than " + minValue + ".");
    }

    /**
     * Prints the unacceptable threshold message/prompt
     *
     * @param minValue the minimum acceptable value of a new threshold
     */
    public void unacceptableThresholdMessage(int minValue) {
        System.out.println("Unacceptable Threshold. Enter Minimum Value of " + minValue + ".");
    }

    /**
     * Prints the threshold update success message
     */
    public void thresholdEditSuccessMessage() {
        System.out.println("Threshold Update Successful!");
    }

    // Manage Items

    /**
     * Prints all items in the system and select item prompt
     *
     * @param im the item manager
     */
    public void viewAllItems(ItemManager im){
        System.out.println(im.toString());
        System.out.println("Enter Item Number to See Options.");
        System.out.println("Enter 0 to Go Back to Upper Level Menu.");
    }

    /**
     * Prints remove item menu
     */
    public void singleItemMenu() {
        System.out.println("1. Remove Item.");
        System.out.println("Enter 0 to Go Back to Upper Level Menu.");
    }

    /**
     * Prints item removal success message
     */
    public void itemRemovalSuccess() {
        System.out.println("Item Successfully Removed!");
    }

    /**
     * Prints all items that are awaiting admin approval (requested items)
     *
     * @param im the item manager
     */
    public void viewAllRequestedItems(ItemManager im) {
        System.out.println(im.getRequestedItems());
    }

    /**
     * Prints the requested items menu
     */
    public void requestedItemMenu() {
        System.out.println("1. Remove Item.");
        System.out.println("2. Approve Item.");
        System.out.println("Enter 0 to Go Back to Upper Level Menu.");
    }
}