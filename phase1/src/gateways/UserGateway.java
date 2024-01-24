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

public class UserGateway extends Gateway {
    private HashMap<String, ArrayList<ArrayList<String>>> userInfo;


    public UserGateway() {
        this.userInfo = new HashMap<>();
    }

    /**
     *
     * @return getting data from csv file to Arraylist
     * @throws FileNotFoundException
     */
    public ArrayList<User> getUserInfoCSV() throws FileNotFoundException {
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
            ArrayList<String> wishList = new ArrayList<>();
            ArrayList<String> status = new ArrayList<>();

            username.add(record[1]);
            password.add(record[2]);
            status.add(record[5]);

            String[] inventory = record[3].split(delimiter2);
            for (String i : inventory){
                if (i.length() > 0){
                inventoryList.add(i);
                }
            }
            String[] wish = record[4].split(delimiter2);
            for (String j : wish){
                if (j.length() > 0){
                    wishList.add(j);
                }
            }

            info.add(username);
            info.add(password);
            info.add(inventoryList);
            info.add(wishList);
            info.add(status);
            this.userInfo.put(record[0], info);
        }
        return convertToUser(this.userInfo);
    }

    /**
     * Convert the HashMap of user into the ArrayList of user objects
     * @param userList
     */
    public ArrayList<User> convertToUser(HashMap<String, ArrayList<ArrayList<String>>> userList) {
        ArrayList finalList = new ArrayList();
        ArrayList listTemp;
        for (Map.Entry temp : userList.entrySet()){
            listTemp = (ArrayList) temp.getValue();
            User us = new User(((ArrayList)listTemp.get(0)).get(0).toString(), ((ArrayList)listTemp.get(1)).get(0).toString());
            us.setInventory((ArrayList)listTemp.get(2));
            us.setWishlist((ArrayList)listTemp.get(3));
            us.setStatus(UserStatus.valueOf(((ArrayList)listTemp.get(4)).get(0).toString()));
            us.setId(temp.getKey().toString());
            finalList.add(us);
        }
        return finalList;
    }

    /**
     * upload the latest information contains in the UserManager into the csv file
     * @param um
     */
    public void updateUserInfoCsv(UserManager um) {
        try (PrintWriter writer = new PrintWriter(new File("src/database/User.csv"))) {
            StringBuilder sb = new StringBuilder();
            ArrayList listTemp;

            sb.append("UUID,");
            sb.append("USERNAME,");
            sb.append("PASSWORD,");
            sb.append("INVENTORY,");
            sb.append("WISHLIST,");
            sb.append("STATUS");
            sb.append("\n");

            HashMap<String, User> userIdToUser = um.getUserIdToUser();
            for (HashMap.Entry<String, User> entry: userIdToUser.entrySet()){
                User us = entry.getValue();
                sb.append(us.getId() + ',');
                sb.append(us.getUsername() + ',');
                sb.append(us.getPassword() + ',');
                sb.append(String.join("/", us.getInventory()) + ',');
                sb.append(String.join("/", us.getWishlist()) + ',');
                sb.append(us.getStatus().toString());
                sb.append("\n");
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
