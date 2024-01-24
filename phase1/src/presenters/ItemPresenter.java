package presenters;

import usecases.ItemManager;
import usecases.UserManager;
import entities.Item;

public class ItemPresenter {

    /**
     * Presents prompts for the main menu in the Item system.
     */
    public void itemMainMenu() {
        System.out.println("== Manage Items ==");
        System.out.println("1. View All Items");
        System.out.println("2. Upload New Item to Inventory");
        System.out.println("3. View Your Inventory");
        System.out.println("4. View Your Wishlist");
        goBackToUpperLevelMenu();
    }

    /**
     * Prints all items in the ItemManager.
     * @param im the input ItemManager whose items we want to view.
     * @param um the input UserManager
     */
    public void viewAllItems(ItemManager im, UserManager um) {
        int i = 1;
        for (Item item: im.getAllInfo()) {
            String ownerId = um.findUserByItemInventory(im.getItemIdByItem(item));
            String owner = um.usernameByUserId(ownerId);
            System.out.println(i + "." + item + ", owned by " + owner + "\n");
            i++;
        }
        System.out.println("Enter Item Number To Select an Item.");
        goBackToUpperLevelMenu();
    }

    /**
     * Determines whether the input number is within the range of menu items.
     * @param input the input string.
     * @param numRange the range the input should fall within.
     * @return a boolean indicating whether the input is valid or not.
     */
    public boolean isBadInput(String input, int numRange) {
        try {
            int selection = Integer.parseInt(input);
            if (selection < 0 || selection > numRange) return true;
        } catch (NumberFormatException e){
            return true;
        }

        return false;
    }

    /**
     * The menu prompt for one item.
     */
    public void singleItemMenu(){
        System.out.println("1. Add Item to Wishlist");
        goBackToUpperLevelMenu();
    }

    public void itemRemovalMenu() {
        System.out.println("1. Remove Item");
        goBackToUpperLevelMenu();
    }

    /**
     * The prompt for entering an item to be added to the inventory,
     */
    public void addItemName(){
        System.out.println("Please Enter the Name of the Item: "); //"Please Enter the Name of the Item to be added to the inventory: "
    }

    /**
     * The prompt to add the description for a given item being added to the inventory.
     */
    public void addItemDescription(){
        System.out.println("Please Enter a Description of the Item, or Enter 0 to Go Back to Upper Level Menu. : ");
    }

    /**
     * The confirmation message if the item is successfully added to the inventory.
     */
    public void itemCreationSuccess(){
        System.out.println("Item Successfully Added to The System for Review!");
    }

    /**
     * The confirmation message if the item is successfully removed from the inventory.
     */
    public void removeFromInventoryMessage(){
        System.out.println("Item Successfully Removed From Your Inventory!");
    }

    /**
     * The confirmation message if the item is failed to be removed from the inventory
     */
    public void removalFailMessage() {
        System.out.println("This Item Cannot Be Removed From Your Inventory.");
    }

    /**
     * The confirmation message if the item is successfully added to the wishlist.
     */
    public void addToWishlistMessage(){
        System.out.println("Item Successfully Added to Your Wishlist!");
        goBackToUpperLevelMenu();
    }

    /**
     * The confirmation message if the item is successfully removed from the wishlist.
     */
    public void removeFromWishlistMessage(){
        System.out.println("Item Successfully Removed From Your Wishlist!");
    }

    /**
     * The message presented if the input value for a menu is out of range.
     */
    public void badInputMessage(){
        System.out.println("Invalid Option, Please Try Again, or Enter 0 to Go Back to Upper Level Menu.");
    }

    /**
     * Prints the information about a given item.
     * @param im the ItemManager where the item exists.
     * @param itemId the id of the item whose information we wish to retrieve.
     * @param i the number of the item in the inventory.
     * @return the string of the item info.
     */
    public String printItemInfo(ItemManager im, String itemId, int i){
        return i + ". " + im.singleItemToString(itemId);
    }

    /**
     * Prints menu to go back to upper level menu
     */
    public void goBackToUpperLevelMenu() {
        System.out.println("Enter 0 to Go Back to Upper Level Menu.");
    }

    /**
     * Print prompt asking for input from user
     */
    public void itemPrompt() {
        System.out.println("Enter Item Number to Select an Item.");
    }

    /**
     * Print prompt when user entered an invalid item name
     */
    public void invalidItemName() {
        System.out.println("The Name Entered Contains Invalid Characters, Please Try Again.");
    }

    /**
     * Print prompt when user entered an invalid description
     */
    public void invalidDescription() {
        System.out.println("The Description Entered Contains Invalid Characters, Please Try Again.");
    }

}
