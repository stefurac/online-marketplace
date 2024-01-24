package presenters;

import entities.Item;
import usecases.ItemManager;
import usecases.UserManager;

/**
 * A presenter that presents the functionalities of UserSystem
 */
public class ItemPresenter extends MenuPresenter{

    /**
     * Creates an instance of UserPresenter, setting the cancel string to the default String of "c"
     */
    public ItemPresenter() {
    }
    /**
     * Prints mainMenuOptions
     */
    public void itemMenu() {
        System.out.println("== Item Menu ==");
        System.out.println("1. Browse All Items");
        System.out.println("2. Request to Upload Item to Inventory");
        System.out.println("3. Browse Inventory");
        System.out.println("4. Browse Wishlist");
        zeroGoBack(); //System.out.println("0. Go Back");
    }

    /**
     * Prints all items menu
     */
    public void allItemsMenu() {
        System.out.println("1. Add an Item To Wishlist");
        zeroGoBack(); //System.out.println("0. Go Back");
    }

    /**
     * Prints wishlist menu
     */
    public void wishlistMenu() {
        System.out.println("1. Add to Wishlist");
        System.out.println("2. Remove from Wishlist");
        zeroGoBack(); //System.out.println("0. Go Back");
    }

    /**
     * Prints inventory menu
     */
    public void inventoryMenu() {
        System.out.println("1. Remove from Inventory");
        zeroGoBack(); //System.out.println("0. Go Back");
    }

    /**
     * Prints all items in the system
     * @param um user manager in this system
     * @param im item manager in this system
     */
    public void viewAllItems(ItemManager im, UserManager um) {
        int i = 1;
        for (Item item: im.itemList()) {
            String ownerId = um.userIdByInventoryItem(im.getItemIdByItem(item));
            String owner = um.usernameByUserId(ownerId);
            System.out.println(i + ". " + im.getToString(item.getId()) +  ", owned by " + owner );
            i++;

        }
    }

    /**
     * Prints wishlist of user with this user id
     * @param um user manager in this system
     * @param im item manager in this system
     * @param userId user id of a user
     */
    public void viewWishlist(UserManager um, ItemManager im, String userId) {
        int i = 1;
        for (String item : um.wishlistByUserId(userId)) {
            System.out.println(i + ". " + im.getToString(item));
            i++;
        }
    }

    /**
     * Prints inventory of user with this user id
     * @param um user manager in this system
     * @param im item manager in this system
     * @param userId user id of a user
     */
    public void viewInventory(UserManager um, ItemManager im, String userId) {
        int i = 1;
        for (String item : um.inventoryByUserId(userId)) {
            System.out.println(i + ". " + im.getToString(item));
            i++;
        }
    }

    /**
     * Prints enter item name prompt
     */
    public void addItemName(){
        System.out.println("Please Enter the Name of the Item or press 0 to go back. "); }

    /**
     * Prints the prompt to add the description for a given item being added to the inventory.
     */
    public void addItemDescription(){
        System.out.println("Please Enter a Description of the Item or press 0 to go back. "); }

    /**
     * Prints the confirmation message if the item is successfully added to the inventory.
     */
    public void itemCreationSuccess(){
        System.out.println("Item Successfully Added to The System for Review!");
    }

    public void invalidItemName() {
        System.out.println("The Name Entered Contains Invalid Characters, Please Try Again or press 0 to go back."); }

    /**
     * Prints prompt when user entered an invalid description
     */
    public void invalidDescription() {
        System.out.println("The Description Entered Contains Invalid Characters, Please Try Again or press 0 to go back.");
    }

    /**
     * Prints add item prompt
     */
    public void addItemPrompt() {
        System.out.println("Select the number corresponding to the Item you would like to add or press 0 to go back.");
    }

    /**
     * Prints remove item prompt
     */
    public void removeItemPrompt() {
        System.out.println("Select the number corresponding to the Item you would like to remove or press 0 to go back.");
    }

    /**
     * Prints already in wishlist message
     */
    public void alreadyInWishlistPrompt(){
        System.out.println(" The Item Already Exists in Your Wishlist! ");
    }

    /**
     * Prints not in wishlist message
     */
    public void notInWishlistPrompt(){
        System.out.println(" The Item is Not in Your Wishlist! ");
    }

    /**
     * Prints not in inventory message
     */
    public void notInInventoryPrompt() {
        System.out.println(" The Item is Not in Your inventory! ");
    }

    /**
     * Prints inventory empty message
     */
    public void noInventoryPrompt() {
        System.out.println(" Your Inventory is Empty! ");
    }

    /**
     * Prints no items of that sort message
     */
    public void noItemsPrompt() {
        System.out.println(" You do not have any items of that sort! ");
    }

    /**
     * Prints wishlist empty message
     */
    public void noWishlistPrompt(){
        System.out.println(" Your Wishlist is Empty! ");
    }

    /**
     * Prints item successfully added to wishlist message
     */
    public void addToWishlistMessage(){
        System.out.println(" Item Successfully Added to Your Wishlist! ");
    }

    /**
     * Prints item successfully removed from wishlist message
     */
    public void removedFromWishlistPrompt() {
        System.out.println(" Item Successfully Removed From Your Wishlist! ");
    }

    /**
     * Prints item successfully removed from inventory message
     */
    public void removeFromInventoryPrompt() {
        System.out.println(" \nItem Successfully Removed From Your Inventory!\n ");
    }

    /**
     * Prints information about an enumerated item
     * @param i a number that enumerates the item in the display
     * @param itemId the item id of an item
     * @param username the username of a user
     * @param im item manager in the system
     */
    public void displayItem(int i, String itemId, String username, ItemManager im) {
        String name = im.nameByItemId(itemId);
        String description = im.descriptionByItemId(itemId);
        String status = im.statusByItemId(itemId).toString();
        System.out.println(i + ". " + name + " ("+ status +"): " + description + " - Owner: " + username);
    }

}