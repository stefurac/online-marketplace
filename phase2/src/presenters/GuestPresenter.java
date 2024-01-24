package presenters;

public class GuestPresenter extends UserPresenter{

    /**
     * Prints the guest menu options
     */
    public void guestMainMenu() {
        System.out.println("== Guest Main Menu ==");
        System.out.println("1. Manage Profile");
        System.out.println("2. Manage Items");
        System.out.println("3. Manage Trades");
        System.out.println("4. Points Guideline");
        System.out.println("0. Login/Register");
    }

    /**
     * Prints the login/register prompt to manage profile
     */
    public void lrPromptProfile() {
        System.out.println("\nPlease Login/Register to Manage Profile \n");
    }

    /**
     * Prints the login/register prompt to manage trades
     */
    public void lrPromptTrades() {
        System.out.println("\nPlease Login/Register to Manage Trades \n");
    }

    /**
     * Prints the login/register prompt to upload item
     */
    public void lrPromptUploadItem() {
        System.out.println("\nPlease Login/Register to Upload Item \n");
    }

    /**
     * Prints the login/register prompt to view inventory
     */
    public void lrPromptViewInventory() {
        System.out.println("\nPlease Login/Register to View Inventory \n");
    }

    /**
     * Prints the login/register prompt to view wishlist
     */
    public void lrPromptViewWishlist() {
        System.out.println("\nPlease Login/Register to View Wishlist \n");
    }

    /**
     * Prints the login/register prompt to select an item
     */
    public void lrPromptSelectItem() {
        System.out.println("\nPlease Login/Register to Select an Item\n");
    }

    /**
     * Prints the browse all items title
     */
    public void browseAllItems() {
        System.out.println("== Browse All Items ==");
    }
}
