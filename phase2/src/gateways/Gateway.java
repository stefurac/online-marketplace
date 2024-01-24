package gateways;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


/**
 * The parent Gateway class from which all gateway classes inherit
 */
public class Gateway {
    public ArrayList<String> gateList;

    /**
     * Instantiates a Gateway instance
     */
    public Gateway() {
        this.gateList = new ArrayList<>();
    }

    /**
     * @param sheetName a String that represents the name of the csv file
     * @return the scanner object...
     */
    public Scanner getSheetByFileName(String sheetName) {
        //catch If you have method which connects to resource (eg opens file/network)
        //throw if class higher in hierarchy needs information about error.
        Scanner result = null;
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        String pathName = currentWorkingDir.normalize().toString();
        try {
            switch (sheetName) {
                case "database/User.csv": {
                    //File myObj = new File("src/database/User.csv");
                    File myObj = new File(pathName + "/src/database/User.csv");
                    result = new Scanner(myObj);
                    break;
                }
                case "database/Item.csv": {
                    //File myObj = new File("src/database/Item.csv");
                    File myObj = new File(pathName + "/src/database/Item.csv");
                    result = new Scanner(myObj);
                    break;
                }
                case "database/Admin.csv": {
                    //File myObj = new File("src/database/Admin.csv");
                    File myObj = new File(pathName + "/src/database/Admin.csv");
                    result = new Scanner(myObj);
                    break;
                }
                case "database/Meeting.csv": {
                    //File myObj = new File("src/database/Meeting.csv");
                    File myObj = new File(pathName + "/src/database/Meeting.csv");
                    result = new Scanner(myObj);
                    break;
                }
                case "database/Threshold.csv": {
                    //File myObj = new File("src/database/Threshold.csv");
                    File myObj = new File(pathName + "/src/database/Threshold.csv");
                    result = new Scanner(myObj);
                    break;
                }
                default: {
                    //File myObj = new File("src/database/Trade.csv");
                    File myObj = new File(pathName + "/src/database/Trade.csv");
                    result = new Scanner(myObj);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An Error Occurred.");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Returns the log in info of an Admin or a User; to be extended in and called from subclasses
     * @return a HashMap where each key is an Admin's or a User's username, and each value is a (current password, user ID) pair
     */
    public HashMap<String, ArrayList<String>> getLoginInfo(boolean isAdmin) {  // returns a dictionary where each key is a User's username, and each value is a (current password, user ID) pair
        HashMap<String, ArrayList<String>> usernamesToPasswordsIds = new HashMap<>();
        String delimiter1 = ",";

        Scanner scanner;
        if (isAdmin) {
            scanner = getSheetByFileName("database/Admin.csv");
        } else {
            scanner = getSheetByFileName("database/User.csv");
        }

        scanner.nextLine();
        while (scanner.hasNext()) {
            String[] record = scanner.nextLine().split(delimiter1);
            ArrayList<String> passwordId = new ArrayList<>();
            passwordId.add(record[2]);  // password
            passwordId.add(record[0]);  // ID
            usernamesToPasswordsIds.put(record[1], passwordId);
        }
        return usernamesToPasswordsIds;
    }
}