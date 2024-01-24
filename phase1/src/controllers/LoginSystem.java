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
    private int passwordLengthLimit;
    private String[] invalidString = new String[]{",", "/"};

    private LoginPresenter lp = new LoginPresenter();

    private final UserGateway ug = new UserGateway();
    private final AdminGateway ag = new AdminGateway();
    private final ItemGateway ig = new ItemGateway();
    private final ThresholdGateway thrG = new ThresholdGateway();

    private final UserManager um = new UserManager(ug.getUserInfoCSV());
    private final AdminManager adm = new AdminManager(ag.getAdminInfoCSV());
    private final ItemManager im = new ItemManager(ig.getItemInfoCSV());

    private UserSystem us = new UserSystem(um, adm, im);
    private AdminSystem as = new AdminSystem(adm, um);

    private Scanner input = new Scanner(System.in);


    /**
     * Creates an instance of LoginSystem
     *
     * @throws FileNotFoundException thrown when a file with the specified pathname does not exist
     */
    public LoginSystem() throws FileNotFoundException {
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
                case "3":  // "3. Exit"
                    lp.closingMessage();
                    systemClosed = true;
                    break;
                case "1": // "1. Register"
                    registerAccount();
                    break;
                case "2": // "2. Login"
                    systemClosed = loginMenu();
                    break;

                default:
                    lp.invalidInputMessage();
                    break;
            }
        } while (!systemClosed);
    }

    private boolean loginMenu() throws FileNotFoundException {
        boolean logOff = false;
        boolean exitMenu = false;

        do {
            lp.selectOptionPrompt();
            lp.loginOptions(); // "1. User Login" "2. Admin Login" "3. Go Back"
            String selection = input.nextLine();
            switch (selection) {
                case "3": // "3. Go Back"
                    exitMenu = true;
                    break;
                case "2": // "2. Admin Login"
                    String adminId = adminLogin();
                    if (!adminId.equals("")){
                        as.run(adminId);
                        exitMenu = true;
                        logOff = true;
                        lp.closingMessage();
                    }
                    break;
                case "1": // "1. User Login"
                    String userId = userLogin();  // can return empty String, in which case the user wishes to cancel login
                    if (!userId.equals("")) {
                        us.run(userId);
                        exitMenu = true;
                        logOff = true;
                        lp.closingMessage();  // logging off from an account session terminates the system
                    }
                    break;

                default:
                    lp.invalidInputMessage();

            }
        }while(!exitMenu);
        return logOff;
    }

    private String adminLogin() {
        lp.adminLoginMessage();
        lp.enterUsernamePrompt();

        String username = input.nextLine();
        if (!username.toUpperCase().equals(lp.getCancelString())) {
            while (!adm.getUsernames().contains(username)) { // checks if username exists in the system
                lp.usernameNotFoundPrompt();
                lp.enterUsernamePrompt();
                username = input.nextLine();
                if (username.toUpperCase().equals(lp.getCancelString())) {
                    return "";  // return of empty String indicates the user has cancelled log-in action
                }
            }

            lp.enterPasswordPrompt();
            String password = input.nextLine();
            if (!password.toUpperCase().equals(lp.getCancelString())) {
                while (adm.incorrectPassword(username, password)) {
                    lp.incorrectPasswordPrompt();
                    lp.enterPasswordPrompt();
                    password = input.nextLine();
                    if (password.toUpperCase().equals(lp.getCancelString())) {
                        return "";  // return of empty String indicates the user has cancelled log-in action
                    }
                }

                String userId = adm.adminIdByUsername(username);
                lp.welcomeMessage(username);
                return userId;
            }
        }
        return "";  // return of empty String indicates the user has cancelled log-in action
    }

    private String userLogin() {
        lp.userLoginMessage();
        lp.enterUsernamePrompt();

        String username = input.nextLine();
        if (!username.toUpperCase().equals(lp.getCancelString())) {
            boolean containsUsername;
            while (!um.getUsernames().contains(username)) { // checks if username exists in the system
                lp.usernameNotFoundPrompt();
                lp.enterUsernamePrompt();
                username = input.nextLine();
                if (username.toUpperCase().equals(lp.getCancelString())) {
                    return "";  // return of empty String indicates the user has cancelled log-in action
                }
            }

            lp.enterPasswordPrompt();
            String password = input.nextLine();
            if (!password.toUpperCase().equals(lp.getCancelString())) {
                while (!um.checkPassword(username, password)) {
                    lp.incorrectPasswordPrompt();
                    lp.enterPasswordPrompt();
                    password = input.nextLine();
                    if (password.toUpperCase().equals(lp.getCancelString())) {
                        return "";  // return of empty String indicates the user has cancelled log-in action
                    }
                }

                String userId = um.userIdByUsername(username);
                lp.welcomeMessage(username);
                return userId;
            }
        }
        return "";  // return of empty String indicates the user has cancelled log-in action
    }

    private String registerAccount() {
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
                        return "";  // this return is never intended to be used, but rather as a way to force-exit the method call
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
                        lp.registrationResultMessage(true);
                        break;
                    case "N":
                        lp.registrationResultMessage(false);
                        break;
                }
            }
        }
        ug.updateUserInfoCsv(um);  // update User.csv file
        return "";  // this return is never intended to be used
    }

    private String checkUsernameAvailability() {
        String newUsername = input.nextLine();
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
