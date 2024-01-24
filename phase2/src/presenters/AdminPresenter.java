package presenters;

import usecases.AdminManager;
import usecases.UserManager;

/**
 * A presenter that displays the functionalities of AdminSystem
 */
public class AdminPresenter extends MenuPresenter{
    private final AdminManager adm;
    private final UserManager um;

    /**
     * Creates an instance of AdminPresenter
     * @param adm an instance of admin manager
     * @param um an instance of user manager
     */
    public AdminPresenter(AdminManager adm, UserManager um){
        this.adm = adm;
        this.um = um;
    }

    /**
     * Prints the user has no trades message
     */
    public void userNoTradeFoundMessage() {
        System.out.println("User has No Trades!");
    }

    /**
     * Prints the enter current password prompt
     */
    public void currentPasswordPrompt() {
        System.out.println("Please Enter Current Password, or " + getCancelString() + " to Go Back:");
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
        System.out.println("Please Enter New Password, or  " + getCancelString() + "  to Go Back: ");
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
        System.out.println("Your Username is " + username + ". Please Enter New Username, or " + getCancelString() + " to Go Back: ");
    }

    /**
     * Prints the new username prompt during admin registration
     */
    public void newUsernamePrompt() {
        System.out.println("Enter New Username, or " + getCancelString() + " to Go Back: ");
    }

    /**
     * Prints the admin main menu
     */
    public void adminMainMenu() {
        System.out.println("== Main Menu ==");
        System.out.println("1. Manage Profile");
        System.out.println("2. Manage User Accounts");
        System.out.println("3. Manage Admin Accounts");
        System.out.println("4. Manage Item Requests");
        System.out.println("5. Manage Thresholds");
        zeroLogOut(); //System.out.println("0. Log Out");
    }

    /**
     * Prints the manage item requests title
     */
    public void manageItemRequestsTitle() {
        System.out.println("== Manage Item Requests ==");
    }

    /**
     * Prints the manage profile menu
     */
    public void adminManageProfileMenu() {
        System.out.println("== Manage Profile ==");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        zeroMainMenu(); //System.out.println("0. Main Menu");
    }

    /**
     * Prints the manage user menu
     */
    public void adminManageUserMenu() {
        System.out.println("== Manage User Accounts ==");
        System.out.println("1. Manage All Users");
        System.out.println("2. Manage Flagged Users (Users on Probation)");
        System.out.println("3. Manage Frozen Users");
        System.out.println("4. Manage Unfreeze Requests");
        zeroMainMenu(); //System.out.println("0. Main Menu");
    }

    public void manageUsersMenu() {
        System.out.println("== Manage Users ==");
        System.out.println("1. View All Users");
        System.out.println("2. Undo User Actions");
        System.out.println("3. Reset User Password");
        zeroGoBack();
    }
    /**
     * Prints the manage flagged users menu
     */
    public void manageFlaggedMenu(){
        System.out.println("== Manage Flagged Users: ==");
        System.out.println("1. Freeze Flagged User");
        System.out.println("2. Unflag Flagged User");
        zeroGoBack(); //System.out.println("0. Go Back");
    }

    /**
     * Prints the manage admin menu
     */
    public void adminManageAdminMenu() {
        System.out.println("== Manage Admin Accounts ==");
        System.out.println("1. View Admin");
        System.out.println("2. Add Admin");
        zeroMainMenu(); //System.out.println("0. Main Menu");
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
        zeroMainMenu(); //System.out.println("0. Main Menu");
    }

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
     * @param username the username of a new admin
     * @param password the system generated passcode for the new admin
     */
    public void generateAdminMsg(String username, String password) {
        adminUsernameMessage(username);
        generatePasswordMsg(password);
    }

    /**
     * Prints a system generated password
     * @param password a String that represents a password
     */
    public void generatePasswordMsg(String password) {
        System.out.println("Generated Admin Password: " + password + "\n");
    }

    /**
     * Prints user did not make last action message
     */
    public void userNotLastActorMessage() {
        System.out.println("This User Did Not Make The Last Action In This Trade!");
    }

    /**
     * Prints cannot undo admin actions message
     */
    public void cannotUndoAdminActionMessage() {
        System.out.println("It Is Only Possible To Undo User Actions!");
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

    /**
     * Prints all users title
     */
    public void allUsersTitle() {
        System.out.println("== All Users ==");
    }

    /**
     * Prints all users in the system
     */
    public void allUsers() {
        System.out.println(um);
    }

    /**
     * Prints the user profile of a user in the system with this user id
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
    public void frozenUsersTitle() {
        System.out.println("== Frozen Users ==");
    }

    /**
     * Prints the requested users title and description
     */
    public void requestedUsersTitle() {
        System.out.println("== Requested Users ==");
        System.out.println("The following users have requested to be Unfrozen:");
    }

    /**
     * Prints the flagged users title
     */
    public void flaggedUsersTitle() {
        System.out.println("== Flagged Users ==");
    }

    /**
     * Prints the change username's status prompt
     * @param status the new status of a user
     */
    public void statusPrompt(String status) {
        System.out.println("Enter Username to " + status + " Account, or " + getCancelString() + " to Go Back:" );
    }

    /**
     * Prints the threshold edit prompt
     */
    public void thresholdEditPrompt() {
        System.out.println("Please Enter New Threshold, or " + getCancelString() + " to Go Back: ");
    }

    /**
     * Prints the invalid threshold message/prompt
     * @param minValue the minimum acceptable value of a new threshold
     */
    public void badThresholdMessage(int minValue) {
        System.out.println("Invalid Input; Enter Value Greater Than " + minValue + ".");
    }

    /**
     * Prints the unacceptable threshold message/prompt
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

    /**
     * Prints cannot undo return meeting message
     */
    public void cannotUndoReturnMeetingMessage() {
        System.out.println("You cannot undo a return trade for which a meeting has just been initialized and not yet edited.");
    }

    /**
     * Prints undo actions menu
     */
    public void undoUserActionsMenu() {
        System.out.println("== Undo User Actions ==");
        System.out.println("1. Undo Add to Inventory");
        System.out.println("2. Undo Remove from Inventory");
        System.out.println("3. Undo Add to Wishlist");
        System.out.println("4. Undo Remove from Wishlist");
        System.out.println("5. Undo Username Change");
        System.out.println("6. Undo Password Change (Reset Password)");
        System.out.println("7. Undo Trade Action");
        zeroGoBack();
    }

    /**
     * Prints password successfully reset message
     * @param username a String that represents a username
     * @param password a String that represents a password
     */
    public void undoPasswordSuccessful(String username, String password) {
        System.out.println("\nUser " + username + "'s Password Successfully Reset to: " + password + "\n");
    }

    /**
     * Prints undo trade success message
     */
    public void successUndoTradeMessage() {
        System.out.println("Trade action successfully undone!");
    }

    /**
     * Prints undo inventory add title
     */
    public void undoInventoryAddTitle() {
        System.out.println("== Undo Add Inventory Item ==");
    }

    /**
     * Prints username's inventory title
     * @param username a String that represents a username
     */
    public void inventoryTitle(String username) {
        System.out.println("== " + username + "'s Inventory ==");
    }

    /**
     * Prints username's wishlist title
     * @param username a String that represents a username
     */
    public void wishlistTitle(String username) {
        System.out.println(username + "'s Wishlist:");
    }

    /**
     * Prints username's inventory history title
     * @param username a String that represents a username
     */
    public void inventoryHistoryTitle(String username) {
        System.out.println(username + "'s Inventory History:");
    }

    /**
     * Prints undo inventory add prompt
     */
    public void undoInventoryAddPrompt() {
        System.out.println("Undo Most Recently Added Inventory Item? Y/N:");
    }

    /**
     * Prints undo inventory remove prompt
     */
    public void undoInventoryRemovePrompt() {
        System.out.println("Undo Most Recently Removed Inventory Item? Y/N");
    }

    /**
     * Prints undo wishlist add prompt
     */
    public void undoWishlistAddPrompt() {
        System.out.println("Undo Most Recently Added Wishlist Item? Y/N");
    }

    /**
     * Prints undo successful message
     */
    public void undoSuccessfulMessage() {
        System.out.println("Undo Successful! Select Another User, or 0 to Go Back:");
    }

    /**
     * Prints no record of inventory update message
     * @param username a String that represents a username
     */
    public void noInventoryUpdateMessage(String username) {
        System.out.println(username + "'s Account Has No Record of an Inventory Update! Select Another User, or 0 to Go Back:");
    }

    /**
     * Prints no record of wishlist update message
     * @param username a String that represents a username
     */
    public void noWishlistUpdateMessage(String username) {
        System.out.println(username + "'s Account Has No Record of a Wishlist Update! Select Another User, or 0 to Go Back:");
    }

    /**
     * Prints last inventory update cannot be undone message
     * @param username a String that represents a username
     */
    public void noInventoryUndo(String username) {
        System.out.println(username + "'s Last Inventory Update Cannot Be Undone! Select Another User, or 0 to Go Back:");
    }

    /**
     * Prints last wishlist update cannot be undone message
     * @param username a String that represents a username
     */
    public void noWishlistUndo(String username) {
        System.out.println(username + "'s Last Wishlist Update Cannot Be Undone! Select Another User, or 0 to Go Back:");
    }

    /**
     * Prints a prompt indicating the valid options
     * @param size the number of options that are displayed
     */
    public void selectionRange(int size) {
        System.out.println("Please Enter a Number Between 0 and " + size + ":");
    }

    /**
     * Prints undo remove wishlist item title
     */
    public void undoWishlistRemoveTitle() {
        System.out.println("== Undo Remove Wishlist Item ==");
    }

    /**
     * Prints username's wishlist history title
     * @param username a String that represents a username
     */
    public void wishlistHistoryTitle(String username) {
        System.out.println(username + "'s Wishlist History:");
    }

    /**
     * Prints undo inventory remove prompt
     */
    public void undoWishlistRemovePrompt() {
        System.out.println("Undo Most Recently Removed Inventory Item? Y/N");
    }

    /**
     * Prints undo last username update title
     */
    public void undoUsernameTitle() {
        System.out.println("== Undo Last Username Update  ==");
    }

    /**
     * Prints no record of username update message
     * @param username a String that represents a username
     */
    public void noUsernameUpdate(String username) {
        System.out.println(username + "'s Account Has No Record of a Username Update! Select Another User, or 0 to Go Back:");
    }
    /**
     * Prints undo last password update title
     */
    public void undoPasswordTitle() {
        System.out.println("== Undo Last Password Update  ==");
    }

    /**
     * Prints no record of password update message
     * @param username a String that represents a username
     */
    public void noPasswordUpdate(String username) {
        System.out.println(username + "'s Account Has No Record of a Password Update! Select Another User, or 0 to Go Back:");
    }

    /**
     * Prints undo add wishlist title
     */
    public void undoWishlistAddTitle() {
        System.out.println("== Undo Add Wishlist Item ==");
    }

    /**
     * Prints reset password title
     */
    public void resetPasswordTitle() {
        System.out.println("== Reset Password ==");
    }

    /**
     * Prints password reset prompt
     */
    public void passwordResetPrompt() {
        System.out.println("Enter Username to Reset Password:");
    }

    /**
     * Prints select item, or 0 to go back prompt
     */
    public void selectItemOrZero() {
        System.out.println("Select Item, or 0 to Go Back:");
    }

    /**
     * Prints item request options
     */
    public void itemRequestOptions() {
        System.out.println("1. Accept Item");
        System.out.println("2. Reject Item");
    }

    /**
     * Prints item accepted message
     */
    public void itemAccepted() {
        System.out.println("Item Has Been Accepted!");
    }

    /**
     * Prints item rejected message
     */
    public void itemRejected() {
        System.out.println("Item Has Been Rejected!");
    }

    /**
     * Prints undo remove inventory item title
     */
    public void undoInventoryRemoveTitle() {
        System.out.println("== Undo Remove Inventory Item ==");
    }

    /**
     * Prints no flagged users message
     */
    public void noFlaggedUsers() {
        System.out.println("No Flagged Users in the System!");
    }

    /**
     * Prints no frozen users message
     */
    public void noFrozenUsers() {
        System.out.println("No Frozen Users in the System!");
    }

    /**
     * Prints undo trade title
     */
    public void undoTrade(String currStatus) {
        System.out.println("== Undo " + currStatus + "ing a Trade ==");
    }
}