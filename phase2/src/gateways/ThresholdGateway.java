package gateways;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Gateway that reads/writes threshold data
 */
public class ThresholdGateway extends Gateway{

    public List<Integer> thresholdInfo;

    /**
     * Instantiates a ThresholdGateway instance
     */
    public ThresholdGateway() {
        this.thresholdInfo = new ArrayList<>();
    }

    /**
     * Gets an array list of users from database/Threshold.csv
     * @return an array list of users from database/Threshold.csv
     */
    public List<Integer> getThresholdInfoCSV() {
        List<Integer> thresholdInfo = new ArrayList<>();
        /* Scan the csv file */
        Scanner itemScanner = getSheetByFileName("database/Threshold.csv");
        /* skip the first line of csv file(title) */
        itemScanner.nextLine();
        /* Set up the delimiter as ',' */
        String delimiter1 = ",";
        /* slice the string with delimiter to string list*/
        String[] record = itemScanner.nextLine().split(delimiter1);
        /* add CancelThreshold */
        thresholdInfo.add(Integer.parseInt(record[0]));
        /* add WeeklyTradeLimit */
        thresholdInfo.add(Integer.parseInt(record[1]));
        /* add BorrowDifference */
        thresholdInfo.add(Integer.parseInt(record[2]));
        /* add EditThreshold */
        thresholdInfo.add(Integer.parseInt(record[3]));
        /* add Password limit */
        thresholdInfo.add(Integer.parseInt(record[4]));

        return thresholdInfo;
    }

    /**
     * Uploads the latest thresholds into the csv file
     * @param thresholdList the list of thresholds
     */
    public void updateThresholdInfoCsv(List<Integer> thresholdList){
        try (PrintWriter writer = new PrintWriter(new File("src/database/Threshold.csv"))) {

            String sb = "CANCEL THRESHOLD," +
                    "WEEKLY TRADE LIMIT," +
                    "BORROW DIFFERENCE," +
                    "EDIT THRESHOLD," +
                    "PASSWORD LIMIT" +
                    "\n" +
                    thresholdList.get(0).toString() + ',' +
                    thresholdList.get(1).toString() + ',' +
                    thresholdList.get(2).toString() + ',' +
                    thresholdList.get(3).toString() + ',' +
                    thresholdList.get(4).toString() +
                    "\n";
            writer.write(sb);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
