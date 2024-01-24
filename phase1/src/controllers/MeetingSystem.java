package controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import gateways.ItemGateway;
import gateways.MeetingGateway;
import gateways.TradeGateway;
import gateways.UserGateway;
import javafx.util.Pair;
import presenters.TradePresenter;
import usecases.*;
import entities.*;

public class MeetingSystem {

    /**
     * MeetingSystem is a controller for Meetings.
     *
     * MeetingManager is the UseCase Class MeetingManager.
     * TradeManager is the UseCase Class TradeManager.
     * UserManager is the UseCase Class UserManager.
     * ItemManager is the UseCase Class ItemManager.
     * userId is the id of the User who is currently using the system.
     * TradePresenter is the Presenter for Trade.
     * TradeGateway is the Gateway Class for updating information of Trade.
     * UserGateway is the Gateway Class for updating information of User.
     * MeetingGateway is the Gateway Class for updating information of User.
     * ItemGateway is the Gateway Class for updating information of Item.
     */
    private MeetingManager mm;
    private TradeManager tm;
    private UserManager um;
    private ItemManager im;
    private String userId;
    private TradePresenter tp = new TradePresenter();
    private TradeGateway tg = new TradeGateway();
    private UserGateway ug = new UserGateway();
    private MeetingGateway mg = new MeetingGateway();
    private ItemGateway ig = new ItemGateway();
    private String[] invalidStrings = new String[] {",", "/"};

    /**
     * The constructor for TradeSystem.
     *
     * @param userId is the id of the User who is currently using the system.
     * @param tm is the TradeManager.
     * @param mm is the MeetingManager.
     * @param um is the UserManager.
     * @param im is the ItemManager.
     */
    MeetingSystem(String userId, TradeManager tm, MeetingManager mm, UserManager um, ItemManager im) {
        this.tm = tm;
        this.mm = mm;
        this.um = um;
        this.im = im;
        this.userId = userId;
    }

    void helperMeetingSystem(String tradeIdInput) {
        String meetingId = tm.getMeetingIdByTradeId(tradeIdInput);
        boolean eligible = mm.usersTurn(meetingId, getUserNum(tradeIdInput));
        if (eligible) {
            helperEligibleSwitch(tradeIdInput, meetingId);
        } else {
            helperNotEligibleSwitch(meetingId);
        }
    }

    private void helperEligibleSwitch(String tradeIdInput, String meetingId) {
        tp.getMeetingNotification("Eligible");
        tp.getGoBackMenuNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                updateToCsv();
                break;

            case "1":
                // Get meeting info
                tp.getMeetingInfo(mm.findMeeting(meetingId));
                break;

            case "2":
                // Edit meeting
                helperEditMeeting(tradeIdInput, meetingId);
                updateToCsv();
                break;

            case "3":
                // Confirm meeting
                helperConfirmMeeting(tradeIdInput, meetingId);
                tp.getConfirmMeetingNotification();
                updateToCsv();
                break;

            default:
                tp.invalidSelection();
                helperMeetingSystem(tradeIdInput);
        }
    }

    private void helperNotEligibleSwitch(String meetingId) {
        tp.getMeetingNotification("Not Eligible");
        tp.getGoBackMenuNotification();
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        switch (selection) {
            case "0":
                updateToCsv();
                break;

            case "1":
                tp.getMeetingInfo(mm.findMeeting(meetingId));

            default:
                tp.invalidSelection();
                helperNotEligibleSwitch(meetingId);
        }
    }

    private void helperEditMeeting(String tradeIdInput, String meetingId) {
        Pair<String, LocalDate> placeTime = helperPromptPlaceTime(tradeIdInput);
        String place = placeTime.getKey();
        LocalDate dateTime = placeTime.getValue();
        if (mm.editTimePlace(meetingId, dateTime, place, getUserNum(tradeIdInput))) {
            mm.confirmTimePlace(meetingId, getUserNum(tradeIdInput));
            tp.successfulInitiateMeeting(dateTime, place);
        }
        else {
            tp.meetingCancelledNotification();
            tm.cancelTrade(tradeIdInput);

            for (String itemId: tm.getItemIdsByTradeId(tradeIdInput)) {
                im.makeItemAvailable(itemId);
            }

            if (tradeIdInput.startsWith("@return")) {
                um.changeStatus(tm.getReceiverByTradeId(tradeIdInput), UserStatus.FLAGGED);
                um.changeStatus(tm.getRequesterByTradeId(tradeIdInput), UserStatus.FLAGGED);
            }
        }
    }

    private Pair<String, LocalDate> helperPromptPlaceTime(String tradeIdInput) {
        Scanner input = new Scanner(System.in);
        tp.promptMeetingPlace();
        String place = input.nextLine();

        while (containsInvalidString(place)) {
            tp.invalidItemName();
            place = input.nextLine();
        }

        LocalDate dateTime = helperPromptTime(tradeIdInput);
        return new Pair<>(place, dateTime);
    }

    private LocalDate helperPromptTime(String tradeIdInput) {
        if (tradeIdInput.startsWith("@return")) {
            return mm.getTimeByMeetingId((tm.getMeetingIdByTradeId(tradeIdInput)));
        }
        else {
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
                    return helperPromptTime(tradeIdInput);
                }
            } catch (DateTimeParseException e) {
                tp.invalidInput();
                return helperPromptTime(tradeIdInput);
            }
        }
    }

    private void helperConfirmMeeting(String tradeIdInput, String meetingId) {
        mm.confirmTimePlace(meetingId, getUserNum(tradeIdInput));
        if (mm.isMeetingIdConfirmed(meetingId)) {
            tm.meetingConfirmed(tradeIdInput);
        }
    }

    private String getUserId() {
        return this.userId;
    }

    private int getUserNum(String tradeIdInput) {
        if ((tm.findTrade(tradeIdInput)).getRequester().equals(getUserId())) {
            return 0;
        } else {
            return 1;
        }
    }

    private boolean containsInvalidString(String anyString) {
        for (String s: invalidStrings) {
            if (anyString.contains(s)) return true;
        }
        return false;
    }

    private void updateToCsv() {
        tg.updateTradeInfoCsv(tm);
        mg.updateMeetInfoCsv(mm);
        ug.updateUserInfoCsv(um);
        ig.updateItemInfoCsv(im);
    }
}
