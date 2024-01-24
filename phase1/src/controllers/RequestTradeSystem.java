package controllers;
import entities.Item;
import entities.UserStatus;
import gateways.ItemGateway;
import gateways.TradeGateway;
import gateways.UserGateway;
import presenters.TradePresenter;
import usecases.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RequestTradeSystem {
    /**
     * RequestTradeSystem is an controller for requesting trades.
     * userId is the id of the User who is currently using the system.
     * TradePresenter is the Presenter for Trade.
     * TradeManager is the UseCase Class TradeManager.
     * ItemManager is the UseCase Class ItemManager.
     * UserManager is the UseCase Class UserManager.
     * ItemGateway is the Gateway Class for updating information of Item.
     * TradeGateway is the Gateway Class for updating information of Trade.
     * UserGateway is the Gateway Class for updating information of User.
     */
    private String userId;
    private TradePresenter tp = new TradePresenter();
    private TradeManager tm;
    private ItemManager im;
    private UserManager um;
    private ItemGateway ig;
    private TradeGateway tg;
    private UserGateway ug;

    Scanner input = new Scanner(System.in);

    // The constructor for TradeSystem
    RequestTradeSystem(String userId, TradeManager tm, ItemManager im, UserManager um) {
        this.im = im;
        this.um = um;
        this.tm = tm;
        this.userId = userId;
        this.ig = new ItemGateway();
        this.tg = new TradeGateway();
        this.ug = new UserGateway();
    }

    // The helper function for RequestTrade
    void helperRequestTrade() {
        tp.getTradeSelectionNotification("One-way-trade", "Two-way-trade");
        tp.getGoBackMenuNotification();
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                // update the file at here
                updateToCsv();
                break;

            case "1":
               if (!validBorrowDiff(getUserId())) {
                   tp.violateBorrowDiff();
                   um.changeStatus(getUserId(), UserStatus.FLAGGED);
               }
               else {
                   ArrayList<String> itemWithOwner1 = helperRequestOneItem();
                   String userId1 = itemWithOwner1.get(1);
                   itemWithOwner1.remove(1);
                   helperRequestTemPerItem(userId1, itemWithOwner1);
               }

               updateToCsv();
               break;

            case "2":
                if (helperGetAvailableItemForUserId(getUserId()).isEmpty()) {
                    tp.noAvailableItemNotification("your");
                }
                else {
                    ArrayList<String> itemWithOwner2 = helperRequestTwoItem();
                    String userId2 = itemWithOwner2.get(2);
                    itemWithOwner2.remove(2);
                    helperRequestTemPerItem(userId2, itemWithOwner2);
                }

                updateToCsv();
                break;

            default:
                tp.invalidSelection();
                helperRequestTrade();
        }
    }

    private void helperRequestTemPerItem(String userId2, ArrayList<String> al1){
        tp.getTradeSelectionNotification("Permanent", "Temporary");
        String selection = input.nextLine();
        switch (selection) {
            case "1":
                helperPermanentOrTemporary(userId2, al1, "Permanent");
                break;
            case "2":
                helperPermanentOrTemporary(userId2, al1, "Temporary");
                break;
            default:
                tp.invalidSelection();
                helperRequestTemPerItem(userId2, al1);
        }
    }

    private void helperPermanentOrTemporary(String userId2, ArrayList<String> al1, String duration1) {
        if (duration1.equals("Permanent")) {
            tm.requestTrade(getUserId(), userId2, al1, -1);
            tp.successfulRequestTradeMade("permanent");
        }
        else {
            tm.requestTrade(getUserId(), userId2, al1, 30);
            tp.successfulRequestTradeMade("temporary");
        }
        if (al1.size() == 2) {
            im.makeItemUnavailable(al1.get(1));
        }
    }

    private ArrayList<String> helperRequestOneItem(){
        tp.getUserNameNotification();
        ArrayList<String> usernames = um.getUsernames();
        usernames.remove(um.usernameByUserId(getUserId()));
        tp.getAllUsername(usernames);
        String userId2 = getUsernameInput(input);
        // get the Available items in the users inventory
        ArrayList<String> itemList = helperGetAvailableItemForUserId(userId2);
        if (itemList.isEmpty() || haveRequestedAllItem(itemList)) {
            tp.noAvailableItemNotification("this user's");
            return helperRequestOneItem();
        }
        else {
            ArrayList<Item> itemObjectList = new ArrayList<>();
            for (String itemId: itemList) {
                itemObjectList.add(im.findItem(itemId));
            }
            tp.getItemListInfo(itemObjectList);
            tp.getItemIdNotification();
            String itemId2 = getItemInput(input, userId2);
            ArrayList<String> itemWithOwner = new ArrayList<>();
            itemWithOwner.add(itemId2);
            itemWithOwner.add(userId2);
            return itemWithOwner;
        }
    }

    private ArrayList<String> helperGetAvailableItemForUserId(String userId){
        ArrayList<String> itemIdList = um.inventoryByUserId(userId);
        ArrayList<String> itemList = new ArrayList<>();
        // add available items
        for (String ItemId: itemIdList){
            if(String.valueOf(im.getItemStatus(ItemId)).equals("AVAILABLE")){
                itemList.add(ItemId);
            }
        }
        return itemList;
    }

    private ArrayList<String> helperRequestTwoItem(){
        ArrayList<String> itemWithOwner = helperRequestOneItem();
        String ourUserId = getUserId();
        ArrayList<String> itemList = helperGetAvailableItemForUserId(ourUserId);
        ArrayList<Item> itemObjectList = new ArrayList<>();
        for (String itemId: itemList) {
            itemObjectList.add(im.findItem(itemId));
        }
        // print out the information of items in the inventory
        tp.getItemIdNotification2();
        tp.getItemListInfo(itemObjectList);
        String itemId = getItemInput(input, ourUserId);
        itemWithOwner.add(1, itemId);
        return itemWithOwner;
    }

    private String getUsernameInput(Scanner input) {
        String username2 = input.nextLine();
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
        String itemId2 = input.nextLine();
        while (!hasItemToBeTraded(userId2, itemId2) || haveRequested(itemId2)) {
            // checks if item name exists in user's inventory or unavailable for Trade
            if (haveRequested(itemId2)) {
                tp.getHaveRequested();
            }
            else {
                tp.getNotFound("Item");
            }
            if (userId2.equals(getUserId())) {
                tp.getItemIdNotification2();
            }
            else {
                tp.getItemIdNotification();
            }
            itemId2 = input.nextLine();
        }
        return itemId2;
    }

    private boolean hasItemToBeTraded(String userId, String itemId) {
        if (um.itemIdInInventory(userId, itemId) &&
                String.valueOf(im.getItemStatus(itemId)).equals("AVAILABLE")) {
            return true;
        } else {
            return false;
        }
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

    private boolean validBorrowDiff(String userId) {
        if (!tm.checkBorrowDiff(userId)) {
            um.changeStatus(getUserId(), UserStatus.FLAGGED);
            return false;
        } else {
            return true;
        }
    }

    private String getUserId() {
        return this.userId;
    }

    private void updateToCsv() {
        ig.updateItemInfoCsv(im);
        tg.updateTradeInfoCsv(tm);
        ug.updateUserInfoCsv(um);
    }
}
