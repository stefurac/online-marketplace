package gateways;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ThresholdGateway extends Gateway{

    public List<Integer> thresInfo;

    public ThresholdGateway() {
        this.thresInfo = new ArrayList<>();
    }

    /**
     *
     * @return the list of the threshold number
     * @throws FileNotFoundException
     */
    public List<Integer> getThresholdInfoCSV() throws FileNotFoundException {
        List<Integer> thresInfo = new ArrayList<>();
        /* Scan the csv file */
        Scanner itemScanner = getSheetByFileName("database/Threshold.csv");
        /* skip the first line of csv file(title) */
        itemScanner.nextLine();
        /* Set up the delimiter as ',' */
        String delimiter1 = ",";
        /* slice the string with delimiter to string list*/
        String[] record = itemScanner.nextLine().split(delimiter1);
        /* add CancelThreshold */
        thresInfo.add(Integer.parseInt(record[0]));
        /* add WeeklyTradeLimit */
        thresInfo.add(Integer.parseInt(record[1]));
        /* add BorrowDifference */
        thresInfo.add(Integer.parseInt(record[2]));
        /* add EditThershold */
        thresInfo.add(Integer.parseInt(record[3]));
        /* add Password limit */
        thresInfo.add(Integer.parseInt(record[4]));

        return thresInfo;
    }

    public void updateThresholdInfoCsv(List<Integer> thresList){
        try (PrintWriter writer = new PrintWriter(new File("src/database/Threshold.csv"))) {
            StringBuilder sb = new StringBuilder();

            sb.append("CancelThreshold,");
            sb.append("WeeklyTradeLimit,");
            sb.append("BorrowDifference,");
            sb.append("EditThershold,");
            sb.append("PasswordLimit");
            sb.append("\n");

            sb.append(thresList.get(0).toString() + ',');
            sb.append(thresList.get(1).toString() + ',');
            sb.append(thresList.get(2).toString() + ',');
            sb.append(thresList.get(3).toString() + ',');
            sb.append(thresList.get(4).toString());
            sb.append("\n");

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
