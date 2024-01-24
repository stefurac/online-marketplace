package controllers;

import entities.UserStatus;
import gateways.ThresholdGateway;
import gateways.UserGateway;
import presenters.UserPresenter;
import usecases.AdminManager;
import usecases.ItemManager;
import usecases.TradeManager;
import usecases.UserManager;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A controller that controls the system responsible for Users
 */
public class UserSystem {
    private final int passwordLengthLimit;
    private boolean isDemo;
    private final UserGateway ug = new UserGateway();
    private final ThresholdGateway thrG = new ThresholdGateway();
    private final UserPresenter up = new UserPresenter();
    private final UserManager um;
    private final AdminManager adm; //remove adm from us?
    private final ItemManager im;
    private TradeManager tm; //remove tm from us?
    private final Scanner input = new Scanner(System.in); // create a scanner object

    /**
     * Creates an instance of UserSystem
     * @param um an instance of UserManager
     * @param adm an instance of AdminManager
     * @param im an instance of ItemManager
     */
    public UserSystem (UserManager um, AdminManager adm, ItemManager im) {
        this.um = um;
        this.adm = adm;
        this.im = im;
        passwordLengthLimit = thrG.getThresholdInfoCSV().get(4);
    }

    /**
     * The run() method that the higher-level controller calls during runtime
     * @param userId the user ID of the User who's currently logged in
     */
    public void run(String userId, boolean isDemo) {
        this.isDemo = isDemo;
        // check if frozen, and print frozen warning message if so
        boolean isFrozen = um.statusByUserId(userId) == UserStatus.FROZEN;
        if (isFrozen) {
            up.frozenWarningMessage();
        }

        boolean loggedOff = false;
        do {
            //up.chooseOptionPrompt();
            up.mainMenuOptions(isFrozen);

            String selection2 = input.nextLine();
            switch (selection2) {
                case "0":  // "Log off" if not frozen, or "4. Request to be unfrozen" if frozen
                    loggedOff = true;
                    break;

                case "1":  // "View user options"
                    // Enters user menu
                    displayUserMenu(userId);
                    break;

                case "2":  // "View item options"
                    ItemSystem is = new ItemSystem(userId, um, im, up);
                    is.run(isDemo);
                    break;

                case "3":  // "View trade options"
                    TradeSystem ts = new TradeSystem(userId, um);
                    ts.run(isDemo);
                    break;

                case "4":
                    up.pointsSystem();
                    quit();
                    break;

                case "5":  // Request to be unfrozen
                    if (isFrozen) {
                        requestUnfreeze(userId);
                        break;
                    }
                default:
                    up.invalidInputMessage();
                    break;
            }
        } while (!loggedOff);
    }
    private void quit() {
        boolean quit = false;
        do {
            String selection = input.nextLine();
            if (selection.equals("0")) {
                quit = true;
            }
            else {
                System.out.println("Please Select 0 to Go Back.");
            }
        }while(!quit);
    }

    private void displayUserMenu(String userId) {
        boolean quitUserMenu = false;

        do {
            //up.chooseOptionPrompt();
            up.userMenuOptions();

            String selection = input.nextLine();
            switch (selection) {
                case "0":  // "Return to main menu"
                    quitUserMenu = true;
                    break;
                case "1":  // "View my profile"
                    viewProfile(userId);
                    break;
                case "2":  // "Change my username"
                    changeUsername(userId);
                    break;
                case "3":  // "Change my password"
                    changePassword(userId);
                    break;
                default:
                    up.invalidInputMessage();
                    break;
            }
        } while (!quitUserMenu);
    }

    private void changeUsername(String userId) {
        up.changeUsernamePrompt(um.usernameByUserId(userId));
        String newUsername = input.nextLine();
        if (!newUsername.toUpperCase().equals(up.getCancelString())) {
            while (!um.checkUsername(newUsername)) {
                up.usernameUnavailablePrompt();
                newUsername = input.nextLine();

                if (newUsername.toUpperCase().equals(up.getCancelString())) {
                    return;  // this return is never intended to be used, but rather as a way to force-exit the method call
                }
            }
            um.changeUsername(userId, newUsername);
            up.updateSuccessMessage("username");
        }
        ug.updateUserInfoCsv(um, isDemo);  // update User.csv file
    }

    private void changePassword(String userId) {
        up.currentPasswordPrompt();
        String passwordIn = input.nextLine();
        if (!passwordIn.toUpperCase().equals(up.getCancelString())) {
            while (um.incorrectPassword(um.usernameByUserId(userId), passwordIn)) {
                up.incorrectPasswordPrompt();
                passwordIn = input.nextLine();

                if (passwordIn.toUpperCase().equals(up.getCancelString())) {
                    return;  // this return is never intended to be used, but rather as a way to force-exit the method call
                }
            }

            up.newPasswordPrompt();
            String newPassword = input.nextLine();
            if (!newPassword.toUpperCase().equals(up.getCancelString())) {
                while (newPassword.length() < passwordLengthLimit) {
                    up.invalidNewPasswordMessage();
                    newPassword = input.nextLine();
                    if (newPassword.toUpperCase().equals(up.getCancelString())) {
                        return;  // this return is never intended to be used, but rather as a way to force-exit the method call
                    }
                }
                um.changePassword(userId, newPassword);
                up.updateSuccessMessage("password");
            }
        }
        ug.updateUserInfoCsv(um, isDemo);  // update User.csv file
    }

    private void viewProfile(String userId) {
        ArrayList<String> userProfile = new ArrayList<>();

        userProfile.add(um.usernameByUserId(userId));
        userProfile.add(userId);
        userProfile.add("" + um.statusByUserId(userId));
        userProfile.add("" + um.pointsByUserId(userId));
        up.displayProfile(userProfile);
    }

    private void requestUnfreeze(String userId) {
        boolean requestSent = um.unfreezeRequest(userId);
        if (requestSent) {
            up.unfreezeRequestSentMessage();
        } else {
            up.unfreezeRequestAlreadySentMessage();
        }
        ug.updateUserInfoCsv(um, isDemo);
    }
}

