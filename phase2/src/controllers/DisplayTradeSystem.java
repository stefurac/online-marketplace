package controllers;
import entities.Item;
import presenters.*;
import usecases.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A controller class responsible for displaying trades
 */
public class DisplayTradeSystem {

    boolean isDemo;
    private final TradeManager tm;
    private final ItemManager im;
    private final UserManager um;
    private final TradePresenter tp;
    private final UserPresenter up = new UserPresenter();
    private final ItemPresenter ip = new ItemPresenter();
    private final String userId;
    private final DoActionTradeSystem dats;


    /**
     * Instantiates a DisplayTradeSystem instance
     * @param userId the ID of the user currently in session
     * @param tm an instance of TradeManager
     * @param im an instance of ItemManager
     * @param um an instance of UserManager
     * @param dats an instance of DoActionTradeSystem
     */
    DisplayTradeSystem(String userId, TradeManager tm, ItemManager im, UserManager um, DoActionTradeSystem dats) {
        this.im = im;
        this.um = um;
        this.tm = tm;
        this.tp = new TradePresenter(tm, um, im);
        this.userId = userId;
        this.dats = dats;
    }
    Scanner input = new Scanner(System.in);

    /**
     * the methods that runs the display-trade menu and all subsequent actions
     */
    void run(boolean isDemo) { //existingTradesMenu()
        this.isDemo = isDemo;
        // Options for displaying Trades
        // Display all based on type Trades
        boolean quitMenu = false;

        do {
            tp.existingTradeMenu(); // borrowing, lending, trade (two way), all, TradeStatus.
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    quitMenu = true;
                    break;
                case "1":  // Manage Trades in Progress
                    manageTradesMenu();
                    break;
                case "2":  // Browse Trades by Completion Status
                    tradeByStatusMenu();
                    break;
                case "3":  // Browse Trades by Type
                    tradeByTypeMenu();
                    break;
                default:
                    tp.invalidSelection();
                    run(isDemo);
                    break;
            }
        } while (!quitMenu);

    }


    private void manageTradesMenu() {
        while (true) {
            helperDisplayAllTrade();
            int tradesSize = tm.getAllTradeIdByUserId(userId).size();
            tp.promptNumForManageTrade();
            String tradeChoice = input.nextLine();

            while (isBadInput(tradeChoice, tradesSize)) {  // validating input
                up.invalidInputMessage();
                tradeChoice = input.nextLine();
            }

            if (tradeChoice.equals("0")) {  // exiting the menu
                break;
            }

            int itemChoiceIndex = Integer.parseInt(tradeChoice) - 1;
            String tradeId = tm.getAllTradeIdByUserId(userId).get(itemChoiceIndex);
            dats.run(tradeId, isDemo);
        }
    }

    void tradeByTypeMenu() {
        boolean quitMenu = false;

        do {
            // Options for displaying Trades
            // Display all based on type Trades
            up.chooseOptionPrompt();
            tp.tradeByTypeMenu(); // borrowing, lending, trade (two way), all, TradeStatus.
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    quitMenu = true;
                    break;
                case "1":
                    helperDisplayAllTrade();
                    break;
                case "2":
                    helperDisplayTrade(tm.getAllBorrowTradeIdByUserId(getUserId()));
                    break;
                case "3":
                    helperDisplayTrade(tm.getAllLendTradeIdByUserId(getUserId()));
                    break;
                case "4":
                    helperDisplayTrade(tm.getAllTwoWayTradeIdByUserId(getUserId()));
                    break;
                default:
                    tp.invalidSelection();
                    tradeByTypeMenu();
                    break;
            }  }
        while (!quitMenu);
    }


    void tradeByStatusMenu() {
        // Options for displaying Trades
        // Display all based on type Trades
        boolean quitMenu = false;

        do {
            tp.getAllTypeOptions(); // borrowing, lending, trade (two way), all, TradeStatus.
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    quitMenu = true;
                    break;
                case "1":
                    helperDisplayTrade(tm.getAllStatusTradeByUserId(getUserId(), "REQUESTED"));
                    break;

                case "2":
                    helperDisplayTrade(tm.getAllStatusTradeByUserId(getUserId(), "ACCEPTED"));
                    break;

                case "3":
                    helperDisplayTrade(tm.getAllStatusTradeByUserId(getUserId(), "REJECTED"));
                    break;

                case "4":
                    helperDisplayTrade(tm.getAllStatusTradeByUserId(getUserId(), "CANCELLED"));
                    break;

                case "5":
                    helperDisplayTrade(tm.getAllStatusTradeByUserId(getUserId(), "CONFIRMED"));
                    break;

                case "6":
                    helperDisplayTrade(tm.getAllStatusTradeByUserId(getUserId(), "COMPLETED"));
                    break;

                default:
                    tp.invalidSelection();
                    tradeByStatusMenu();
                    break;
            } }while (!quitMenu);
    }

    private void helperDisplayTrade(ArrayList<String> tradeIds) {
        if (tradeIds.isEmpty()) {
            tp.noTradeSortPrompt();
        }
        else {
            tp.getTypeTradeNotification();
            for (String tradeId : tradeIds) {
                tp.tradeInfo(tradeId);
            }
        }
    }

    private void helperDisplayAllTrade() {
        ArrayList<String> allTrades = tm.getAllTradeIdByUserId(userId);

        if (allTrades.isEmpty()) {
            tp.noTradePrompt();
        }
        else {
            tp.viewAllTrades(userId);
        }
    }

    void helperDisplayRecent() {
        boolean quitMenu = false;

        do {
            tp.getRecentSelectionNotification();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    quitMenu = true;
                    break;

                case "1":
                    displayRecentItems();
                    break;

                case "2":
                    displayRecentBorrowItems();
                    break;

                case "3":
                    displayRecentLendItems();
                    break;

                case "4":
                    displayRecentTwoWayItems();
                    break;


//            case "4":
//                displayTopTraders();
//                break;

                default:
                    tp.invalidSelection();
                    break;
            } }while (!quitMenu);
    }

    private void displayRecentBorrowItems() {
        tp.promptNumRecentItems("recent items you have borrowed");
        int num1 = getIntInput();
        ArrayList<String> al1 = tm.getRecentItemsBorrowedByUserId(getUserId(), num1);
        ArrayList<Item> itemList1 = new ArrayList<>();
        for (String itemId: al1) {
            itemList1.add(im.itemByItemId(itemId));
        }
        if (itemList1.isEmpty()) {ip.noItemsPrompt();}
        else {
            tp.getItemListInfo(itemList1);
        }}

    private void displayRecentLendItems() {
        tp.promptNumRecentItems("you have lent");
        int num2 = getIntInput();
        ArrayList<String> al2 = tm.getRecentItemsLendByUserId(getUserId(), num2);
        ArrayList<Item> itemList2 = new ArrayList<>();
        for (String itemId: al2) {
            itemList2.add(im.itemByItemId(itemId));
        }
        if (itemList2.isEmpty()) {ip.noItemsPrompt();}
        else {
            tp.getItemListInfo(itemList2);
        }}

    private void displayRecentTwoWayItems() {
        tp.promptNumRecentItems("you have borrowed and lent at the same time");
        int num3 = getIntInput();
        ArrayList<String> al3 = tm.getRecentItemsInTwoWayByUserId(getUserId(), num3);
        ArrayList<Item> itemList3 = new ArrayList<>();
        for (String itemId: al3) {
            itemList3.add(im.itemByItemId(itemId));
        }
        if (itemList3.isEmpty()) {ip.noItemsPrompt();}
        else {

            tp.getItemListInfo(itemList3);
        } }

    private void displayRecentItems() {
        tp.promptNumRecentItems("you have traded with");
        int num3 = getIntInput();
        ArrayList<String> al3 = tm.getRecentItemsByUserId(getUserId(), num3);
        ArrayList<Item> itemList3 = new ArrayList<>();
        for (String itemId: al3) {
            itemList3.add(im.itemByItemId(itemId));

        }
        if (itemList3.isEmpty()) {ip.noItemsPrompt();}
        else {
            tp.getItemListInfo(itemList3);
        } }


    public void displayTopTraders() {
        tp.promptNumForTopTraders();
        int num4 = getIntInput();
        ArrayList<String> al4 = tm.getTopTradersByUserId(getUserId(), num4);
        ArrayList<String> usernameList = new ArrayList<>();
        for (String userId: al4) {
            usernameList.add(um.usernameByUserId(userId));
        }
        if (usernameList.isEmpty()) {tp.noTradePartnerPrompt();}
        else {
            tp.getAllUsername(usernameList);
        } }

    private String getUserId() {
        return this.userId;
    }

    private int getIntInput() {
        Scanner input = new Scanner(System.in);
        try {
            int n = input.nextInt();
            if (n >= 0) {
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

    private boolean isBadInput(String input, int maxValue) {
        try {
            int selection = Integer.parseInt(input);
            if (selection < 0 || selection > maxValue) return true;
        } catch (NumberFormatException e){
            return true;
        }
        return false;
    }


}