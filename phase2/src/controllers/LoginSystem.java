package controllers;

import gateways.*;
import presenters.LoginPresenter;
import usecases.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Controls the system for registering/logging in users, and logging in admin
 */
public class LoginSystem {
    private final int passwordLengthLimit;
    private final String[] invalidString = new String[]{",", "/"};
    private final LoginPresenter lp = new LoginPresenter();
    private final UserGateway ug = new UserGateway();
    private final AdminGateway ag = new AdminGateway();
    private final ItemGateway ig = new ItemGateway();
    private final TradeGateway tg = new TradeGateway();
    private final MeetingGateway mg = new MeetingGateway();
    private final UserManager um = new UserManager(ug.getUserInfoCSV());
    private final AdminManager adm = new AdminManager(ag.getAdminInfoCSV());
    private final ItemManager im = new ItemManager(ig.getItemInfoCSV());
    private final MeetingManager mm = new MeetingManager(3, mg.getMeetInfoCsv());
    private final UserSystem us = new UserSystem(um, adm, im);
    private final AdminSystem as = new AdminSystem(adm, um, mm, im);
    private final GuestSystem gs = new GuestSystem(im, um);
    private final Scanner input = new Scanner(System.in);


    /**
     * Creates an instance of LoginSystem
     *
     */
    public LoginSystem() {
        ThresholdGateway thrG = new ThresholdGateway();
        passwordLengthLimit = thrG.getThresholdInfoCSV().get(4);
    }

    /**
     * The run() method that the main() method calls during runtime
     *
     * @throws FileNotFoundException thrown when a file with the specified pathname does not exist
     */
    public void run() throws FileNotFoundException {
        boolean systemClosed = false;
        lp.welcomeMessage(); // "Welcome to [insert system name]! Please select an option:"
        do {
            lp.options(); // "1. Register" "2. Login" "3. Exit"
            String selection = input.nextLine();
            switch (selection) {
                case "0":  // "0. Exit"
                    lp.closingMessage();
                    systemClosed = true;
                    break;
                case "1": // "1. Login"
                    //systemClosed = loginMenu();
                    loginMenu();
                    break;
                case "2": // "2. Register"
                    registerAccount();
                    break;
                case "3": // "3. Continue as Guest"
                    gs.run();
                    break;
                case "4": // "4. Demo"
                    String userId = demoId();
                    us.run(userId, true);

                default:
                    lp.invalidInputMessage();
                    break;
            }
        } while (!systemClosed);
    }

    private String demoId(){
        String username = "demo_user";
        String password = "demo_password";
        lp.demoLogin(username, password);
        um.addUser(username, password);
        return um.userIdByUsername(username);
    }

    private void loginMenu() throws FileNotFoundException {
        //boolean logOff = false;
        boolean exitMenu = false;

        do {
            //lp.selectOptionPrompt();
            lp.loginOptions(); // "1. User Login" "2. Admin Login" "3. Go Back"
            String selection = input.nextLine();
            switch (selection) {
                case "0": // "0. Go Back"
                    exitMenu = true;
                    break;
                case "2": // "2. Admin Login"
                    String adminId = login(true);
                    if (!adminId.equals("")){
                        as.run(adminId);
                        exitMenu = true;
                        //logOff = true;
                        lp.logoutMessage();
                    }
                    break;
                case "1": // "1. User Login"
                    String userId = login(false);  // can return empty String, in which case the user wishes to cancel login
                    if (!userId.equals("")) {
                        us.run(userId, false);
                        exitMenu = true;
                        //logOff = true;
                        lp.logoutMessage();  // logging off from an account session terminates the system
                    }
                    break;
                default:
                    lp.invalidInputMessage();
                    break;
            }
        }while(!exitMenu);
    }

    private String login(boolean isAdmin) throws FileNotFoundException {
        LoginManager lm;  // LM in instantiated anew every time the login() method is called
        if (isAdmin) {
            lm = new LoginManager(ag.getLoginInfo());
            lp.adminLoginMessage();
        } else {
            lm = new LoginManager(ug.getLoginInfo());
            lp.userLoginMessage();
        }

        // obtaining and checking username
        lp.enterUsernamePrompt();
        String username = input.nextLine();
        if (!username.toUpperCase().equals(lp.getCancelString())) {
            while (!lm.containsUsername(username)) { // checks if username exists in the system
                lp.usernameNotFoundPrompt();
                lp.enterUsernamePrompt();
                username = input.nextLine();
                if (username.toUpperCase().equals(lp.getCancelString())) {
                    return "";  // return of empty String indicates the user has cancelled log-in action
                }
            }

            // obtaining and checking password
            lp.enterPasswordPrompt();
            String password = input.nextLine();
            if (!password.toUpperCase().equals(lp.getCancelString())) {
                while (!lm.checkPassword(username, password)) {
                    lp.incorrectPasswordPrompt();
                    lp.enterPasswordPrompt();
                    password = input.nextLine();
                    if (password.toUpperCase().equals(lp.getCancelString())) {
                        return "";  // return of empty String indicates the user has cancelled log-in action
                    }
                }

                // displaying welcome message and returning the user ID
                lp.welcomeMessage(username);
                return lm.getUserId(username);
            }
        }
        return "";  // return of empty String indicates the user has cancelled log-in action
    }

    private void registerAccount() {
        lp.userRegistrationMessage();

        lp.newUsernamePrompt();
        String newUsername = checkUsernameAvailability();  // a loop that ensures the availability of the new username
        if (!newUsername.toUpperCase().equals(lp.getCancelString())) {
            lp.welcomeNewUserMessage(newUsername);

            lp.newPasswordPrompt(passwordLengthLimit);
            String newPassword = input.nextLine();

            if (!newPassword.toUpperCase().equals(lp.getCancelString())) {
                while (newPassword.length() < passwordLengthLimit || containsInvalidString(newPassword)) {
                    lp.invalidNewPasswordMessage();
                    newPassword = input.nextLine();
                    if (newPassword.toUpperCase().equals(lp.getCancelString())) {
                        return;  // this return is never intended to be used, but rather as a way to force-exit the method call
                    }
                }

                lp.proceedRegistrationPrompt();
                String yn = input.nextLine().toUpperCase();

                while (!yn.equals("Y") && !yn.equals("N")) {
                    lp.invalidInputMessage();
                    yn = input.nextLine().toUpperCase();
                }

                switch (yn) {
                    case "Y":
                        um.addUser(newUsername, newPassword);
                        String id = um.userIdByUsername(newUsername);
                        um.addPoints(id, 50); // + 50 points for registering
                        lp.registrationResultMessage(true);
                        break;
                    case "N":
                        lp.registrationResultMessage(false);
                        break;
                }
            }
        }
        ug.updateUserInfoCsv(um, false);  // update User.csv file
    }

    private String checkUsernameAvailability() {
        String newUsername;
        do {
            newUsername = input.nextLine();
        } while (newUsername.equals(""));  // ensures username is nonempty

        while (!um.checkUsername(newUsername) || containsInvalidString(newUsername)) {
            lp.usernameUnavailablePrompt();
            newUsername = input.nextLine();
            if (newUsername.toUpperCase().equals(lp.getCancelString())) {
                return lp.getCancelString();  // return of the cancel String indicates the user has cancelled registration action
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

}
