package utils;

import model.Boat;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {


    public List<String> getBoardSize() throws FileNotFoundException {
        List<String> readFromFile;
        List<String> values = new ArrayList<>();
        FileReader fileReader = new FileReader();
        readFromFile = fileReader.readFile();

        for (String board: readFromFile) {
            if (board.contains("Board:")){
                String[] splitAtLabel = board.split(":");
                String dimension = splitAtLabel[1];
                String[] splitAtComma = dimension.split(",");
                String orientation = splitAtComma[0];
                String value = splitAtComma[1];

                values.add(orientation);
                values.add(value);

            }
        }
        return values;
    }

    public List<Boat> readBoatToList() throws FileNotFoundException {
        List<Boat> boatList = new ArrayList<>();
        List<String> readFromFile = new ArrayList<>();
        FileReader fileReader = new FileReader();
        int boatID = 0;

        readFromFile = fileReader.readFile();

        for (String boat: readFromFile) {
            if(boat.contains("Boat:")){
                String[] labelSplit = boat.split(":");
                String boatNameBeforeSplit = labelSplit[1];

                String[] boatNameSplit = boatNameBeforeSplit.split(",");
                String boatName = boatNameSplit[0];
                String length = boatNameSplit[1];

                boatList.add(new Boat(boatID,boatName,Integer.parseInt(length),Integer.parseInt(length)));
                boatID++;
            }
        }
        return boatList;
    }
}
