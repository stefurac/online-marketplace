package controllers;
import entities.Item;
import entities.Trade;
import presenters.*;
import usecases.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DisplayTradeSystem {
    /**
     * A controller class which branches from TradeSystem
     *
     * tm is the usecase for Trade
     * im is the usecase for Item
     * um is the usecase for User
     * tp is the presenter for Trade
     * userId is the current user's userId
     */
    private TradeManager tm;
    private ItemManager im;
    private UserManager um;
    private TradePresenter tp = new TradePresenter();
    private String userId;

    DisplayTradeSystem(String userId, TradeManager tm, ItemManager im, UserManager um) {
        this.im = im;
        this.um = um;
        this.tm = tm;
        this.userId = userId;
    }

    void helperDisplaySelection() {
        // Options for displaying Trades
        // Display all based on type Trades
        tp.getAllTypeOptions(); // borrowing, lending, trade (two way), all, TradeStatus.
        tp.getGoBackMenuNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        ArrayList<Trade> trade = new ArrayList<>();
        switch (selection) {
            case "0":
                break;

            case "1":
                helperDisplayTrade(trade, "Borrow", tm.getAllBorrowTradeIdByUserId(getUserId()));
                break;

            case "2":
                helperDisplayTrade(trade, "Lend", tm.getAllLendTradeIdByUserId(getUserId()));
                break;

            case "3":
                helperDisplayTrade(trade, "All", tm.getAllTradeIdByUserId(getUserId()));
                break;

            case "4":
                helperDisplayTrade(trade, "Borrow with Lend", tm.getAllTwoWayTradeIdByUserId(getUserId()));
                break;

            case "5":
                helperDisplayTrade(trade, "Requested", tm.getAllStatusTradeByUserId(getUserId(), "REQUESTED"));
                break;

            case "6":
                helperDisplayTrade(trade, "Accepted", tm.getAllStatusTradeByUserId(getUserId(), "ACCEPTED"));
                break;

            case "7":
                helperDisplayTrade(trade, "Rejected", tm.getAllStatusTradeByUserId(getUserId(), "REJECTED"));
                break;

            case "8":
                helperDisplayTrade(trade, "Cancelled", tm.getAllStatusTradeByUserId(getUserId(), "CANCELLED"));
                break;

            case "9":
                helperDisplayTrade(trade, "Confirmed", tm.getAllStatusTradeByUserId(getUserId(), "CONFIRMED"));
                break;

            case "10":
                helperDisplayTrade(trade, "Completed", tm.getAllStatusTradeByUserId(getUserId(), "COMPLETED"));
                break;

            default:
                tp.invalidSelection();
                helperDisplaySelection();
        }
    }

    private void helperDisplayTrade(ArrayList<Trade> trade, String presenterNotification, ArrayList<String> arrayList) {
        tp.getTypeTradeNotification(presenterNotification);
        for (String tradeId : arrayList) {
            trade.add(tm.findTrade(tradeId));
        }
        tp.getTradeListInfo(trade);
    }

    void helperDisplayRecent() {
        tp.getRecentSelectionNotification();
        tp.getGoBackMenuNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                break;

            case "1":
                displayRecentBorrowItems();
                break;

            case "2":
                displayRecentLendItems();
                break;

            case "3":
                displayRecentTwoWayItems();
                break;

            case "4":
                displayTopTraders();
                break;

            default:
                tp.invalidSelection();
        }
    }

    private void displayRecentBorrowItems() {
        tp.promptNumRecentItems("for borrow trade");
        int num1 = getIntInput();
        ArrayList<String> al1 = tm.getRecentItemsBorrowedByUserId(getUserId(), num1);
        ArrayList<Item> itemList1 = new ArrayList<>();
        for (String itemId: al1) {
            itemList1.add(im.findItem(itemId));
        }
        tp.getItemListInfo(itemList1);
    }

    private void displayRecentLendItems() {
        tp.promptNumRecentItems("for lend trade");
        int num2 = getIntInput();
        ArrayList<String> al2 = tm.getRecentItemsLendByUserId(getUserId(), num2);
        ArrayList<Item> itemList2 = new ArrayList<>();
        for (String itemId: al2) {
            itemList2.add(im.findItem(itemId));
        }
        tp.getItemListInfo(itemList2);
    }

    private void displayRecentTwoWayItems() {
        tp.promptNumRecentItems("for borrow with lend trade");
        int num3 = getIntInput();
        ArrayList<String> al3 = tm.getRecentItemsInTwoWayByUserId(getUserId(), num3);
        ArrayList<Item> itemList3 = new ArrayList<>();
        for (String itemId: al3) {
            itemList3.add(im.findItem(itemId));
        }
        tp.getItemListInfo(itemList3);
    }

    private void displayTopTraders() {
        tp.promptNumForTopTraders();
        int num4 = getIntInput();
        ArrayList<String> al4 = tm.getTopTradersByUserId(getUserId(), num4);
        ArrayList<String> usernameList = new ArrayList<>();
        for (String userId: al4) {
            usernameList.add(um.usernameByUserId(userId));
        }
        tp.getAllUsername(usernameList);
    }

    private String getUserId() {
        return this.userId;
    }

    private int getIntInput() {
        Scanner input = new Scanner(System.in);
        try {
            int n = input.nextInt();
            if (n > 0) {
                return n;
            } else {
                tp.invalidInput();
                return getIntInput();
            }
        } catch (InputMismatchException e) {
            tp.invalidInput();
            return getIntInput();
        }
    }
}