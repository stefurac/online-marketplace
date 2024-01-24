package gateways;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import entities.Item;
import entities.ItemStatus;
import usecases.ItemManager;


/**
 * The Gateway that reads/writes Item data
 */
public class ItemGateway extends Gateway {
    public HashMap<String, ArrayList<String>> itemInfo;

    /**
     * Instantiates an ItemGateway instance
     */
    public ItemGateway() {
        this.itemInfo = new HashMap<>();
    }

    /**
     * Gets an array list of items from database/Item.csv
     * @return an array list of items from database/Item.csv
     */
    public ArrayList<Item> getItemInfoCSV() {
        /* Scan the csv file */
        Scanner itemScanner = getSheetByFileName("database/Item.csv");
        /* skip the first line of csv file(title) */
        itemScanner.nextLine();
        /* Set up the delimiter as ',' */
        String delimiter1 = ",";
        while (itemScanner.hasNext()){
            /* slice the string with delimiter to string list*/
            String[] record = itemScanner.nextLine().split(delimiter1);
            ArrayList<String> info = new ArrayList<>();
            /* add Name */
            info.add(record[1]);
            /* add Description */
            info.add(record[2]);
            /* add Status */
            info.add(record[3]);
            /* using UUID as key and push to the HashMap */
            this.itemInfo.put(record[0], info);
        }
        return convertToItem(this.itemInfo);
    }

    /**
     * Converts the HashMap of users (loaded from csv) into an ArrayList of item objects
     * @param itemInfo a hash map that maps a item's id to the item object
     * @return an array list of users
     */
    public ArrayList<Item> convertToItem(HashMap<String, ArrayList<String>> itemInfo) {
        ArrayList<Item> finalList = new ArrayList<>();
        ArrayList<String> listTemp;
        for (Map.Entry<String, ArrayList<String>> temp : itemInfo.entrySet()){
            listTemp = temp.getValue();
            /* Initialize a Item object and import the information from csv with information of name and description*/
            Item it = new Item(listTemp.get(0), listTemp.get(1));
            /* String process to Enum, and set status */
            it.setStatus(ItemStatus.valueOf(listTemp.get(2)));
            /* set the id of the item */
            it.setId(temp.getKey());
            /* Add all item objects to a ArrayList */
            finalList.add(it);
        }
        return finalList;
    }

    /**
     * Uploads the latest information contained in ItemManager into the csv file
     * @param im the item manager in the system
     */
    public void updateItemInfoCsv(ItemManager im, boolean isDemo) {
        if (!isDemo){
            this.convertToHash(im);
            try (PrintWriter writer = new PrintWriter(new File("src/database/item.csv"))) {
                StringBuilder sb = new StringBuilder();
                ArrayList<String> listTemp;

                sb.append("UUID,");
                sb.append("NAME,");
                sb.append("DESCRIPTION,");
                sb.append("STATUS");
                sb.append("\n");

                for (Map.Entry<String, ArrayList<String>> temp : this.itemInfo.entrySet()){
                    sb.append(temp.getKey()).append(',');
                    listTemp = temp.getValue();
                    sb.append(listTemp.get(0)).append(',');
                    sb.append(listTemp.get(1)).append(',');
                    sb.append(listTemp.get(2));
                    sb.append("\n");
                }

                writer.write(sb.toString());
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Convert ItemManager info to HashMap
     * @param im the item manager in the system
     */
    public void convertToHash(ItemManager im) {
        ArrayList<Item> hash = im.itemList();
        /* clean the original data*/
        this.itemInfo.clear();
        for (Item item : hash) {
            ArrayList<String> info = new ArrayList<>();
            /* put the item name */
            info.add(item.getName());
            /* put the item description */
            info.add(item.getDescription());
            /* put the item status */
            info.add(item.getStatus().toString());
            /* upload the latest information to a HashMap */
            this.itemInfo.put(item.getId(), info);
//          alternative code:
//            while (i <  hash.size()) {
//                ArrayList<String> info = new ArrayList<>();
//                /* put the item name */
//                info.add(hash.get(i).getName());
//                /* put the item description */
//                info.add(hash.get(i).getDescription());
//                /* put the item status */
//                info.add(hash.get(i).getStatus().toString());
//                /* upload the latest information to a HashMap */
//                this.itemInfo.put(hash.get(i).getId(), info);
//                i++;
        }
    }

}

