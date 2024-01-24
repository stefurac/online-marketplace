package controllers;

import entities.*;
import gateways.*;
import presenters.*;
import usecases.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Controls the system responsible for admin
 */
public class AdminSystem {
    private final ItemGateway ig = new ItemGateway();
    private final AdminGateway ag = new AdminGateway();
    private final UserGateway ug = new UserGateway();
    private final ThresholdGateway tg = new ThresholdGateway();
    private final TradeGateway tdg = new TradeGateway();
    private final AdminManager adm;
    private final UserManager um;
    private final ItemManager im;
    private final MeetingGateway mg;
    private MeetingManager mm;
    private final TradeManager tm;
    private final AdminPresenter ap;
    private final ItemPresenter ip = new ItemPresenter(); //might delete later;
    private final UserPresenter up = new UserPresenter(); //might delete later;
    private final TradePresenter tp; //might delete later;
    private final Scanner input = new Scanner(System.in); // create a scanner object
    private final int passwordLength;
    private final String[] invalidString = new String[]{",", "/"};

    /**
     * Creates an instance of UserSystem
     *
     * @param adm an instance of AdminManager
     * @param um an instance of UserManager
     */
    public AdminSystem(AdminManager adm, UserManager um, MeetingManager mm, ItemManager im) {
        this.um = um;
        this.adm = adm;
        this.im = im;
        //this.im = new ItemManager(ig.getItemInfoCSV());
        this.mm = mm;
        this.ap = new AdminPresenter(adm, um);
        this.passwordLength = tg.getThresholdInfoCSV().get(4);
        List<Integer> al = new ArrayList<>();
        List<Integer> db = tg.getThresholdInfoCSV();
        al.add(db.get(0));
        al.add(db.get(1));
        al.add(db.get(2));
        MeetingGateway mg = new MeetingGateway();
        this.mm = new MeetingManager(db.get(3), mg.getMeetInfoCsv());
        this.tm = new TradeManager(al, tdg.getTradeInfoCsv());
        this.tp = new TradePresenter(tm, um, im);
        this.mg = new MeetingGateway();
    }


    /**
     * The run() method that the higher-level controller calls during runtime
     * @param adminId the admin id of the admin who is currently logged in
     */
    public void run(String adminId) {
        boolean logOff = false;
        do {
            ap.adminMainMenu();
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    logOff = true;
                    updateToCSV();
                    break;
                case "1":
                    manageProfileControls(adminId);
                    break;
                case "2":
                    manageUserControls();
                    break;
                case "3":
                    manageAdminControls();
                    break;
                case "4":
                    manageItemRequests();
                    break;
                case "5":
                    manageThresholdControls();
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        } while (!logOff);
    }

    private void manageProfileControls(String adminId){ //
        boolean quitToMainMenu = false;
        do {
            ap.adminManageProfileMenu();

            String selection = input.nextLine();
            switch (selection) {
                case "1":
                    changeUsername(adminId);
                    break;
                case "2":
                    changePassword(adminId);
                    break;
                case "0":
                    quitToMainMenu = true;
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        }while(!quitToMainMenu);
    }

    private void manageUserControls() {
        boolean quitToMainMenu = false;
        do {
            ap.adminManageUserMenu();
            String selection = input.nextLine();
            switch (selection) {
                case "1":
                    manageAllUsersControls();
                    break;
                case "2":
                    flagControls();
                    break;
                case "3":
                    ap.frozenUsersTitle();
                    if (um.userIdsByStatus(UserStatus.FROZEN).size() == 0) {
                        ap.noFrozenUsers();
                    }
                    statusControls(UserStatus.FROZEN, UserStatus.GOOD, "Unfreeze");
                    break;
                case "4":
                    ap.requestedUsersTitle();
                    statusControls(UserStatus.REQUESTED, UserStatus.GOOD, "Unfreeze");
                    break;
                case "0":
                    quitToMainMenu = true;
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        }while(!quitToMainMenu);

    }

    private void manageAllUsersControls() {
        boolean quit = false;
        do {
            ap.manageUsersMenu();
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    quit = true;
                    break;

                case "1":
                    viewAllUsers();
                    break;

                case "2":
                    undoControls();
                    break;
                case "3":
                    resetPassword();
                    break;

                default:
                    ap.invalidInputMessage();
                    break;
            }
        }while(!quit);
    }

    private void viewAllUsers() {
        ap.allUsersTitle();
        ap.allUsers();
        ap.pageBreak();
        ap.zeroGoBack();
        boolean quit = false;
        do {
            String selection = input.nextLine();
            if (isZero(selection)) {
                quit = true;
            } else {
                ap.selectZero();
            }
        }while(!quit);
    }

    private void undoControls() {
        boolean quit = false;
        do {
            ap.undoUserActionsMenu();
            String selection = input.nextLine();
            switch (selection) {
                case "0":
                    quit = true;
                    break;
                case "1":
                    undoAddInventory();
                    break;
                case "2":
                    undoRemoveInventory();
                    break;
                case "3":
                    undoAddWishlist();
                    break;
                case "4":
                    undoRemoveWishlist();
                    break;
                case "5":
                    undoUsername();
                    break;
                case "6":
                    undoPassword();
                    break;
                case "7":
                    undoTradeStatus();
                    quit = true;
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        } while(!quit);
    }
    // == UNDO HELPERS ==
    private void undoAddInventory() {
        ap.undoInventoryAddTitle();
        ap.allUsers();
        ArrayList<String> usernames = um.getUsernames();
        ap.zeroGoBack();
        //ap.pageBreak();
        ap.selectUserOrZero();
        boolean quitToUndoMenu = false;
        do {
            String selection = input.nextLine();
            if (isGoodSelection(selection, usernames.size())) {
                int i = Integer.parseInt(selection);
                if (isZero(i)) {
                    quitToUndoMenu = true;
                }
                  else {
                    String username = usernames.get(i - 1);
                    String userId = um.userIdByUsername(username);
                    ap.inventoryTitle(username);
                    ip.viewInventory(um, im, userId);
                    ap.pageBreak();
                    ap.undoInventoryAddPrompt();
                    String yn = input.nextLine();
                    if (yn.equals("Y")) {
                        ArrayList<String> inventory = um.inventoryByUserId(userId);
                        if (inventory.size() >= 1) {
                            String lastItem = inventory.get(inventory.size()-1);
                            if (im.removeByItemId(lastItem)) {
                                ap.undoSuccessfulMessage();
                                updateToCSV();
                            }
                            else {
                               ap.noInventoryUndo(username);
                            }
                        }
                        else {
                            ap.noInventoryUpdateMessage(username);
                        }
                    }
                    else {
                        ap.selectUserOrZero();
                    }
                }
            }
            else {
                ap.selectionRange(usernames.size());
            }
        } while (!quitToUndoMenu);
    }

    private void undoRemoveInventory() {
        ap.undoInventoryRemoveTitle();
        ap.allUsers();
        ArrayList<String> usernames = um.getUsernames();
        ap.zeroGoBack();
        ap.selectUserOrZero();
        boolean quitToUndoMenu = false;
        do {
            String selection = input.nextLine();
            if (isGoodSelection(selection, usernames.size())) {
                int i = Integer.parseInt(selection);
                if (isZero(i)) {
                    quitToUndoMenu = true;
                }
                else {
                    String username = usernames.get(i - 1);
                    String userId = um.userIdByUsername(username);
                    ap.inventoryHistoryTitle(username);
                    ArrayList<String> inventoryHistory = um.inventoryHistoryByUserId(userId);
                    for (String itemId : inventoryHistory) {
                        ap.printString(im.getToString(itemId));
                    }
                    ap.undoInventoryRemovePrompt();
                    ap.pageBreak();
                    String yn = input.nextLine();
                    if (yn.equals("Y")) {
                        if (inventoryHistory.size() >= 1) {
                            String lastItem = inventoryHistory.get(inventoryHistory.size()-1);
                            if (im.reAddByItemId(lastItem) && um.addToInventory(userId, lastItem) && um.removeFromInventoryHistory(userId, lastItem)) {
                                ap.undoSuccessfulMessage();
                                updateToCSV();
                            }
                            else {
                                ap.noInventoryUndo(username);
                            }
                        }
                        else {
                            ap.noInventoryUpdateMessage(username);
                        }
                    }
                    else {
                        ap.selectUserOrZero();
                    }
                }
            }
            else {
                ap.selectionRange(usernames.size());
            }
        } while (!quitToUndoMenu);
    }

    private void undoAddWishlist() {
        ap.undoWishlistAddTitle();
        ap.allUsers();
        ArrayList<String> usernames = um.getUsernames();
        ap.zeroGoBack();
        ap.selectUserOrZero();
        boolean quitToUndoMenu = false;
        do {
            String selection = input.nextLine();
            if (isGoodSelection(selection, usernames.size())) {
                int i = Integer.parseInt(selection);
                if (isZero(i)) {
                    quitToUndoMenu = true;
                }
                else {
                    String username = usernames.get(i - 1);
                    String userId = um.userIdByUsername(username);
                    ap.wishlistTitle(username);
                    ip.viewWishlist(um, im, userId);
                    ap.undoWishlistAddPrompt();
                    ap.pageBreak();
                    String yn = input.nextLine();
                    if (yn.equals("Y")) {
                        ArrayList<String> wishlist = um.wishlistByUserId(userId);
                        if (wishlist.size() >= 1) {
                            String lastItem = wishlist.get(wishlist.size()-1);
                            if (um.systemRemoveFromWishlist(userId, lastItem)) {
                                ap.undoSuccessfulMessage();
                                updateToCSV();
                            }
                            else {
                                ap.noWishlistUndo(username);
                            }
                        }
                        else {
                            ap.noWishlistUpdateMessage(username);
                        }
                    }
                    else {
                        ap.selectUserOrZero();
                    }
                }
            }
            else {
                ap.selectionRange(usernames.size());
            }
        } while (!quitToUndoMenu);
    }

    private void undoRemoveWishlist() {
        ap.undoWishlistRemoveTitle();
        ap.allUsers();
        ArrayList<String> usernames = um.getUsernames();
        ap.zeroGoBack();
        ap.selectUserOrZero();
        boolean quitToUndoMenu = false;
        do {
            String selection = input.nextLine();
            if (isGoodSelection(selection, usernames.size())) {
                int i = Integer.parseInt(selection);
                if (isZero(i)) {
                    quitToUndoMenu = true;
                }
                else {
                    String username = usernames.get(i - 1);
                    String userId = um.userIdByUsername(username);
                    ap.wishlistHistoryTitle(username);
                    ArrayList<String> wishlistHistory = um.wishlistHistoryByUserId(userId);
                    for (String itemId : wishlistHistory) {
                        ap.printString(im.getToString(itemId));
                    }
                    ap.undoWishlistRemovePrompt();
                    ap.pageBreak();
                    String yn = input.nextLine();
                    if (yn.equals("Y")) {
                        if (wishlistHistory.size() >= 1) {
                            String lastItem = wishlistHistory.get(wishlistHistory.size()-1);
                            if (um.addToWishlist(userId, lastItem) && um.removeFromWishlistHistory(userId, lastItem)) {
                                ap.undoSuccessfulMessage();
                                updateToCSV();
                            }
                            else {
                                ap.noWishlistUndo(username);
                            }
                        }
                        else {
                            ap.noWishlistUpdateMessage(username);
                        }
                    }
                    else {
                        ap.selectUserOrZero();
                    }
                }
            }
            else {
                ap.selectionRange(usernames.size());
            }
        } while (!quitToUndoMenu);
    }

    private boolean isGoodSelection(String selection, int size) {
        try {
            int i = Integer.parseInt(selection);
            return 0 <= i && i <= size;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }

    private boolean isZero(int selection) {
        return selection == 0;
    }

    private boolean isZero(String selection) {
        return selection.equals("0");
    }

    private void undoUsername() {
        ap.undoUsernameTitle();
        ap.allUsers();
        ArrayList<String> usernames = um.getUsernames();
        ap.zeroGoBack();
        ap.selectUserOrZero();
        boolean quitToUndoMenu = false;
        do {
            String selection = input.nextLine();
            if (isGoodSelection(selection, usernames.size())) {
                int i = Integer.parseInt(selection);
                if (isZero(i)) {
                    quitToUndoMenu = true;
                }
                else {
                    String username = usernames.get(i - 1);
                    String userId = um.userIdByUsername(username);
                    if (um.swapUsernames(userId)) {
                        ap.undoSuccessfulMessage();
                        updateToCSV();
                    }
                    else {
                        ap.noUsernameUpdate(username);
                    }
                }
            }
            else {
                ap.selectionRange(usernames.size());
            }
        } while (!quitToUndoMenu);
    }

    private void undoPassword() {
        ap.undoPasswordTitle();
        ap.allUsers();
        ArrayList<String> usernames = um.getUsernames();
        ap.zeroGoBack();
        ap.selectUserOrZero();
        boolean quitToUndoMenu = false;
        do {
            String selection = input.nextLine();
            if (isGoodSelection(selection, usernames.size())) {
                int i = Integer.parseInt(selection);
                if (isZero(i)) {
                    quitToUndoMenu = true;
                }
                else {
                    String username = usernames.get(i - 1);
                    String userId = um.userIdByUsername(username);
                    if (um.swapPasswords(userId)) {
                        ap.undoSuccessfulMessage();
                        updateToCSV();
                    }
                    else {
                        ap.noPasswordUpdate(username);
                    }
                }
            }
            else {
                ap.selectionRange(usernames.size());
            }
        } while (!quitToUndoMenu);
    }

    private String userChoice() {
        ap.allUsersTitle();
        ap.allUsers();
        ArrayList<String> usernames = um.getUsernames();
        ap.zeroGoBack();
        ap.selectionRange(usernames.size());
        String choice = input.nextLine();
        if (isZero(choice)) {
            return "0";
        }
        ArrayList<String> choices = new ArrayList<>();
        int i = 1;
        for (String ignored : um.getUsernames()) {
            String userChoice = Integer.toString(i);
            choices.add(userChoice);
            i++;
        }
        while (!(choices).contains(choice) || isZero(choice)) {
            if (isZero(choice)) {return "0";}
            up.invalidInputMessage();
            choice = input.nextLine();
        }
        int choiceIndex = Integer.parseInt(choice) - 1;
        String username = um.userList().get(choiceIndex).getUsername();
        return um.userIdByUsername(username);
    }

    private String tradeChoice(String userId) {
        tp.viewAllTrades(userId);
        ap.selectionRange(tm.getAllTradeIdByUserId(userId).size());
        String tradeChoice = input.nextLine();
        if (isZero(tradeChoice)) { return "0"; }
        ArrayList<String> tradeChoices = new ArrayList<>();
        int i = 1;

        ArrayList<String> trades = tm.getAllTradeIdByUserId(userId);
            for (String ignored : trades) {
                String choice = Integer.toString(i);
                tradeChoices.add(choice);
                i++;
        }
        while (!(tradeChoices).contains(tradeChoice) || isZero(tradeChoice)) {
            if (isZero(tradeChoice)) { return "0";
            }

            tradeChoice = input.nextLine();
        }
        int choiceIndex = Integer.parseInt(tradeChoice) - 1;
        return tm.getAllTradeIdByUserId(userId).get(choiceIndex);}

    private TradeStatus newStatus(TradeStatus status) {
        if (status==TradeStatus.REQUESTED) {
            return TradeStatus.ADMIN_DELETED;
        }
        if (status==TradeStatus.ACCEPTED) {
            return TradeStatus.REQUESTED;
        }
        if (status==TradeStatus.REJECTED) {
            return TradeStatus.ACCEPTED;
        }
        if (status==TradeStatus.CONFIRMED) {
            return TradeStatus.ACCEPTED;
        }
        if (status==TradeStatus.REQUESTER_CONFIRMED || status==TradeStatus.RECEIVER_CONFIRMED)  {
            return TradeStatus.CONFIRMED;
        }
        if (status==TradeStatus.COMPLETED ) {
           // return tm.getPreviousStatus(tradeId);
            return TradeStatus.CONFIRMED;

        }
        if (status==TradeStatus.CANCELLED) {
            return TradeStatus.ACCEPTED;
        }
        return TradeStatus.ADMIN_DELETED;

    }

    private String currStatus(TradeStatus status) {
        if (status==TradeStatus.REQUESTED) {
            return "Request";
        }
        if (status==TradeStatus.ACCEPTED) {
            return "Accept";
        }
        if (status==TradeStatus.REJECTED) {
            return "Reject";
        }
        if (status==TradeStatus.REQUESTER_CONFIRMED) {
            return "Requester Confirm";
        }
        if (status==TradeStatus.RECEIVER_CONFIRMED) {
            return "Receiver Confirm";
        }
        if (status==TradeStatus.CONFIRMED) {
            return "Confirm";
        }
        if (status==TradeStatus.COMPLETED) {
            return "Complete";
        }
        if (status==TradeStatus.CANCELLED) {
            return "Cancel";
        }
        return "Admin Delete";
    }

    private boolean userLastActed(String userId, String tradeId, TradeStatus status) {
        if ((status==TradeStatus.REQUESTED || status==TradeStatus.REQUESTER_CONFIRMED) && tm.findTrade(tradeId).getRequester().equals(userId)) {
            return true;
        }
        return (status == TradeStatus.REJECTED || status == TradeStatus.RECEIVER_CONFIRMED || status == TradeStatus.CANCELLED) && tm.findTrade(tradeId).getReceiver().equals(userId);
    }

    private void undoMeetingAction(String userId, String tradeId) {
        String meetingId = tm.findTrade(tradeId).getMeetingId();
        Meeting meeting = mm.findMeeting(meetingId);
        if (meeting.getUserLastEdited().equals(userId)) {
            if (meeting.getUsersTurn()[0] && meeting.getUsersTurn()[1]) {  // new meeting
                if (tradeId.startsWith("@return")) {
                    ap.cannotUndoReturnMeetingMessage();
                } else {
                    tm.changeStatus(tradeId, TradeStatus.REQUESTED);
                    mm.deleteMeeting(meetingId);  // deletes the meeting

                    if (tm.getItemIdsByTradeId(tradeId).size() == 2) {  // if two-way trade
                        String receiverItemId = tm.getItemIdsByTradeId(tradeId).get(2);
                        im.changeStatus(receiverItemId, ItemStatus.AVAILABLE);  // sets receiver's item to AVAILABLE
                    }
                }
            } else {  // not a new meeting
                if (!tradeId.startsWith("@return")) {
                    meeting.setTime(LocalDate.now());
                }
                meeting.setPlace("");

                meeting.setUsersTurns(new boolean[]{meeting.getUsersTurn()[1], meeting.getUsersTurn()[0]});
                if (tm.findTrade(tradeId).getRequester().equals(userId)) {
                    meeting.setUsersEditCount(new int[]{meeting.getUsersEditCount()[0] - 1, meeting.getUsersEditCount()[1]});
                    meeting.setUserLastEdited(tm.getReceiverByTradeId(tradeId));
                } else {
                    meeting.setUsersEditCount(new int[]{meeting.getUsersEditCount()[0], meeting.getUsersEditCount()[1] - 1});
                    meeting.setUserLastEdited(tm.getRequesterByTradeId(tradeId));
                }
            }
            ap.successUndoTradeMessage();
            updateToCSV();
        } else {
            ap.userNotLastActorMessage();
        }
    }

    private void undoConfirmed(String userId, String tradeId) {
        String meetingId = tm.findTrade(tradeId).getMeetingId();
        Meeting meeting = mm.findMeeting(meetingId);

        tm.changeStatus(tradeId, TradeStatus.ACCEPTED);
        if (tm.findTrade(tradeId).getRequester().equals(userId)) {  // user last edited was the requester
            meeting.setUsersTurns(new boolean[]{true, false});
            meeting.setUsersEditCount(new int[]{meeting.getUsersEditCount()[0] - 1, meeting.getUsersEditCount()[1]});
            meeting.setUserLastEdited(tm.getReceiverByTradeId(tradeId));
        } else {
            meeting.setUsersTurns(new boolean[]{false, true});  // user last edited was the receiver
            meeting.setUsersEditCount(new int[]{meeting.getUsersEditCount()[0], meeting.getUsersEditCount()[1] - 1});
            meeting.setUserLastEdited(tm.getRequesterByTradeId(tradeId));
        }
        ap.successUndoTradeMessage();
        updateToCSV();
    }


    private void undoTradeStatus() {

        while (true) {
            String userId = userChoice();
            if (isZero(userId)) {
                break;
            }

            if (tm.getAllTradeIdByUserId(userId).isEmpty()) {
                ap.userNoTradeFoundMessage();
                break;
            }

            String tradeId = tradeChoice(userId);
            if (isZero(tradeId)) {
                break;
            }

            TradeStatus tradeStatus = tm.findTrade(tradeId).getStatus();
            String currStatus = currStatus(tradeStatus);
            TradeStatus updatedStatus = newStatus(tradeStatus);
            ap.undoTrade(currStatus);
            if (currStatus.equals("Admin Delete")) {
                ap.cannotUndoAdminActionMessage();
                break;
            }
            if (currStatus.equals("Accept")) {
                undoMeetingAction(userId, tradeId);
                updateToCSV();
                break;
            } else if (currStatus.equals("Confirm")) {
                undoConfirmed(userId, tradeId);
                updateToCSV();
                break;
            }
            if (userLastActed(userId, tradeId, tradeStatus)) {
            tm.changeStatus(tradeId, updatedStatus);
            if (currStatus.equals("Request")) {
                for (String itemIds: tm.getItemIdsByTradeId(tradeId)) {
                    im.changeStatus(itemIds, ItemStatus.AVAILABLE);
                    updateToCSV();
                    break;
                }
            }
            if (currStatus.equals("Complete")) {
                for (String itemIds: tm.getItemIdsByTradeId(tradeId)) {
                    im.changeStatus(itemIds, ItemStatus.UNAVAILABLE);
                    updateToCSV();
                }
                helperUpdatePoints(tradeId);
            }
            ap.successUndoTradeMessage();
            updateToCSV();
            }
        else {
            ap.userNotLastActorMessage();
            }
            break;
        }
    }

    private void helperUpdatePoints(String tradeId) {
        String receiverId = tm.getReceiverByTradeId(tradeId);
        String requesterId = tm.getRequesterByTradeId(tradeId);
        List<String> itemIds = tm.getItemIdsByTradeId(tradeId);
        if (itemIds.size() == 1) {
            um.removePoints(receiverId, 20);
            if (um.premiumUserIds().contains(requesterId)) {um.addPoints(requesterId, 10);}
            else {um.removePoints(requesterId, 10); }
        } else {
            um.removePoints(requesterId, 30);
            um.removePoints(receiverId, 30);
        }
        updateToCSV();
    }

    private void resetPassword() {
        ap.resetPasswordTitle();
        ap.allUsers();
        ap.pageBreak();
        ap.zeroGoBack();
        ap.passwordResetPrompt();
        String username = input.nextLine();
        if (um.getUsernames().contains(username)){
            String userId = um.userIdByUsername(username);
            String resetPassword = generatePassword();
            um.changePassword(userId, resetPassword);
            ap.undoPasswordSuccessful(username, resetPassword);
            updateToCSV();
        }
        else {
            ap.noUpdateMsg();
        }
    }
    // == UNDO HELPERS ==

    private void flagControls() {
        ap.flaggedUsersTitle();
        if (um.userIdsByStatus(UserStatus.PROBATION).size() == 0) {
            ap.noFlaggedUsers();
        }
        profilesByStatus(UserStatus.PROBATION);
        boolean quit = false;
        do {
            ap.manageFlaggedMenu();
            String selection = input.nextLine();
            switch (selection) {
                case "1":
                    statusControls(UserStatus.PROBATION, UserStatus.FROZEN, "Freeze");
                    //quit = true;
                    break;
                case "2":
                    statusControls(UserStatus.PROBATION, UserStatus.GOOD, "Unflag");
                    //quit = true;
                    break;
                case "0":
                    quit = true;
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        }while(!quit);
    }

    private void statusControls(UserStatus currStatus, UserStatus setStatus, String action) {
        profilesByStatus(currStatus);
        ap.statusPrompt(action);
        String username = input.nextLine();
        if (!username.toUpperCase().equals(ap.getCancelString())) {
            if (um.getUsernames().contains(username)){
                String userId = um.userIdByUsername(username);
                if (um.userIdsByStatus(currStatus).contains(userId)) {
                    um.changeStatus(userId, setStatus);
                    ap.updateSuccessMessage();
                    updateToCSV();

                    if (currStatus == UserStatus.PROBATION && setStatus == UserStatus.FROZEN) {
                        um.removePoints(um.userIdByUsername(username), 40);
                        updateToCSV();
                    }

                    else if (um.usersAbove500().contains(um.userIdByUsername(username)) && setStatus == UserStatus.GOOD)
                    {         um.userByUserId(userId).setStatus(UserStatus.PREMIUM); }
                }
                else {
                    ap.noUpdateMsg();
                }
            } else {
                ap.usernameNotFound();
            }
        }
    }

    private void profilesByStatus(UserStatus status) {
        ArrayList<String> userIds = um.userIdsByStatus(status);
        for (String userId : userIds) {
            ap.userProfile(userId);
        }
    }

    private void manageAdminControls() {
        boolean quitToMainMenu = false;
        do {
            ap.adminManageAdminMenu();

            String selection = input.nextLine();
            switch (selection) {
                case "1":
                    ap.viewAdmin();
                    break;
                case "2":
                    registerAdmin();
                    break;
                case "0":
                    quitToMainMenu = true;
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        }while(!quitToMainMenu);

    }

    private void manageItemRequests() {
        boolean quitToMainMenu = false;
        do {
            ap.manageItemRequestsTitle();
            ArrayList<String> unchecked = im.itemIdsByStatus(ItemStatus.UNCHECKED);
            uncheckedItems();
            ap.zeroGoBack();
            ap.selectItemOrZero();
            String selection = input.nextLine();
            if (isGoodSelection(selection, unchecked.size())) {
                int i = Integer.parseInt(selection);
                if (isZero(selection)) {
                    quitToMainMenu = true;
                }
                else {
                    String itemId = unchecked.get(i - 1);
                    String userId = um.userIdByInventoryItem(itemId);
                    uncheckedOptions(itemId, userId);
                }
            }
        }while(!quitToMainMenu);
    }

    private void uncheckedOptions(String itemId, String userId) {
        ap.itemRequestOptions();
        ap.zeroGoBack();
        boolean quit = false;
        do{
            String selection = input.nextLine();
            switch (selection){
                case "0":
                    quit=true;
                    break;
                case "1":
                    ap.itemAccepted();
                    im.changeStatus(itemId, ItemStatus.AVAILABLE);
                    um.userByUserId(userId).addPoints(50);
                    updateToCSV();
                    quit = true;
                    //manageItemRequests();
                    break;
                case "2":
                    ap.itemRejected();
                    im.deleteItem(itemId);
                    um.systemRemoveFromInventory(userId, itemId);
                    updateToCSV();
                    quit = true;
                    break;
                default:
                    ap.invalidInputMessage();
            }
        }while(!quit);
    }

    private void uncheckedItems() {
        int i = 1;
        ArrayList<String> items = im.itemIdsByStatus(ItemStatus.UNCHECKED);
        for (String itemId : items) {
            String userId = um.userIdByInventoryItem(itemId);
            String username = um.usernameByUserId(userId);
            ip.displayItem(i, itemId, username, im);
            i++;
        }
    }

    private void manageThresholdControls() {
        boolean quitToMainMenu = false;
        do {
            //ap.chooseOptionPrompt();
            ap.adminManageThresholdMenu();

            String selection = input.nextLine();
            switch (selection) {
                // Note: refer to ThresholdGateway for the index for each threshold
                case "1": //"1. Edit Meeting Cancellation Threshold"
                    //ap.thresholdEditPrompt();
                    editThreshold(0, 1);  // the lowest acceptable value for CancelThreshold is 1
                    break;

                case "2": //"2. Edit Weekly Trade Limit"
                    //ap.thresholdEditPrompt();
                    editThreshold(1, 1);  // the lowest acceptable value for WeeklyTradeLimit is 1
                    break;

                case "3": //"3. Edit Borrowed-Lent Difference Threshold"
                    //ap.thresholdEditPrompt();
                    editThreshold(2, 0);  // the lowest acceptable value for BorrowDifference is 0
                    break;

                case "4": //"4. Edit Meeting Edit Threshold"
                    //ap.thresholdEditPrompt();
                    editThreshold(3, 1);  // the lowest acceptable value for EditThreshold is 1
                    break;

                case "5": //"5. Edit Minimum Password Length"
                    //ap.thresholdEditPrompt();
                    editThreshold(4, 1);  // the lowest acceptable value for Password limit is 1
                    break;

                case "0": //"0. Main Menu"
                    quitToMainMenu = true;
                    break;

                default:
                    ap.invalidInputMessage();
                    break;
            }
        } while(!quitToMainMenu);
    }

    private String generatePassword() {
        int random = ThreadLocalRandom.current().nextInt(100000,999999);
        return String.valueOf(random);

    }
    private void registerAdmin() {
        ap.adminRegisterPrompt();
        ap.newUsernamePrompt();

        String newUsername = checkUsernameAvailability();
        if (!newUsername.toUpperCase().equals(ap.getCancelString())) {

            ap.adminUsernameMessage(newUsername);
            String newPassword = generatePassword();
            ap.proceedRegistrationPrompt();
            String yn = input.nextLine().toUpperCase();

            while (!yn.equals("Y") && !yn.equals("N")) {
                ap.invalidInputMessage();
                yn = input.nextLine().toUpperCase();
            }
            switch (yn) {
                case "Y":
                    adm.addAdmin(newUsername, newPassword);
                    ap.registrationResultMsg(true);
                    ap.generateAdminMsg(newUsername, newPassword);
                    break;
                case "N":
                    ap.registrationResultMsg(false);
                    break;
            }
        }
        updateToCSV();
    }

    private String checkUsernameAvailability() {
        String newUsername = input.nextLine();
        while (!adm.availableUsername(newUsername) || containsInvalidString(newUsername)) {
            ap.usernameUnavailablePrompt();
            newUsername = input.nextLine();
            if (newUsername.toUpperCase().equals(ap.getCancelString())) {
                return ap.getCancelString();  // return of the cancel String indicates the user has cancelled registration action
            }
        }
        return newUsername;
    }

    private boolean containsInvalidString(String anyString) {
        for (String s: invalidString) {
            if (anyString.contains(s)) return true;
        }
        return false;
    }

    private void changeUsername(String adminId) {
        ap.changeUsernamePrompt(adm.usernameByAdminId(adminId));
        String newUsername = input.nextLine();
        if (!newUsername.toUpperCase().equals(ap.getCancelString())) {
            while (!adm.availableUsername(newUsername)) {
                ap.usernameUnavailablePrompt();
                newUsername = input.nextLine();

                if (newUsername.toUpperCase().equals(ap.getCancelString())) {
                    return;
                }
            }
            adm.changeUsername(adminId, newUsername);
            ap.updateSuccessMessage();
        }
        updateToCSV();
    }

    private void changePassword(String adminId) {
        ap.currentPasswordPrompt();
        String passwordIn = input.nextLine();
        if (!passwordIn.toUpperCase().equals(ap.getCancelString())) {
            while (adm.incorrectPassword(adm.usernameByAdminId(adminId), passwordIn)) {
                ap.incorrectPasswordPrompt();
                passwordIn = input.nextLine();
                if (passwordIn.toUpperCase().equals(ap.getCancelString())) {
                    return;  // this return is never intended to be used, but rather as a way to force-exit the method call
                }
            }
            ap.newPasswordPrompt();
            String newPassword = input.nextLine();
            if (!newPassword.equals(ap.getCancelString())) {
                while (newPassword.length() < passwordLength) {
                    ap.invalidNewPasswordMessage();
                    newPassword = input.nextLine();
                    if (newPassword.toUpperCase().equals(ap.getCancelString())) {
                        return;  // this return is never intended to be used, but rather as a way to force-exit the method call
                    }
                }
                adm.changePassword(adminId, newPassword);
                updateToCSV();
            }
        }
        updateToCSV();
    }

    private void editThreshold(int thresholdIndex, int minValue) {  // refer to ThresholdGateway for the index for each threshold
        ap.thresholdEditPrompt();
        int newThreshold = -1;
        boolean badInput = true;
        boolean cancelled = false;

        do {
            String inputString = input.nextLine();
            if (inputString.toUpperCase().equals(ap.getCancelString())) {
                cancelled = true;
                break;
            }
            try {
                newThreshold = Integer.parseInt(inputString);

                if (newThreshold < minValue) {
                    ap.unacceptableThresholdMessage(minValue);
                } else {
                    badInput = false;  // valid input is obtained
                }
            } catch (NumberFormatException e) {
                ap.badThresholdMessage(minValue);
            }
        } while (badInput);

        if (!cancelled) {
            List<Integer> thresholdList = tg.getThresholdInfoCSV();
            thresholdList.set(thresholdIndex, newThreshold);

            tg.updateThresholdInfoCsv(thresholdList);
            ap.thresholdEditSuccessMessage();
        }
    }

    private void updateToCSV() {
        ug.updateUserInfoCsv(um, false);
        ag.updateAdminInfoCsv(adm);
        ig.updateItemInfoCsv(im, false);
        tdg.updateTradeInfoCsv(tm, false);
        mg.updateMeetInfoCsv(mm, false);
    }
}