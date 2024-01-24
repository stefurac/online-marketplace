package gateways;

import entities.Trade;
import entities.TradeStatus;
import usecases.TradeManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The Gateway that reads/writes Trade data
 */
public class TradeGateway extends Gateway {

    public HashMap<String, ArrayList<ArrayList<String>>> tradeInfo;

    /**
     * Instantiates a TradeGateway instance
     */
    public TradeGateway() {
        this.tradeInfo = new HashMap<>();
    }

    /**
     * Gets a list of threshold from database/Threshold.csv
     * @return a list of threshold from database/Threshold.csv
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

        return thresholdInfo;
    }

    /**
     * Gets an array list of trades from database/Trade.csv
     * @return an array list of trades from database/Trade.csv
     */
    public ArrayList<Trade> getTradeInfoCsv() {
        Scanner tradeScanner = getSheetByFileName("database/Trade.csv");
        String delimiter1 = ",";
        String delimiter2 = "/";
        tradeScanner.nextLine();
        while (tradeScanner.hasNext()){
            String[] record = tradeScanner.nextLine().split(delimiter1);
            ArrayList<ArrayList<String>> info = new ArrayList<>();
            ArrayList<String> status = new ArrayList<>();
            ArrayList<String> duration = new ArrayList<>();
            ArrayList<String> meetingId = new ArrayList<>();
            ArrayList<String> requester = new ArrayList<>();
            ArrayList<String> receiver = new ArrayList<>();
            ArrayList<String> localDate = new ArrayList<>();
            ArrayList<String> items = new ArrayList<>();

            status.add(record[1]);
            duration.add(record[2]);
            meetingId.add(record[3]);
            requester.add(record[4]);
            receiver.add(record[5]);
            localDate.add(record[6]);

            String[] item = record[7].split(delimiter2);
            for (String i : item){
                if (i.length() > 0) {
                    items.add(i);
                }
            }

            info.add(status);
            info.add(duration);
            info.add(meetingId);
            info.add(requester);
            info.add(receiver);
            info.add(localDate);
            info.add(items);

            this.tradeInfo.put(record[0], info);

        }
        return convertToTrade(this.tradeInfo);
    }

    /**
     * Converts the HashMap of trades (loaded from csv) into an ArrayList of trade objects
     * @param tradeInfo a hash map that maps a trade's id to the trade object
     * @return an array list of trades
     */
    public ArrayList<Trade> convertToTrade(HashMap<String, ArrayList<ArrayList<String>>> tradeInfo) {
        ArrayList<Trade> finalList = new ArrayList<>();
        ArrayList<ArrayList<String>> listTemp;

        for (Map.Entry<String, ArrayList<ArrayList<String>>> temp : tradeInfo.entrySet()){

            listTemp = temp.getValue();

            Trade tr = new Trade((listTemp.get(3)).get(0), (listTemp.get(4)).get(0), listTemp.get(6));
            tr.setStatus(TradeStatus.valueOf((listTemp.get(0)).get(0)));
            tr.setDuration(Integer.parseInt((listTemp.get(1)).get(0)));
            tr.setMeetingId((listTemp.get(2)).get(0));
            tr.setRequester((listTemp.get(3)).get(0));
            tr.setReceiver((listTemp.get(4)).get(0));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            tr.setDateCreated((LocalDate.parse((listTemp.get(5)).get(0), formatter)));

            tr.setTradeId(temp.getKey());

            finalList.add(tr);

        }
        return finalList;
    }

    /**
     * Uploads the latest information contained in TradeManager into the csv file
     * @param tm the trade manager in this system
     */
    public void updateTradeInfoCsv(TradeManager tm, boolean isDemo) {
        if (!isDemo) {
            try (PrintWriter writer = new PrintWriter(new File("src/database/Trade.csv"))) {
                StringBuilder sb = new StringBuilder();
                //ArrayList listTemp;

                sb.append("TRADE ID,");
                sb.append("STATUS,");
                sb.append("DURATION,");
                sb.append("MEETING ID,");
                sb.append("REQUESTER,");
                sb.append("RECEIVER,");
                sb.append("LOCAL DATE,");
                sb.append("ITEMS");
                sb.append("\n");

                HashMap<String, Trade> tradeList = tm.getTradeIdToTrade();
                for (HashMap.Entry<String, Trade> entry : tradeList.entrySet()){
                    Trade tr = entry.getValue();
                    sb.append(tr.getTradeId()).append(',');
                    sb.append(tr.getStatus().toString()).append(',');
                    sb.append((tr.getDuration())).append(','); //String.valueOf
                    sb.append(tr.getMeetingId()).append(',');
                    sb.append(tr.getRequester()).append(',');
                    sb.append(tr.getReceiver()).append(',');
                    sb.append(tr.getDateCreated().toString()).append(',');
                    sb.append(String.join("/", tr.getItemIds())).append(',');
                    sb.append("\n");
                }

                writer.write(sb.toString());
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}