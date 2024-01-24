package controllers;

import entities.Item;
import entities.ItemStatus;
import gateways.ItemGateway;
import gateways.UserGateway;
import presenters.ItemPresenter;
import presenters.UserPresenter;
import usecases.ItemManager;
import usecases.UserManager;
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
    boolean isDemo;
    private final ItemManager im;
    private final UserManager um;
    private final UserPresenter up;
    private final ItemPresenter ip = new ItemPresenter();
    private final ItemGateway ig = new ItemGateway();
    private final UserGateway ug = new UserGateway();
    private final String userId;
    private final String[] invalidStrings = new String[] {",", "/"};
    private final Scanner input = new Scanner(System.in); // create a scanner object

    /**
     * @param userId Id of the current user invoking ItemSystem
     * @param um     UserManager
     * @param im     ItemManager
     */

    // make this work with the gateway such that it is the same as the MeetingSystem; that is, is also has an item manager
    // that already exists
    public ItemSystem(String userId, UserManager um, ItemManager im, UserPresenter up) {
        this.userId = userId;
        this.im = im;
        this.um = um;
        this.up = up;
    }

    /**
     * the methods that runs the item menu and all subsequent actions
     */
    public void run(boolean isDemo) {
        this.isDemo = isDemo;
        boolean quitItemMenu = false;

        do {
            //up.chooseOptionPrompt();
            ip.itemMenu();

            String selection = input.nextLine();
            switch (selection) {
                case "0":  // "Return to main menu"
                    quitItemMenu = true;
                    break;
                case "1":
                    browseItems(im, um); //changed call from ip.viewAllItems(im, um);
                    allItemsMenu();
                    break;
                case "2":
                    addToInventoryMenu();
                    break;
                case "3":
                    ip.viewInventory(um, im, userId);
                    inventoryMenu();
                    break;
                case "4":
                    ip.viewWishlist(um, im, userId);
                    wishlistMenu();
                    break;
                default:
                    up.invalidInputMessage();
                    break;
            }
        } while (!quitItemMenu);
    }

    private void allItemsMenu() {
        boolean quitAllItemsMenu = false;

        do {
            up.chooseOptionPrompt();
            ip.allItemsMenu();

            String selection = input.nextLine();
            switch (selection) {
                case "0":  // "Return to main menu"
                    quitAllItemsMenu = true;
                    break;
                case "1":
                    browseItems(im, um); //changed call from ip.viewAllItems(im, um);
                    ip.addItemPrompt();
                    String itemChoice = input.nextLine();
                    if (itemChoice.equals("0")) {quitAllItemsMenu = true; break;}
                    ArrayList<String> choices = new ArrayList<>();
                    int i = 1;
                    for (Item ignored : im.itemList()) {
                        String choice = Integer.toString(i);
                        choices.add(choice);
                        i++;
                    }

                    while (!(choices).contains(itemChoice) || itemChoice.equals("0")) {
                        if (itemChoice.equals("0")) {quitAllItemsMenu = true; break;}
                        up.invalidInputMessage();

                        itemChoice = input.nextLine();
                    }
                    int itemChoiceIndex = Integer.parseInt(itemChoice) - 1;
                    Item item = im.itemList().get(itemChoiceIndex);
                    addToWishlist(im.getItemIdByItem(item));
                    quitAllItemsMenu = true;
                    break;
                default:
                    up.invalidInputMessage();
                    break;
            }
        } while (!quitAllItemsMenu);
    }

    private void addToInventoryMenu() {
        boolean quitAddInventoryMenu = false;

        do {
            ip.addItemName();
            String itemName = input.nextLine();
            switch (itemName) {
                case "0":  // "Return to main menu"
                    quitAddInventoryMenu = true;
                    break;
                default:

                    while (containsInvalidString(itemName) || itemName.equals("")) {
                        ip.invalidItemName();
                        itemName = input.nextLine();
                        if (itemName.equals("0")) {quitAddInventoryMenu = true; break;}
                    }
                    ip.addItemDescription();
                    String itemDescription = input.nextLine();
                    switch (itemDescription) {
                        case "0":  // "Return to main menu"
                            quitAddInventoryMenu = true;
                            break;
                        default:
                            while (containsInvalidString(itemDescription)) {
                                ip.invalidDescription();
                                itemDescription = input.nextLine();
                                if (itemDescription.equals("0")) {quitAddInventoryMenu = true; break;}
                            }
                            String itemId = im.createNewItem(itemName, itemDescription);
                            ig.updateItemInfoCsv(im, isDemo);
                            um.addToInventory(userId, itemId);
                            ug.updateUserInfoCsv(um, isDemo);
                            ip.itemCreationSuccess();
                            quitAddInventoryMenu = true;

                    }
            }
            ig.updateItemInfoCsv(im, isDemo);
        } while (!quitAddInventoryMenu);
    }

    private void inventoryMenu() {
        boolean quitInventoryMenu = false;

        do {
            up.chooseOptionPrompt();
            ip.inventoryMenu();

            String selection = input.nextLine();
            switch (selection) {
                case "0":  // "Return to main menu"
                    quitInventoryMenu = true;
                    break;
                case "1":

                    int i = 1;

                    ArrayList<String> inventory = um.inventoryByUserId(userId);
                    if (inventory.isEmpty()) {
                        ip.noInventoryPrompt();
                        break;
                    }
                    ArrayList<String> inventoryChoices = new ArrayList<>();

                    for (String ignored : inventory) {
                        String choice = Integer.toString(i);
                        inventoryChoices.add(choice);
                        i++;
                    }

                    ip.viewInventory(um, im, userId);
                    ip.removeItemPrompt();

                    String inventoryChoice = input.nextLine();

                    while (!(inventoryChoices).contains(inventoryChoice) || inventoryChoice.equals("0")) {
                        if (inventoryChoice.equals("0")) {
                            quitInventoryMenu = true;
                            break;
                        }
                        up.invalidInputMessage();
                        inventoryChoice = input.nextLine();
                    }
                    if (inventoryChoice.equals("0")) {
                        quitInventoryMenu = true;
                        break;
                    }
                    int itemChoiceIndex = Integer.parseInt(inventoryChoice) - 1;
                    String itemId = inventory.get(itemChoiceIndex);
                    removeFromInventory(inventory, itemId);
                    quitInventoryMenu = true;
                    break;
                default:
                    up.invalidInputMessage();
                    break;
            }
        } while (!quitInventoryMenu);
    }

    private void wishlistMenu() {
        boolean quitWishlistMenu = false;

        do {
            up.chooseOptionPrompt();
            ip.wishlistMenu();

            String selection = input.nextLine();
            switch (selection) {
                case "0":  // "Return to main menu"
                    quitWishlistMenu = true;
                    break;
                case "1":
                    ip.viewAllItems(im, um);
                    ip.addItemPrompt();
                    String itemChoice = input.nextLine();
                    if (itemChoice.equals("0")) {quitWishlistMenu = true; break;}
                    ArrayList<String> choices = new ArrayList<>();
                    int i = 1;
                    for (Item item: im.itemList()) {
                        String choice = Integer.toString(i);
                        choices.add(choice);
                        i++;
                    }

                    while (!(choices).contains(itemChoice) || itemChoice.equals("0")) {
                        if (itemChoice.equals("0")) {quitWishlistMenu = true; break;}
                        up.invalidInputMessage();

                        itemChoice = input.nextLine();
                    }
                    int itemChoiceIndex = Integer.parseInt(itemChoice) - 1;
                    Item item = im.itemList().get(itemChoiceIndex);
                    addToWishlist(im.getItemIdByItem(item));
                    quitWishlistMenu = true;
                    break;
                case "2":

                    int iterator = 1;


                    ArrayList<String> wishlist = um.wishlistByUserId(userId);
                    if (wishlist.isEmpty()) {
                        ip.noWishlistPrompt();
                        break;
                    }
                    ArrayList<String> wishlistChoices = new ArrayList<>();

                    for (String ignored : wishlist) {
                        String choice = Integer.toString(iterator);
                        wishlistChoices.add(choice);
                        iterator++;
                    }
                    ip.viewWishlist(um, im, userId);
                    ip.removeItemPrompt();

                    String wishlistChoice = input.nextLine();

                    while (!(wishlistChoices).contains(wishlistChoice) || wishlistChoice.equals("0")) {
                        if (wishlistChoice.equals("0")) {
                            quitWishlistMenu = true;
                            break;
                        }
                        up.invalidInputMessage();
                        wishlistChoice = input.nextLine();
                    }
                    if (wishlistChoice.equals("0")) {
                        quitWishlistMenu = true;
                        break;
                    }
                    int wishlistChoiceIndex = Integer.parseInt(wishlistChoice) - 1;
                    String itemId = wishlist.get(wishlistChoiceIndex);
                    removeFromWishlist(itemId);
                    quitWishlistMenu = true;
                    break;
                default:
                    up.invalidInputMessage();
                    break;
            }
        } while (!quitWishlistMenu);
    }
    private void addToWishlist(String itemId) {
        if (!um.wishlistByUserId(userId).contains(itemId)) {
            um.addToWishlist(userId, itemId);
            ip.addToWishlistMessage();        }
        else { ip.alreadyInWishlistPrompt(); }
        ug.updateUserInfoCsv(um, isDemo);
    }

    private void removeFromWishlist(String itemId) {
        if (um.wishlistByUserId(userId).contains(itemId)) {
            //um.systemRemoveFromWishlist(userId, itemId);
            um.userRemoveFromWishList(userId, itemId);
            ug.updateUserInfoCsv(um, isDemo);
            ip.removedFromWishlistPrompt();        }
        else { ip.notInWishlistPrompt(); }
    }

    private void removeFromInventory(ArrayList<String> inventory, String itemId) {
        if (inventory.contains(itemId)) {
            //um.systemRemoveFromInventory(userId, itemId);
            im.removeByItemId(itemId);
            um.userRemoveFromInventory(userId, itemId, im);
            ip.removeFromInventoryPrompt();
        } else {
            ip.notInInventoryPrompt();
        }
        ig.updateItemInfoCsv(im, isDemo);
        ug.updateUserInfoCsv(um, isDemo);
    }

    private boolean containsInvalidString(String anyString) {
        for (String s: invalidStrings) {
            if (anyString.contains(s)) return true;
        }
        return false;
    }

    private void browseItems(ItemManager im, UserManager um) { //, ItemStatus status
        int i = 1;
        ArrayList<String> items = im.itemIdsByStatus(ItemStatus.AVAILABLE);
        items.addAll(im.itemIdsByStatus(ItemStatus.UNAVAILABLE));
        for (String itemId: items) {
            String userId = um.userIdByInventoryItem(itemId);
            String username = um.usernameByUserId(userId);
            ip.displayItem(i, itemId, username, im);
            i++;
        }
    }
}