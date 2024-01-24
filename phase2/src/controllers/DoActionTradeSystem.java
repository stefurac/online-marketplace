package controllers;

import entities.UserStatus;
import gateways.*;
import presenters.TradePresenter;
import usecases.*;
import entities.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import javafx.util.*;

/**
 * A controller class that effects all actions that can be done to a trade
 */
public class DoActionTradeSystem {
    boolean isDemo;

    private final TradeManager tm;
    private final ItemManager im;
    private final UserManager um;
    private final MeetingManager mm;
    private final MeetingSystem ms;
    private final TradePresenter tp;
    private final String userId;
    private final TradeGateway tg = new TradeGateway();
    private final ItemGateway ig = new ItemGateway();
    private final UserGateway ug = new UserGateway();
    private final MeetingGateway mg = new MeetingGateway();
    private final String[] invalidStrings = new String[] {",", "/"};

    /**
     * Instantiates a DoActionTradeSystem instance
     * @param userId the ID of the user currently in session
     * @param tm an instance of TradeManager
     * @param im an instance of ItemManager
     * @param um an instance of UserManager
     * @param mm an instance of MeetingManager
     */
    DoActionTradeSystem(String userId, TradeManager tm, ItemManager im, UserManager um, MeetingManager mm) {
        this.im = im;
        this.um = um;
        this.tm = tm;
        this.mm = mm;
        this.tp = new TradePresenter(tm, um, im);
        this.userId = userId;
        this.ms = new MeetingSystem(getUserId(), tm, mm, um, im);
    }

    /**
     * the methods that runs the do-action menu and all subsequent actions
     */
    void run(String tradeIdInput, boolean isDemo) { //helperActionToTrade(String tradeIdInput)
        this.isDemo = isDemo;
//        // Do action to existing Trade
//        tp.getTradeListInfo(listTrade);
//        tp.getManagementNotification();
//        Scanner input = new Scanner(System.in);
//        String tradeIdInput = promptTradeId(input);
//        // Options of accept, reject, confirm a Trade
        if (eligibleToAcceptOrReject(tradeIdInput)) {
            helperSwitchAcceptRejectTrade(tradeIdInput);
        } else if (eligibleToConfirmTrade(tradeIdInput) && !eligibleToViewMeeting(tradeIdInput)) {
            helperConfirmTrade(tradeIdInput);
        } else if (eligibleToViewMeeting(tradeIdInput) && !eligibleToConfirmTrade(tradeIdInput)) {
            ms.run(tradeIdInput, isDemo);
        } else if (eligibleToConfirmTrade(tradeIdInput) && eligibleToViewMeeting(tradeIdInput)) {
            helperSwitchConfirmOrMeeting(tradeIdInput);
        } else {
            tp.noTradeActionAvailable();
        } updateToCsv();
    }

    private void helperSwitchConfirmOrMeeting(String tradeIdInput) {
        boolean quitMenu = false;

        do {
            tp.getSwitchConfirmOrMeetingNotification();

            tp.getGoBackMenuNotification();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    updateToCsv();
                    quitMenu = true;
                    break;
                case "1":
                    // This confirms Trade
                    helperConfirmTrade(tradeIdInput);
                    updateToCsv();
                    break;

                case "2":
                    ms.run(tradeIdInput, isDemo);
                    updateToCsv();
                    break;

                default:
                    tp.invalidSelection();
                    helperSwitchConfirmOrMeeting(tradeIdInput);
            }
        } while (!quitMenu); }

    private void helperSwitchAcceptRejectTrade(String tradeIdInput) {
        boolean quitMenu = false;

        do {
            // Checks whether it has status request and the user is the receiver

            tp.getAcceptRejectNotification();
            tp.getGoBackMenuNotification();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    updateToCsv();
                    quitMenu = true;
                    break;

                case "1":
                    // This accepts Trade, but can't make accept if too many incomplete trade and trade limit in one week
                    if (!validIncompleteTrade(getUserId())) {
                        tp.violateIncompleteTrade();
                        ug.updateUserInfoCsv(um, isDemo);
                        quitMenu = true;
                        break; }
                    else if (!validIncompleteTrade(getUserId())) {
                            tp.violateIncompleteTrade();
                            ug.updateUserInfoCsv(um, isDemo);
                        quitMenu = true;
                            break;
                    } else if (tm.checkTradeLimit(getUserId()) && !um.premiumUserIds().contains(getUserId())) {
                        tp.violateTradeLimit();
                        quitMenu = true;
                        break;
                    } else {
                        tm.acceptTrade(tradeIdInput);
                        for (String itemId : tm.getItemIdsByTradeId(tradeIdInput)) {
                            im.changeStatus(itemId, ItemStatus.UNAVAILABLE);
                            ig.updateItemInfoCsv(im, isDemo);
                        }

                        tp.successfulActionTrade("accepted");
                        helperInitiateMeeting(tradeIdInput);
                        updateToCsv();
                        quitMenu = true;
                        break;
                    }

                case "2":
                    tm.rejectTrade(tradeIdInput);
                    if (!(tm.getItemIdsByTradeId(tradeIdInput).size() == 1)) {
                        im.changeStatus(tm.getItemIdsByTradeId(tradeIdInput).get(1), ItemStatus.UNAVAILABLE);
                        ig.updateItemInfoCsv(im, isDemo);
                    }
                    tp.successfulActionTrade("rejected");
                    updateToCsv();
                    quitMenu = true;
                    break;

                default:
                    tp.invalidSelection();
                    helperSwitchAcceptRejectTrade(tradeIdInput);
            }
        } while (!quitMenu); }

    private boolean eligibleToAcceptOrReject(String tradeIdInput) {
        return String.valueOf(tm.getTradeStatusByTradeId(tradeIdInput)).equals("REQUESTED") &&
                tm.getReceiverByTradeId(tradeIdInput).equals(getUserId());
    }

    private void helperInitiateMeeting(String tradeIdInput) {
        Pair<String, LocalDate> placeTime = helperPromptPlaceTime();
        String place = placeTime.getKey();
        LocalDate dateTime = placeTime.getValue();
        tm.setMeetingIdByTradeId(tradeIdInput, mm.createNewMeeting(dateTime, place, getUserId()));
        tp.successfulInitiateMeeting(dateTime, place);
    }

    private Pair<String, LocalDate> helperPromptPlaceTime() {
        Scanner input = new Scanner(System.in);

        tp.promptMeetingPlace();
        String place = input.nextLine();
        while (containsInvalidString(place)) {
            tp.invalidItemName();
            place = input.nextLine();
        }
        LocalDate dateTime = helperPromptTime();
        return new Pair<>(place, dateTime);
    }

    private LocalDate helperPromptTime() {
        Scanner input = new Scanner(System.in);
        tp.promptMeetingTime();
        String dateStr = input.nextLine();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTime = LocalDate.parse(dateStr, formatter);
            if (dateTime.isAfter(LocalDate.now())) {
                return dateTime;
            } else {
                tp.invalidDate();
                return helperPromptTime();
            }
        } catch (DateTimeParseException e) {
            tp.invalidInput();
            return helperPromptTime();
        }
    }

    private void helperConfirmTrade(String tradeIdInput) {
        tp.getConfirmNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();

        switch (selection) {
            case "Y":
                if (String.valueOf(tm.getTradeStatusByTradeId(tradeIdInput)).equals("CONFIRMED")) {
                    tm.confirmTrade(tradeIdInput, getUserId());
                } else {
                    helperCompleteTrade(tradeIdInput);
                }
                tp.successfulActionTrade("confirmed");
                updateToCsv();
                break;
            case "N":
                break;
            default:
                tp.invalidSelection();
                helperConfirmTrade(tradeIdInput);
        }
    }

    private boolean eligibleToConfirmTrade(String tradeIdInput) {
        // if CONFIRMED or REQUESTER_CONFIRMED when the current user is the receiver ot vice versa
        if (String.valueOf(tm.getTradeStatusByTradeId(tradeIdInput)).equals("CONFIRMED")) {
            return true;
        } else if (String.valueOf(tm.getTradeStatusByTradeId(tradeIdInput)).equals("REQUESTER_CONFIRMED") &&
                tm.getReceiverByTradeId(tradeIdInput).equals(getUserId())) {
            return true;
        } else return String.valueOf(tm.getTradeStatusByTradeId(tradeIdInput)).equals("RECEIVER_CONFIRMED") &&
                tm.getRequesterByTradeId(tradeIdInput).equals(getUserId());
    }

    private boolean eligibleToViewMeeting(String tradeIdInput) {
        switch (String.valueOf(tm.getTradeStatusByTradeId(tradeIdInput))) {
            case "ACCEPTED":
            case "CONFIRMED":
            case "REQUESTER_CONFIRMED":
            case "RECEIVER_CONFIRMED":
            case "COMPLETED":
                return true;
            default:
                return false;
        }
    }

    private void helperCompleteTrade(String tradeIdInput) {
        helperRemoveFromWishlist(tradeIdInput);
        tm.completeTrade(tradeIdInput);
        if (tm.getDurationByTradeId(tradeIdInput) > 0) {
            String newTradeId = tm.createReverseTrade(tradeIdInput);

            int dur = tm.getDurationByTradeId(tradeIdInput);
            String oldMeetingId = tm.getMeetingIdByTradeId(tradeIdInput);
            LocalDate newTime = mm.getTimeByMeetingId(oldMeetingId).plusDays(dur);
            String place = mm.getPlaceByMeetingId(oldMeetingId);
            tm.setMeetingIdByTradeId(newTradeId, mm.createNewMeeting(newTime, place, getUserId()));


        } else {
            helperSwapInventories(tradeIdInput);  // items swap inventories only when a permanent trade is completed
            for (String itemIds: tm.getItemIdsByTradeId(tradeIdInput)) {
                im.changeStatus(itemIds, ItemStatus.AVAILABLE);
                ig.updateItemInfoCsv(im, isDemo);
            }
            helperUpdatePoints(tradeIdInput);
        }
    }


    private void helperUpdatePoints(String tradeId) {
        String receiverId = tm.getReceiverByTradeId(tradeId);
        String requesterId = tm.getRequesterByTradeId(tradeId);
        List<String> itemIds = tm.getItemIdsByTradeId(tradeId);
        if (itemIds.size() == 1) {
            um.addPoints(receiverId, 20);
            if (um.premiumUserIds().contains(requesterId)) {um.removePoints(requesterId, 10);}
            else {um.addPoints(requesterId, 10); }
        } else {
            um.addPoints(requesterId, 30);
            um.addPoints(receiverId, 30);
        }
        ug.updateUserInfoCsv(um, isDemo);
    }

    private void helperSwapInventories(String tradeId) {
        String receiverId = tm.getReceiverByTradeId(tradeId);
        String requesterId = tm.getRequesterByTradeId(tradeId);
        List<String> itemIds = tm.getItemIdsByTradeId(tradeId);
        if (itemIds.size() == 1) {
            um.addToInventory(requesterId, itemIds.get(0));
            um.systemRemoveFromInventory(receiverId, itemIds.get(0));
        } else {
            um.addToInventory(requesterId, itemIds.get(0));
            um.systemRemoveFromInventory(receiverId, itemIds.get(0));

            um.addToInventory(receiverId, itemIds.get(1));
            um.systemRemoveFromInventory(requesterId, itemIds.get(1));
        }
        ug.updateUserInfoCsv(um, isDemo);
    }



    private void helperRemoveFromWishlist(String tradeId) {
        String itemId = tm.getItemIdsByTradeId(tradeId).get(0);
        String requester = tm.getRequesterByTradeId(tradeId);
        if (um.itemIdInWishlist(requester, itemId)) {
            um.systemRemoveFromWishlist(requester, itemId);
        }
        if (tm.getItemIdsByTradeId(tradeId).size() == 2) {
            String itemId2 = tm.getItemIdsByTradeId(tradeId).get(1);
            String receiver = tm.getReceiverByTradeId(tradeId);
            if (um.itemIdInWishlist(receiver, itemId2)) {
                um.systemRemoveFromWishlist(receiver, itemId2);
            }
        }
        ug.updateUserInfoCsv(um, isDemo);
    }

    private String getUserId() {
        return this.userId;
    }

    private boolean validIncompleteTrade(String userId) {
        if (tm.checkIncompleteTrade(userId, um) && !um.premiumUserIds().contains(userId)) {
            um.changeStatus(getUserId(), UserStatus.PROBATION);
            ug.updateUserInfoCsv(um, isDemo);
            return false;
        } else {
            return true;
        }
    }

    private boolean containsInvalidString(String anyString) {
        for (String s: invalidStrings) {
            if (anyString.contains(s)) return true;
        }
        return false;
    }

    private void updateToCsv() {
        ig.updateItemInfoCsv(im, isDemo);
        tg.updateTradeInfoCsv(tm, isDemo);
        mg.updateMeetInfoCsv(mm, isDemo);
        ug.updateUserInfoCsv(um, isDemo);
    }
}
