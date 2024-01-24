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

/**
 * MeetingSystem is a controller for Meetings.
 */
public class MeetingSystem {
    boolean isDemo;

    private final MeetingManager mm;
    private final TradeManager tm;
    private final UserManager um;
    private final ItemManager im;
    private final String userId;
    private final TradePresenter tp;
    private final TradeGateway tg = new TradeGateway();
    private final UserGateway ug = new UserGateway();
    private final MeetingGateway mg = new MeetingGateway();
    private final ItemGateway ig = new ItemGateway();
    private final String[] invalidStrings = new String[] {",", "/"};

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
        this.tp = new TradePresenter(tm, um, im);
        this.userId = userId;
    }

    /**
     * the methods that runs the meeting menu and all subsequent actions
     */
    void run(String tradeIdInput, boolean isDemo) { //helperMeetingSystem(String tradeIdInput)
        this.isDemo = isDemo;
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
//                if (mm.getPlaceByMeetingId(meetingId).equals("")) {
//                }
                helperConfirmMeeting(tradeIdInput, meetingId);
                tp.getConfirmMeetingNotification();
                updateToCsv();

                break;

            default:
                tp.invalidSelection();
                run(tradeIdInput, isDemo);
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
                im.changeStatus(itemId, ItemStatus.AVAILABLE);
                ig.updateItemInfoCsv(im, isDemo);

            }

            if (tradeIdInput.startsWith("@return")) {
                um.changeStatus(tm.getReceiverByTradeId(tradeIdInput), UserStatus.PROBATION);
                um.changeStatus(tm.getRequesterByTradeId(tradeIdInput), UserStatus.PROBATION);
                ug.updateUserInfoCsv(um, isDemo);
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
        tg.updateTradeInfoCsv(tm, isDemo);
        mg.updateMeetInfoCsv(mm, isDemo);
        ug.updateUserInfoCsv(um, isDemo);
        ig.updateItemInfoCsv(im, isDemo);
    }
}
