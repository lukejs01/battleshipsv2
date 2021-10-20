package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {

    public FileReader() throws FileNotFoundException {
    }



    public List<String> readFile() throws FileNotFoundException {
        File file = new File("./src/config.txt");
        Scanner scanner = new Scanner(file);
        String data = "";
        List<String> dataList = new ArrayList<>();
        while (scanner.hasNextLine()){
            data = scanner.nextLine();
            dataList.add(data);
        }
        return dataList;
    }




}
