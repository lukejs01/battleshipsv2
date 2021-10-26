package model;


import utils.Checker;
import utils.FileHelper;

import java.io.FileNotFoundException;
import java.util.*;

public class Board {

    private int boardSize = 0;
    private int cellSize = 0;
    private final int DEFAULT_BOARD_SIZE = 5;
    private final int DEFAULT_CELL_SIZE = 2;



    List<List<Tile>> boardGrid = new ArrayList<>();
    List<Boat> boats = new FileHelper().readBoatToList();
    List<Boat> displayBoats = new ArrayList<>(boats);

    public int boatCheck = boats.size();


    private List<String> horizontalBoarder = new ArrayList<>(Arrays.asList("A","B","C","D","E","F","G","H","I","J"));
    private List<String> verticalBoarder = new ArrayList<>(Arrays.asList("0","1","2","3","4","5","6","7","8","9"));

    public Board() throws FileNotFoundException {
    }

    public void populateBoard() {
        for (int p = 0; p < boardSize; p++) {
            List<Tile> row = new ArrayList<>();
            for (int i = 0; i < boardSize; i++) {
                row.add(new Tile());
            }
            boardGrid.add(row);
        }
    }



    public void renderBoard(){
        int total = boardSize * cellSize;
        char c = ' ';
        int verticalBoarderCounter = 0;
        int horizontalBoarderCounter = 0;



        while (horizontalBoarderCounter != boardSize){
            if (horizontalBoarderCounter < 10){
                System.out.print(" " + horizontalBoarder.get(horizontalBoarderCounter));
                horizontalBoarderCounter++;

            } else if (horizontalBoarderCounter >= 10){
                int firstIndex = (horizontalBoarderCounter/10) - 1;
                int secondIndex = horizontalBoarderCounter%10;
                System.out.print(horizontalBoarder.get(firstIndex) + horizontalBoarder.get(secondIndex));
                horizontalBoarderCounter++;
            }
        }

        System.out.println();

        for (int i = 0; i <= total; i++){
            for (int k = 0 ; k <= total; k++){
                if (k % cellSize == 0 && i % cellSize != 0 && k == total){
                    if (verticalBoarderCounter < 10){
                        System.out.print("| " + verticalBoarder.get(verticalBoarderCounter));

                    }else if (verticalBoarderCounter >= 10){
                        int firstIndex = (verticalBoarderCounter / 10) - 1;
                        int secondIndex = verticalBoarderCounter % 10;
                        System.out.print("| " + verticalBoarder.get(firstIndex) + verticalBoarder.get(secondIndex));
                    }

                    verticalBoarderCounter++;
                } else if (k % cellSize == 0 && i % cellSize != 0) {
                    c = '|';
                } else if (i % cellSize == 0 || k % cellSize == 0) {
                    c = '-';
                }else {
                    c = boardGrid.get(i / 2).get(k / 2).state; // if c == 5 no state???? try
                }
                System.out.print(c);
                c = ' ';
            }
            System.out.println();
        }
    }

    public void setBoardDimensions() throws FileNotFoundException {
        FileHelper fileHelper = new FileHelper();
        List<String> boardSizesAsString = fileHelper.getBoardSize();
        String state = "";
        for (String s : boardSizesAsString) {
            if (s.equals("size")) {
                state = "size";
            }
            if (state.equals("size") && !s.equals("size")) {
                boardSize = Integer.parseInt(s);
                state = "";
            }
            if (s.equals("cellSize")) {
                state = "cellSize";
            }
            if (state.equals("cellSize") && !s.equals("cellSize")) {
                cellSize = Integer.parseInt(s);
                state = "";
            }
        }
        if (cellSize == 0 || boardSize == 0){
            boardSize = DEFAULT_BOARD_SIZE;
            cellSize = DEFAULT_CELL_SIZE;

        }
        if (cellSize > 5 || boardSize > 20){
            boardSize = DEFAULT_BOARD_SIZE;
            cellSize = DEFAULT_CELL_SIZE;
        }
    }

    private void displayShipToConsole(Boat boatToRemove){
        System.out.println("ID   Boat Name             Length ");


        for (Boat toCheck: displayBoats) {
            if (toCheck.equals(boatToRemove)){
                displayBoats.remove(boatToRemove);
                break;
            }
        }

        for (Boat boat: displayBoats) {
            String str = (boat.getId() + "    " + boat.getBoatName()+ "                 " );
            String output = new StringBuilder(str).insert(27,boat.getBoatSize()).toString();
            System.out.print(output);
            System.out.println();
        }
    }

    private void placeShipWithLength4(String userChoice){
            boolean isValid = new Checker().coordinatesCheckFor4(userChoice);
            if (isValid){
                boolean checkState = false;


                String id = Character.toString(userChoice.charAt(0));
                Boat boat = boats.get(Integer.parseInt(id));
                String positionH = Character.toString(userChoice.charAt(1));
                String positionV = Character.toString(userChoice.charAt(2));
                String alignment = Character.toString(userChoice.charAt(3));

                if (!boat.getPlaced()){
                    positionH = horizontalToDigitConversion(positionH);
                    if (alignment.equals("H")){
                        if (Integer.parseInt(positionH) + boat.getBoatSize() > boardSize){
                            System.out.println("Ship can't be placed there, try another co-ordinate!");
                        } else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH) + i).isHasShip()) {
                                    checkState = true;
                                    System.out.println("A boat is already in this position");
                                    break;

                                }
                            }
                            if (!checkState) {
                                displayShipToConsole(boat);
                                boatCheck = boatCheck - 1;
                                for (int j = 0; j < boat.getBoatSize(); j++) {

                                    boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH) + j).state = 'S';
                                    boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH) + j).setHasShip(true);
                                    boat.coordinatesOnBoard.add(digitToHorizontalConversion(positionH) + positionV);
                                    boat.setPlaced(true);
                                }
                            }
                        }
                    }
                    if (alignment.equals("V")){
                        if (Integer.parseInt(positionV) + boat.getBoatSize() > boardSize){
                            System.out.println("Ship can't be placed there, try another co-ordinate!");

                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
//                            positionH = horizontalToDigitConversion(positionH);
                                if (boardGrid.get(Integer.parseInt(positionV) + i).get(Integer.parseInt(positionH)).isHasShip()) {
                                    checkState = true;
                                    System.out.println("A boat is already in this position");
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                displayShipToConsole(boat);
                                for (int j = 0; j < boat.getBoatSize(); j++) {

                                    boardGrid.get(Integer.parseInt(positionV) + j).get(Integer.parseInt(positionH)).state = 'S';
                                    boardGrid.get(Integer.parseInt(positionV) + j).get(Integer.parseInt(positionH)).setHasShip(true);
                                    boat.coordinatesOnBoard.add(digitToHorizontalConversion(positionH) + positionV);
                                    boat.setPlaced(true);

                                }
                            }
                        }
                    }
                }


            } else {
                System.out.println("ERROR! Incorrect input for co-ordinates");
                }

}

    private void placeShipWithLength5(String userChoice){
            int isValid = new Checker().coordinatesCheckFor5(userChoice);
            if (isValid == 0){
                System.out.println("ERROR! invalid input");
            }
            if (isValid == 1){
                boolean checkState = false;
                String id = Character.toString(userChoice.charAt(0));
                Boat boat = boats.get(Integer.parseInt(id));
                String positionH = Character.toString(userChoice.charAt(1));
                String positionH2 = Character.toString(userChoice.charAt(2));
                String positionV = Character.toString(userChoice.charAt(3));
                String alignment = Character.toString(userChoice.charAt(4));

                if (!boat.getPlaced()){
                    positionH = horizontalToDigitConversion(positionH);
                    positionH2 = horizontalToDigitConversion(positionH2);
                    if (alignment.equals("H")){
                        if ((checkDoubleDigit(positionH,positionH2)) + boat.getBoatSize() > boardSize){
                            System.out.println("Ship can't be placed there, try another co-ordinate!");
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(Integer.parseInt(positionV)).get(checkDoubleDigit(positionH,positionH2) + i).isHasShip()) {
                                    checkState = true;
                                    System.out.println("A boat is already in this position");
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                displayShipToConsole(boat);
                                for (int j = 0; j < boat.getBoatSize(); j++){
                                    boardGrid.get(Integer.parseInt(positionV)).get(checkDoubleDigit(positionH, positionH2) + j).state = 'S';
                                    boardGrid.get(Integer.parseInt(positionV)).get(checkDoubleDigit(positionH, positionH2) + j).setHasShip(true);
                                    boat.coordinatesOnBoard.add(digitToHorizontalConversion(positionH) + digitToHorizontalConversion(positionH2) + positionV);
                                    boat.setPlaced(true);
                                }
                            }
                        }
                    }
                    if (alignment.equals("V")){
                        if (Integer.parseInt(positionV) + boat.getBoatSize() > boardSize){
                            System.out.println("Ship can't be placed there, try another co-ordinate!");
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(Integer.parseInt(positionV) + i).get(checkDoubleDigit(positionH,positionH2)).isHasShip()) {
                                    checkState = true;
                                    System.out.println("A boat is already in this position");
                                    break;
                                }
                            }
                                if (!checkState){
                                    boatCheck = boatCheck - 1;
                                    displayShipToConsole(boat);
                                    for (int j = 0; j < boat.getBoatSize(); j++){
                                        boardGrid.get(Integer.parseInt(positionV) + j).get(checkDoubleDigit(positionH,positionH2)).state = 'S';
                                        boardGrid.get(Integer.parseInt(positionV) + j).get(checkDoubleDigit(positionH, positionH2)).setHasShip(true);
                                        boat.coordinatesOnBoard.add(digitToHorizontalConversion(positionH) + digitToHorizontalConversion(positionH2) + positionV);
                                        boat.setPlaced(true);
                                    }
                                }

                        }
                    }
                }

            }else {
                System.out.println("ERROR! Incorrect input for co-ordinates");
            }

            if (isValid == 2){
                boolean checkState = false;
                String id = Character.toString(userChoice.charAt(0));
                Boat boat = boats.get(Integer.parseInt(id));
                String positionH = Character.toString(userChoice.charAt(1));
                String positionV = Character.toString(userChoice.charAt(2));
                String positionV2 = Character.toString(userChoice.charAt(3));
                String alignment = Character.toString(userChoice.charAt(4));
                if (!boat.getPlaced()){
                    positionH = horizontalToDigitConversion(positionH);
                    if (alignment.equals("H")){
                        if (Integer.parseInt(positionH) + boat.getBoatSize() > boardSize){
                            System.out.println("Ship can't be placed there, try another co-ordinate!");
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(checkDoubleDigit(positionV, positionV2)).get(Integer.parseInt(positionH) + i).isHasShip()) {
                                    checkState = true;
                                    System.out.println("A boat is already in this position");
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                displayShipToConsole(boat);
                                for (int j = 0; j < boat.getBoatSize(); j++){
                                    boardGrid.get(checkDoubleDigit(positionV, positionV2)).get(Integer.parseInt(positionH) + j).state = 'S';
                                    boardGrid.get(checkDoubleDigit(positionV, positionV2)).get(Integer.parseInt(positionH) + j).setHasShip(true);
                                    boat.coordinatesOnBoard.add(digitToHorizontalConversion(positionH) + positionV + positionV2);
                                    boat.setPlaced(true);
                                }
                            }
                        }
                    }
                    if (alignment.equals("V")){
                        if (checkDoubleDigit(positionV, positionV2) + boat.getBoatSize() > boardSize){
                            System.out.println("Ship can't be placed there, try another co-ordinate!");
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(checkDoubleDigit(positionV, positionV2) + i).get(Integer.parseInt(positionH)).isHasShip()) {
                                    checkState = true;
                                    System.out.println("A boat is already in this position");
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                displayShipToConsole(boat);
                                for (int j = 0; j < boat.getBoatSize(); j++){
                                    boardGrid.get(checkDoubleDigit(positionV, positionV2) + j).get(Integer.parseInt(positionH)).state = 'S';
                                    boardGrid.get(checkDoubleDigit(positionV, positionV2) + j).get(Integer.parseInt(positionH)).setHasShip(true);
                                    boat.coordinatesOnBoard.add(digitToHorizontalConversion(positionH) + positionV + positionV2);
                                    boat.setPlaced(true);
                                }
                            }
                        }
                    }
                }

            }else {
                System.out.println("ERROR! Incorrect input for co-ordinates");
            }

    }

    private void placeShipWithLength6(String userChoice){
        if (userChoice.length() == 6){
            boolean isValid = new Checker().coordinatesCheckFor6(userChoice);
            if (isValid){
                boolean checkState = false;
                String id = Character.toString(userChoice.charAt(0));
                Boat boat = boats.get(Integer.parseInt(id));
                String positionH = Character.toString(userChoice.charAt(1));
                String positionH2 = Character.toString(userChoice.charAt(2));
                String positionV = Character.toString(userChoice.charAt(3));
                String positionV2 = Character.toString(userChoice.charAt(4));
                String alignment = Character.toString(userChoice.charAt(5));

                if (!boat.getPlaced()){
                    positionH = horizontalToDigitConversion(positionH);
                    positionH2 = horizontalToDigitConversion(positionH2);
                    if (alignment.equals("H")){
                        if (checkDoubleDigit(positionH, positionH2) + boat.getBoatSize() > boardSize){
                            System.out.println("Ship can't be placed there, try another co-ordinate!");
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(checkDoubleDigit(positionV, positionV2)).get(checkDoubleDigit(positionH, positionH2) + i).isHasShip()) {
                                    checkState = true;
                                    System.out.println("A boat is already in this position");
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                displayShipToConsole(boat);
                                for (int j = 0; j < boat.getBoatSize(); j++){
                                    boardGrid.get(checkDoubleDigit(positionV, positionV2)).get(checkDoubleDigit(positionH, positionH2) + j).state = 'S';
                                    boardGrid.get(checkDoubleDigit(positionV, positionV2)).get(checkDoubleDigit(positionH, positionH2) + j).setHasShip(true);
                                    boat.coordinatesOnBoard.add(digitToHorizontalConversion(positionH) + digitToHorizontalConversion(positionH2) + positionV + positionV2);
                                    boat.setPlaced(true);
                                }
                            }
                        }
                    }
                    if (alignment.equals("V")){
                        if (checkDoubleDigit(positionV,positionV2) + boat.getBoatSize() > boardSize){
                            System.out.println("Ship can't be placed there, try another co-ordinate!");
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(checkDoubleDigit(positionV, positionV2) + i).get(checkDoubleDigit(positionH, positionH2)).isHasShip()) {
                                    checkState = true;
                                    System.out.println("A boat is already in this position");
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                displayShipToConsole(boat);
                                for (int j = 0; j < boat.getBoatSize(); j++){
                                    boardGrid.get(checkDoubleDigit(positionV, positionV2) + j).get(checkDoubleDigit(positionH, positionH2)).state = 'S';
                                    boardGrid.get(checkDoubleDigit(positionV, positionV2) + j).get(checkDoubleDigit(positionH, positionH2)).setHasShip(true);
                                    boat.coordinatesOnBoard.add(digitToHorizontalConversion(positionH) + digitToHorizontalConversion(positionH2) + positionV + positionV2);
                                    boat.setPlaced(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void placeShip(){

        Scanner sc = new Scanner(System.in);

        renderBoard();
        displayShipToConsole(new Boat());

        do {
            renderBoard();
            System.out.println("Choose a ship id and where you for like to place it(e.g 1 A3 V)");
            String userChoice = sc.nextLine().toUpperCase(Locale.ROOT).replaceAll("\\s+","");

            if (userChoice.length() >= 4 && userChoice.length() <= 6) {
                if (userChoice.length() == 4){
                    placeShipWithLength4(userChoice);
                }
                if (userChoice.length() == 5){
                    placeShipWithLength5(userChoice);
                }
                if (userChoice.length() == 6){
                    placeShipWithLength6(userChoice);
                }
            }
        }while(boatCheck > 0);
    }

    public void autoPlaceShip(){
        String id = "";
        String positionH = "";
        String positionH2 = "";
        String positionV = "";
        String positionV2 = "";
        String alignment = "";

        for (int i = 0; i < boats.size(); i++){
            Boat boat = boats.get(i);
            if (boardSize <= 10){
                do {
                    Random rand = new Random();
                    id = String.valueOf(i);
                    positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(boardSize)));
                    positionV = String.valueOf(rand.nextInt(boardSize));
                    int alignCheck = rand.nextInt(2);
                    if (alignCheck == 1){
                        alignment = "H";
                    }else{
                        alignment = "V";
                    }
                    String userChoice = id + positionH + positionV + alignment;
                    placeShipWithLength4(userChoice);

                }while (!boat.placed);
            }
            if (boardSize > 10){ // 01 should be 10 etc need to make conversion1
                Random rand = new Random();
                    int userChoiceLen  = rand.nextInt(3);
                    if (userChoiceLen == 0){
                        do {
                            id = String.valueOf(i);
                            positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(10)));
                            positionH2 = "";
                            positionV = String.valueOf(rand.nextInt(10));
                            positionV2 = "";
                            int alignCheck = rand.nextInt(2);
                            if (alignCheck == 1){
                                alignment = "H";
                            }else{
                                alignment = "V";
                            }
                            String userChoice = id + positionH + positionV + alignment;
                            placeShipWithLength4(userChoice);

                        }while (!boat.placed);
                    }
                    if (userChoiceLen == 1){
                        do {
                            id = String.valueOf(i);

                            int alignCheck = rand.nextInt(2);
                            if (alignCheck == 1){
                                alignment = "H";
                            }else{
                                alignment = "V";
                            }
                            int randHozOrVert = rand.nextInt(2);
                            if (randHozOrVert == 1){
                                positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(2)));
                                positionH2 = digitToHorizontalConversion(String.valueOf(rand.nextInt(boardSize-10)));
                                positionV = String.valueOf(rand.nextInt(10));
                                positionV2 = " ";
                                String userChoice = id + positionH + positionH2 + positionV + alignment;
                                placeShipWithLength5(userChoice);
                            }else{
                                positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(10)));
                                positionH2 = " ";
                                positionV = String.valueOf(rand.nextInt(2));
                                positionV2 = String.valueOf(rand.nextInt(boardSize-10));
                                String userChoice = id + positionH + positionV + positionV2 + alignment;
                                placeShipWithLength5(userChoice);
                            }

                        }while (!boat.placed);
                    }
                    if (userChoiceLen == 2) {
                        do {
                            id = String.valueOf(i);
                            positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(2)));
                            positionH2 = digitToHorizontalConversion(String.valueOf(rand.nextInt(boardSize-10)));
                            positionV = String.valueOf(rand.nextInt(2));
                            positionV2 = String.valueOf(rand.nextInt(boardSize-10));
                            int alignCheck = rand.nextInt(2);
                            if (alignCheck == 1){
                                alignment = "H";
                            }else{
                                alignment = "V";
                            }
                            String userChoice = id + positionH + positionH2 + positionV + positionV2 + alignment;
                            placeShipWithLength6(userChoice);
                        } while(!boat.placed);

                    }
            }
        }

    }


    private String horizontalToDigitConversion(String toConvert){
        for (int i = 0; i < horizontalBoarder.size();i++){
            if (toConvert.equals(horizontalBoarder.get(i))){
                return verticalBoarder.get(i);
            }
        }
        return null;
    }

    private String digitToHorizontalConversion(String toConvert){
        for (int i = 0; i < verticalBoarder.size(); i++){
            if (toConvert.equals(verticalBoarder.get(i))){
                return horizontalBoarder.get(i);
            }
        }
        return null;
    }

    private int checkDoubleDigit(String num1, String num2){
        int num1ToInt = Integer.parseInt(num1);
        int num2ToInt = Integer.parseInt(num2);
        if (num1ToInt == 0){
            num1ToInt = 10;
            return num1ToInt + num2ToInt;
        }
        return num1ToInt + num2ToInt;
    }

    private String doubleDigitConversion(String str){
        if (str.equals("0")){
            return "1";
        }
        return str;
    }
    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public List<List<Tile>> getBoardGrid() {
        return boardGrid;
    }

    public void setBoardGrid(List<List<Tile>> boardGrid) {
        this.boardGrid = boardGrid;
    }

    public List<Boat> getBoats() {
        return boats;
    }


    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }

    public List<Boat> getDisplayBoats() {
        return displayBoats;
    }

    public void setDisplayBoats(List<Boat> displayBoats) {
        this.displayBoats = displayBoats;
    }
}
