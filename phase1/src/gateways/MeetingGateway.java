package gateways;

import entities.Meeting;
import usecases.MeetingManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MeetingGateway extends Gateway {

    public HashMap<String, ArrayList<ArrayList<String>>> meetInfo;

    public MeetingGateway() {
        this.meetInfo = new HashMap<>();
    }

    public ArrayList<Meeting> getMeetInfoCsv() throws FileNotFoundException {
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
            ArrayList<String> userEditCount = new ArrayList<>();

            time.add(record[1]);
            place.add(record[2]);

            String[] turn = record[3].split(delimiter2);
            for (String i : turn){
                if (i.length() > 0) {
                    usersTurn.add(i);
                }
            }

            String[] edit = record[4].split(delimiter2);
            for (String i : edit){
                if (i.length() > 0) {
                    userEditCount.add(i);
                }
            }

            info.add(time);
            info.add(place);
            info.add(usersTurn);
            info.add(userEditCount);

            this.meetInfo.put(record[0], info);

        }
        return convertToMeet(this.meetInfo);
    }

    public ArrayList<Meeting> convertToMeet(HashMap<String, ArrayList<ArrayList<String>>> meetList) {
        ArrayList finalList = new ArrayList();
        ArrayList listTemp;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Map.Entry temp : meetList.entrySet()){

            listTemp = (ArrayList) temp.getValue();

            Meeting mt = new Meeting(LocalDate.parse(((ArrayList)listTemp.get(0)).get(0).toString(), formatter), ((ArrayList)listTemp.get(1)).get(0).toString());
            mt.setId(temp.getKey().toString());
            List<Boolean> bools = new ArrayList<>();
            List<Integer> ints = new ArrayList<>();

            for (String x:((ArrayList<String>)listTemp.get(2))){
                bools.add(Boolean.parseBoolean(x));
            }

            boolean[] ss = new boolean[2];
            ss[0] = bools.get(0);
            ss[1] = bools.get(1);

            for (String y:((ArrayList<String>)listTemp.get(3))){
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

    public void updateMeetInfoCsv(MeetingManager mm) {
        try (PrintWriter writer = new PrintWriter(new File("src/database/Meeting.csv"))) {
            StringBuilder sb = new StringBuilder();
            ArrayList listTemp;

            sb.append("MeetingID,");
            sb.append("Time,");
            sb.append("place,");
            sb.append("usersturn,");
            sb.append("usereditcount");
            sb.append("\n");

            Map<String, Meeting> meetList = mm.getMeetingIdToMeeting();
            for (Map.Entry<String, Meeting> entry : meetList.entrySet()){
                Meeting mt = entry.getValue();
                sb.append(mt.getMeetingId() + ',');
                sb.append(mt.getTime().toString() + ',');
                sb.append(mt.getPlace() + ',');
                sb.append(String.valueOf(mt.getUsersTurn()[0]) + '/' + mt.getUsersTurn()[1] + ',');
                sb.append(String.valueOf(mt.getUsersEditCount()[0]) + '/' + mt.getUsersEditCount()[1] + ',');

                sb.append("\n");
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}