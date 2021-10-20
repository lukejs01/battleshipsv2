package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Checker {
    private List<String> horizontalBoarder = new ArrayList<>(Arrays.asList("A","B","C","D","E","F","G","H","I","J"));
    private List<String> verticalBoarder = new ArrayList<>(Arrays.asList("0","1","2","3","4","5","6","7","8","9"));

    public boolean coordinatesCheckFor4(String toCheck){
        int stateCheck = 0;
        if (toCheck.length() == 4){
            String id = Character.toString(toCheck.charAt(0));
            String positionH = Character.toString(toCheck.charAt(1));
            String positionV = Character.toString(toCheck.charAt(2));
            String alignment = Character.toString(toCheck.charAt(3));

            for (String digit: verticalBoarder) {
                if (digit.equals(id)){
                    stateCheck++;
                }
            }
            if (stateCheck == 1){
                for (String letter: horizontalBoarder) {
                    if (letter.equals(positionH)){
                        stateCheck++;
                    }
                }
            }
            if (stateCheck == 2){
                for (String digit: verticalBoarder) {
                    if (digit.equals(positionV)){
                        stateCheck++;
                    }
                }
            }
            if (stateCheck == 3){
                if (alignment.equals("H") || alignment.equals("V")){
                    return true;
                }
            }
        }

        return false;
    }

    public int coordinatesCheckFor5(String toCheck){
        // for this function the return value will be used as the state for the check called
        // 0 = false/ invalid input
        // 1 = input format AA1
        // 2 = input format A11
        int stateCheck = 0;
        int unknownIdentifier = 0;
        if (toCheck.length() == 4){
            String id = Character.toString(toCheck.charAt(0));
            String positionH = Character.toString(toCheck.charAt(1));
            String positionV = Character.toString(toCheck.charAt(2));
            String positionUnknown = Character.toString(toCheck.charAt(3));
            String alignment = Character.toString(toCheck.charAt(4));

            for (String digit: verticalBoarder) {
                if (digit.equals(id)){
                    stateCheck++;
                }
            }
            if (stateCheck == 1){
                if (alignment.equals("H") || alignment.equals("V")){
                    stateCheck++;
                }
            }
            if (stateCheck == 2){
                for (String letter: horizontalBoarder) {
                    if (letter.equals(positionH)){
                        stateCheck++;
                    }
                }
            }
            if (stateCheck == 3){
                for (String letter: verticalBoarder) {
                    if (letter.equals(positionV)){
                        stateCheck++;
                    }
                }
            }

            if (stateCheck == 4){
                for (String digit: verticalBoarder) {
                    if (digit.equals(positionUnknown)){
                        unknownIdentifier = 2;
                    }
                }
                for (String letter: horizontalBoarder) {
                    if (letter.equals(positionUnknown)){
                        unknownIdentifier = 1;
                    }
                }
            }

        }

        return unknownIdentifier;
    }
}
