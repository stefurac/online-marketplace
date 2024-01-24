package gateways;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Gateway {
    public ArrayList<String> gateList;

    public Gateway() {
        this.gateList = new ArrayList<>();
    }

    public Scanner getSheetByFileName(String sheetName) throws FileNotFoundException {
        Scanner result = null;
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        String pathName = currentWorkingDir.normalize().toString();
        try {
            if (sheetName.equals("database/User.csv")) {
//                File myObj = new File("src/database/User.csv");
                File myObj = new File(pathName + "/src/database/User.csv");
                result = new Scanner(myObj);
            } else if(sheetName.equals("database/Item.csv")) {
//                File myObj = new File("src/database/Item.csv");
                File myObj = new File(pathName + "/src/database/Item.csv");
                result = new Scanner(myObj);
            }else if(sheetName.equals("database/Admin.csv")) {
//                File myObj = new File("src/database/Admin.csv");
                File myObj = new File(pathName + "/src/database/Admin.csv");
                result = new Scanner(myObj);
            }else if(sheetName.equals("database/Meeting.csv")){
//                File myObj = new File("src/database/Meeting.csv");
                File myObj = new File(pathName + "/src/database/Meeting.csv");
                result = new Scanner(myObj);
            }else if(sheetName.equals("database/Threshold.csv")){
//                File myObj = new File("src/database/Threshold.csv");
                File myObj = new File(pathName + "/src/database/Threshold.csv");
                result = new Scanner(myObj);
            }else{
//                File myObj = new File("src/database/Trade.csv");
                File myObj = new File(pathName + "/src/database/Trade.csv");
                result = new Scanner(myObj);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return result;
    }
}