package model;


import utils.Checker;
import utils.FileHelper;

import java.io.FileNotFoundException;
import java.util.*;

public class Board {

    // declaring all the variables

    private int boardSize = 0;
    private int cellSize = 0;
    private final int DEFAULT_BOARD_SIZE = 5;
    private final int DEFAULT_CELL_SIZE = 2;



    List<List<Tile>> boardGrid = new ArrayList<>(); // 2d list with tile objects
    List<Boat> boats = new FileHelper().readBoatToList(); // boats and different copies
    List<Boat> displayBoats = new ArrayList<>(boats);
    List<Boat> inGameBoats = new ArrayList<>(boats);

    public int boatCheck = boats.size();


    private List<String> horizontalBoarder = new ArrayList<>(Arrays.asList("A","B","C","D","E","F","G","H","I","J"));
    private List<String> verticalBoarder = new ArrayList<>(Arrays.asList("0","1","2","3","4","5","6","7","8","9"));

    public Board() throws FileNotFoundException {
    }

    public void populateBoard() { // adds all the tiles to the board
        for (int p = 0; p < boardSize; p++) {
            List<Tile> row = new ArrayList<>(); // creates another list to be added to the array
            for (int i = 0; i < boardSize; i++) {
                row.add(new Tile()); // adds the tiles to the row
            }
            boardGrid.add(row); // adds the row to the list
        }
    }



    public void renderBoard(boolean hidden){ // hidden allows the function to hide specific parts of the outputs depending on the argument
        // false doesnt show where a ship is placed but where is hit and missed - for opponents board
        // true shows that and where the ships are placed - for players board
        int total = boardSize * cellSize;
        char c = ' ';
        int verticalBoarderCounter = 0;
        int horizontalBoarderCounter = 0;



        while (horizontalBoarderCounter != boardSize){ // outputs the horizontal boarder up to the board size before that is greater than 10
            if (horizontalBoarderCounter < 10){
                System.out.print(" " + horizontalBoarder.get(horizontalBoarderCounter)); // outputs the value from the list at the index using the counter
                horizontalBoarderCounter++;

            } else if (horizontalBoarderCounter >= 10){ // if the board size is over 10 the boarder stacks e.g. AA AB
                int firstIndex = (horizontalBoarderCounter/10) - 1; // should the division of 10 for which ever number - to determine how many times it has gone over 10
                int secondIndex = horizontalBoarderCounter%10; // gets the remainder of 10 to find the second digit
                System.out.print(horizontalBoarder.get(firstIndex) + horizontalBoarder.get(secondIndex));
                horizontalBoarderCounter++;
            }
        }

        System.out.println();

        for (int i = 0; i <= total; i++){
            for (int k = 0 ; k <= total; k++){
                if (k % cellSize == 0 && i % cellSize != 0 && k == total){ // calculates when its the end of the line to add the boarder
                    if (verticalBoarderCounter < 10){
                        System.out.print("| " + verticalBoarder.get(verticalBoarderCounter)); // outputs boarder

                    }else if (verticalBoarderCounter >= 10){
                        int firstIndex = (verticalBoarderCounter / 10) - 1;
                        int secondIndex = verticalBoarderCounter % 10;
                        System.out.print("| " + verticalBoarder.get(firstIndex) + verticalBoarder.get(secondIndex)); // used to output the boarder when it has looped 10 times - to stack the boarder e.g. 00 01
                    }

                    verticalBoarderCounter++;
                } else if (k % cellSize == 0 && i % cellSize != 0) { // outputs the cell walls
                    c = '|';
                } else if (i % cellSize == 0 || k % cellSize == 0) { // outputs the roof of the cells
                    c = '-';
                }else {
                    if (hidden && boardGrid.get(i / 2).get(k / 2).state == 'S' ){ // if hidden is true, S meaning ship will not be outputted so the user cant see the ship when the board is rendered
                            c = ' ';
                    } else {
                        c = boardGrid.get(i / 2).get(k / 2).state; // outputs the location of the ship
                    }
                }
                System.out.print(c);
                c = ' ';
            }
            System.out.println();
        }
    }


    public void setBoardDimensions(String difficulty) throws FileNotFoundException { // reads in from the list that is given by the file reader object function
        // difficulty is what is passed and searched for in the txt file to determine board size
        FileHelper fileHelper = new FileHelper();
        List<String> boardSizesAsString = fileHelper.getBoardSize(); // gets the list with the board size
        String state = "";
        for (String s : boardSizesAsString) {
            if (s.equals(difficulty)) { // if the passed argument difficulty matched then the state will be changed to size to tell the program the next element in the list will be the size
                state = "size";
            }
            if (state.equals("size") && !s.equals(difficulty)) { // if its no longer the string parameter passed but the state is still size, that value will be used for the size
                boardSize = Integer.parseInt(s);
                state = "";
            }
            if (s.equals("cellSize")) { // same process as seen above
                state = "cellSize";
            }
            if (state.equals("cellSize") && !s.equals("cellSize")) {
                cellSize = Integer.parseInt(s);
                state = "";
            }
        }
        if (cellSize <= 0 || boardSize <= 0){ // setting default sizes if the user somehow messes up the txt file
            boardSize = DEFAULT_BOARD_SIZE;
            cellSize = DEFAULT_CELL_SIZE;

        }
        if (cellSize > 5 || boardSize > 20){
            boardSize = DEFAULT_BOARD_SIZE;
            cellSize = DEFAULT_CELL_SIZE;
        }
    }

    private void displayShipToConsole(Boat boatToRemove){ // displays the ships that need to be placed when the user is placing them
        // boats to remove is the boat that is passed when it has just been added to the board
        System.out.println("ID   Boat Name             Length ");


        for (Boat toCheck: displayBoats) { // checks whether the boat to remove matches one in the list of boats left to place
            if (toCheck.equals(boatToRemove)){
                displayBoats.remove(boatToRemove); // removes the boat from that list
                break;
            }
        }

        for (Boat boat: displayBoats) { // loops through that list and displays them
            String str = (boat.getId() + "    " + boat.getBoatName()+ "                 " );
            String output = new StringBuilder(str).insert(27,boat.getBoatSize()).toString(); // inserting boat size at hard coded value for formatting
            System.out.print(output);
            System.out.println();
        }
    }

    private void placeShipWithLength4(String userChoice, boolean auto){ // adds ship if userchoice is 4 in length - auto is what decides what outputs are shown to the user depending on it being true or false
            boolean isValid = new Checker().coordinatesCheckFor4(userChoice); // uses the checker function to validate the user input
            if (isValid){
                boolean checkState = false;


                String id = Character.toString(userChoice.charAt(0)); // splits the user input in to variables to make it easier to manipulate data
                Boat boat = boats.get(Integer.parseInt(id)); // gets the boat with that id passed
                String positionH = Character.toString(userChoice.charAt(1));
                String positionV = Character.toString(userChoice.charAt(2));
                String alignment = Character.toString(userChoice.charAt(3));

                if (!boat.getPlaced()){ // checks with the boat is already placed
                    positionH = horizontalToDigitConversion(positionH); // calls a function to change a character to its number equivalent e.g. A=0 B=1
                    if (alignment.equals("H")){ // checks the direction in which the ship will be placed - H is horizontal
                        if (Integer.parseInt(positionH) + boat.getBoatSize() > boardSize){ // checks if the position + the size of the boat isnt bigger than the board meaning the ship cant be placed
                            if (!auto){ // if not auto then the user is placing their own ships so different outputs will be displayed
                                // auto being true means that when a ship is randomly being placed it wont trigger console warnings and ruining the format display
                                System.out.println("Ship can't be placed there, try another co-ordinate!");

                            }
                        } else {
                            for (int i = 0; i < boat.getBoatSize(); i++) { // now checks whether there is a ship already in any of them positions
                                if (boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH) + i).isHasShip()) {
                                    checkState = true;
                                    if (!auto) {
                                        System.out.println("A boat is already in this position");
                                    }
                                    break;

                                }
                            }
                            if (!checkState) { // if the program reaches this point then the ship can be placed
                                // different variables will now be updated to reflect the ship being placed
                                if (!auto){
                                    displayShipToConsole(boat); // displays what ships have left to place for the next iteration in the loop
                                }
                                boatCheck = boatCheck - 1; // counter for amount of boards left to place is decreased
                                for (int j = 0; j < boat.getBoatSize(); j++) { // loops the size of the boat

                                    boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH) + j).state = 'S'; // changes the state of the tile in the 2d list using the co-ordinates
                                    boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH) + j).setHasShip(true); // + j will get the next tile providing that the co-ordinates are the head of the ship

                                    String posHHolder = positionH;
                                    if (Integer.parseInt(positionH) + j >= 10){ // checks whether to stack the co-ordinates if they go above 10
                                        posHHolder = "A" + digitToHorizontalConversion(String.valueOf(((Integer.parseInt(positionH)+j)%10))); // calculates the second value by getting the remainder of 10
                                        // the converting that number to its equivalent number to a letter e.g. 0=A 1=B
                                    } else {
                                        posHHolder = digitToHorizontalConversion(String.valueOf(Integer.parseInt(positionH)+ j));
                                    }
                                    boat.coordinatesOnBoard.add(posHHolder + positionV); // that is then added to the co-ordinates list to track where the ship is
                                    boat.setPlaced(true);
                                }
                            }
                        }
                    }// this is the same as seen above but for the vertical placing of a ship
                    if (alignment.equals("V")){
                        if (Integer.parseInt(positionV) + boat.getBoatSize() > boardSize){
                            if (!auto){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }

                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(Integer.parseInt(positionV) + i).get(Integer.parseInt(positionH)).isHasShip()) {
                                    checkState = true;
                                    if (!auto){
                                        System.out.println("A boat is already in this position");
                                    }
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                if (!auto){
                                    displayShipToConsole(boat);
                                }
                                for (int j = 0; j < boat.getBoatSize(); j++) {

                                    boardGrid.get(Integer.parseInt(positionV) + j).get(Integer.parseInt(positionH)).state = 'S';
                                    boardGrid.get(Integer.parseInt(positionV) + j).get(Integer.parseInt(positionH)).setHasShip(true);
                                    String posVHolder = positionV;
                                    if (Integer.parseInt(positionV) + j >= 10){
                                        posVHolder = "0" + (((Integer.parseInt(positionV) + j) % 10));
                                    } else {
                                        posVHolder = String.valueOf(Integer.parseInt(positionV) + j);
                                    }
                                    boat.coordinatesOnBoard.add(digitToHorizontalConversion(positionH) + posVHolder);
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

    private void placeShipWithLength5(String userChoice, boolean auto){ // same as seen on lines 156 - 211 in terms of placing the ship
            int isValid = new Checker().coordinatesCheckFor5(userChoice); // int returns shows how the input is formatted either AA1 or A11
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
                            if (!auto){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(Integer.parseInt(positionV)).get(checkDoubleDigit(positionH,positionH2) + i).isHasShip()) {
                                    checkState = true;
                                    if (!auto){
                                        System.out.println("A boat is already in this position");
                                    }
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                if (!auto){
                                    displayShipToConsole(boat);
                                }
                                for (int j = 0; j < boat.getBoatSize(); j++){
                                    boardGrid.get(Integer.parseInt(positionV)).get(checkDoubleDigit(positionH, positionH2) + j).state = 'S';
                                    boardGrid.get(Integer.parseInt(positionV)).get(checkDoubleDigit(positionH, positionH2) + j).setHasShip(true);


                                    boat.coordinatesOnBoard.add(digitToHorizontalConversion(positionH)
                                            + digitToHorizontalConversion(String.valueOf(Integer.parseInt(positionH2 )+ j)) + positionV);
                                    boat.setPlaced(true);
                                }
                            }
                        }
                    }
                    if (alignment.equals("V")){
                        if (Integer.parseInt(positionV) + boat.getBoatSize() > boardSize){
                            if (!auto){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(Integer.parseInt(positionV) + i).get(checkDoubleDigit(positionH,positionH2)).isHasShip()) {
                                    checkState = true;
                                    if (!auto){
                                        System.out.println("A boat is already in this position");
                                    }
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                if (!auto){
                                    displayShipToConsole(boat);
                                }
                                for (int j = 0; j < boat.getBoatSize(); j++){
                                    boardGrid.get(Integer.parseInt(positionV) + j).get(checkDoubleDigit(positionH,positionH2)).state = 'S';
                                    boardGrid.get(Integer.parseInt(positionV) + j).get(checkDoubleDigit(positionH, positionH2)).setHasShip(true);
                                    String posVHolder = positionV;
                                    if (Integer.parseInt(positionV) + j >= 10){
                                        posVHolder = "0" + (((Integer.parseInt(positionV) + j)%10));
                                    } else {
                                        posVHolder = String.valueOf(Integer.parseInt(positionV) + j);
                                    }
                                    boat.coordinatesOnBoard.add(digitToHorizontalConversion(positionH) + digitToHorizontalConversion(positionH2) + posVHolder);
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
                            if (!auto){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(checkDoubleDigit(positionV, positionV2)).get(Integer.parseInt(positionH) + i).isHasShip()) {
                                    checkState = true;
                                    if (!auto){
                                        System.out.println("A boat is already in this position");
                                    }
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                if (!auto){
                                    displayShipToConsole(boat);
                                }
                                for (int j = 0; j < boat.getBoatSize(); j++){
                                    boardGrid.get(checkDoubleDigit(positionV, positionV2)).get(Integer.parseInt(positionH) + j).state = 'S';
                                    boardGrid.get(checkDoubleDigit(positionV, positionV2)).get(Integer.parseInt(positionH) + j).setHasShip(true);
                                    String posHHolder = positionH;
                                    if (Integer.parseInt(positionH) + j >= 10){
                                        posHHolder = "A" + digitToHorizontalConversion(String.valueOf(((Integer.parseInt(positionH)+j)%10)));
                                    } else {
                                        posHHolder = digitToHorizontalConversion(String.valueOf(Integer.parseInt(positionH)+ j));
                                    }
                                    boat.coordinatesOnBoard.add(posHHolder + positionV + positionV2);
                                    boat.setPlaced(true);
                                }
                            }
                        }
                    }
                    if (alignment.equals("V")){
                        if (checkDoubleDigit(positionV, positionV2) + boat.getBoatSize() > boardSize){
                            if (!auto){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(checkDoubleDigit(positionV, positionV2) + i).get(Integer.parseInt(positionH)).isHasShip()) {
                                    checkState = true;
                                    if (!auto){
                                        System.out.println("A boat is already in this position");
                                    }
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                if (!auto){
                                    displayShipToConsole(boat);
                                }
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

    private void placeShipWithLength6(String userChoice, boolean auto){ // see function above lines 156-211
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
                            if (!auto){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(checkDoubleDigit(positionV, positionV2)).get(checkDoubleDigit(positionH, positionH2) + i).isHasShip()) {
                                    checkState = true;
                                    if (!auto){
                                        System.out.println("A boat is already in this position");
                                    }
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                if (!auto){
                                    displayShipToConsole(boat);
                                }
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
                            if (!auto){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }
                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++) {
                                if (boardGrid.get(checkDoubleDigit(positionV, positionV2) + i).get(checkDoubleDigit(positionH, positionH2)).isHasShip()) {
                                    checkState = true;
                                    if (!auto){
                                        System.out.println("A boat is already in this position");
                                    }
                                    break;
                                }
                            }
                            if (!checkState){
                                boatCheck = boatCheck - 1;
                                if (!auto){
                                    displayShipToConsole(boat);
                                }
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

    public void placeShip(boolean auto){ // function used to group the ship placing functions

        Scanner sc = new Scanner(System.in);

        renderBoard(false);
        displayShipToConsole(new Boat());

        do {
            renderBoard(false);
            System.out.println("Choose a ship id and where you for like to place it(e.g 1 A3 V)");
            String userChoice = sc.nextLine().toUpperCase(Locale.ROOT).replaceAll("\\s+",""); // for taking user choice - Also strips white space and changes strings to uppercase

            if (userChoice.length() >= 4 && userChoice.length() <= 6) {
                if (userChoice.length() == 4){
                    placeShipWithLength4(userChoice,auto);
                }
                if (userChoice.length() == 5){
                    placeShipWithLength5(userChoice,auto);
                }
                if (userChoice.length() == 6){
                    placeShipWithLength6(userChoice,auto);
                }
            }
        }while(boatCheck > 0); // loops until all ships are placed
    }


    public void autoPlaceShip(){ // used to auto place ships
        String id = "";
        String positionH = "";
        String positionH2 = "";
        String positionV = "";
        String positionV2 = "";
        String alignment = "";

        for (int i = 0; i < boats.size(); i++){
            Boat boat = boats.get(i);
            if (boardSize <= 10){ // for board size less than 10
                do {
                    Random rand = new Random();
                    id = String.valueOf(i); // user input as separate variables
                    positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(boardSize))); // random numbers generate by board size bounds
                    positionV = String.valueOf(rand.nextInt(boardSize));
                    int alignCheck = rand.nextInt(2);
                    if (alignCheck == 1){
                        alignment = "H";
                    }else{
                        alignment = "V";
                    }
                    String userChoice = id + positionH + positionV + alignment; // string is concatenated as userChoice which will be passed in to the function
                    placeShipWithLength4(userChoice,true);

                }while (!boat.placed); // this is done until the ship is placed
            }
            if (boardSize > 10){ // for board sizes greater than 10 to include different length of inputs
                Random rand = new Random();
                    int userChoiceLen  = rand.nextInt(3); // decides the length and/or format of the input e.g. AA1 A11 AA11
                    if (userChoiceLen == 0){
                        do { // formatted A1 for length of input at 4
                            // see above
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
                            placeShipWithLength4(userChoice,true);

                        }while (!boat.placed);
                    }
                    if (userChoiceLen == 1){ // for length 5 either AA1 or A11
                        do {
                            id = String.valueOf(i);

                            int alignCheck = rand.nextInt(2);
                            if (alignCheck == 1){
                                alignment = "H";
                            }else{
                                alignment = "V";
                            }
                            int randHozOrVert = rand.nextInt(2); // randomly decides whether it is AA1 or A11
                            if (randHozOrVert == 1){ // AA1
                                positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(2)));
                                positionH2 = digitToHorizontalConversion(String.valueOf(rand.nextInt(boardSize-10))); // to pick second letter in the bounds - 10
                                positionV = String.valueOf(rand.nextInt(10));
                                positionV2 = " ";
                                String userChoice = id + positionH + positionH2 + positionV + alignment;
                                placeShipWithLength5(userChoice,true);
                            }else{
                                positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(10)));
                                positionH2 = " ";
                                positionV = String.valueOf(rand.nextInt(2));
                                positionV2 = String.valueOf(rand.nextInt(boardSize-10));
                                String userChoice = id + positionH + positionV + positionV2 + alignment;
                                placeShipWithLength5(userChoice,true);
                            }

                        }while (!boat.placed);
                    }
                    if (userChoiceLen == 2) {
                        do { // for AA11 see above
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
                            placeShipWithLength6(userChoice,true);
                        } while(!boat.placed);

                    }
            }
        }

    }

    public boolean shootAtShip(){ // to shoot at the ships grouped functions together
        Scanner sc = new Scanner(System.in);

        System.out.println("Opponents board...");
        renderBoard(true);

        System.out.println("Choose a co-ordinate to fire at ( e.g A3 )");
        System.out.println("Press 0 to exit or 1 to see ship status: ");
        String userChoice = sc.nextLine().toUpperCase(Locale.ROOT).replaceAll("\\s+",""); // takes the input of the ship to shoot at


        if (userChoice.equals("0")){ // 0 is to exit the program - if returned true then the program will end
            System.out.println("You have chosen to quit! Thank you for playing battleships");
            return true;
        }

        if (userChoice.equals("1")){
            shootShipStatus(); // displays a menu of what ships are left with the health to help the player but they will lose a turn if they do this
        }

        if (userChoice.length() == 2){
            shootWithLen2(userChoice);
        }
        if (userChoice.length() == 3){
            shootWithLen3(userChoice);
        }
        if (userChoice.length() == 4){
            shootWithLen4(userChoice);
        }
        return false; // function return false and the game carries on playing
    }

    public void autoShootShip(){ // auto shoot ship use for computer
        String positionH = "";
        String positionH2 = "";
        String positionV = "";
        String positionV2 = "";

        if (boardSize <= 10){ // random co-ordinates for board size less than 10
            Random rand = new Random();
            positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(boardSize))); // random number for posH converted in to letter equivalent
            positionV = String.valueOf(rand.nextInt(boardSize)); // random number for posV
            String userChoice = positionH + positionV; // concat to a single string for user input
            shootWithLen2(userChoice);

        }
        if (boardSize > 10){ // for board size greater than 10
            Random rand = new Random();
            int randomLength = rand.nextInt(4);
            if (randomLength == 0){
                positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(boardSize)));
                positionV = String.valueOf(rand.nextInt(boardSize));
                String userChoice = positionH + positionV;
                shootWithLen2(userChoice);
            }
            if (randomLength == 1){
                positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(2)));
                positionH2 = digitToHorizontalConversion(String.valueOf(rand.nextInt(boardSize - 10))); // - 10 for second digit stops the random number from being out of bounds
                positionV = String.valueOf(rand.nextInt(boardSize));
                String userChoice = positionH + positionH2 + positionV;
                shootWithLen3(userChoice);
            }
            if (randomLength == 2){
                positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(boardSize)));
                positionV = String.valueOf(rand.nextInt(2));
                positionV2 = String.valueOf(rand.nextInt(boardSize - 10));
                String userChoice = positionH + positionV + positionV2;
                shootWithLen3(userChoice);
            }
            if (randomLength == 3){
                positionH = digitToHorizontalConversion(String.valueOf(rand.nextInt(2)));
                positionH2 = digitToHorizontalConversion(String.valueOf(rand.nextInt(boardSize - 10)));
                positionV = String.valueOf(rand.nextInt(2));
                positionV2 = String.valueOf(rand.nextInt(boardSize - 10));
                String userChoice = positionH + positionH2 + positionV + positionV2;
                shootWithLen4(userChoice);
            }
        }
    }

    private void shootWithLen2(String userChoice){
        boolean isValid = new Checker().shootCheckFor2(userChoice); // uses checker to validate input, see function
        if (isValid){
            String positionH = Character.toString(userChoice.charAt(0));
            positionH = horizontalToDigitConversion(positionH);
            String positionV = Character.toString(userChoice.charAt(1));

            try{ // try is to catch any inputs outside the bounds
                if (boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH)).isHasShip()){ // checks whether a ship is at that co-ordinate
                    if (boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH)).state != 'H'){ // if ship hasn't already been hit
                        boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH)).state = 'H'; // change the state to hit
                        Boat boat = findBoatWithCoordinates(userChoice); // see function
                        boat.health = boat.health - 1; // decreases boat health
                        if (boat.health == 0){ // display destroyed message if boat health hits 0
                            System.out.println("You have destroyed the " + boat.getBoatName() + " boat!");
                            inGameBoats.remove(boat); // removes boats from in game list

                        }
                        System.out.println("HIT!");
                    } else {
                        System.out.println("Ship has already been hit");
                    }
                } else {
                    System.out.println("MISSED! there was no ship at that location");
                    boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH)).state = 'M'; // changes state to missed if boat isn't there
                }
            }catch (Exception e){
                System.out.println("ERROR!");
            }
        }
    }

    private void shootWithLen3(String userChoice){ // see function above
        int isValid = new Checker().shootCheckFor3(userChoice); // checks valid input
        if (isValid == 1){ // 1 means the input is A11
            //digit
            String positionH = Character.toString(userChoice.charAt(0));
            positionH = horizontalToDigitConversion(positionH);
            String positionV = Character.toString(userChoice.charAt(1));
            String positionV2 = Character.toString(userChoice.charAt(2));

            try{
                if (boardGrid.get(checkDoubleDigit(positionV,positionV2)).get(Integer.parseInt(positionH)).isHasShip()){
                    if (boardGrid.get(checkDoubleDigit(positionV,positionV2)).get(Integer.parseInt(positionH)).state != 'H'){
                        boardGrid.get(checkDoubleDigit(positionV,positionV2)).get(Integer.parseInt(positionH)).state = 'H';
                        Boat boat = findBoatWithCoordinates(userChoice);
                        boat.health = boat.health - 1;
                        if (boat.health == 0){
                            System.out.println("You have destroyed the " + boat.getBoatName() + " boat!");
                            inGameBoats.remove(boat);
                        }
                        System.out.println("HIT!");
                    } else {
                        System.out.println("Ship has already been hit");
                    }
                } else {
                    System.out.println("MISSED! there was no ship at that location");
                    boardGrid.get(checkDoubleDigit(positionV,positionV2)).get(Integer.parseInt(positionH)).state = 'M';
                }
            } catch (Exception e){
                System.out.println("ERROR!");
            }
        }
        if (isValid == 2){ // 2 means the input is AA1
            //letter
            String positionH = Character.toString(userChoice.charAt(0));
            positionH = horizontalToDigitConversion(positionH);
            String positionH2 = Character.toString(userChoice.charAt(1));
            positionH2 = horizontalToDigitConversion(positionH2);
            String positionV= Character.toString(userChoice.charAt(2));

            try {
                if (boardGrid.get(Integer.parseInt(positionV)).get(checkDoubleDigit(positionH,positionH2)).isHasShip()){
                    if (boardGrid.get(Integer.parseInt(positionV)).get(checkDoubleDigit(positionH,positionH2)).state != 'H'){
                        boardGrid.get(Integer.parseInt(positionV)).get(checkDoubleDigit(positionH,positionH2)).state = 'H';
                        Boat boat = findBoatWithCoordinates(userChoice);
                        boat.health = boat.health - 1;
                        if (boat.health == 0){
                            System.out.println("You have destroyed the " + boat.getBoatName() + " boat!");
                            inGameBoats.remove(boat);
                        }
                        System.out.println("HIT!");
                    } else {
                        System.out.println("Ship has already been hit");
                    }
                } else {
                    System.out.println("MISSED! there was no ship at that location");
                    boardGrid.get(Integer.parseInt(positionV)).get(checkDoubleDigit(positionH,positionH2)).state = 'M';
                }
            } catch (Exception e){
                System.out.println("ERROR!");
            }
        }
    }

    private void shootWithLen4(String userChoice){ // see functions above
        boolean isValid = new Checker().shootCheckFor4(userChoice);
        if (isValid){

            String positionH = Character.toString(userChoice.charAt(0));
            positionH = horizontalToDigitConversion(positionH);
            String positionH2 = Character.toString(userChoice.charAt(1));
            positionH2 = horizontalToDigitConversion(positionH2);
            String positionV = Character.toString(userChoice.charAt(2));
            String positionV2 = Character.toString(userChoice.charAt(3));

            try {
                if (boardGrid.get(checkDoubleDigit(positionV,positionV2)).get(checkDoubleDigit(positionH,positionH2)).isHasShip()){
                    if (boardGrid.get(checkDoubleDigit(positionV,positionV2)).get(checkDoubleDigit(positionH,positionH2)).state != 'H'){
                        boardGrid.get(checkDoubleDigit(positionV,positionV2)).get(checkDoubleDigit(positionH,positionH2)).state = 'H';
                        Boat boat = findBoatWithCoordinates(userChoice);
                        boat.health = boat.health - 1;
                        if (boat.health == 0){
                            System.out.println("You have destroyed the " + boat.getBoatName() + " boat!");
                            inGameBoats.remove(boat);

                        }
                        System.out.println("HIT!");
                    } else {
                        System.out.println("Ship has already been hit");
                    }
                } else {
                    System.out.println("MISSED! there was no ship at that location");
                    boardGrid.get(checkDoubleDigit(positionV,positionV2)).get(checkDoubleDigit(positionH,positionH2)).state = 'M';
                }
            }catch (Exception e){
                System.out.println("ERROR!");
            }
        }
    }

    private void shootShipStatus(){ // shows boats that haven't been destroyed with their health
        System.out.println("ID   Boat Name             Health ");

        for (Boat boat: inGameBoats) { // loops through boats left on the board
            String str = (boat.getId() + "    " + boat.getBoatName()+ "                 " );
            String output = new StringBuilder(str).insert(27,boat.getHealth()).toString(); // outputs string built to display the output
            System.out.print(output);
            System.out.println();
        }
    }

    private Boat findBoatWithCoordinates(String coordinates){ // finds the boat to alter the status of the boat
        for (Boat boat: boats) { // loops through all boats
            for (String coordinate: boat.coordinatesOnBoard){ // loops through all the co-ordinates for that ship
                if (coordinate.equals(coordinates)){ // checks whether us user input matches the co-ordinates
                    return boat; // returns the boat if they match
                }
            }
        }
        return new Boat(); // else returns an empty boat
    }

    private String horizontalToDigitConversion(String toConvert){ // used to convert a letter to its equivalent digit
        for (int i = 0; i < horizontalBoarder.size();i++){ // loops through the letters list
            if (toConvert.equals(horizontalBoarder.get(i))){ // if the toConvert matches the index from the letters list
                return verticalBoarder.get(i); // gets that index from the digit list
            }
        }
        return null;
    }

    private String digitToHorizontalConversion(String toConvert){ // reversed as seen above - digit to letter
        for (int i = 0; i < verticalBoarder.size(); i++){
            if (toConvert.equals(verticalBoarder.get(i))){
                return horizontalBoarder.get(i);
            }
        }
        return null;
    }

    private int checkDoubleDigit(String num1, String num2){ // used to convert the index for the digit boarder display
        // so 01 would equal 10
        // allows the value to be used as the index
        int num1ToInt = Integer.parseInt(num1);
        int num2ToInt = Integer.parseInt(num2);
        if (num1ToInt == 0){
            num1ToInt = 10;
            return num1ToInt + num2ToInt;
        }
        return num1ToInt + num2ToInt;
    }


    public List<Boat> getInGameBoats() {
        return inGameBoats;
    }





}
