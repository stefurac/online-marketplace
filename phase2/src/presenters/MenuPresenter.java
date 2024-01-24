package presenters;

public class MenuPresenter {

    /**
     * Returns the cancel string
     * @return the String that represents cancelling a system prompt
     */
    public String getCancelString() {
        // the string that the user enters when they wish to cancel the current action at every non-menu-option input prompt
        return "0";
    }

    /**
     * Prints a page break
     */
    public void pageBreak() {
        System.out.println("===============================================");
    }

    /**
     * Prints 0. to go back
     */
    public void zeroGoBack() {
        System.out.println("0. Go Back");
    }

    /**
     * Prints 0. to exit
     */
    public void zeroToExit() {
        System.out.println("0. Exit");
    }

    /**
     * Prints 0. to return to main menu
     */
    public void zeroMainMenu() {
        System.out.println("0. Main Menu");
    }

    /**
     * Prints 0. to log out
     */
    public void zeroLogOut() {
        System.out.println("0. Log Out");
    }

    /**
     * Prints the system closing message
     */
    public void closingMessage() {
        System.out.println("Thank You, Goodbye!");
    }

    /**
     * Prints the invalid message message/prompt
     */
    public void invalidInputMessage() {
        System.out.println("Please Enter a Valid Option:");
    }

    /**
     * Prints chooseOptionPrompt
     */
    public void chooseOptionPrompt() {
        System.out.println("Please Select an Option By Entering the Corresponding Number: ");
    }

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

    /**
     * Prints select a user, or 0 to go back prompt
     */
    public void selectUserOrZero() {
        System.out.println("Select a User, or 0 to Go Back:");
    }

    /**
     * Prints select 0 to go back prompt
     */
    public void selectZero() {
        System.out.println("Please Select 0 to Go Back.");
    }

    /**
     * Prints an arbitrary string
     */
    public void printString(String string) {
        System.out.println(string);
    }
}
