package gateways;

import entities.Meeting;
import usecases.MeetingManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The Gateway that reads/writes Meeting data
 */
public class MeetingGateway extends Gateway {

    public HashMap<String, ArrayList<ArrayList<String>>> meetInfo;

    /**
     * Instantiates a MeetingGateway instance
     */
    public MeetingGateway() {
        this.meetInfo = new HashMap<>();
    }

    /**
     * Gets an array list of meetings from database/Meeting.csv
     * @return an array list of meetings from database/Meeting.csv
     */
    public ArrayList<Meeting> getMeetInfoCsv() {
        Scanner meetScanner = getSheetByFileName("database/Meeting.csv");
        String delimiter1 = ",";
        String delimiter2 = "/";
        meetScanner.nextLine();
        while (meetScanner.hasNext()){
            String[] record = meetScanner.nextLine().split(delimiter1);
            ArrayList<ArrayList<String>> info = new ArrayList<>();
            ArrayList<String> time = new ArrayList<>();
            ArrayList<String> place = new ArrayList<>();
            ArrayList<String> usersTurn = new ArrayList<>();
            ArrayList<String> usersLastEdited = new ArrayList<>();
            ArrayList<String> userEditCount = new ArrayList<>();

            time.add(record[1]);
            place.add(record[2]);

            String[] turn = record[3].split(delimiter2);
            for (String i : turn){
                if (i.length() > 0) {
                    usersTurn.add(i);
                }
            }

            String[] lastEdited = record[4].split(delimiter2);
            for (String i : lastEdited){
                if (i.length() > 0) {
                    usersLastEdited.add(i);
                }
            }

            String[] edit = record[5].split(delimiter2);
            for (String i : edit){
                if (i.length() > 0) {
                    userEditCount.add(i);
                }
            }

            info.add(time);
            info.add(place);
            info.add(usersTurn);
            info.add(usersLastEdited);
            info.add(userEditCount);

            this.meetInfo.put(record[0], info);

        }
        return convertToMeeting(this.meetInfo);
    }

    /**
     * Converts the HashMap of meetings (loaded from csv) into an ArrayList of meeting objects
     * @param meetingInfo a hash map that maps a meeting's id to the meeting object
     * @return an array list of meetings
     */
    public ArrayList<Meeting> convertToMeeting(HashMap<String, ArrayList<ArrayList<String>>> meetingInfo) {
        ArrayList<Meeting> finalList = new ArrayList<>();
        ArrayList<ArrayList<String>> listTemp;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Map.Entry<String, ArrayList<ArrayList<String>>> temp : meetingInfo.entrySet()){

            listTemp = temp.getValue();  // {time, place, usersTurn, usersLastEdited, userEditCount}

            Meeting mt = new Meeting(LocalDate.parse((listTemp.get(0)).get(0), formatter), (listTemp.get(1)).get(0), listTemp.get(3).get(0)); //3rd parameter removed?
            mt.setId(temp.getKey());
            List<Boolean> booleans = new ArrayList<>();
            List<Integer> ints = new ArrayList<>();

            for (String x: listTemp.get(2)){  // getting usersTurns
                booleans.add(Boolean.parseBoolean(x));
            }

            boolean[] ss = new boolean[2];
            ss[0] = booleans.get(0);
            ss[1] = booleans.get(1);

            mt.setUserLastEdited(listTemp.get(3).get(0));  // getting userLastEdited

            for (String y: listTemp.get(4)){  // getting usersEditCount
                ints.add(Integer.parseInt(y));
            }
            int[] tt = new int[2];
            tt[0] = ints.get(0);
            tt[1] = ints.get(1);

            mt.setUsersTurns(ss);
            mt.setUsersEditCount(tt);

            finalList.add(mt);

        }
        return finalList;
    }


    /**
     * Uploads the latest information contained in MeetingManager into the csv file
     * @param mm the meeting manager in this system
     */
    public void updateMeetInfoCsv(MeetingManager mm, boolean isDemo) {
        if (!isDemo) {
            try (PrintWriter writer = new PrintWriter(new File("src/database/Meeting.csv"))) {
                StringBuilder sb = new StringBuilder();
                //ArrayList listTemp;

                sb.append("MEETING ID,");
                sb.append("TIME,");
                sb.append("PLACE,");
                sb.append("USER'S TURN,");
                sb.append("USER LAST EDITED,");
                sb.append("USER EDIT COUNT");
                sb.append("\n");

                Map<String, Meeting> meetList = mm.getMeetingIdToMeeting();
                for (Map.Entry<String, Meeting> entry : meetList.entrySet()){
                    Meeting mt = entry.getValue();
                    sb.append(mt.getMeetingId()).append(',');
                    sb.append(mt.getTime().toString()).append(',');
                    sb.append(mt.getPlace()).append(',');
                    sb.append((mt.getUsersTurn()[0])).append('/').append(mt.getUsersTurn()[1]).append(',');
                    sb.append(mt.getUserLastEdited()).append(',');
                    sb.append(mt.getUsersEditCount()[0]).append('/').append(mt.getUsersEditCount()[1]).append(',');

                    sb.append("\n");
                }

                writer.write(sb.toString());
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}