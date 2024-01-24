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

public class AdminGateway extends Gateway {
    public HashMap<String, ArrayList<String>> adminInfo;


    public AdminGateway() {
        this.adminInfo = new HashMap<>();
    }

    /**
     *
     * @return getting data from csv file to ArrayList of Admins
     * @throws FileNotFoundException
     */
    public ArrayList<Admin> getAdminInfoCSV() throws FileNotFoundException {
        /* Scan the csv file */
        Scanner itemScanner = getSheetByFileName("database/Admin.csv");
        /* skip the first line of csv file(title) */
        itemScanner.nextLine();
        /* Set up the delimiter as ',' */
        String delimiter1 = ",";
        while (itemScanner.hasNext()){
            /* slice the string with delimiter to string list*/
            String[] record = itemScanner.nextLine().split(delimiter1);
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
     * This is a helper function for getAdminInfoCSV
     * Load HashMap(from csv) information to a ArrayList of Admin so that AdminManager can be instantiated with latest data
     * @param adminList that is gotten from the csv file as form of HashMap
     * @return a ArrayList of Items
     */
    public ArrayList<Admin> convertToAdmin(HashMap<String, ArrayList<String>> adminList) {
        ArrayList finalList = new ArrayList();
        ArrayList listTemp;
        for (Map.Entry temp : adminList.entrySet()){
            listTemp = (ArrayList) temp.getValue();
            /* Initialize a Admin object and import the information from csv with information of name and description*/
            Admin ad = new Admin(listTemp.get(0).toString(), listTemp.get(1).toString());
            /* set the id of the Admin */
            ad.setId(temp.getKey().toString());
            /* Add all Admin objects to a ArrayList */
            finalList.add(ad);
        }
        return finalList;
    }

    /**
     * upload the latest information contains in the AdminManager into the csv file
     * @param adm
     */
    public void updateAdminInfoCsv(AdminManager adm) {
        try (PrintWriter writer = new PrintWriter(new File("src/database/Admin.csv"))) {
            StringBuilder sb = new StringBuilder();
            ArrayList listTemp;

            sb.append("UUID,");
            sb.append("USERNAME,");
            sb.append("PASSWORD,");
            sb.append("\n");

            ArrayList adminList = adm.getAdminList();
            for (int i = 0; i < adminList.size(); i++){
                Admin ad = (Admin) adminList.get(i);
                sb.append(ad.getId() + ',');
                sb.append(ad.getUsername() + ',');
                sb.append(ad.getPassword() + ',');
                sb.append("\n");
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
