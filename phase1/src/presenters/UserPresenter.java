package presenters;

import java.util.ArrayList;

/**
 * A presenter that presents the functionalities of UserSystem
 */
public class UserPresenter {
    private String cancelString = "C";  // the string that the user enters when they wish to cancel the current action at every non-menu-option input prompt

    /**
     * Creates an instance of UserPresenter, setting the cancel string to the default String of "c"
     */
    public UserPresenter() {
    }

    /**
     * Creates an instance of UserPresenter, taking in a String as the cancel string
     * @param cancelString the cancel string that allows user to cancel during any non-menu-option input
     */
    public UserPresenter(String cancelString) {
        this.cancelString = cancelString;
    }

    /**
     * Returns the cancel string currently in use in the UserPresenter
     * @return the cancel string currently in use in the UserPresenter
     */
    public String getCancelString() {
        return cancelString;
    }

    /**
     * Prints chooseOptionPrompt
     */
    public void chooseOptionPrompt() {
        System.out.println("Please Select an Option By Entering the Corresponding Number: ");
    }

    /**
     * Prints invalidInputMessage
     */
    public void invalidInputMessage() {
        System.out.println("Sorry; You Have Entered an Invalid Option.");
    }
    /**
     * Prints incorrectPasswordPrompt
     */
    public void incorrectPasswordPrompt() {
        System.out.println("Incorrect Password. Please Re-Enter Password:");
    }

    /**
     * Prints mainMenuOptions
     */
    public void mainMenuOptions(boolean isFrozen) {
        System.out.println("== Main Menu ==");
        System.out.println("1. Manage Profile");
        System.out.println("2. Manage Items");
        System.out.println("3. Manage Trades");
        if (isFrozen) {
            System.out.println("4. Request to be Unfrozen");
            System.out.print("5. ");
        } else {
            System.out.print("4. ");
        }
        System.out.println("Log Out");

    }

    /**
     * Prints userMenuOptions
     */
    public void userMenuOptions() {
        System.out.println("== Manage Profile ==");
        System.out.println("1. View Profile");
        System.out.println("2. Change Username");
        System.out.println("3. Change Password");
        System.out.println("4. Main Menu");
    }

    /**
     * Prints yourCurrentListMessage
     */
    public void yourCurrentListMessage(boolean isInventoryList) {
        if (isInventoryList) {
            System.out.println("Your Current Inventory Contains :");
        } else {
            System.out.println("Your Current Wishlist Contains :");
        }
    }

    /**
     * Prints changeUsernamePrompt
     */
    public void changeUsernamePrompt(String username) {
        System.out.println("Your Username is " + username + ". Please Enter New Username, or \"" + cancelString + "\" to Cancel: ");
    }

    /**
     * Prints usernameUnavailablePrompt
     */
    public void usernameUnavailablePrompt() {
        System.out.println("Username Unavailable. Enter New Username:");
    }

    /**
     * Prints updateSuccessMessage
     */
    public void updateSuccessMessage(String thing) {
        System.out.println("Your " + thing + " Has Been Updated Successfully!");
    }

    /**
     * Prints currentPasswordPrompt
     */
    public void currentPasswordPrompt() {
        System.out.println("Please Enter Your Current Password, or \"" + cancelString + "\" to Cancel: ");
    }

    /**
     * Prints newPasswordPrompt
     */
    public void newPasswordPrompt() {
        System.out.println("Please Enter Your New Password, or \"" + cancelString + "\" to Cancel: ");
    }

    /**
     * Prints invalidNewPasswordMessage
     */
    public void invalidNewPasswordMessage() {
        System.out.println("Sorry; Invalid Password.");
    }

    /**
     * Prints frozenWarningMessage
     */
    public void frozenWarningMessage() {
        System.out.println("Notice: Your Account Has Been Frozen Due to Violating Our Platform's Rules!");
        System.out.println("You may still browse items and modify your inventory and wishlist, but you may not participate in any new trades. If you would like to request to be unfrozen, please select the corresponding option on the user menu.");
    }

    /**
     * Prints frozenReminderMessage
     */
    public void frozenReminderMessage() {
        System.out.println("Notice: Your Account is Frozen. You Have Already Sent an Unfreeze Request.");
    }

    /**
     * Prints unfreezeRequestSentMessage
     */
    public void unfreezeRequestSentMessage() {
        System.out.println("You have successfully sent a request to be unfrozen to the team of administrators. If an administrator deems that your request is reasonable, your account status will be changed backed to good-standing.");
    }

    /**
     * Prints unfreezeRequestAlreadySentMessage
     */
    public void unfreezeRequestAlreadySentMessage() {
        System.out.println("Your request to be unfrozen has already been sent. Please be patient, and if an administrator deems that your request is reasonable, your account status will be changed backed to good-standing.");
    }

    /**
     * Displays a User's profile
     * @param userInfo an ArrayList that contains the User's username, user ID, and current status in that order
     */
    public void displayProfile(ArrayList<String> userInfo) {
        System.out.println("== Your Profile ==");
        System.out.println("Username: " + userInfo.get(0));
        System.out.println("User ID: " + userInfo.get(1));
        System.out.println("Current status: " + userInfo.get(2));
    }
}
