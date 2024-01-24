package presenters;
import entities.Item;
import entities.Meeting;
import entities.Trade;

import java.time.*;
import java.util.ArrayList;

public class TradePresenter {

    private ArrayList<String> validInput = new ArrayList<>();

    /**
     * Constructor of TradePresenter, create a list of valid inputs by user
     */
    public TradePresenter() {
        this.validInput.add("Borrowing trades from another user");
        this.validInput.add("Lending trades to another user");
        this.validInput.add("All trades");
        this.validInput.add("Two way trades (borrow and lend simultaneously)"); // Two-Way Trade
        this.validInput.add("Requested");
        this.validInput.add("Accepted");
        this.validInput.add("Rejected");
        this.validInput.add("Cancelled");
        this.validInput.add("Confirmed");
        this.validInput.add("Completed");
    }

    /**
     * @return inputs from user that can be accepted by the system
     */
    public ArrayList<String> getValidInput() {
        return this.validInput;
    }

    /**
     * User will see different menus based on their status
     *
     * @param s string representation of status
     */
    public void getSelectionMenu(String s) {
        System.out.println("== Manage Trades ==");
        if (s.equals("Bad")) {
            System.out.println("Please enter a number to the corresponding option:");
            System.out.println("1. Display All Your Trades" + "\n2. Display Recent Items Traded or Common Traders");
        } else {
            System.out.println("Please enter a number to the corresponding option:");
            System.out.println("1. Display All Your Trades" +
                    "\n2. Display Recent Items Traded or Common Traders" +
                    "\n3. Request for a Trade" +
                    "\n4. Manage Your Trades ");
        }

    }

    /**
     * List all types of trades that can be selected by the user
     */
    public void getAllTypeOptions() {
        System.out.println("Here are the following types of trades you can select:");
        for (int i = 0; i < getValidInput().size(); i++) {
            System.out.println((i + 1) + ". " + getValidInput().get(i));
        }
    }

    /**
     * Message before displaying a specified type of trade
     *
     * @param s string representation of trade type
     */
    public void getTypeTradeNotification(String s) {
        System.out.println("Below are the trades of type: " + s);
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
     * Display a string representation of all trades in the tradeList
     *
     * @param tradeList a list of Trade
     */
    public void getTradeListInfo(ArrayList<Trade> tradeList) {
        for (Trade trade : tradeList) {
            System.out.println(trade);
        }
    }

    /**
     * Display all username in usernameList
     *
     * @param usernameList a list of string representing username
     */
    public void getAllUsername(ArrayList<String> usernameList) {
        for (String username: usernameList) {
            System.out.println(username);
        }
    }

    public void getManagementNotification() {
        System.out.println("Please enter the tradeId you want to manage");
    }

    /**
     * Message asking for id of an item that the user want to trade
     */
    public void getItemIdNotification() {
        System.out.println("Please enter the item ID that you want to make a trade: ");
    }

    /**
     * Message asking for id of an item in the user's inventory that the user want to trade
     */
    public void getItemIdNotification2() {
        System.out.println("Please enter the item ID in your inventory that you want to make a trade: ");
    }

    /**
     * Message asking for id for a trade the user want to manage
     */
    public void getTradeNotification() {
        System.out.println("Please enter the trade ID that you want to manage: ");
    }

    /**
     * Message when the given trade is not found
     *
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
        String s1 = "Recent items for borrow trade";
        String s2 = "Recent items for lend trade";
        String s3 = "Recent items for borrow with lend trade";
        String s4 = "Top traders you traded with";
        System.out.println("What kind of information do you want to display: " + "\n1." + s1 + "\n2." + s2 +
                "\n3." + s3 + "\n4." + s4);
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
     *
     * @param s string representing type of trade
     */
    public void successfulRequestTradeMade(String s) {
        System.out.println("You have successfully requested a " + s + " trade");
    }

    /**
     * Message when successfully complete an action to a trade
     *
     * @param s string representing trade action
     */
    public void successfulActionTrade(String s) {
        System.out.println("You have successfully " + s + " a trade");
    }

    /**
     * Message when successfully initiate a meeting
     *
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
        System.out.println("Please enter the number of top traders that you want to view");
    }

    /**
     * Message asking for number of recent items the user want to view
     *
     * @param s string representation of a number
     */
    public void promptNumRecentItems(String s) {
        System.out.println("Please enter the number of recent items " + s + "that you want to view");
    }

    /**
     * Message when no trade action is available with the specified tradeID
     */
    public void noTradeActionAvailable() {
        System.out.println("No available actions can be made with the trade ID");
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
        System.out.println("Press 0 if you want to go back to the upper menu:");
    }


    /**
     * Menu option for meetings depending on the current status of user
     *
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
    }

    /**
     * List the information of the given meeting
     *
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
}