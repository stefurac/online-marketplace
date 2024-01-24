package controllers;

import java.util.*;
import java.lang.*;
import entities.UserStatus;
import gateways.*;
import presenters.TradePresenter;
import usecases.*;
/**
 * TradeSystem is an controller dealing with trades.
 */
public class TradeSystem {

    boolean isDemo;
    private final TradeManager tm;
    private final UserManager um;
    private final ItemManager im;
    private final MeetingManager mm;
    private final TradePresenter tp;
    private final DisplayTradeSystem dts;
    private final RequestTradeSystem rts;
    private final DoActionTradeSystem dats;
    private final String userId;
    private final ItemGateway ig = new ItemGateway();
    private final TradeGateway tg = new TradeGateway();
    private final UserGateway ug = new UserGateway();
    private final MeetingGateway mg = new MeetingGateway();
    /**
     * The constructor for TradeSystem.
     * @param userId is the id of the User who is currently using the system.
     * @param um is the UserManager.
     */
    public TradeSystem(String userId, UserManager um) {
        List<Integer> al = new ArrayList<>();
        List<Integer> db = tg.getThresholdInfoCSV();
        al.add(db.get(0));  // CancelThreshold
        al.add(db.get(1));  // WeeklyTradeLimit
        al.add(db.get(2));  // BorrowDifference
        this.tm = new TradeManager(al, tg.getTradeInfoCsv());
        this.mm = new MeetingManager(db.get(3), mg.getMeetInfoCsv());
        this.um = um;
        this.im = new ItemManager(ig.getItemInfoCSV());
        this.userId = userId;
        this.rts = new RequestTradeSystem(userId, tm, im, um);
        this.dats = new DoActionTradeSystem(userId, tm, im, um, mm);
        this.dts = new DisplayTradeSystem(userId, tm, im, um, dats);
        this.tp = new TradePresenter(tm, um, im);
    }

    /**
     * The method that runs the TradeSystem
     */
    public void run(boolean isDemo) {
        this.isDemo = isDemo;
        if (!(String.valueOf(um.statusByUserId(userId)).equals("GOOD") || String.valueOf(um.statusByUserId(userId)).equals("PREMIUM"))) {
            helperBadSwitch();
        }
        else {
            helperGoodSwitch();
        }
    }

    private void helperBadSwitch() {
        boolean quitMenu = false;

        do {
            tp.getSelectionMenu("Bad");
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    quitMenu = true;
                    break;
                case "1":
                    dts.run(isDemo);
                    break;
                case "2":
                    dts.helperDisplayRecent();
                    break;
                case "3":
                    dts.displayTopTraders();
                    break;
                default:
                    tp.invalidSelection();
                    helperBadSwitch();
                    break;
            } } while (!quitMenu);
    }

    private void helperGoodSwitch() {
        boolean quitMenu = false;

        do {
            // switching to bad menu should User becomes flagged during this menu
            if (!(String.valueOf(um.statusByUserId(userId)).equals("GOOD") || String.valueOf(um.statusByUserId(userId)).equals("PREMIUM"))) {
                helperBadSwitch();
            }

            tp.getSelectionMenu("Good");
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    quitMenu = true;
                    break;
                case "1":  // Browse and Manage Existing Trades
                    dts.run(isDemo);
                    break;
                case "2":  // Browse and Manage Suggested Trades
                    suggestedTradesMenu();
                    break;
                case "3":  // Request a Trade via Username
                    if (checkPreRequesting()) {
                        rts.run(isDemo);
                    }
                    break;
                case "4":  // Browse Recently Traded Items
                    dts.helperDisplayRecent();
                    break;
                case "5":  // Browse Top Traders
                    dts.displayTopTraders();
                    break;

                default:
                    tp.invalidSelection();
                    helperGoodSwitch();
                    break;
            }
        } while (!quitMenu);
    }

    private String getUserId() {
        return this.userId;
    }

    private boolean validIncompleteTrade(String userId) {
        if (tm.checkIncompleteTrade(userId, um)) {
            um.changeStatus(getUserId(), UserStatus.PROBATION);
            ug.updateUserInfoCsv(um, isDemo);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkPreRequesting() {  // checks for flagging criteria before helperRequestTrade() is called
        if (!validIncompleteTrade(getUserId())) {
            tp.violateIncompleteTrade();  // user will be flagged for having too many INCOMPLETE trades
            return false;
        } else if (tm.checkTradeLimit(getUserId())) {
            tp.violateTradeLimit();  // user will NOT be flagged for having too many trades in a week
            return false;
        }
        return true;
    }

    private void suggestedTradesMenu() {
        boolean quitMenu = false;

        do {
            tp.suggestPotentialTradeMenu();

            Scanner scanner = new Scanner(System.in);
            String selection = scanner.nextLine();

            switch (selection) {
                case "0":
                    quitMenu = true;
                    break;

                case "1": // Suggest Potential One-Way Trades
                    if (rts.validBorrowDiff(getUserId())) {
                        rts.suggestOneWay(userId);
                    }
                    break;

                case "2": // Suggest Potential Two-Way Trades
                    rts.suggestTwoWay(userId);
                    break;

                default:
                    tp.invalidSelection();
                    break;
            }
        } while (!quitMenu);
    }

}


