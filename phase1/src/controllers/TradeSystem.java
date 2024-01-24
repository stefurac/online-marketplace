package controllers;
import java.io.FileNotFoundException;
import java.util.*;
import java.lang.*;
import entities.Trade;
import entities.UserStatus;
import gateways.*;
import presenters.TradePresenter;
import usecases.*;
public class TradeSystem {
    /**
     * TradeSystem is an controller dealing with trades.
     * TradeManager is the UseCase Class TradeManager.
     * UserManager is the UseCase Class UserManager.
     * ItemManager is the UseCase Class ItemManager.
     * MeetingManager is the UseCase Class MeetingManager.
     * TradePresenter is the Presenter for Trade.
     * DisplayTradeSystem is the Controller for displaying trades.
     * RequestTradeSystem is the Controller for requesting trades.
     * DoActionTradeSystem is the controller for accepting, rejecting or completing a trade.
     * userId is the id of the User who is currently using the system.
     * ItemGateway is the Gateway Class for updating information of Item.
     * TradeGateway is the Gateway Class for updating information of Trade.
     * UserGateway is the Gateway Class for updating information of User.
     * MeetingGateway is the Gateway Class for updating information of User.
     */
    private TradeManager tm;
    private UserManager um;
    private ItemManager im;
    private MeetingManager mm;
    private TradePresenter tp = new TradePresenter();
    private DisplayTradeSystem dts;
    private RequestTradeSystem rts;
    private DoActionTradeSystem dats;
    private String userId;
    private ItemGateway ig = new ItemGateway();
    private TradeGateway tg = new TradeGateway();
    private UserGateway ug = new UserGateway();
    private MeetingGateway mg = new MeetingGateway();
    /**
     * The constructor for TradeSystem.
     * @param userId is the id of the User who is currently using the system.
     * @param um is the UserManager.
     */
    public TradeSystem(String userId, UserManager um) throws FileNotFoundException {
        List<Integer> al = new ArrayList<>();
        List<Integer> db = tg.getThresholdInfoCSV();
        al.add(db.get(0));
        al.add(db.get(1));
        al.add(db.get(2));
        this.tm = new TradeManager(al, tg.getTradeInfoCsv());
        this.mm = new MeetingManager(db.get(3), mg.getMeetInfoCsv());
        this.um = um;
        this.im = new ItemManager(ig.getItemInfoCSV());
        this.userId = userId;
        this.dts = new DisplayTradeSystem(userId, tm, im, um);
        this.rts = new RequestTradeSystem(userId, tm, im, um);
        this.dats = new DoActionTradeSystem(userId, tm, im, um, mm);
    }

    /**
     * The method that runs the TradeSystem
     */
    public void run() {
        if (!String.valueOf(um.statusByUserId(userId)).equals("GOOD")) {
            checkUserStatus("Bad");
        }
        else {
            checkUserStatus("Good");
        }
    }

    private void checkUserStatus(String s){
        if (s.equals("Bad")) {
            helperBadSwitch(s);
        } else {
            helperGoodSwitch(s);
        }
    }

    private void helperBadSwitch(String s) {
            tp.getSelectionMenu("Bad");
            tp.getGoBackMenuNotification();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    break;
                case "1":
                    dts.helperDisplaySelection();
                    break;
                case "2":
                    dts.helperDisplayRecent();
                    break;
                default:
                    tp.invalidSelection();
                    helperBadSwitch(s);
            }
    }

    private void helperGoodSwitch(String s) {
            tp.getSelectionMenu("Good");
            tp.getGoBackMenuNotification();
            Scanner input = new Scanner(System.in);
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    break;
                case "1":
                    dts.helperDisplaySelection();
                    break;
                case "2":
                    dts.helperDisplayRecent();
                    break;
                case "3":
                    if (!validIncompleteTrade(getUserId())) {
                        tp.violateIncompleteTrade();
                        checkUserStatus("Bad");
                        break;
                    }
                    else if (!tm.checkTradeLimit(getUserId())) {
                        tp.violateTradeLimit();
                        checkUserStatus("Good");
                        break;
                    } else {
                        rts.helperRequestTrade();
                        break;
                    }
                case "4":
                    if (!availableActionTrade().isEmpty()) {
                        dats.helperActionToTrade(availableActionTrade());
                    }
                    else {
                        tp.noTradeActionAvailable();
                    }
                    break;
                default:
                    tp.invalidSelection();
                    helperGoodSwitch(s);
            }
    }

    private String getUserId() {
        return this.userId;
    }

    private boolean validIncompleteTrade(String userId) {
        if (!tm.checkIncompleteTrade(userId)) {
            um.changeStatus(getUserId(), UserStatus.FLAGGED);
            ug.updateUserInfoCsv(um);
            return false;
        } else {
            return true;
        }
    }

    private ArrayList<Trade> availableActionTrade() {
        List<String> allTrade = tm.getAllTradeIdByUserId(getUserId());
        ArrayList<Trade> al = new ArrayList<>();
        for (String tradeId: allTrade) {
            if (validActionTrade(tradeId)) {
                al.add(tm.findTrade(tradeId));
            }
        }

        return al;
    }

    private boolean validActionTrade(String tradeId) {
        if (String.valueOf(tm.getTradeStatusByTradeId(tradeId)).equals("REQUESTED") &&
                tm.getRequesterByTradeId(tradeId).equals(getUserId())) {
            return false;
        } else if (String.valueOf(tm.getTradeStatusByTradeId(tradeId)).equals("REJECTED")) {
            return false;
        } else if (String.valueOf(tm.getTradeStatusByTradeId(tradeId)).equals("CANCELLED")) {
            return false;
        } else {
            return true;
        }
    }
}


