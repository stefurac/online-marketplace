package presenters;

import java.util.ArrayList;

/**
 * A presenter that presents the functionalities of UserSystem
 */
public class UserPresenter extends MenuPresenter{

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
        System.out.println("4. Points Guideline");
        if (isFrozen) {
            System.out.println("5. Request to be Unfrozen");
        }
        System.out.println("0. Log Out");

    }

    /**
     * Prints userMenuOptions
     */
    public void userMenuOptions() {
        System.out.println("== Manage Profile ==");
        System.out.println("1. View Profile");
        System.out.println("2. Change Username");
        System.out.println("3. Change Password");
        zeroMainMenu(); //System.out.println("0. Main Menu");
    }

    /**
     * Prints changeUsernamePrompt
     */
    public void changeUsernamePrompt(String username) {
        System.out.println("Your Username is " + username + ". Please Enter New Username, or \"" + getCancelString() + "\" to Cancel: ");
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
        System.out.println("Please Enter Your Current Password, or \"" + getCancelString() + "\" to Cancel: ");
    }

    /**
     * Prints newPasswordPrompt
     */
    public void newPasswordPrompt() {
        System.out.println("Please Enter Your New Password, or \"" + getCancelString() + "\" to Cancel: ");
    }

    /**
     * Prints invalidNewPasswordMessage
     */
    public void invalidNewPasswordMessage() {
        System.out.println("Invalid Password.");
    }

    /**
     * Prints frozenWarningMessage
     */
    public void frozenWarningMessage() {
        System.out.println("Notice: Your Account Has Been Frozen Due to Violating Our Platform's Rules!");
        System.out.println("You may still browse items and modify your inventory and wishlist, but you may not participate in any new trades. If you would like to request to be unfrozen, please select the corresponding option on the user menu.");
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
        System.out.println("Current points: " + userInfo.get(3));
    }

    /**
     * Prints point system guidelines
     */
    public void pointsSystem() {
        System.out.println("== Points Guideline ==");
        System.out.println("We reward good behaviour! The following is a guideline for how to earn points in Group_0025's Trade System:");
        System.out.println("+50 Points: Registration!");
        System.out.println("+25 Points: If an Admin approves your item!");
        System.out.println("+20 Points: Whenever you lend!");
        System.out.println("+30 Points: Whenever you trade!");
        System.out.println("+10 Points (Regular User): Whenever you borrow!");

        System.out.println("\nThe following is a guideline for how to lose points in Group_0025's Trade System:");
        System.out.println("-10 Points: If the System puts you on Probation!");
        System.out.println("-40 Points: If an Admin freezes your account!");
        System.out.println("-10 Points (Premium User): Borrowing costs Premium Users 10 points!");
        pageBreak();
        zeroGoBack();
    }
}
