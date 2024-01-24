package presenters;
import entities.Item;
import entities.Meeting;
import usecases.ItemManager;
import usecases.TradeManager;
import usecases.UserManager;

import java.time.*;
import java.util.ArrayList;

public class TradePresenter extends MenuPresenter {

    private final ArrayList<String> validInput = new ArrayList<>();
    private final TradeManager tm;
    private final UserManager um;
    private final ItemManager im;

    /**
     * Constructor of TradePresenter, create a list of valid inputs by user
     */
    public TradePresenter(TradeManager tm, UserManager um, ItemManager im) {
        this.tm = tm;
        this.um = um;
        this.im = im;
        this.validInput.add("Browse Requested Trades");
        this.validInput.add("Browse Accepted Trades");
        this.validInput.add("Browse Rejected Trades");
        this.validInput.add("Browse Cancelled Trades");
        this.validInput.add("Browse Confirmed Trades");
        this.validInput.add("Browse Completed Trades");
    }

    /**
     * Prints existing trades menu
     */
    public void existingTradeMenu() {
        System.out.println("== Existing Trades Menu ==");
        System.out.println("1. Manage Trades in Progress");
        System.out.println("2. Browse Trades by Completion Status");
        System.out.println("3. Browse Trades by Type");
        zeroGoBack(); //System.out.println("0. Go Back");
    }

    /**
     * Prints existing trades by type menu
     */
    public void tradeByTypeMenu() {
        System.out.println("== Existing Trades By Type Menu ==");
        System.out.println("1. Browse all Trades");
        System.out.println("2. Browse Trades in Which You Borrow");
        System.out.println("3. Browse Trades in Which You Lend");
        System.out.println("4. Browse Trades in Which You Lend And Borrow At The Same Time");
        zeroGoBack();
    }

    /**
     * @return inputs from user that can be accepted by the system
     */
    public ArrayList<String> getValidInput() {
        return this.validInput;
    }

    /**
     * User will see different menus based on their status
     * @param s string representation of status
     */
    public void getSelectionMenu(String s) {
        System.out.println("== Trade Menu ==");
        if (s.equals("Bad")) {
            //System.out.println("Please enter a number to the corresponding option:");
            System.out.println("1. Browse and Manage Existing Trades" + "\n2. Browse Recent Items" +
                    "\n3. Browse Frequent Trading Partners" + "\n0. Go Back ");
        } else {
            //System.out.println("Please enter a number to the corresponding option:");
            System.out.println("1. Browse and Manage Existing Trades" +
                    "\n2. Browse and Manage Suggested Trades" +
                    "\n3. Request a Trade via Username" +
                    "\n4. Browse Recently Traded Items" +
                    "\n5. Browse Top Traders" +
                    "\n0. Go Back ");
        }

    }

    // right now 3 goes to request trades, 4 goes to manage trades
    /**
     * List all types of trades that can be selected by the user
     */
    public void getAllTypeOptions() {
        System.out.println("== Existing Trades by Status Menu ==");
        for (int i = 0; i < getValidInput().size(); i++) {
            System.out.println((i + 1) + ". " + getValidInput().get(i));
        }
        zeroGoBack(); //System.out.println("0. Go Back");
    }

    /**
     * Message before displaying a specified type of trade
     */
    public void getTypeTradeNotification() {
        System.out.println("Below are the trades as selected:");
    }

    /**
     * Message asking for username the current user want to trade with
     */
    public void getUserNameNotification() {
        System.out.println("Please enter the username that you want to trade with:");
    }

    /**
     * Display a string representation of all items in the itemList
     *
     * @param itemList a list of Item
     */
    public void getItemListInfo(ArrayList<Item> itemList) {
        for (Item item : itemList) {
            System.out.println(item);
        }
    }

    /**
     * Prints suggest trades menu
     */
    public void suggestPotentialTradeMenu() {
        System.out.println("1. Suggest Potential One-Way Trades");
        System.out.println("2. Suggest Potential Two-Way Trades");
        System.out.println("0. Go Back");
    }


    /**
     * Prints all trade that belong to this user
     * @param userId the user id of a user
     */
    public void viewAllTrades(String userId) {
        int i = 1;
        for (String tradeId : tm.getAllTradeIdByUserId(userId)) {
            System.out.println(i + ". ");
            tradeInfo(tradeId);
            System.out.println();
            i++;
        }
    }

    /**
     * Prints trade info of this trade
     * @param tradeId the trade is of a trade
     */
    public void tradeInfo(String tradeId) {
        System.out.println("Requester: " + um.usernameByUserId(tm.getRequesterByTradeId(tradeId)));
        System.out.println("Receiver: " + um.usernameByUserId(tm.getReceiverByTradeId(tradeId)));
        System.out.println("Items: ");
        for (String item : tm.getItemIdsByTradeId(tradeId)) {
            System.out.println(item);
        }
        System.out.println("Trade status: " + tm.getTradeStatusByTradeId(tradeId));
    }

    /**
     * Display all username in usernameList
     * @param usernameList a list of string representing username
     */
    public void getAllUsername(ArrayList<String> usernameList) {
        for (String username: usernameList) {
            System.out.println(username);
        }
    }

    /**
     * Message asking for id of an item that the user want to trade
     */
    public void getItemIdNotification() {
        System.out.println("Please Enter Item List Index: ");
    }

    /**
     * Message when the given trade is not found
     * @param s a tradeId the user wishes to find
     */
    public void getNotFound(String s) {
        System.out.println(s + " doesn't exist.");
    }

    public void getTheSameUserName() {
        System.out.println("Username is the same as yours or username");
    }

    /**
     * Menu option to choose which type of trade to display
     *
     * @param s1 a type of trade
     * @param s2 a type of trade
     */
    public void getTradeSelectionNotification(String s1, String s2) {
        System.out.println("Which type of trades do you want to display \n" + "1." + s1 + "\n" + "2." + s2);
    }

    /**
     * Menu option to choose which type of information to display
     */
    public void getRecentSelectionNotification() {
        String s1 = "Recent Items That You Have Made Trades With From Your Inventory";
        String s2 = "Recent Items That You Have Borrowed";
        String s3 = "Recent Items That You Have Lent";
        String s4 = "Recent Items That You Have Borrowed And Lent At The Same Time";
        //String s4 = "Top traders you traded with";
        System.out.println("What kind of information do you want to display: " + "\n1." + s1 + "\n2." + s2 +
                "\n3." + s3 + "\n4." + s4);
        zeroGoBack(); //System.out.println("0. Go Back");
    }

    /**
     * Menu option to accept the request or reject the request
     */
    public void getAcceptRejectNotification() {
        System.out.println("Please enter " + "\n1. Accept the requested trade" + "\n2. Reject the requested trade");
    }

    /**
     * Menu option to confirm the trade or view user's meeting
     */
    public void getSwitchConfirmOrMeetingNotification() {
        System.out.println("Please enter " + "\n1. Confirm the trade" + "\n2. Manage your meeting");
    }

    /**
     * Menu option to confirm trade or defer user's decision
     */
    public void getConfirmNotification() {
        System.out.println("Please enter Y to confirm trade or N to defer your decision");
    }

    /**
     * Message when successfully requested a trade
     * @param s string representing type of trade
     */
    public void successfulRequestTradeMade(String s) {
        System.out.println("You have successfully requested a " + s + " trade");
    }

    /**
     * Message when successfully complete an action to a trade
     * @param s string representing trade action
     */
    public void successfulActionTrade(String s) {
        System.out.println("You have successfully " + s + " a trade");
    }

    /**
     * Message when successfully initiate a meeting
     * @param time  the exact time the meeting will take place
     * @param place the place the meeting will happen
     */
    public void successfulInitiateMeeting(LocalDate time, String place) {
        System.out.println("You have successfully initiated a meeting at " + place + ", " + time);
    }

    /**
     * Message asking for number of traders the user want to view
     */
    public void promptNumForTopTraders() {
        System.out.println("Please enter the number of top traders that you want to view or press 0 to go back.");
    }

    /**
     * Prints select trade prompt
     */
    public void promptNumForManageTrade() {
        System.out.println("Please enter the number corresponding to the trade that you would like to manage or press 0 to go back.");
    }

    /**
     * Message asking for number of recent items the user want to view
     * @param s string representation of a number
     */
    public void promptNumRecentItems(String s) {
        System.out.println("Please enter the number of recent items " + s + " that you want to view or press 0 to go back.");
    }

    /**
     * Message when no trade action is available with the specified tradeID
     */
    public void noTradeActionAvailable() {
        System.out.println("No available actions can be made with the trade ID");
    }

    /**
     * Prints no trade partners message
     */
    public void noTradePartnerPrompt() {
        System.out.println("You currently do not have any frequent trading partners!");
    }

    /**
     * Prints no current trades message
     */
    public void noTradePrompt() {
        System.out.println("You currently do not have any trades! ");
    }

    /**
     * Prints no current trades of that sort message
     */
    public void noTradeSortPrompt() {
        System.out.println("You currently do not have any of trades of that sort! ");
    }

    /**
     * Message when the user's borrowing exceeds lending by more than the threshold
     */
    public void violateBorrowDiff() {
        System.out.println("You have many more number of borrowing than number of lending");
    }

    /**
     * Message when the user has number of incomplete trade exceeding the threshold
     */
    public void violateIncompleteTrade() {
        System.out.println("You have too many cancelled trades");
    }

    /**
     * Message when the user has number of trade in a week exceeding the threshold
     */
    public void violateTradeLimit() {
        System.out.println("You have reached trade limit for this week");
    }

    /**
     * Message when the user made an invalid selection
     */
    public void invalidSelection() {
        System.out.println("Please enter the option according to the available options: ");
    }

    /**
     * Message when the user made an invalid input
     */
    public void invalidInput() {
        System.out.println("Invalid input!");
    }

    /**
     * Message when the user made an invalid meeting time
     */
    public void invalidDate() {
        System.out.println("Invalid date! You need to provide a meeting time that is after your current time");
    }

    /**
     * Message asking for meeting place
     */
    public void promptMeetingPlace() {
        System.out.println("Please enter the place you want to meet");
    }

    /**
     * Message asking for meeting time
     */
    public void promptMeetingTime() {
        System.out.println("Please enter the time you want to meet in format of YYYY-MM-DD");
    }

    /**
     * Menu option to go back to upper menu
     */
    public void getGoBackMenuNotification() {
        System.out.println("Press 0 if you want to go back to the upper menu.");
    } //TODO move to MP


    /**
     * Menu option for meetings depending on the current status of user
     * @param s string representation of status
     */
    public void getMeetingNotification(String s) {
        if (s.equals("Not Eligible")) {
            System.out.println("Please enter a number to the corresponding option:");
            System.out.println("1. Display your meeting information");
        } else {
            System.out.println("Please enter a number to the corresponding option:");
            System.out.println("1. Display your meeting information" +
                    "\n2. Edit your meetings " +
                    "\n3. Confirm your meetings ");
        }
        System.out.println("0. Go Back");
    }

    /**
     * List the information of the given meeting
     * @param meeting a Meeting
     */
    public void getMeetingInfo(Meeting meeting) {
        System.out.println(meeting);
    }

    /**
     * Message when a meeting is cancelled by the user
     */
    public void meetingCancelledNotification() {
        System.out.println("This meeting has been cancelled successfully.");
    }

    /**
     * Message when a meeting has been confirmed
     */
    public void getConfirmMeetingNotification(){
        System.out.println("This meeting has been confirmed successfully.");
    }

    /**
     * Message when the item is not in s inventory
     * @param s the user
     */
    public void noAvailableItemNotification(String s) {
        System.out.println("No available items in " + s + " inventory.");
    }

    /**
     * Message when you have requested the item previously
     */
    public void getHaveRequested() {
        System.out.println("You have requested to trade with this item previously");
    }

    /**
     * Message when there is an invalid character in the input
     */
    public void invalidItemName() {
        System.out.println("The name you entered contains invalid characters, please try again.");
    }

    /**
     * Prints enumerated items
     * @param itemIds an array list of item ids
     */
    public void enumerateItems(ArrayList<String> itemIds) {
        if (itemIds.size() == 0) {
            System.out.println("No Item to Show");
        } else {
            for (int i = 0; i < itemIds.size(); i++) {
                System.out.println(i + 1 + ". " + im.nameByItemId(itemIds.get(i)) + ", owned by " + um.usernameByUserId(um.userIdByInventoryItem(itemIds.get(i))));
            }
        }
    }

    /**
     * @param userItemIds a nested array list of user item pairs
     */
    public void enumerateUserItemPairs(ArrayList<ArrayList<String>> userItemIds) {
        if (userItemIds.size() == 0) {
            System.out.println("No Match Found");
        } else {
            for (int i = 0; i < userItemIds.size(); i++) {
                String borrowerId = userItemIds.get(i).get(0);
                String itemId = userItemIds.get(i).get(1);

                System.out.print(i + 1 + ". " + im.nameByItemId(itemId) + ", ");
                System.out.println("to be lent to " + um.usernameByUserId(borrowerId));
            }
        }
    }

    /**
     * Prints suggest trades cannot continue message
     */
    public void suggestTradeQuitedMessage() {
        System.out.println("Suggest Trade Cannot Continue");
    }

    /**
     * Prints potential borrowers options
     */
    public void startWithWhatMessage() {
        System.out.println("1. Start with Items on My Wishlist for Potential Lenders");
        System.out.println("2. Start with Items in My Inventory for Potential Borrowers");
        System.out.println("0. Go Back");
    }

    /**
     * Prints enter item index prompt
     */
    public void itemIndexPrompt() {
        System.out.println("Please Enter Item Index:");
    }

    /**
     * Prints enter trade index prompt
     */
    public void tradeIndexPrompt() {
        System.out.println("Please Enter Trade Index, or \"0\" to quit:");
    }

    /**
     * Prints enter item-borrower index prompt
     */
    public void itemBorrowerPairIndexPrompt() {
        System.out.println("Enter a List Index to Select the Item - Borrower Pair: ");
    }

    /**
     * Prints wishlist available message
     */
    public void availableOnWishlist() {
        System.out.println("The Following Items on Your Wishlist Are Currently Available:");
    }

    /**
     * Prints inventory available message
     */
    public void availableOnInventory() {
        System.out.println("The Following Items on Your Inventory Are Currently Available:");
    }

    /**
     * Prints inventory-wishlist available message
     */
    public void yourInventoryTheirWishlist() {
        System.out.println("Available Items in Your Inventory and on Their Wishlist:");
    }

    /**
     * Prints wishlist-inventory available message
     */
    public void theirInventoryYourWishlist() {
        System.out.println("Available Items in Their Inventory and on Your Wishlist:");
    }

}