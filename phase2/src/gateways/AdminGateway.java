package gateways;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import entities.Admin;
import usecases.AdminManager;

/**
 * The Gateway that reads/writes Admin data
 */
public class AdminGateway extends Gateway {
    public HashMap<String, ArrayList<String>> adminInfo;


    /**
     * Instantiates an AdminGateway instance
     */
    public AdminGateway() {
        this.adminInfo = new HashMap<>();
    }

    /**
     * Gets an array list of admin from database/Admin.csv
     * @return an array list of admin from database/Admin.csv
     */
    public ArrayList<Admin> getAdminInfoCSV() {
        /* Scan the csv file */
        Scanner adminScanner = getSheetByFileName("database/Admin.csv");
        /* skip the first line of csv file(title) */
        adminScanner.nextLine();
        /* Set up the delimiter as ',' */
        String delimiter1 = ",";
        while (adminScanner.hasNext()){
            /* slice the string with delimiter to string list*/
            String[] record = adminScanner.nextLine().split(delimiter1);
            ArrayList<String> info = new ArrayList<>();
            /* add username */
            info.add(record[1]);
            /* add password */
            info.add(record[2]);
            /* using UUID as key and push to the HashMap */
            this.adminInfo.put(record[0], info);
        }
        return convertToAdmin(this.adminInfo);
    }

    /**
     * Converts the HashMap of admin (loaded from csv) into an ArrayList of admin objects
     * @param adminInfo a hash map that maps a admin's id to the admin object
     * @return an array list of admin
     */
    public ArrayList<Admin> convertToAdmin(HashMap<String, ArrayList<String>> adminInfo) {
        ArrayList<Admin> finalList = new ArrayList<>();
        ArrayList<String> listTemp;
        for (Map.Entry<String, ArrayList<String>> temp : adminInfo.entrySet()){
            listTemp = temp.getValue();
            /* Initialize a Admin object and import the information from csv with information of name and description*/
            Admin ad = new Admin(listTemp.get(0), listTemp.get(1));
            /* set the id of the Admin */
            ad.setId(temp.getKey());
            /* Add all Admin objects to a ArrayList */
            finalList.add(ad);
        }
        return finalList;
    }

    /**
     * Uploads the latest information contained in AdminManager into the csv file
     * @param adm the admin manager in the system
     */
    public void updateAdminInfoCsv(AdminManager adm) {
        try (PrintWriter writer = new PrintWriter(new File("src/database/Admin.csv"))) {
            StringBuilder sb = new StringBuilder();
            //ArrayList listTemp;

            sb.append("UUID,");
            sb.append("USERNAME,");
            sb.append("PASSWORD,");
            sb.append("\n");

            //ArrayList adminList = adm.getAdminList();
            HashMap<String, Admin> adminIdToAdmin = adm.getAdminIdToAdmin();
            for (HashMap.Entry<String, Admin> entry: adminIdToAdmin.entrySet()){
                Admin ad = entry.getValue();
                sb.append(ad.getId()).append(','); // changed from sb.append(ad.getId() + ',');
                sb.append(ad.getUsername()).append(','); // changed from sb.append(ad.getUsername() + ',');
                sb.append(ad.getPassword()).append(','); //changed from sb.append(ad.getPassword() + ',');
                sb.append("\n");
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns the log in info of all Admins
     * @return a HashMap where each key is an Admin's username, and each value is a (current password, user ID) pair
     */
    public HashMap<String, ArrayList<String>> getLoginInfo() {
        return super.getLoginInfo(true);
    }
}
