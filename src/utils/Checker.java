package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Checker {
    private List<String> horizontalBoarder = new ArrayList<>(Arrays.asList("A","B","C","D","E","F","G","H","I","J")); // used to check against inputs
    private List<String> verticalBoarder = new ArrayList<>(Arrays.asList("0","1","2","3","4","5","6","7","8","9"));

    public boolean coordinatesCheckFor4(String toCheck){ // checker for adding ship with input size 4
        int stateCheck = 0;
        if (toCheck.length() == 4){
            String id = Character.toString(toCheck.charAt(0)); // splits the string in to separate variables
            String positionH = Character.toString(toCheck.charAt(1));
            String positionV = Character.toString(toCheck.charAt(2));
            String alignment = Character.toString(toCheck.charAt(3));

            for (String digit: verticalBoarder) { // loops through the list seen above
                if (digit.equals(id)){
                    stateCheck++; // if they match the variable then state will be updated
                    break;
                }
            }
            if (stateCheck == 1){
                for (String letter: horizontalBoarder) {
                    if (letter.equals(positionH)){
                        stateCheck++;
                        break;
                    }
                }
            }
            if (stateCheck == 2){
                for (String digit: verticalBoarder) {
                    if (digit.equals(positionV)){
                        stateCheck++;
                        break;
                    }
                }
            }
            if (stateCheck == 3){
                if (alignment.equals("H") || alignment.equals("V")){
                    return true; // if all of them match then the function returns true meaning that the input is valid

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
        // still the same principal as the one above
        int stateCheck = 0;
        int unknownIdentifier = 0;
        if (toCheck.length() == 5){
            String id = Character.toString(toCheck.charAt(0));
            String positionH = Character.toString(toCheck.charAt(1));
            String positionUnknown = Character.toString(toCheck.charAt(2));
            String positionV = Character.toString(toCheck.charAt(3));
            String alignment = Character.toString(toCheck.charAt(4));

            for (String digit: verticalBoarder) {
                if (digit.equals(id)){
                    stateCheck++;
                    break;
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
                        break;
                    }
                }
            }
            if (stateCheck == 3){
                for (String letter: verticalBoarder) {
                    if (letter.equals(positionV)){
                        stateCheck++;
                        break;
                    }
                }
            }

            if (stateCheck == 4){
                for (String digit: verticalBoarder) {
                    if (digit.equals(positionUnknown)) {
                        unknownIdentifier = 2;
                        break;
                    }
                }
                for (String letter: horizontalBoarder) {
                    if (letter.equals(positionUnknown)) {
                        unknownIdentifier = 1;
                        break;
                    }
                }
            }

        }

        return unknownIdentifier;
    }

    public boolean coordinatesCheckFor6(String toCheck){
//        same as the two functions above
        int stateCheck = 0;
        if (toCheck.length() == 6){
            String id = Character.toString(toCheck.charAt(0));
            String positionH = Character.toString(toCheck.charAt(1));
            String positionH2 = Character.toString(toCheck.charAt(2));
            String positionV2 = Character.toString(toCheck.charAt(3));
            String positionV = Character.toString(toCheck.charAt(4));
            String alignment = Character.toString(toCheck.charAt(5));
            for (String digit: verticalBoarder) {
                if (digit.equals(id)){
                    stateCheck++;
                    break;
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
                        break;
                    }
                }
            }
            if (stateCheck == 3){
                for (String letter: verticalBoarder) {
                    if (letter.equals(positionV)){
                        stateCheck++;
                        break;
                    }
                }
            }
            if (stateCheck == 4){
                for (String letter: horizontalBoarder) {
                    if (letter.equals(positionH2)) {
                        stateCheck++;
                        break;
                    }
                }
            }
            if (stateCheck == 5){
                for (String digit: verticalBoarder){
                    if (digit.equals(positionV2)){
                        return true;
                    }
                }
            }

        }
        return false;
    }

    // to check if a co-ordinate for shooting at a ship is valid
    public boolean shootCheckFor2(String toCheck){ // the co-ordinates as passed
        // still the same principal as the ones above
        // loops through to check whether the characters match, if so return true
        String positionH = Character.toString(toCheck.charAt(0));
        String positionV = Character.toString(toCheck.charAt(1));
        int stateCheck = 0;

        if (toCheck.length() == 2){
            for (String letter: horizontalBoarder){
                if (letter.equals(positionH)){
                    stateCheck++;
                    break;
                }
            }
            if (stateCheck == 1){
                for (String digit: verticalBoarder){
                    if (digit.equals(positionV)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public int shootCheckFor3(String toCheck){
        // same as above but returns an int
        // the int is associated with the format of the user input
        // 0 for incorrect
        // 1 for A11
        // 2 for AA1
        int stateCheck = 0;
        int unknownIdentifier = 0;
        if (toCheck.length() == 3){
            String positionH = Character.toString(toCheck.charAt(0));
            String positionUnknown = Character.toString(toCheck.charAt(1));
            String positionV = Character.toString(toCheck.charAt(2));

            for (String letter: horizontalBoarder) {
                if (letter.equals(positionH)){
                    stateCheck++;
                    break;
                }
            }
            if (stateCheck == 1){
                for (String letter: verticalBoarder) {
                    if (letter.equals(positionV)){
                        stateCheck++;
                        break;
                    }
                }
            }

            if (stateCheck == 2){
                for (String digit: verticalBoarder) {
                    if (digit.equals(positionUnknown)) {
                        unknownIdentifier = 2;
                        break;
                    }
                }
                for (String letter: horizontalBoarder) {
                    if (letter.equals(positionUnknown)) {
                        unknownIdentifier = 1;
                        break;
                    }
                }
            }

        }

        return unknownIdentifier;
    }

    public boolean shootCheckFor4(String toCheck){
        // same as above to functions but an extra check for the extra character
        String positionH = Character.toString(toCheck.charAt(0));
        String positionH2 = Character.toString(toCheck.charAt(1));
        String positionV = Character.toString(toCheck.charAt(2));
        String positionV2 = Character.toString(toCheck.charAt(3));
        int stateCheck = 0;

        if (toCheck.length() == 4){
            for (String letter: horizontalBoarder) {
                if (letter.equals(positionH)){
                    stateCheck++;
                    break;
                }
            }
            if (stateCheck == 1){
                for (String letter: horizontalBoarder) {
                    if (letter.equals(positionH2)){
                        stateCheck++;
                        break;
                    }
                }
            }
            if (stateCheck == 2){
                for (String digit: verticalBoarder) {
                    if (digit.equals(positionV)){
                        stateCheck++;
                        break;
                    }
                }
            }
            if (stateCheck == 3){
                for (String digit: verticalBoarder) {
                    if (digit.equals(positionV2)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
