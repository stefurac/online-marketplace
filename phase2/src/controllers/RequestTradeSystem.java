package controllers;
import entities.Item;
import entities.ItemStatus;
import entities.TradeStatus;
import entities.UserStatus;
import gateways.ItemGateway;
import gateways.TradeGateway;
import gateways.UserGateway;
import presenters.TradePresenter;
import usecases.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * RequestTradeSystem is an controller for requesting trades.
 */
public class RequestTradeSystem {
    boolean isDemo;
    private final String userId;
    private final TradeManager tm;
    private final ItemManager im;
    private final UserManager um;
    private final ItemGateway ig;
    private final TradeGateway tg;
    private final UserGateway ug;
    private final TradePresenter tp;

    Scanner input = new Scanner(System.in);

    /**
     * Instantiates a RequestTradeSystem instance
     * @param userId the ID of the user currently in session
     * @param tm an instance of TradeManager
     * @param im an instance of ItemManager
     * @param um an instance of UserManager
     */
    public RequestTradeSystem(String userId, TradeManager tm, ItemManager im, UserManager um) {
        this.im = im;
        this.um = um;
        this.tm = tm;
        this.tp = new TradePresenter(tm, um, im);
        this.userId = userId;
        this.ig = new ItemGateway();
        this.tg = new TradeGateway();
        this.ug = new UserGateway();
    }

    /**
     * the methods that runs the request-trade menu and all subsequent actions
     */
    void run(boolean isDemo) { //helperRequestTrade()
        this.isDemo = isDemo;
        tp.getTradeSelectionNotification("One-way trade", "Two-way trade");
        tp.getGoBackMenuNotification();
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                // update the file at here
                updateToCsv();
                break;

            case "1":  // One-way trade
               if (validBorrowDiff(userId)) {
                   ArrayList<String> itemWithOwner1 = helperRequestOneItem();
                   if (itemWithOwner1.equals(new ArrayList<String>())) {
                       break;  // item selection aborted
                   }
                   String userId1 = itemWithOwner1.get(1);  // userId1 is that of the other party
                   itemWithOwner1.remove(1);  // remove because at this point, itemWithOwner1 contains {itemToBorrow, receiverId}
                   helperRequestTemPerItem(userId1, itemWithOwner1);  // at this point, itemWithOwner1 contains {itemToBorrow}
               }

               updateToCsv();
               break;

            case "2":  // Two-way trade
                if (helperGetAvailableItemForUserId(getUserId()).isEmpty()) {
                    tp.noAvailableItemNotification("your");
                }
                else {
                    ArrayList<String> itemWithOwner2 = helperRequestTwoItem();
                    if (itemWithOwner2.equals(new ArrayList<String>())) {
                        break;  // item selection aborted
                    }
                    String userId2 = itemWithOwner2.get(2);  // userId2 is that of the other party
                    itemWithOwner2.remove(2);  // remove because at this point, itemWithOwner2 contains {itemToBorrow, itemToLend, receiverId}
                    helperRequestTemPerItem(userId2, itemWithOwner2);  // at this point, itemWithOwner2 contains {itemToBorrow, itemToLend}
                }

                updateToCsv();
                break;

            default:
                tp.invalidSelection();
                run(isDemo);
        }
    }

    private void helperRequestTemPerItem(String userId2, ArrayList<String> al1) {
        tp.getTradeSelectionNotification("Permanent", "Temporary");
        String selection = input.nextLine();
        switch (selection) {
            case "1":
                helperPermanentOrTemporary(userId2, al1, "Permanent");
                break;
            case "2":
                helperPermanentOrTemporary(userId2, al1, "Temporary");
                break;
            case "0":
                break;
            default:
                tp.invalidSelection();
                helperRequestTemPerItem(userId2, al1);
        }
    }

    public void suggestOneWay(String userId) {
        String toBeBorrowed = startWithWishList(userId);
        if (toBeBorrowed.equals("")) {
            tp.suggestTradeQuitedMessage();
        } else {
            ArrayList<String> itemList = new ArrayList<>();  // an ArrayList that adheres to the format of helperRequestTemPerItem()
            itemList.add(toBeBorrowed);
            helperRequestTemPerItem(um.userIdByInventoryItem(toBeBorrowed), itemList);
            updateToCsv();
        }
    }

    public void suggestTwoWay(String userId) {
        String toBeLent;
        String toBeBorrowed;

        boolean quitMenu = false;

        do {
            tp.startWithWhatMessage();
            Scanner scanner = new Scanner(System.in);
            String selection = scanner.nextLine();

            switch (selection) {
                case "0":
                    quitMenu = true;
                    break;

                case "1": // Start with Wishlist
                    toBeBorrowed = startWithWishList(userId);  // start with the wishlist to determine which item will be borrowed

                    if (toBeBorrowed.equals("")) {
                        tp.suggestTradeQuitedMessage();
                        break;
                    } else {
                        toBeLent = suggestItemToLend(userId, um.userIdByInventoryItem(toBeBorrowed));  // determine which item in inventory will be lent

                        if (toBeLent.equals("")) {
                            tp.suggestTradeQuitedMessage();
                            break;
                        } else {
                            ArrayList<String> itemList = new ArrayList<>();  // an ArrayList that adheres to the format of helperRequestTemPerItem()
                            itemList.add(toBeBorrowed);
                            itemList.add(toBeLent);
                            helperRequestTemPerItem(um.userIdByInventoryItem(toBeBorrowed), itemList);
                            updateToCsv();
                        }
                    }
                    break;

                case "2": //  Start with Inventory
                    ArrayList<String> match = startWithInventory(userId);  // start with the inventory to determine which item will be lent

                    if (match.equals(new ArrayList<String>())) {
                        tp.suggestTradeQuitedMessage();
                        break;
                    } else {
                        toBeLent = match.get(1);
                        String borrowerId = match.get(0);

                        toBeBorrowed = suggestItemToBorrow(userId, borrowerId);  // determine which item on wishlist will be borrowed
                        if (toBeBorrowed.equals("")) {
                            tp.suggestTradeQuitedMessage();
                            break;
                        } else {
                            ArrayList<String> itemList = new ArrayList<>();  // an ArrayList that adheres to the format of helperRequestTemPerItem()
                            itemList.add(toBeBorrowed);
                            itemList.add(toBeLent);
                            helperRequestTemPerItem(um.userIdByInventoryItem(toBeBorrowed), itemList);
                            updateToCsv();
                        }
                    }
                    break;

                default:
                    tp.invalidSelection();
                    break;
            }
        } while (!quitMenu);
    }

    public boolean validBorrowDiff(String userId) {
        if (!tm.checkBorrowDiff(userId) && !um.premiumUserIds().contains(getUserId())) {
            tp.violateBorrowDiff();
            um.changeStatus(getUserId(), UserStatus.PROBATION);
            ug.updateUserInfoCsv(um, isDemo);
            return false;
        } else {
            return true;
        }
    }

    private void helperPermanentOrTemporary(String userId2, ArrayList<String> al1, String duration1) {
        // note: the size of al1 determines if this trade is one-way or two-way
        if (duration1.equals("Permanent")) {
            tm.requestTrade(getUserId(), userId2, al1, -1);
            tp.successfulRequestTradeMade("permanent");
        }
        else {
            tm.requestTrade(getUserId(), userId2, al1, 30);
            tp.successfulRequestTradeMade("temporary");
        }
        if (al1.size() == 2) {  // indicative of a two-way trade
            im.changeStatus(al1.get(1), ItemStatus.UNAVAILABLE);  // changes the status of the requester's item if two-way
            ig.updateItemInfoCsv(im, isDemo);
        }
    }

    private ArrayList<String> helperRequestOneItem(){
        tp.getUserNameNotification();
        ArrayList<String> usernames = um.getUsernames();
        usernames.remove(um.usernameByUserId(getUserId()));
        tp.getAllUsername(usernames);
        String userId2 = getUsernameInput(input);

        if (!userId2.equals("0")) {
            // get the Available items in the users inventory
            ArrayList<String> itemList = helperGetAvailableItemForUserId(userId2);
            if (itemList.isEmpty() || haveRequestedAllItem(itemList)) {
                tp.noAvailableItemNotification("this user's");
                return helperRequestOneItem();
            } else {
                ArrayList<Item> itemObjectList = new ArrayList<>();
                for (String itemId : itemList) {
                    itemObjectList.add(im.itemByItemId(itemId));
                }
                tp.getItemListInfo(itemObjectList);
                tp.getItemIdNotification();
                String itemId2 = getItemInput(input, userId2);
                if (itemId2.equals("")) {
                    return new ArrayList<>();  // item selection aborted
                } else {
                    ArrayList<String> itemWithOwner = new ArrayList<>();
                    itemWithOwner.add(itemId2);
                    itemWithOwner.add(userId2);
                    return itemWithOwner;
                }
            }
        } else {
            return new ArrayList<>();
        }
    }

    private ArrayList<String> helperGetAvailableItemForUserId(String userId){
        ArrayList<String> itemIdList = um.inventoryByUserId(userId);
        ArrayList<String> itemList = new ArrayList<>();
        // add available items
        for (String ItemId: itemIdList){
            if(String.valueOf(im.statusByItemId(ItemId)).equals("AVAILABLE")){
                itemList.add(ItemId);
            }
        }
        return itemList;
    }

    private ArrayList<String> helperRequestTwoItem(){
        ArrayList<String> itemWithOwner = helperRequestOneItem();
        if (itemWithOwner.equals(new ArrayList<String>())) {
            return new ArrayList<>();  // item selection aborted
        }
        String ourUserId = getUserId();
        ArrayList<String> itemList = helperGetAvailableItemForUserId(ourUserId);
        ArrayList<Item> itemObjectList = new ArrayList<>();
        for (String itemId: itemList) {
            itemObjectList.add(im.itemByItemId(itemId));
        }
        // print out the information of items in the inventory
        tp.getItemIdNotification();
        tp.getItemListInfo(itemObjectList);
        String itemId = getItemInput(input, ourUserId);
        if (itemId.equals("")) {
            return new ArrayList<>();  // item selection aborted
        } else {
            itemWithOwner.add(1, itemId);
            return itemWithOwner;
        }
    }

    private String getUsernameInput(Scanner input) {
        String username2 = input.nextLine();
        if (username2.equals("0")) {
            return username2;
        }

        if (username2.equals(um.usernameByUserId(getUserId()))){
            tp.getTheSameUserName();
            tp.getUserNameNotification();
            return getUsernameInput(input);
        }
        else if (!um.getUsernames().contains(username2)){
            tp.getNotFound("Username");
            tp.getUserNameNotification();
            return getUsernameInput(input);
        }
        else {
            return um.userIdByUsername(username2);
        }
    }

    private String getItemInput(Scanner input, String userId2) {
        ArrayList<String> inventory = um.availableInventoryByUserId(userId2, im);
        tp.enumerateItems(inventory);

        tp.itemIndexPrompt();
        String itemChoice = input.nextLine();

        while (true) {
            if (isBadInput(itemChoice, inventory.size())) {
                tp.invalidInput();
                itemChoice = input.nextLine();
            } else if (itemChoice.equals("0")) {
                return "";  // breaks out of the loop; item selection aborted

            } else {
                String itemId2 = inventory.get(Integer.parseInt(itemChoice) - 1);
                if (haveRequested(itemId2)) {
                    tp.getHaveRequested();
                } else if (!hasItemToBeTraded(userId2, itemId2)) {
                    tp.getNotFound("Item");
                } else {
                    return itemId2;  // breaks out of the loop
                }

                tp.itemIndexPrompt();
                itemChoice = input.nextLine();
            }
        }
    }

    private boolean hasItemToBeTraded(String userId, String itemId) {
        return um.itemIdInInventory(userId, itemId) && String.valueOf(im.statusByItemId(itemId)).equals("AVAILABLE");
    }

    private boolean haveRequested(String itemId) {
        for (String tradeId: tm.getAllTradeIdByUserId(userId)) {
            // If it's already a requested trade with the exact item with the current user as the requester
            if (String.valueOf(tm.getTradeStatusByTradeId(tradeId)).equals("REQUESTED") &&
                    tm.getRequesterByTradeId(tradeId).equals(getUserId()) &&
                    tm.getItemIdsByTradeId(tradeId).contains(itemId)) {
                return true;
            }
        }
        return false;
    }

    private boolean haveRequestedAllItem(ArrayList<String> itemList) {
        for (String itemId: itemList){
            if (!haveRequested(itemId)) {
                return false;
            }
        }
        return true;
    }

    private String getUserId() {
        return this.userId;
    }

    private String startWithWishList(String userId) {  // returns the item ID to be borrowed
        tp.availableOnWishlist();

        ArrayList<String> availableItems = um.availableWishlistByUserId(userId, im);
        availableItems = filterAlreadyRequested(availableItems, userId);  // filters out items this user has already requested for
        tp.enumerateItems(availableItems);

        if (availableItems.size() == 0) {
            return "";  // trade suggestion cannot continue
        } else {

            Scanner scanner = new Scanner(System.in);
            tp.tradeIndexPrompt();
            String input = scanner.nextLine();

            while (isBadInput(input, availableItems.size())) {
                tp.invalidInput();
                tp.tradeIndexPrompt();
                input = scanner.nextLine();
            }
            if (Integer.parseInt(input) == 0) {
                return "";
            } else {
                return availableItems.get(Integer.parseInt(input) - 1);
            }
        }
    }

    private ArrayList<String> startWithInventory(String userId) {  // returns an (item ID, user ID) pair with the user ID as a potential borrower
        tp.availableOnInventory();

        ArrayList<ArrayList<String>> potentialPairs = um.findPotentialBorrowers(userId, im);
        tp.enumerateUserItemPairs(potentialPairs);

        if (potentialPairs.size() == 0) {
            return new ArrayList<>();  // trade suggestion cannot continue
        } else {

            Scanner scanner = new Scanner(System.in);
            tp.itemBorrowerPairIndexPrompt();
            String input = scanner.nextLine();

            while (isBadInput(input, potentialPairs.size())) {
                tp.invalidInput();
                tp.tradeIndexPrompt();
                input = scanner.nextLine();
            }
            if (Integer.parseInt(input) == 0) {
                return new ArrayList<>();
            } else {
                return potentialPairs.get(Integer.parseInt(input) - 1);
            }
        }
    }

    private String suggestItemToLend(String userId, String otherUserId) {
        tp.yourInventoryTheirWishlist();

        ArrayList<String> otherUserWishlist = um.availableWishlistByUserId(otherUserId, im);
        otherUserWishlist = um.filterByOwner(otherUserWishlist, userId);  // filters for items owned by this user
        otherUserWishlist = filterAlreadyRequested(otherUserWishlist, otherUserId);  // filters out items the other user has already requested for
        tp.enumerateItems(otherUserWishlist);

        if (otherUserWishlist.size() == 0) {
            return "";
        } else {

            Scanner scanner = new Scanner(System.in);
            tp.tradeIndexPrompt();
            String input = scanner.nextLine();

            while (isBadInput(input, otherUserWishlist.size())) {
                tp.invalidInput();
                tp.tradeIndexPrompt();
                input = scanner.nextLine();
            }
            if (Integer.parseInt(input) == 0) {
                return "";
            } else {
                return otherUserWishlist.get(Integer.parseInt(input) - 1);
            }
        }
    }

    private String suggestItemToBorrow(String userId, String otherUserId) {
        tp.theirInventoryYourWishlist();

        ArrayList<String> availableWishlist = um.availableWishlistByUserId(userId, im);
        availableWishlist = um.filterByOwner(availableWishlist, otherUserId);  // filters for items owned by the other user
        availableWishlist = filterAlreadyRequested(availableWishlist, userId);  // filters out items this user has already requested for

        tp.enumerateItems(availableWishlist);

        if (availableWishlist.size() == 0) {
            return "";
        } else {

            Scanner scanner = new Scanner(System.in);
            tp.tradeIndexPrompt();
            String input = scanner.nextLine();

            while (isBadInput(input, availableWishlist.size())) {
                tp.invalidInput();
                tp.tradeIndexPrompt();
                input = scanner.nextLine();
            }
            if (Integer.parseInt(input) == 0) {
                return "";
            } else {
                return availableWishlist.get(Integer.parseInt(input) - 1);
            }
        }
    }

    // returns only the items in the inputted list for which a trade is not already requested
    private ArrayList<String> filterAlreadyRequested(ArrayList<String> itemList, String possibleRequesterId) {
        ArrayList<String> itemsListFiltered = new ArrayList<>();
        ArrayList<String> requesterTrades = tm.getAllStatusTradeByUserId(possibleRequesterId, String.valueOf(TradeStatus.REQUESTED));

        boolean alreadyRequested;

        for (String itemId : itemList) {
            alreadyRequested = false;
            for (String tradeId : requesterTrades) {
                if (tm.getItemIdsByTradeId(tradeId).get(0).equals(itemId)) {
                    alreadyRequested = true;
                    break;
                }
            }
            if (!alreadyRequested) {
                itemsListFiltered.add(itemId);
            }
        }
        return itemsListFiltered;
    }

    private boolean isBadInput(String input, int maxValue) {
        try {
            int selection = Integer.parseInt(input);
            if (selection < 0 || selection > maxValue) return true;
        } catch (NumberFormatException e){
            return true;
        }
        return false;
    }

    private void updateToCsv() {
        ig.updateItemInfoCsv(im, isDemo);
        tg.updateTradeInfoCsv(tm, isDemo);
        ug.updateUserInfoCsv(um, isDemo);
    }
}
