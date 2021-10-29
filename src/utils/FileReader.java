package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {

    public FileReader() throws FileNotFoundException {
    }



    public List<String> readFile() throws FileNotFoundException { // this reads the file line by line
        File file = new File("./src/config.txt"); // gets file and stores it as an object
        Scanner scanner = new Scanner(file);
        String data = "";
        List<String> dataList = new ArrayList<>();
        while (scanner.hasNextLine()){ // reads line by line using a loop
            data = scanner.nextLine(); // adds that line to a list of strings so it is easier to manipulate the data
            dataList.add(data);
        }
        return dataList;
    }




}
