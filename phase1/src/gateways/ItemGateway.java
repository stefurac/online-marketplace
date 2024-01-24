package gateways;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import entities.Item;
import usecases.ItemManager;


public class ItemGateway extends Gateway {
    public HashMap<String, ArrayList<String>> itemInfo;
    public ItemGateway() {
        this.itemInfo = new HashMap<>();
    }

    /**
     *
     * @return getting data from csv file to ArrayList of Items
     * @throws FileNotFoundException
     */
    public ArrayList<Item> getItemInfoCSV() throws FileNotFoundException {
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
     * This is a helper function for getItemInfoCSC
     * Load HashMap(from csv) information to a ArrayList of Item so that ItemManager can be instantiated with latest data
     * @param itemList that is gotten from the csv file as form of HashMap
     * @return a ArrayList of Items
     */
    public ArrayList<Item> convertToItem(HashMap<String, ArrayList<String>> itemList) {
        ArrayList finalList = new ArrayList();
        ArrayList listTemp;
        for (Map.Entry temp : itemList.entrySet()){
            listTemp = (ArrayList) temp.getValue();
            /* Initialize a Item object and import the information from csv with information of name and description*/
            Item it = new Item(listTemp.get(0).toString(), listTemp.get(1).toString());
            /* String process to Enum, and set status */
            it.setStatus(Item.ItemStatus.valueOf(listTemp.get(2).toString()));
            /* set the id of the item */
            it.setId(temp.getKey().toString());
            /* Add all item objects to a ArrayList */
            finalList.add(it);
        }
        return finalList;
    }

    /**
     * update the current HashMap of item information to the csv file
     * @param im
     */
    public void updateItemInfoCsv(ItemManager im) {
        this.convertToHash(im);
        try (PrintWriter writer = new PrintWriter(new File("src/database/item.csv"))) {
            StringBuilder sb = new StringBuilder();
            ArrayList listTemp;

            sb.append("UUID,");
            sb.append("NAME,");
            sb.append("DESCRIPTION,");
            sb.append("STATUS");
            sb.append("\n");

            for (Map.Entry temp : this.itemInfo.entrySet()){
                sb.append(temp.getKey().toString() + ',');
                listTemp = (ArrayList) temp.getValue();
                sb.append(listTemp.get(0).toString() + ',');
                sb.append(listTemp.get(1).toString() + ',');
                sb.append(listTemp.get(2).toString());
                sb.append("\n");
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Convert ItemManager info to HashMap
     * @param im
     * @return return a HashMap of the latest data info from ItemManager
     */
    public HashMap<String, ArrayList<String>> convertToHash(ItemManager im) {
        ArrayList<Item> hash = im.getAllInfo();
        /* clean the original data*/
        this.itemInfo.clear();
        for (int i = 0; i <  hash.size(); i++){
            ArrayList<String> info = new ArrayList<>();
            /* put the item name */
            info.add(hash.get(i).getName());
            /* put the item description */
            info.add(hash.get(i).getDescription());
            /* put the item status */
            info.add(hash.get(i).getStatus().toString());
            /* upload the latest information to a HashMap */
            this.itemInfo.put(hash.get(i).getId(), info);
        }
        return this.itemInfo;
    }

}

