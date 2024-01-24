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

public class DoActionTradeSystem {

    /**
     * A controller class which branches from TradeSystem
     *
     * tm is the usecase for Trade
     * im is the usecase for Item
     * um is the usecase for User
     * mm is the usecase for Meeting
     * tp is the presenter for Trade
     * userId is the current user's userId
     * tg is the gateway for Trade
     * ig is the gateway for Item
     * ug is the gateway for User
     * mg is the gateway for Meeting
     */

    private TradeManager tm;
    private ItemManager im;
    private UserManager um;
    private MeetingManager mm;
    private MeetingSystem ms;
    private TradePresenter tp = new TradePresenter();
    private String userId;
    private TradeGateway tg = new TradeGateway();
    private ItemGateway ig = new ItemGateway();
    private UserGateway ug = new UserGateway();
    private MeetingGateway mg = new MeetingGateway();
    private String[] invalidStrings = new String[] {",", "/"};

    DoActionTradeSystem(String userId, TradeManager tm, ItemManager im, UserManager um, MeetingManager mm) {
        this.im = im;
        this.um = um;
        this.tm = tm;
        this.mm = mm;
        this.userId = userId;
        this.ms = new MeetingSystem(getUserId(), tm, mm, um, im);
    }

    void helperActionToTrade(ArrayList<Trade> listTrade) {
        // Do action to existing Trade
        tp.getTradeListInfo(listTrade);
        tp.getManagementNotification();
        Scanner input = new Scanner(System.in);
        String tradeIdInput = promptTradeId(input);
        // Options of accept, reject, confirm a Trade
        if (eligibleToAcceptOrReject(tradeIdInput)) {
            helperSwitchAcceptRejectTrade(tradeIdInput);
        } else if (eligibleToConfirmTrade(tradeIdInput) && !eligibleToViewMeeting(tradeIdInput)) {
            helperConfirmTrade(tradeIdInput);
        } else if (eligibleToViewMeeting(tradeIdInput) && !eligibleToConfirmTrade(tradeIdInput)) {
            ms.helperMeetingSystem(tradeIdInput);
        } else if (eligibleToConfirmTrade(tradeIdInput) && eligibleToViewMeeting(tradeIdInput)) {
            helperSwitchConfirmOrMeeting(tradeIdInput);
        } else {
            tp.noTradeActionAvailable();
            helperActionToTrade(listTrade);
        }
    }

    private void helperSwitchConfirmOrMeeting(String tradeIdInput) {
        tp.getSwitchConfirmOrMeetingNotification();
        tp.getGoBackMenuNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                updateToCsv();
                break;
            case "1":
                // This confirms Trade
                helperConfirmTrade(tradeIdInput);
                updateToCsv();
                break;

            case "2":
                ms.helperMeetingSystem(tradeIdInput);
                updateToCsv();
                break;

            default:
                tp.invalidSelection();
                helperSwitchConfirmOrMeeting(tradeIdInput);
        }
    }

    private void helperSwitchAcceptRejectTrade(String tradeIdInput) {
        // Checks whether it has status request and the user is the receiver
        tp.getAcceptRejectNotification();
        tp.getGoBackMenuNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                updateToCsv();
                break;

            case "1":
                // This accepts Trade, but can't make accept if too many incomplete trade and trade limit in one week
                if (!validIncompleteTrade(getUserId())) {
                    tp.violateIncompleteTrade();
                    ug.updateUserInfoCsv(um);
                    break;
                } else if (!tm.checkTradeLimit(getUserId())) {
                    tp.violateTradeLimit();
                    break;
                } else {
                    tm.acceptTrade(tradeIdInput);
                    for (String itemId : tm.getItemIdsByTradeId(tradeIdInput)) {
                        im.makeItemUnavailable(itemId);
                    }

                    tp.successfulActionTrade("accepted");
                    helperInitiateMeeting(tradeIdInput);
                    updateToCsv();
                    break;
                }

            case "2":
                tm.rejectTrade(tradeIdInput);
                if (!(tm.getItemIdsByTradeId(tradeIdInput).size() == 1)) {
                    im.makeItemAvailable(tm.getItemIdsByTradeId(tradeIdInput).get(1));
                }
                tp.successfulActionTrade("rejected");
                updateToCsv();
                break;

            default:
                tp.invalidSelection();
                helperSwitchAcceptRejectTrade(tradeIdInput);
        }
    }

    private boolean eligibleToAcceptOrReject(String tradeIdInput) {
        return String.valueOf(tm.getTradeStatusByTradeId(tradeIdInput)).equals("REQUESTED") &&
                tm.getReceiverByTradeId(tradeIdInput).equals(getUserId());
    }

    private void helperInitiateMeeting(String tradeIdInput) {
        Pair<String, LocalDate> placeTime = helperPromptPlaceTime();
        String place = placeTime.getKey();
        LocalDate dateTime = placeTime.getValue();
        tm.setMeetingIdByTradeId(tradeIdInput, mm.createNewMeeting(dateTime, place));
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
        // if CONFIRMED or REQUESTERCONFIRMED when the current user is the receiver ot vice versa
        if (String.valueOf(tm.getTradeStatusByTradeId(tradeIdInput)).equals("CONFIRMED")) {
            return true;
        } else if (String.valueOf(tm.getTradeStatusByTradeId(tradeIdInput)).equals("REQUESTERCONFIRMED") &&
                tm.getReceiverByTradeId(tradeIdInput).equals(getUserId())) {
            return true;
        } else if (String.valueOf(tm.getTradeStatusByTradeId(tradeIdInput)).equals("RECEIVERCONFIRMED") &&
                tm.getRequesterByTradeId(tradeIdInput).equals(getUserId())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean eligibleToViewMeeting(String tradeIdInput) {
        switch (String.valueOf(tm.getTradeStatusByTradeId(tradeIdInput))) {
            case "ACCEPTED":
            case "CONFIRMED":
            case "REQUESTERCONFIRMED":
            case "RECEIVERCONFIRMED":
            case "COMPLETED":
                return true;
            default:
                return false;
        }
    }

    private void helperCompleteTrade(String tradeIdInput) {
        helperRemoveFromWishlist(tradeIdInput);
        helperSwapInventories(tradeIdInput);
        tm.completeTrade(tradeIdInput);
        if (tm.getDurationByTradeId(tradeIdInput) > 0) {
            String newTradeId = tm.createReverseTrade(tradeIdInput);

            int dur = tm.getDurationByTradeId(tradeIdInput);
            String oldMeetingId = tm.getMeetingIdByTradeId(tradeIdInput);
            LocalDate newTime = mm.getTimeByMeetingId(oldMeetingId).plusDays(dur);
            String place = mm.getPlaceByMeetingId(oldMeetingId);
            tm.setMeetingIdByTradeId(newTradeId, mm.createNewMeeting(newTime, place));

        } else {
            for (String itemIds: tm.getItemIdsByTradeId(tradeIdInput)) {
                im.makeItemAvailable(itemIds);
            }
        }
    }

    private void helperSwapInventories(String tradeId) {
        String receiverId = tm.getReceiverByTradeId(tradeId);
        String requesterId = tm.getRequesterByTradeId(tradeId);
        List<String> itemIds = tm.getItemIdsByTradeId(tradeId);
        if (itemIds.size() == 1) {
            um.addToInventory(requesterId, itemIds.get(0));
            um.removeFromInventory(receiverId, itemIds.get(0));
        } else {
            um.addToInventory(requesterId, itemIds.get(0));
            um.removeFromInventory(receiverId, itemIds.get(0));

            um.addToInventory(receiverId, itemIds.get(1));
            um.removeFromInventory(requesterId, itemIds.get(1));
        }
    }

    private void helperRemoveFromWishlist(String tradeId) {
        String itemId = tm.getItemIdsByTradeId(tradeId).get(0);
        String requester = tm.getRequesterByTradeId(tradeId);
        if (um.itemIdInWishlist(requester, itemId)) {
            um.removeFromWishlist(requester, itemId);
        }
        if (tm.getItemIdsByTradeId(tradeId).size() == 2) {
            String itemId2 = tm.getItemIdsByTradeId(tradeId).get(1);
            String receiver = tm.getReceiverByTradeId(tradeId);
            if (um.itemIdInWishlist(receiver, itemId2)) {
                um.removeFromWishlist(receiver, itemId2);
            }
        }
    }

    private String promptTradeId(Scanner input) {
        String tradeIdInput = input.nextLine();
        // Checks whether the tradeId exists and belong to userId involved
        while (!(validTradeId(tradeIdInput))) {
            tp.getNotFound("Trade ID");
            tp.getTradeNotification();
            tradeIdInput = input.nextLine();
        }
        return tradeIdInput;
    }

    private boolean validTradeId(String tradeId) {
        if (tm.getTradeIdToTrade().containsKey(tradeId)) {
            return tm.findTrade(tradeId).getRequester().equals(getUserId()) ||
                    tm.findTrade(tradeId).getReceiver().equals(getUserId());
        }
        // all conditions are not true
        return false;
    }

    private String getUserId() {
        return this.userId;
    }

    private boolean validIncompleteTrade(String userId) {
        if (!tm.checkIncompleteTrade(userId)) {
            um.changeStatus(getUserId(), UserStatus.FLAGGED);
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
        ig.updateItemInfoCsv(im);
        tg.updateTradeInfoCsv(tm);
        mg.updateMeetInfoCsv(mm);
        ug.updateUserInfoCsv(um);
    }
}
