package gateways;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import entities.User;
import entities.UserStatus;
import usecases.UserManager;

/**
 * The Gateway that reads/writes User data
 */
public class UserGateway extends Gateway {
    private final HashMap<String, ArrayList<ArrayList<String>>> userInfo;

    /**
     * Instantiates a UserGateway instance
     */
    public UserGateway() {
        this.userInfo = new HashMap<>();
    }

    /**
     * Gets an array list of users from database/User.csv
     * @return an array list of users from database/User.csv
     */
    public ArrayList<User> getUserInfoCSV() {
        Scanner userScanner = getSheetByFileName("database/User.csv");
        String delimiter1 = ",";
        String delimiter2 = "/";
        userScanner.nextLine();
        while (userScanner.hasNext()){
            String[] record = userScanner.nextLine().split(delimiter1);
            ArrayList<ArrayList<String>> info = new ArrayList<>();
            ArrayList<String> username = new ArrayList<>();
            ArrayList<String> password = new ArrayList<>();
            ArrayList<String> inventoryList = new ArrayList<>();
            ArrayList<String> wishlist = new ArrayList<>();
            ArrayList<String> status = new ArrayList<>();
            ArrayList<String> oldUsername = new ArrayList<>();
            ArrayList<String> oldPassword = new ArrayList<>();
            ArrayList<String> inventoryHistory = new ArrayList<>();
            ArrayList<String> wishlistHistory = new ArrayList<>();
            ArrayList<String> points = new ArrayList<>();
            ArrayList<String> incompleteTradeOffset = new ArrayList<>();

            username.add(record[1]);
            password.add(record[2]);
            status.add(record[5]);
            oldUsername.add(record[6]);
            oldPassword.add(record[7]);
            points.add(record[10]);
            incompleteTradeOffset.add(record[11]);


            String[] inventory = record[3].split(delimiter2);
            for (String i : inventory){
                if (i.length() > 0){
                inventoryList.add(i);
                }
            }
            String[] wish = record[4].split(delimiter2);
            for (String i : wish){
                if (i.length() > 0){
                    wishlist.add(i);
                }
            }

            String[] invHistory = record[8].split(delimiter2);
            for (String i : invHistory){
                if (i.length() > 0){
                    inventoryHistory.add(i);
                }
            }

            String[] wishHistory = record[9].split(delimiter2);
            for (String i : wishHistory){
                if (i.length() > 0){
                    wishlistHistory.add(i);
                }
            }


            info.add(username);
            info.add(password);
            info.add(inventoryList);
            info.add(wishlist);
            info.add(status);
            info.add(oldUsername);
            info.add(oldPassword);
            info.add(inventoryHistory);
            info.add(wishlistHistory);
            info.add(points);
            info.add(incompleteTradeOffset);
            this.userInfo.put(record[0], info);
        }
        return convertToUser(this.userInfo);
    }

    /**
     * Converts the HashMap of users (loaded from csv) into an ArrayList of user objects
     * @param userInfo a hash map that maps a user's id to the user object
     * @return an array list of users
     */
    public ArrayList<User> convertToUser(HashMap<String, ArrayList<ArrayList<String>>> userInfo) {
        ArrayList<User> finalList = new ArrayList<>();
        ArrayList<ArrayList<String>> listTemp;
        for (Map.Entry<String, ArrayList<ArrayList<String>>> temp : userInfo.entrySet()){
            listTemp = temp.getValue();
            User user = new User((listTemp.get(0)).get(0), (listTemp.get(1)).get(0));
            user.setInventory(listTemp.get(2));
            user.setWishlist(listTemp.get(3));
            user.setStatus(UserStatus.valueOf((listTemp.get(4)).get(0)));
            user.setId(temp.getKey());
            user.setOldUsername((listTemp.get(5)).get(0));
            user.setOldPassword((listTemp.get(6)).get(0));
            user.setInventoryHistory((listTemp.get(7)));
            user.setWishlistHistory((listTemp.get(8)));
            int i = Integer.parseInt((listTemp.get(9)).get(0));
            user.setPoints(i);
            user.setIncompleteTradeOffset(Integer.parseInt(listTemp.get(10).get(0)));
            finalList.add(user);
        }
        return finalList;
    }

    /**
     * Uploads the latest information contained in UserManager into the csv file
     * @param um the user manager in this system
     */
    public void updateUserInfoCsv(UserManager um, boolean isDemo) {
        if (!isDemo) {
            try (PrintWriter writer = new PrintWriter(new File("src/database/User.csv"))) {
                StringBuilder sb = new StringBuilder();
                //ArrayList listTemp;

                sb.append("UUID,");
                sb.append("USERNAME,");
                sb.append("PASSWORD,");
                sb.append("INVENTORY,");
                sb.append("WISHLIST,");
                sb.append("STATUS,");
                sb.append("OLD USERNAME,");
                sb.append("OLD PASSWORD,");
                sb.append("INVENTORY HISTORY,");
                sb.append("WISHLIST HISTORY,");
                sb.append("POINTS,");
                sb.append("INCOMPLETE TRADE OFFSET");
                sb.append("\n");

                HashMap<String, User> userIdToUser = um.getUserIdToUser();
                for (HashMap.Entry<String, User> entry: userIdToUser.entrySet()){
                    User user = entry.getValue();
                    sb.append(user.getId()).append(',');
                    sb.append(user.getUsername()).append(',');
                    sb.append(user.getPassword()).append(',');
                    sb.append(String.join("/", user.getInventory())).append(',');
                    sb.append(String.join("/", user.getWishlist())).append(',');
                    sb.append(user.getStatus().toString()).append(',');
                    sb.append(user.getOldUsername()).append(',');
                    sb.append(user.getOldPassword()).append(',');
                    sb.append(String.join("/", user.getInventoryHistory())).append(',');
                    sb.append(String.join("/", user.getWishlistHistory())).append(',');
                    sb.append(user.getPoints()).append(',');
                    sb.append(user.getIncompleteTradeOffset());
                    sb.append("\n");
                }

                writer.write(sb.toString());
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Returns the log in info of all Users
     * @return a HashMap where each key is a User's username, and each value is a (current password, user ID) pair
     */
    public HashMap<String, ArrayList<String>> getLoginInfo() throws FileNotFoundException {
        return super.getLoginInfo(false);
    }
}
