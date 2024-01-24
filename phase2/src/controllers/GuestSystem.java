package controllers;

import entities.ItemStatus;
import presenters.GuestPresenter;
import presenters.ItemPresenter;
import usecases.ItemManager;
import usecases.UserManager;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A controller that handles all guest functions
 */
public class GuestSystem {
    private final Scanner input = new Scanner(System.in); // create a scanner object
    private final GuestPresenter gp = new GuestPresenter();
    private final ItemPresenter ip = new ItemPresenter();
    private final ItemManager im;
    private final UserManager um;

    /**
     * Instantiates a GuestSystem instance
     * @param im an instance of ItemManager
     * @param um an instance of UserManager
     */
    public GuestSystem(ItemManager im, UserManager um) {
        this.im = im;
        this.um = um;
    }


    /**
     * the methods that runs the guest menu and all subsequent actions
     */
    public void run() {
        boolean exit = false;
        do {
            gp.guestMainMenu();
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    exit = true;
                    break;
                case "1":
                    gp.lrPromptProfile(); //System.out.println("\nPlease Login/Register to Manage Profile \n");
                    break;
                case "2":
                    guestItemMenu();
                    break;
                case "3":
                    gp.lrPromptTrades(); //System.out.println("\nPlease Login/Register to Manage Trades \n");
                    break;
                case "4":
                    gp.pointsSystem();
                    quit();
                    break;
                default:
                    gp.invalidInputMessage();
                    break;

            }
        } while (!exit);
    }

    // helper method for guest item options
    private void guestItemMenu() {
        boolean quitToGuestMenu = false;
        do {
            ip.itemMenu();
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    quitToGuestMenu = true;
                    break;
                case "1":
                    browseItemsControls();
                    //quitToGuestMenu = true;
                    break;
                case "2":
                    gp.lrPromptUploadItem(); //System.out.println("\nPlease Login/Register to Upload Item \n");
                    quitToGuestMenu = true;
                    break;
                case "3":
                    gp.lrPromptViewInventory(); // System.out.println("\nPlease Login/Register to View Inventory \n");
                    quitToGuestMenu = true;
                    break;
                case "4":
                    gp.lrPromptViewWishlist(); //System.out.println("\nPlease Login/Register to View Wishlist \n");
                    quitToGuestMenu = true;
                    break;

                default:
                    gp.invalidInputMessage();
                    break;
            }
        }
        while (!quitToGuestMenu);
    }


    private void browseItemsControls() {
        boolean quitToItemMenu = false;
        do {
            gp.browseAllItems();
            browseItems(im, um);
            gp.pageBreak();
            gp.zeroGoBack();
            String selection = input.nextLine();
            if ("0".equals(selection)) {
                quitToItemMenu = true;
            } else {
                gp.lrPromptSelectItem();
            }
        } while(!quitToItemMenu);
    }
    private void quit() {
        boolean quit = false;
        do {
            String selection = input.nextLine();
            if (selection.equals("0")) {
                quit = true;
            }
            else {
                System.out.println("Please Select 0 to Go Back.");
            }
        }while(!quit);
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
