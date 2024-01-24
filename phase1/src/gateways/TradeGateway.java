package gateways;

import entities.Trade;
import usecases.TradeManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TradeGateway extends Gateway {

    public HashMap<String, ArrayList<ArrayList<String>>> tradeInfo;

    public TradeGateway() {
        this.tradeInfo = new HashMap<>();
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

        return thresInfo;
    }

    public ArrayList<Trade> getTradeInfoCsv() throws FileNotFoundException {
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
            ArrayList<String> reciever = new ArrayList<>();
            ArrayList<String> localdate = new ArrayList<>();
            ArrayList<String> items = new ArrayList<>();

            status.add(record[1]);
            duration.add(record[2]);
            meetingId.add(record[3]);
            requester.add(record[4]);
            reciever.add(record[5]);
            localdate.add(record[6]);

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
            info.add(reciever);
            info.add(localdate);
            info.add(items);

            this.tradeInfo.put(record[0], info);

        }
        return convertToTrade(this.tradeInfo);
    }

    public ArrayList<Trade> convertToTrade(HashMap<String, ArrayList<ArrayList<String>>> tradeList) {
        ArrayList finalList = new ArrayList();
        ArrayList listTemp;

        for (Map.Entry temp : tradeList.entrySet()){

            listTemp = (ArrayList) temp.getValue();

            Trade tr = new Trade(((ArrayList)listTemp.get(3)).get(0).toString(), ((ArrayList)listTemp.get(4)).get(0).toString(), (List<String>) listTemp.get(6));
            tr.setStatus(Trade.TradeStatus.valueOf(((ArrayList)listTemp.get(0)).get(0).toString()));
            tr.setDuration(Integer.parseInt(((ArrayList)listTemp.get(1)).get(0).toString()));
            tr.setMeetingId(((ArrayList)listTemp.get(2)).get(0).toString());
            tr.setRequester(((ArrayList)listTemp.get(3)).get(0).toString());
            tr.setReceiver(((ArrayList)listTemp.get(4)).get(0).toString());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            tr.setDateCreated((LocalDate.parse(((ArrayList)listTemp.get(5)).get(0).toString(), formatter)));

            tr.setTradeId(temp.getKey().toString());

            finalList.add(tr);

        }
        return finalList;
    }

    /**
     * upload the latest information contains in the TradeManager into the csv file
     * @param tm
     */
    public void updateTradeInfoCsv(TradeManager tm) {
        try (PrintWriter writer = new PrintWriter(new File("src/database/Trade.csv"))) {
            StringBuilder sb = new StringBuilder();
            ArrayList listTemp;

            sb.append("TradeID,");
            sb.append("Status,");
            sb.append("Duration,");
            sb.append("meetingID,");
            sb.append("requester,");
            sb.append("reciever,");
            sb.append("localdate,");
            sb.append("items");
            sb.append("\n");

            HashMap<String, Trade> tradeList = tm.getTradeIdToTrade();
            for (HashMap.Entry<String, Trade> entry : tradeList.entrySet()){
                Trade tr = entry.getValue();
                sb.append(tr.getTradeId() + ',');
                sb.append(tr.getStatus().toString() + ',');
                sb.append(String.valueOf(tr.getDuration()) + ',');
                sb.append(tr.getMeetingId() + ',');
                sb.append(tr.getRequester() + ',');
                sb.append(tr.getReceiver() + ',');
                sb.append(tr.getDateCreated().toString() + ',');
                sb.append(String.join("/", tr.getItemIds()) + ',');
                sb.append("\n");
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}