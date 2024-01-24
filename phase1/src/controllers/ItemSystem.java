package controllers;

import entities.Item;
import gateways.ItemGateway;
import gateways.UserGateway;
import presenters.ItemPresenter;
import usecases.ItemManager;
import usecases.UserManager;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ig is a gateway that reads and updates information of items
 * ug is a gateway that reads and updates information of users
 * ip is a presenter that is responsible for printing menu in this system
 * im ts the ItemManager responsible for keeping track of all items in the system
 * um is the UserManager responsible for keeping track of all users in the system
 * currentUserId is the userId of the User currently using the system
 */
public class ItemSystem {

    private ItemGateway ig = new ItemGateway();
    private UserGateway ug = new UserGateway();
    private ItemPresenter ip = new ItemPresenter();
    private ItemManager im;
    private UserManager um;
    private String currentUserId;
    private String currentItemId = "";
    private String currentListOfItems = "";
    private String[] invalidStrings = new String[] {",", "/"};

    /**
     * @param userId Id of the current user invoking ItemSystem
     * @param um UserManager
     * @throws FileNotFoundException throws exception if file is not found
     */
    public ItemSystem(String userId, UserManager um) throws FileNotFoundException {
        im = new ItemManager(ig.getItemInfoCSV());
        currentUserId = userId;
        this.um = um;
    }

    /**
     * Main method of the controller. It prints menu for user to choose. After the case chosen is done, loop over the
     * menu until the user enter 0. When user enter 0 in the main menu, the function returns and go back to UserSystem
     */
    public void run() {
        boolean indicator = true;
        while (indicator) {
            ip.itemMainMenu();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "1":
                    // view all items in the system
                    ip.viewAllItems(im, um);
                    inputOption(im.toString());
                    if (currentItemId.equals("")) break;
                    viewAllItemsOption();
                    break;
                case "2":
                    // add a new item to the inventory
                    addToInventory();
                    break;
                case "3":
                    // view inventory, then decide which item to delete
                    viewInventory();
                    inputOption(currentListOfItems);
                    if (currentItemId.equals("")) break;
                    removeItemFromInventoryOption();
                    break;
                case "4":
                    // The use should be able to view the wishlist, then decide which item to delete
                    viewWishlist();
                    inputOption(currentListOfItems);
                    if (currentItemId.equals("")) break;
                    removeItemFromWishListOption();
                    break;
                case "0":
                    indicator = false;
                    // update files before exit
                    ig.updateItemInfoCsv(im);
                    ug.updateUserInfoCsv(um);
                    break;
                default:
                    System.out.println("This is an invalid option, please try again.");
                    break;
            }
        }
    }

    /**
     * This is the input option for users when there is unknown numbers of items presented
     * It takes in a list of string representation of items, display them, after the user select the item, set
     * the currentItemId as the itemId for further usage
     * There will be a number before each item, user can press the number to select the desired item
     * The item being chose is retrieved in the helper method
     * @param itemInfo a list of string representation of items
     */
    public void inputOption(String itemInfo) {
        String[] itemsInfo = itemInfo.split("\n");
        int num;
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        while (ip.isBadInput(selection, itemsInfo.length)) {
            ip.badInputMessage();
            selection = input.nextLine();
        }
        num = Integer.parseInt(selection);
        if (num == 0) {
            currentItemId = "";
            return;
        }

        currentItemId = retrieveItemId(itemsInfo, num);
    }

    // helper method
    private String retrieveItemId(String[] itemsInfo, int num) {
        // retrieve itemId
        int endIndex = itemsInfo[num - 1].indexOf("(");
        return itemsInfo[num - 1].substring(3, endIndex).trim();
    }

    private void removeItemFromInventoryOption(){
        ip.itemRemovalMenu();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "1":
                removeFromInventory();
                break;
            case "0":
            default:
                System.out.println("This is an invalid option, please try again.");
                break;
        }
    }

    private void removeItemFromWishListOption(){
        ip.itemRemovalMenu();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "1":
                removeFromWishList();
                break;
            case "0":
            default:
                System.out.println("This is an invalid option, please try again.");
                break;
        }
    }

    /**
     * Menu option for a single item selected by the user.
     */
    public void viewAllItemsOption() {
        ip.singleItemMenu();
        Scanner input = new Scanner(System.in);
        String selection2 = input.nextLine();
        switch (selection2) {
            case "1":
                addToWishList();
                break;
            case "0":
            default:
                System.out.println("This is an invalid option, please try again.");
                break;
        }
    }

    private void addToInventory() {
        Scanner input = new Scanner(System.in);
        ip.addItemName();
        String itemName = input.nextLine();
        while (containsInvalidString(itemName)) {
            ip.invalidItemName();
            itemName = input.nextLine();
        }
        ip.addItemDescription();
        String itemDescription = input.nextLine();
        while (containsInvalidString(itemDescription)) {
            ip.invalidDescription();
            itemDescription = input.nextLine();
        }
        // creating a new item via ItemManager
        String itemId = im.createNewItem(itemName, itemDescription);
        // adding item to user's inventory via UserManager
        um.addToInventory(currentUserId, itemId);
        ip.itemCreationSuccess();
    }

    private void viewInventory() {
        ArrayList<String> inventory = um.inventoryByUserId(currentUserId);
        viewListOfItems(im, inventory);
        System.out.println(currentListOfItems);
        ip.itemPrompt();
        ip.goBackToUpperLevelMenu();
    }

    private void removeFromInventory() {
        if (im.getItemStatus(currentItemId) != Item.ItemStatus.UNAVAILABLE) {
            um.removeFromInventory(currentUserId, currentItemId);
            im.removeItemFromSystem(currentItemId);
            ip.removeFromInventoryMessage();
        }
        else ip.removalFailMessage();
    }

    private void addToWishList() {
        um.addToWishlist(currentUserId, currentItemId);
        ip.addToWishlistMessage();
    }

    private void viewWishlist() {
        ArrayList<String> wishList = um.wishlistByUserId(currentUserId);
        viewListOfItems(im, wishList);
        System.out.println(currentListOfItems);
        ip.itemPrompt();
        ip.goBackToUpperLevelMenu();
    }

    private void viewListOfItems(ItemManager im, ArrayList<String> items) {
        int i = 1;
        currentListOfItems = "";
        for(String itemId: items) {
            currentListOfItems = currentListOfItems.concat(ip.printItemInfo(im, itemId, i) + "\n");
            i ++;
        }
    }

    private void removeFromWishList() {
        um.removeFromWishlist(currentUserId, currentItemId);
        ip.removeFromWishlistMessage();
    }

    private boolean containsInvalidString(String anyString) {
        for (String s: invalidStrings) {
            if (anyString.contains(s)) return true;
        }
        return false;
    }
}

