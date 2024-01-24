package presenters;

/**
 * The presenter that displays login and registration messages/prompts to the screen
 */
public class LoginPresenter extends MenuPresenter{

    /**
     * Prints the system welcome message
     */
    public void welcomeMessage() {
        System.out.println("Welcome to Kensington e-Market! Please Select an Option:");
    }

    /**
     * Prints the option to login or register
     */
    public void options() {
        System.out.println("== Home ==");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Continue as Guest");
//        System.out.println("4. Demo the System");
        zeroToExit(); //System.out.println("0. Exit");
    }

    /**
     * Prints the login menu options
     */
    public void loginOptions() {
        System.out.println("== Login Menu ==");
        System.out.println("1. User Login");
        System.out.println("2. Admin Login");
        zeroGoBack(); //System.out.println("0. Go Back");
    }

    public void logoutMessage() {
        System.out.println("\nLog Out Successful!\n");
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
        System.out.println("Please Enter Username, or " + getCancelString() + " to Go Back: ");
    }

    /**
     * Prints the username not found message
     */
    public void usernameNotFoundPrompt() {
        System.out.println("\nUsername Not Found.\n");
    }

    /**
     * Prints the welcome message upon successful login
     *
     * @param username the username of the user who has successfully logged in
     */
    public void welcomeMessage(String username) {
        System.out.println("\nLog In Successful. Welcome Back, " + username + "!\n");
    }

    /**
     * Prints the password prompt
     */
    public void enterPasswordPrompt() {
        System.out.println("Please Enter Password, or "+ getCancelString() +  " to Go Back: ");
    }

    /**
     * Prints the incorrect password message
     */
    public void incorrectPasswordPrompt() {
        System.out.println("\nIncorrect Password.\n");
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
        System.out.println("Enter New Username, or " + getCancelString() + " to Go Back: ");
    }

    /**
     * Prints the new password prompt
     *
     * @param length the minimum length of a password
     */
    public void newPasswordPrompt(int length) {
        System.out.println("Enter Password ("+length+" or more characters), or " + getCancelString() +" to Go Back:");
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
            System.out.println("\nRegistration Successful! Please Log In to Continue.\n");
        } else {
            System.out.println("\nMaybe later!\n");
        }
    }

    /**
     * Prints the username unavailable prompt
     */
    public void usernameUnavailablePrompt() {
        System.out.println("Username is unavailable. Please enter a new Username: ");
    }

    /**
     * Prints the username and password of a demo user
     * @param username the temp username of a demo user
     * @param password the temp password of a demo user
     */
    public void demoLogin(String username, String password) {
        System.out.println("== Demo System ==");
        System.out.println("Demo Username: " + username);
        System.out.println("Demo Password: " + password);
    }
}
