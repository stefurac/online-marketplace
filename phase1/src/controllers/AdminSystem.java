package controllers;

import entities.UserStatus;
import gateways.AdminGateway;
import gateways.ItemGateway;
import gateways.ThresholdGateway;
import gateways.UserGateway;
import presenters.AdminPresenter;
import usecases.AdminManager;
import usecases.ItemManager;
import usecases.UserManager;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Controls the system responsible for admin
 */
public class AdminSystem {
    private ItemGateway ig = new ItemGateway();
    private AdminGateway ag = new AdminGateway();
    private UserGateway ug = new UserGateway();
    private ThresholdGateway tg = new ThresholdGateway();
    private final AdminManager adm;
    private final UserManager um;
    private ItemManager im;
    private final AdminPresenter ap;
    private String currentItemId;
    private Scanner input = new Scanner(System.in); // create a scanner object
    private int passwordLength;
    private String[] invalidString = new String[]{",", "/"};

    /**
     * Creates an instance of UserSystem
     *
     * @param adm an instance of AdminManager
     * @param um an instance of UserManager
     * @throws FileNotFoundException thrown when a file with the specified pathname does not exist
     */
    public AdminSystem(AdminManager adm, UserManager um) throws FileNotFoundException {
        this.um = um;
        this.adm = adm;
        this.im = new ItemManager(ig.getItemInfoCSV());
        this.ap = new AdminPresenter(adm, um);
        this.passwordLength = tg.getThresholdInfoCSV().get(4);
    }

    /**
     * The run() method that the higher-level controller calls during runtime
     *
     * @param adminId the admin id of the admin who is currently logged in
     * @throws FileNotFoundException thrown when a file with the specified pathname does not exist
     */
    public void run(String adminId) throws FileNotFoundException {
        boolean logOff = false;
        do {
            ap.selectOptionPrompt();
            ap.adminMainMenu();
            String selection = input.nextLine();
            switch (selection) {
                case "0": // "0. Log Off"
                    logOff = true;
                    ag.updateAdminInfoCsv(adm);
                    ig.updateItemInfoCsv(im);
                    ug.updateUserInfoCsv(um);
                    break;
                case "1":  // "1. Manage Profile"
                    manageProfileControls(adminId);
                    break;
                case "2": // "2. Manage User Accounts"
                    manageUsersControls();
                    break;
                case "3": // "3. Manage Admin Accounts"
                    manageAdminControls();
                    break;
                case "4": // "4. Manage Items"
                    manageItemsControls();
                    break;
                case "5": // "5. Manage Thresholds"
                    manageThresholdControls();
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        } while (!logOff);
    }

    private void manageProfileControls(String adminId){ // "1. Manage Profile"
        boolean quitToMainMenu = false;
        do {
            //ap.chooseOptionPrompt();
            ap.adminManageProfileMenu();

            String selection = input.nextLine();
            switch (selection) {
                case "1": //"1. Change Username"
                    changeUsername(adminId);
                    break;
                case "2": //"2. Change Password"
                    changePassword(adminId);
                    break;
                case "0": //"0. Main Menu"
                    quitToMainMenu = true;
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        }while(!quitToMainMenu);
    }

    private void manageUsersControls() { // "2. Manage User Accounts"
        boolean quitToMainMenu = false;
        do {
            ap.adminManageUserMenu();

            String selection = input.nextLine();
            switch (selection) {
                case "1": //"1. View Users"
                    ap.viewUsers();
                    break;
                case "2": //"2. Manage Flagged Users"
                    ap.flaggedUsers();
                    flagControls();
                    break;
                case "3": //"3. Manage Frozen Users"
                    ap.frozenUsers();
                    statusControls(UserStatus.FROZEN, UserStatus.GOOD, "Unfreeze");
                    break;
                case "4": //"4. Manage Unfreeze Requests"
                    ap.requestedUsers();
                    statusControls(UserStatus.REQUESTED, UserStatus.GOOD, "Unfreeze");
                    break;
                case "0": //"0. Main Menu"
                    quitToMainMenu = true;
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        }while(!quitToMainMenu);

    }

    private void flagControls() { // "2. Manage Flagged Users"
        profilesByStatus(UserStatus.FLAGGED);
        boolean quit = false;
        do {
            ap.flagMenu();
            String selection = input.nextLine();
            switch (selection) {
                case "1": //"1. Freeze User"
                    statusControls(UserStatus.FLAGGED, UserStatus.FROZEN, "Freeze");
                    //quit = true;
                    break;
                case "2": //"2. Unflag User"
                    statusControls(UserStatus.FLAGGED, UserStatus.GOOD, "Unflag");
                    //quit = true;
                    break;
                case "0": //"0. Main Menu"
                    quit = true;
                    break;
                default:
                    ap.invalidInputMessage();
                    //ap.flagMenu();
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
                    ug.updateUserInfoCsv(um);  // update User.csv file
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

    private void manageAdminControls() { // "3. Manage Admin Accounts"
        boolean quitToMainMenu = false;
        do {
            //ap.chooseOptionPrompt();
            ap.adminManageAdminMenu();

            String selection = input.nextLine();
            switch (selection) {
                case "1": //"1. View Admin"
                    ap.viewAdmin();
                    break;
                case "2": //"2. Add Admin"
                    registerAdmin();
                    break;
                case "0": //"0. Main Menu"
                    quitToMainMenu = true;
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        }while(!quitToMainMenu);

    }
    private void manageItemsControls() { // "4. Manage Items"
        boolean quitToMainMenu = false;
        do {
            ap.adminManageItemMenu();

            String selection = input.nextLine();
            switch (selection) {
                case "1": //"1. View Items"
                    ap.viewAllItems(im);
                    itemOptions(im.toString());
                    if (currentItemId.equals("")) {
                        quitToMainMenu = true;
                        break;
                    }
                    allItemsOptions();
                    break;
                case "2": //"2. Manage Item Requests"
                    ap.viewAllRequestedItems(im);
                    itemOptions(im.getRequestedItems());
                    if (currentItemId.equals("")) {
                        quitToMainMenu = true;
                        break;
                    }
                    requestedItemOption();
                    break;
                case "0": //"0. Main Menu"
                    quitToMainMenu = true;
                    break;
                default:
                    ap.invalidInputMessage();
                    break;
            }
        }while(!quitToMainMenu);

    }

    private void itemOptions(String items) {
        String[] itemsInfo = items.split("\n");
        int num;
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
        while (isBadInput(selection, itemsInfo.length)) {
            ap.invalidInputMessage();
            selection = input.nextLine();
        }
        num = Integer.parseInt(selection);
        if (num == 0) {
            currentItemId = "";
            return;
        }
        currentItemId = retrieveItemId(itemsInfo, num);
    }

    private String retrieveItemId(String[] itemsInfo, int num) {
        // retrieve itemId
        int endIndex = itemsInfo[num - 1].indexOf("(");
        return itemsInfo[num - 1].substring(3, endIndex);
    }

    private boolean isBadInput(String input, int numRange) {
        try {
            int selection = Integer.parseInt(input);
            if (selection < 0 || selection > numRange) return true;
        } catch (NumberFormatException e){
            return true;
        }
        return false;
    }

    private void allItemsOptions(){
        ap.singleItemMenu();
        Scanner input = new Scanner(System.in);
        String selection2 = input.nextLine();
        switch (selection2) {
            // Remove the chosen item
            case "1":
                removeFromSystem();
                break;
            case "0":
        }
    }

    private void removeFromSystem() {
        im.removeItemFromSystem(currentItemId);
        // remove the item from user's inventory (if such user exists)
        String user = um.findUserByItemInventory(currentItemId);
        if (user != null) um.removeFromInventory(user, currentItemId);
        // remove the item from user's wishlist (if such user exists)
        user = um.findUserByItemWishlist(currentItemId);
        if (user != null) um.removeFromWishlist(user, currentItemId);
        ap.itemRemovalSuccess();
    }

    private void requestedItemOption() {
        ap.requestedItemMenu();
        Scanner input = new Scanner(System.in);
        String selection2 = input.nextLine();
        switch (selection2) {
            // Remove the chosen item
            case "1":
                removeFromSystem();
                break;
            case "2":
                approveItem();
                break;
            case "0":
        }
    }

    private void approveItem() {
        im.makeItemAvailable(currentItemId);
    }

    private void manageThresholdControls() throws FileNotFoundException {
        boolean quitToMainMenu = false;
        do {
            //ap.chooseOptionPrompt();
            ap.adminManageThresholdMenu();

            String selection = input.nextLine();
            switch (selection) {
                // Note: refer to ThresholdGateway for the index for each threshold
                case "1": //"1. Edit Meeting Cancellation Threshold"
                    ap.thresholdEditPrompt("Meeting Cancellation Threshold");
                    editThreshold(0, 1);  // the lowest acceptable value for CancelThreshold is 1
                    break;

                case "2": //"2. Edit Weekly Trade Limit"
                    ap.thresholdEditPrompt("Weekly Trade Limit");
                    editThreshold(1, 1);  // the lowest acceptable value for WeeklyTradeLimit is 1
                    break;

                case "3": //"3. Edit Borrowed-Lent Difference Threshold"
                    ap.thresholdEditPrompt("Borrowed-Lent Difference Threshold");
                    editThreshold(2, 0);  // the lowest acceptable value for BorrowDifference is 0
                    break;

                case "4": //"4. Edit Meeting Edit Threshold"
                    ap.thresholdEditPrompt("Meeting Edit Threshold");
                    editThreshold(3, 1);  // the lowest acceptable value for EditThershold is 1
                    break;

                case "5": //"5. Edit Minimum Password Length"
                    ap.thresholdEditPrompt("Minimum Password Length");
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
        int random = ThreadLocalRandom.current().nextInt(1000,9999);
        return String.valueOf(random);

    }
    private String registerAdmin() {
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
                    ap.generatePassMsg(newUsername, newPassword);
                    break;
                case "N":
                    ap.registrationResultMsg(false);
                    break;
                }
            }
        ag.updateAdminInfoCsv(adm);  // update User.csv file
        return "";  // this return is never intended to be used
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

    private String changeUsername(String adminId) {
        ap.changeUsernamePrompt(adm.usernameByAdminId(adminId));
        String newUsername = input.nextLine();
        if (!newUsername.toUpperCase().equals(ap.getCancelString())) {
            while (!adm.availableUsername(newUsername)) {
                ap.usernameUnavailablePrompt();
                newUsername = input.nextLine();

                if (newUsername.toUpperCase().equals(ap.getCancelString())) {
                    return "";
                }
            }
            adm.changeUsername(adminId, newUsername);
            ap.updateSuccessMessage();
        }
        ag.updateAdminInfoCsv(adm);  // update User.csv file
        return "";
    }

    private String changePassword(String adminId) {
        ap.currentPasswordPrompt();
        String passwordIn = input.nextLine();
        if (!passwordIn.toUpperCase().equals(ap.getCancelString())) {
            while (adm.incorrectPassword(adm.usernameByAdminId(adminId), passwordIn)) {
                ap.incorrectPasswordPrompt();
                passwordIn = input.nextLine();
                if (passwordIn.toUpperCase().equals(ap.getCancelString())) {
                    return "";  // this return is never intended to be used, but rather as a way to force-exit the method call
                }
            }
            ap.newPasswordPrompt();
            String newPassword = input.nextLine();
            if (!newPassword.equals(ap.getCancelString())) {
                while (newPassword.length() < passwordLength) {
                    ap.invalidNewPasswordMessage();
                    newPassword = input.nextLine();
                    if (newPassword.toUpperCase().equals(ap.getCancelString())) {
                        return "";  // this return is never intended to be used, but rather as a way to force-exit the method call
                    }
                }
                adm.changePassword(adminId, newPassword);
                ap.updateSuccessMessage();
            }
        }
        ag.updateAdminInfoCsv(adm);  // update User.csv file
        return "";  // this return should never be used
    }

    private void editThreshold(int thresholdIndex, int minValue) throws FileNotFoundException {  // refer to ThresholdGateway for the index for each threshold
        int newThreshold = -1;
        boolean badInput = true;
        boolean cencelled = false;

        do {
            String inputString = input.nextLine();
            if (inputString.toUpperCase().equals(ap.getCancelString())) {
                cencelled = true;
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

        if (!cencelled) {
            List<Integer> thresList = tg.getThresholdInfoCSV();
            thresList.set(thresholdIndex, newThreshold);
            tg.updateThresholdInfoCsv(thresList);
            ap.thresholdEditSuccessMessage();
        }
    }
}