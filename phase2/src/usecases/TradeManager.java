package usecases;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.*;

import entities.*;

public class TradeManager {
    /**
     * TradeManager is a use case for Trade
     * <p>
     * editThreshold is the threshold for editing.
     * cancelledThreshold is the threshold for incomplete transaction.
     * weeklyTradeLimit is the threshold for weekly Trade limit.
     * borrowDiff is the threshold for the difference between the number of borrowing and number of lending.
     * tradeIdToTrade is to store tradeId and Trade.
     */
    private final int cancelledThreshold;
    private final int weeklyTradeLimit;
    private final int borrowDiff;
    private final HashMap<String, Trade> tradeIdToTrade = new HashMap<>();

    /**
     * Creates a TradeManager with editThreshold, cancelledThreshold, openThreshold, weeklyTradeLimit, and borrowDiff
     * based on the tradeManagerList and stores the HashMap of tradeId to Trade from the database.
     *
     * @param listThresholds List of thresholds from the ThresholdGateway
     * @param listTrade List of Trade
     */
    public TradeManager(List<Integer> listThresholds, List<Trade> listTrade) {
        this.cancelledThreshold = listThresholds.get(0);
        this.weeklyTradeLimit = listThresholds.get(1);
        this.borrowDiff = listThresholds.get(2);
        for (Trade trade: listTrade) {
            this.tradeIdToTrade.put(trade.getTradeId(), trade);
        }
    }

    /**
     * Getters for cancelledThreshold
     *
     * @return an int of cancelledThreshold
     */
    public int getCancelledThreshold() {
        return this.cancelledThreshold;
    }

    /**
     * Getters for weeklyTradeLimit
     *
     * @return an int of weeklyTradeLimit
     */
    public int getWeeklyTradeLimit() {
        return this.weeklyTradeLimit;
    }

    /**
     * Getters for borrowDiff
     *
     * @return an int of borrowDiff
     */
    public int getBorrowDiff() {
        return this.borrowDiff;
    }

    /**
     * Getters for tradeIdToTrade
     *
     * @return a HashMap of tradeId and Trade
     */
    public HashMap<String, Trade> getTradeIdToTrade() {
        return this.tradeIdToTrade;
    }
    
    /**
     * Add a tradeId and Trade to the HashMap of tradeIdToTrade
     *
     * @param tradeId The trade ID
     * @param trade The Trade object corresponding from the trade ID
     */
    public void addTrade(String tradeId, Trade trade) {
        getTradeIdToTrade().put(tradeId, trade);
    }

    /**
     * Request (and created) a Trade and adding the new Trade into the
     *
     * @param userId1 The requester
     * @param userId2 The receiver
     * @param itemIds The List of item IDs to be traded
     * @param duration The duration of the Trade
     */
    public void requestTrade(String userId1, String userId2, ArrayList<String> itemIds, int duration) {
        String tradeId = createNewTrade(userId1, userId2, itemIds);
        findTrade(tradeId).setDuration(duration);
    }

    /**
     * Find a Trade based on the Trade ID
     *
     * @param tradeId the Trade ID
     * @return Trade object corresponding to the Trade ID
     */
    public Trade findTrade(String tradeId) {
        return getTradeIdToTrade().get(tradeId);
    }

    /**
     * Rejects a Trade. Sets the status of the Trade to REJECTED
     *
     * @param tradeId the Trade ID
     */
    public void rejectTrade(String tradeId) {
        Trade trade = findTrade(tradeId);
        trade.setStatus(TradeStatus.REJECTED);
    }

    /**
     * Accepts a Trade. Sets the status of the Trade to ACCEPTED
     *
     * @param tradeId the Trade ID
     */
    public void acceptTrade(String tradeId) {
        Trade trade = findTrade(tradeId);
        trade.setStatus(TradeStatus.ACCEPTED);
    }

    /**
     * The meeting for the Trade is confirmed. Sets the status of the Trade to CONFIRMED
     *
     * @param tradeId the Trade ID
     */
    public void meetingConfirmed(String tradeId) {
        Trade trade = findTrade(tradeId);
        trade.setStatus(TradeStatus.CONFIRMED);
    }

    /**
     * Confirm that the meeting took place in real life.
     *
     * @param tradeId the Trade ID
     * @param userId the user ID that confirms the Trade
     */
    public void confirmTrade(String tradeId, String userId) {
        Trade trade = findTrade(tradeId);
        if (trade.getStatus().equals(TradeStatus.CONFIRMED) && trade.getRequester().equals(userId)) {
            trade.setStatus(TradeStatus.REQUESTER_CONFIRMED);
        } else if (trade.getStatus().equals(TradeStatus.CONFIRMED) && trade.getReceiver().equals(userId)) {
            trade.setStatus(TradeStatus.RECEIVER_CONFIRMED);
        } else if (trade.getStatus().equals(TradeStatus.REQUESTER_CONFIRMED) ||
                trade.getStatus().equals(TradeStatus.RECEIVER_CONFIRMED)) {
            completeTrade(tradeId);
        }
    }

    /**
     * Sets the Trade status into COMPLETED
     *
     * @param tradeId the Trade ID
     */
    public void completeTrade(String tradeId) {
        Trade trade = findTrade(tradeId);
        trade.setStatus(TradeStatus.COMPLETED);
    }

    /**
     * Cancels a Trade (only happens when the editThreshold in Meeting reached). Sets the Trade status into CANCELLED.
     *
     * @param tradeId the Trade ID
     */
    public void cancelTrade(String tradeId) {
        Trade trade = findTrade(tradeId);
        trade.setStatus(TradeStatus.CANCELLED);
    }

    public void changeStatus(String tradeId, TradeStatus status) {
        findTrade(tradeId).setStatus(status); }

    /**
     * Checks whether the userId violates the borrowDiff threshold
     * @param userId the user ID
     * @return false if the userId violates. Otherwise, true.
     */
    public boolean checkBorrowDiff(String userId) {
        int borrowNum = getAllCompletedBorrowTradeByUserId(userId).size();
        int lendNum = getAllCompletedLendTradeByUserId(userId).size();
        return borrowNum - lendNum < getBorrowDiff();
    }

    /**
     * Returns the Trade that the user ID has made in one week
     * @param userId the user ID
     * @return List of Strings of tradeIds
     */
    public List<String> tradesInOneWeek(String userId) {
        ArrayList<String> ls = new ArrayList<>();

        for (Map.Entry<String, Trade> trade : getTradeIdToTrade().entrySet()) {
            // If the username initialized a Trade or if the username has received a Trade and did not reject it
            // and the Trade is not a return trade
            if (helperTradesInOneWeek(trade.getValue(), userId)) {
                LocalDate date1 = trade.getValue().getDateCreated();
                LocalDate date2 = LocalDate.now();
                long daysBetween = ChronoUnit.DAYS.between(date1, date2);
                int diffDays = Integer.parseInt(String.valueOf(daysBetween));
                if (diffDays < 7) {
                    ls.add(trade.getKey());
                }
            }
        }
        return ls;
    }

    // Helper for tradesInOneWeek -- returns true if it's likely to be counted as "Trade in One Week"
    private boolean helperTradesInOneWeek(Trade trade, String userId) {
        if (trade.getTradeId().startsWith("@return")) {
            // if it's a returning Trade, return false
            return false;
        } else if (trade.getStatus().equals(TradeStatus.REJECTED)) {
            // if it's a rejected Trade, return false
            return false;
        } else if (trade.getRequester().equals(userId)) {
            // if the person requests the Trade (and not rejected)
            return true;
        } else {
            return trade.getReceiver().equals(userId) && !trade.getStatus().equals(TradeStatus.REQUESTED);  // if the person has a Trade (and has a state after accepted)
        }
    }

    /**
     * Checks whether the user ID has reached the tradeLimit threshold
     * @param userId the user ID
     * @return true iff the user ID has reached the tradeLimit
     */
    public boolean checkTradeLimit(String userId) {
        int numTradeOneWeek = tradesInOneWeek(userId).size();
        return numTradeOneWeek >= getWeeklyTradeLimit();
    }

    /**
     * Checks whether the user ID has reached the cancelledThreshold
     * @param userId the user ID
     * @return true iff the user ID has exceeded the cancelledThreshold
     */
    public boolean checkIncompleteTrade(String userId, UserManager um) {
        boolean returnBoolean;
        int num = getAllStatusTradeByUserId(userId, "CANCELLED").size();
        returnBoolean = num >= getCancelledThreshold();
        if (returnBoolean) {
            um.setIncompleteTradeOffset(userId, um.getIncompleteTradeOffset(userId) + num);  // resetting incomplete trade count so that this user doesn't go on probation forever
        }

        return returnBoolean;
    }

    /**
     * Returns all trade IDs based on the userId and the trade status
     * @param userId the user ID
     * @param tradeStatus the trade status in String
     * @return the ArrayList of String of tradeIds
     */
    public ArrayList<String> getAllStatusTradeByUserId(String userId, String tradeStatus) {
        ArrayList<String> al = new ArrayList<>();
        TradeStatus status = statusConverter(tradeStatus);

        for (String tradeId: getAllTradeIdByUserId(userId)) {
            if (findTrade(tradeId).getStatus().equals(status)) {
                al.add(tradeId);
            }
        }
        return al;
    }

    // Helper for getAllStatusTradeByUserId
    private TradeStatus statusConverter(String tradeStatus) {
        HashMap<String, TradeStatus> hm = new HashMap<>();
        for (TradeStatus status: TradeStatus.values()) {
            hm.put(String.valueOf(status), status);
        }
        return hm.get(tradeStatus);
    }



    /**
     * Returns all borrow tradeIds with "COMPLETED" status based on the userId, excludes returning trade
     * @param userId the user ID
     * @return the ArrayList of String of tradeIds
     */
    public ArrayList<String> getAllCompletedBorrowTradeByUserId(String userId) {
        HashMap<String, Trade> hm = getAllOneWayTradeId();
        ArrayList<String> al = new ArrayList<>();

        for (Map.Entry<String, Trade> trade : hm.entrySet()) {
            if (trade.getValue().getStatus().equals(TradeStatus.COMPLETED)
                    && trade.getValue().getRequester().equals(userId) && !trade.getKey().startsWith("@return")) {
                al.add(trade.getKey());
            }
        }
        return al;
    }

    /**
     * Returns all lend tradeIds with "COMPLETED" status based on the userId, excludes returning trade
     * @param userId the user ID
     * @return the ArrayList of String of tradeIds
     */
    public ArrayList<String> getAllCompletedLendTradeByUserId(String userId) {
        HashMap<String, Trade> hm = getAllOneWayTradeId();
        ArrayList<String> al = new ArrayList<>();

        for (Map.Entry<String, Trade> trade : hm.entrySet()) {
            if (trade.getValue().getStatus().equals(TradeStatus.COMPLETED)
                    && trade.getValue().getReceiver().equals(userId) && !trade.getKey().startsWith("@return")) {
                al.add(trade.getKey());
            }
        }
        return al;
    }

    /**
     * Return all lend tradeIds based on the userId, excludes returning trade
     * @param userId the user ID
     * @return the ArrayList of String of tradeIds
     */
    public ArrayList<String> getAllLendTradeIdByUserId(String userId) {
        ArrayList<String> al = new ArrayList<>();

        for (String tradeId: getAllTradeIdByUserId(userId)) {
            if (findTrade(tradeId).getReceiver().equals(userId) && findTrade(tradeId).isOneWay()
                    && !tradeId.startsWith("@return")) {
                al.add(tradeId);
            }
        }
        return al;
    }

    /**
     * Return all borrow tradeIds based on the userId, excludes returning trade
     * @param userId the user ID
     * @return the ArrayList of String of tradeIds
     */
    public ArrayList<String> getAllBorrowTradeIdByUserId(String userId) {
        ArrayList<String> al = new ArrayList<>();

        for (String tradeId: getAllTradeIdByUserId(userId)) {
            if (findTrade(tradeId).getRequester().equals(userId) && findTrade(tradeId).isOneWay()
                    && !tradeId.startsWith("@return")) {
                al.add(tradeId);
            }
        }
        return al;
    }

    /**
     * Return all tradeIds based on the userId
     * @param userId the user ID
     * @return the ArrayList of String of tradeIds
     */
    public ArrayList<String> getAllTradeIdByUserId(String userId) {
        ArrayList<String> al = new ArrayList<>();

        for (Map.Entry<String, Trade> trade : getTradeIdToTrade().entrySet()) {
            if ((trade.getValue().getReceiver().equals(userId) ||
                            trade.getValue().getRequester().equals(userId))) {
                al.add(trade.getKey());
            }
        }
        return al;
    }

    /**
     * Returns all two way tradeIds made by userId
     * @param userId the user ID
     * @return an ArrayList of String of tradeIds
     */
    public ArrayList<String> getAllTwoWayTradeIdByUserId(String userId) {
        ArrayList<String> al = new ArrayList<>();

        for (String tradeId: getAllTradeIdByUserId(userId)) {
            if (!findTrade(tradeId).isOneWay() && !tradeId.startsWith("@return")) {
                al.add(tradeId);
            }
        }
        return al;
    }

    /**
     * Returns all one way trade in TradeManager
     * @return a HAshMap of tradeIds and Trade
     */
    public HashMap<String, Trade> getAllOneWayTradeId(){
        HashMap<String, Trade> hashmap = new HashMap<>();
        for (Map.Entry<String, Trade> trade : getTradeIdToTrade().entrySet()){
            if (trade.getValue().isOneWay()){
                hashmap.put(trade.getKey(),trade.getValue());
            }
        }
        return hashmap;
    }

    /**
     * Returns num, recent items, that are borrowed by the userId
     * @param userId the user ID
     * @param num the number of itemIds to be viewed
     * @return an ArrayList of itemIds
     */
    public ArrayList<String> getRecentItemsBorrowedByUserId(String userId, int num) {
        ArrayList<String> al1 = getAllCompletedBorrowTradeByUserId(userId);
        ArrayList<Trade> al = new ArrayList<>();
        for (String tradeId: al1) {
            if (!tradeId.startsWith("@return"))
                al.add(findTrade(tradeId));
        }
        al.sort(new TradeComparator());
        ArrayList<String> returnAl = new ArrayList<>();

        for (int i = al.size() - 1; i >= 0 && num > 0; i--, num--) {
            returnAl.add(al.get(i).getItemIds().get(0));
        }

        return returnAl;
    }

    /**
     * Returns num, recent items, that this user traded
     * @param userId the user ID
     * @param num the number of itemIds to be viewed
     * @return an ArrayList of itemIds
     */
    public ArrayList<String> getRecentItemsByUserId(String userId, int num) {
        ArrayList<String> al1 = getAllTradeIdByUserId(userId);
        ArrayList<Trade> al = new ArrayList<>();
        for (String tradeId: al1) {
            if (!tradeId.startsWith("@return"))
                al.add(findTrade(tradeId));
        }
        al.sort(new TradeComparator());
        ArrayList<String> returnAl = new ArrayList<>();

        for (int i = al.size() - 1; i >= 0 && num > 0; i--, num--) {
            returnAl.add(al.get(i).getItemIds().get(0));
        }

        return returnAl;
    }

    /**
     * Returns num, recent items, that are lent by the userId
     * @param userId the user ID
     * @param num the number of itemIds to be viewed
     * @return an ArrayList of itemIds
     */
    public ArrayList<String> getRecentItemsLendByUserId(String userId, int num) {
        ArrayList<String> al1 = getAllCompletedLendTradeByUserId(userId);
        ArrayList<Trade> al = new ArrayList<>();
        for (String tradeId: al1) {
            if (!tradeId.startsWith("@return"))
                al.add(findTrade(tradeId));
        }
        al.sort(new TradeComparator());
        ArrayList<String> returnAl = new ArrayList<>();

        for (int i = al.size() - 1; i >= 0 && num > 0; i--, num--) {
            returnAl.add(al.get(i).getItemIds().get(0));
        }

        return returnAl;
    }

    /**
     * Returns num, recent items, in a two way trade by userId
     * @param userId the user ID
     * @param num the number of itemIds to be viewed
     * @return an ArrayList of itemIds
     */
    public ArrayList<String> getRecentItemsInTwoWayByUserId(String userId, int num) {
        ArrayList<String> al1 = getAllTwoWayTradeIdByUserId(userId);
        ArrayList<Trade> al = new ArrayList<>();
        for (String tradeId: al1) {
            if (String.valueOf(findTrade(tradeId).getStatus()).equals("COMPLETED") && !tradeId.startsWith("@return")) {
                al.add(findTrade(tradeId));
            }
        }
        ArrayList<String> returnAl = new ArrayList<>();
        al.sort(new TradeComparator());

        for (int i = al.size() - 1; i >= 0 && num > 0; i--, num--) {
            returnAl.add(al.get(i).getItemIds().get(0));
            returnAl.add(al.get(i).getItemIds().get(1));
        }
        return returnAl;
    }

    /**
     * Returns num, top traders, that have traded with userId
     * @param userId the user ID
     * @param num the number of userIds to be viewed
     * @return an ArrayList of userIds
     */
    public ArrayList<String> getTopTradersByUserId(String userId, int num) {
        ArrayList<String> as = getAllTradeIdByUserId(userId);
        ArrayList<String> al = new ArrayList<>();
        for (String tradeId: as) {
            if (String.valueOf(findTrade(tradeId).getStatus()).equals("COMPLETED") && !tradeId.startsWith("@return")) {
                al.add(tradeId);
            }
        }
        ArrayList<String> returnAl = new ArrayList<>();

        HashMap<String, Integer> userToNumTrades = helperGetTopTraders(userId, al);
        helperChoosingTopTraders(num, returnAl, userToNumTrades);
        return returnAl;
    }

    private void helperChoosingTopTraders(int num, ArrayList<String> returnAl, HashMap<String, Integer> userToNumTrades) {
        while (returnAl.size() != num && !userToNumTrades.isEmpty()) {
            String rememberPerson = null;
            int rememberMaxTimes = 0;
            for (Map.Entry<String, Integer> trader : userToNumTrades.entrySet()) {
                if (rememberMaxTimes < trader.getValue()) {
                    rememberMaxTimes = trader.getValue();
                    rememberPerson = trader.getKey();
                }
            }
            returnAl.add(rememberPerson);
            userToNumTrades.remove(rememberPerson);
        }
    }

    private HashMap<String, Integer> helperGetTopTraders(String userId, List<String> al) {
        HashMap<String, Integer> userToNumTrades = new HashMap<>();
        for (String tradeId: al) {
            Trade trade = findTrade(tradeId);
            String person;
            if (trade.getRequester().equals(userId)) {
                person = trade.getReceiver();
            } else {
                person = trade.getRequester();
            }
            if (userToNumTrades.containsKey(person)) {
                userToNumTrades.put(person, userToNumTrades.get(person) + 1);
            } else {
                userToNumTrades.put(person, 1);
            }
        }
        return userToNumTrades;
    }

    /**
     * Creates a new Trade and add it to the TradeManager
     * @param userId1 The requester's userId
     * @param userId2 The receiver's userId
     * @param itemIds The List of itemIds
     * @return a String of the created tradeId
     */
    public String createNewTrade(String userId1, String userId2, ArrayList<String> itemIds) {
        Trade newTrade;
        newTrade = new Trade(userId1, userId2, itemIds);

        addTrade(newTrade.getTradeId(), newTrade);
        return newTrade.getTradeId();
    }

    /**
     * Returns the trade status based on the tradeId
     * @param tradeId the trade ID
     * @return a TradeStatus type of the status of the Trade
     */
    public TradeStatus getTradeStatusByTradeId(String tradeId) {
        return findTrade(tradeId).getStatus();
    }

    /**
     * Returns a receiver's userId based on the tradeId
     * @param tradeId the trade ID
     * @return a String of receiver's userId
     */
    public String getReceiverByTradeId(String tradeId) {
        return findTrade(tradeId).getReceiver();
    }

    /**
     * Returns a requester's userId based on the tradeId
     * @param tradeId the trade ID
     * @return a String of requester's userId
     */
    public String getRequesterByTradeId(String tradeId) {
        return findTrade(tradeId).getRequester();
    }

    /**
     * Get itemIds based on the tradeId
     * @param tradeId the trade ID
     * @return a List of Strings of itemIds
     */
    public ArrayList<String> getItemIdsByTradeId(String tradeId) {
        return findTrade(tradeId).getItemIds();
    }

    /**
     * Get duration based on tradeId
     * @param tradeId the trade ID
     * @return an int of duration
     */
    public int getDurationByTradeId(String tradeId) {
        return findTrade(tradeId).getDuration();
    }

    /**
     * Set meetingId based on tradeId
     * @param tradeId the trade ID
     * @param meetingId the meeting ID
     */
    public void setMeetingIdByTradeId(String tradeId, String meetingId) {
        findTrade(tradeId).setMeetingId(meetingId);
    }

    /**
     * Get meetingId based on tradeId
     * @param tradeId the trade ID
     * @return the String of meetingId
     */
    public String getMeetingIdByTradeId(String tradeId) {
        return findTrade(tradeId).getMeetingId();
    }

    /**
     * Creates a returning Trade when the Trade is temporary
     * @param tradeId the old trade ID
     * @return the new "returning" trade ID
     */
    public String createReverseTrade(String tradeId) {
        Trade trade = findTrade(tradeId);
        Trade newTrade;
        newTrade = new Trade(trade.getReceiver(), trade.getRequester(), trade.getItemIds());

        newTrade.setStatus(TradeStatus.ACCEPTED);
        newTrade.setTradeId("@return" + trade.getTradeId());
        newTrade.setDuration(-1);
        addTrade(newTrade.getTradeId(), newTrade);

        return newTrade.getTradeId();
    }


}