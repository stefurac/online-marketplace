package presenters;

/**
 * The presenter that displays login and registration messages/prompts to the screen
 */
public class LoginPresenter {
    private final String cancelString = "C";  // the string that the user enters when they wish to cancel the current action at every non-menu-option input prompt

    /**
     * Returns the cancel string
     *
     * @return the String that represents cancelling a system prompt
     */
    public String getCancelString() {
        return cancelString;
    }

    /**
     * Prints the system welcome message
     */
    public void welcomeMessage() {
        System.out.println("Welcome to Group_0025's Trade System! Please select an option:");
    }

    /**
     * Prints the select option prompt
     */
    public void selectOptionPrompt() {
        System.out.println("Please Select an Option By Entering the Corresponding Number: ");
    }

    /**
     * Prints the option to login or register
     */
    public void options() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }

    /**
     * Prints the login menu options
     */
    public void loginOptions() {
        System.out.println("1. User Login");
        System.out.println("2. Admin Login");
        System.out.println("3. Go Back");
    }

    /**
     * Prints the system closing message
     */
    public void closingMessage() {
        System.out.println("Thank You, Goodbye!");
    }

    /**
     * Prints the admin login title
     */
    public void adminLoginMessage() {
        System.out.println("== Admin Login ==");
    }

    /**
     * Prints the user login title
     */
    public void userLoginMessage() {
        System.out.println("== User Login ==");
    }

    /**
     * Prints the username prompt
     */
    public void enterUsernamePrompt() {
        System.out.println("Please Enter Username, or \"" + cancelString + "\" to Cancel: ");
    }

    /**
     * Prints the username not found message
     */
    public void usernameNotFoundPrompt() {
        System.out.println("Username Not Found.");
    }

    /**
     * Prints the welcome message upon successful login
     *
     * @param username the username of the user who has successfully logged in
     */
    public void welcomeMessage(String username) {
        System.out.println("Login Successful. Welcome Back, " + username + "!");
    }

    /**
     * Prints the password prompt
     */
    public void enterPasswordPrompt() {
        System.out.println("Please Enter Password, or \"" + cancelString + "\" to Cancel: ");
    }

    /**
     * Prints the incorrect password message
     */
    public void incorrectPasswordPrompt() {
        System.out.println("Incorrect Password.");
    }

    /**
     * Prints the invalid message message/prompt
     */
    public void invalidInputMessage() {
        System.out.println("Please Enter a Valid Option");
    }

    /**
     * Prints the welcome message for a new user
     *
     * @param username the username of a new user
     */
    public void welcomeNewUserMessage(String username) {
        System.out.println("Welcome, " + username + "!");
    }

    /**
     * Prints the user registration title
     */
    public void userRegistrationMessage() {
        System.out.println("== User Registration ==");
    }

    /**
     * Prints the new username prompt
     */
    public void newUsernamePrompt() {
        System.out.println("Enter New Username, or \"" + cancelString + "\" to Cancel: ");
    }

    /**
     * Pritns the new password prompt
     *
     * @param length the minimum length of a password
     */
    public void newPasswordPrompt(int length) {
        System.out.println("Enter Password ("+length+" or more characters), or \"" + cancelString +" to Cancel:");
    }

    /**
     * Prints the invalid password message
     */
    public void invalidNewPasswordMessage() {
        System.out.println("Sorry; Invalid Password.");
    }

    /**
     * Prints the registration confirmation prompt
     */
    public void proceedRegistrationPrompt() {
        System.out.println("Register Account? Y/N: ");
    }

    /**
     * Prints whether the user successfully registers
     *
     * @param isSuccessful is true iff registration is successful
     */
    public void registrationResultMessage(boolean isSuccessful) {
        if (isSuccessful) {
            System.out.println("Registration Successful! Please Login to Continue.");
        } else {
            System.out.println("Maybe later!");
        }
    }

    /**
     * Prints the username unavailable prompt
     */
    public void usernameUnavailablePrompt() {
        System.out.println("Username is unavailable. Please enter a new Username: ");
    }
}
