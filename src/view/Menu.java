package view;

import model.Board;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {

    public Menu() { // no args constructor
    }

    public void displayMenu(){ // displays menu via console
        System.out.println("Welcome to battleships!");
        System.out.println("Please choose a mode you would like to play: ");
        System.out.println("1. Player Vs Computer (EASY)");
        System.out.println("2. Player VS Computer (HARD)");
        System.out.println("3. Player VS Player (EASY)");
        System.out.println("4. Player Vs Player (HARD)");
    }


    public void userGameModeChoice() throws FileNotFoundException {
        boolean check = false; // variable to end switch statement
        Scanner in = new Scanner(System.in); // to take input from console


        do { // loops until a case is entered, then check is amended
            System.out.print("Enter your choice: ");
            int userChoice = in.nextInt();
            System.out.println();
            switch (userChoice){    // runs the functions to change start the game mode
                case 1:
                    System.out.println("You have chosen: Player Vs Computer (EASY)");
                    check = true;
                    playerVsComputerEasy();
                    break;

                case 2:
                    System.out.println("You have chosen: Player Vs Computer (HARD)");
                    check = true;
                    playerVsComputerHard();
                    break;

                case 3:
                    System.out.println("You have chosen: Player Vs Player (EASY)");
                    check = true;
                    playerVsPlayerEasy();
                    break;

                case 4:
                    System.out.println("You have chosen: Player Vs Player (HARD)");
                    check = true;
                    playerVsPlayerHard();
                    break;

                default: // a default for any incorrect input
                    System.out.println("ERROR! Please enter a correct number");
                    break;
            }
        }while (!check);

    }


    private void inGameChoice(Board playerBoard, Board oppBoard) throws FileNotFoundException { // passed two boards player vs computer
        boolean check = false;
        Scanner in = new Scanner(System.in);


        System.out.println("1. Auto place all ships");
        System.out.println("2. Place your own ships");
        System.out.print("Enter your how you would like to place the ships: " );
        int userChoice = in.nextInt();

        do {
            switch (userChoice){
                case 1:
                    System.out.println();
                    System.out.println("You have selected to auto place ships");
                    System.out.println("Loading...");
                    playerBoard.autoPlaceShip(); // is ran when case one is triggered, this will auto place all the ships for the user

                    do {
                        playerBoard.renderBoard(false); // the will display the users board

                        if (!oppBoard.shootAtShip()){ // shoots at the opponents ship if the user doesnt choose to exit the program
                            playerBoard.autoShootShip(); // auto shoots at users board to act as opponent
                        }

                    }while (playerBoard.getInGameBoats().size() > 0 && oppBoard.getInGameBoats().size() > 0); // loop until one of the players boats is empty
                    if (playerBoard.getInGameBoats().size() == 0){ // outputs winning message to the user
                        System.out.println("OH NO! All your ships have been destroyed");
                    }
                    if (oppBoard.getInGameBoats().size() == 0){
                        System.out.println("Congratulations! You sunk your opponents ships");
                    }
                    check = true;
                    break;

                    // this case is nearly identical to the one above but instead allows the user to place their own ships
                case 2:
                    System.out.println();
                    System.out.println("You have selected to place your own ships...");
                    playerBoard.placeShip(false);
                    do {
                        playerBoard.renderBoard(false);
                        if (!oppBoard.shootAtShip()){
                            playerBoard.autoShootShip();
                        }
                    } while (playerBoard.getInGameBoats().size() > 0 && oppBoard.getInGameBoats().size() > 0);
                    if (playerBoard.getInGameBoats().size() == 0){
                        System.out.println("OH NO! All your ships have been destroyed");
                    }
                    if (oppBoard.getInGameBoats().size() == 0){
                        System.out.println("Congratulations! You sunk your opponents ships");
                    }
                    check = true;
                    break;

                default:
                    System.out.println("ERROR! Please enter a correct number");
                    break;
            }

        }while (!check);
    }


    private void inGameChoicePVP(Board player1, Board player2){ // this is the PvP menu for the users
        boolean check = false;
        Scanner in = new Scanner(System.in);

        System.out.println("PLAYER 1 FIRST!"); // the ship placing options
        System.out.println("1. Auto place all ships");
        System.out.println("2. Place your own ships");
        System.out.print("Enter your how you would like to place the ships: " );
        int userChoice = in.nextInt();
        do {
            switch (userChoice){
                case 1:
                    player1.autoPlaceShip(); // auto placing the ships
                    check = true;
                    break;
                case 2:
                    player1.placeShip(false); // user placing the ships
                    check = true;
                    break;
                default:
                    System.out.println("ERROR! Please enter a correct number");
            }
        }while (!check);

        check = false;
        System.out.println("PLAYER 2 NOW!"); // seen above
        System.out.println("1. Auto place all ships");
        System.out.println("2. Place your own ships");
        System.out.print("Enter your how you would like to place the ships: " );
        userChoice = in.nextInt();
        do {
            switch (userChoice){
                case 1:
                    player2.autoPlaceShip();
                    check = true;
                    break;
                case 2:
                    player2.placeShip(false);
                    check = true;
                    break;
                default:
                    System.out.println("ERROR! Please enter a correct number");
            }
        }while (!check);

        do { // this is a loop to allow the players to shoot at each others ships
            System.out.println("Player 1's turn");
            if (player2.shootAtShip()){ // function that allows the players to shoot
                break; // if 0 is entered inside the shootAtShip function then the loop will be broken
            }

            System.out.println("Player 2's turn");
            if (player1.shootAtShip()){
                break;
            }

        }while (player1.getInGameBoats().size() > 0 && player2.getInGameBoats().size() > 0);

        if (player1.getInGameBoats().size() == 0){ // displays winner to the console
            System.out.println("Player 2 has won! All ships have been destroyed");
        }
        if (player2.getInGameBoats().size() == 0){
            System.out.println("Player 1 has won! All ships have been destroyed");
        }
    }

    private void playerVsComputerEasy() throws FileNotFoundException { // this functions set up the board for the specific game mode
        Board playerBoard = new Board();
        playerBoard.setBoardDimensions("easy"); // easy is taken from the txt file and has a board size associated with it
        playerBoard.populateBoard(); // adds tiles to the board depending on the board size



        Board oppBoard = new Board();
        oppBoard.setBoardDimensions("easy");
        oppBoard.populateBoard();
        oppBoard.autoPlaceShip();

        inGameChoice(playerBoard, oppBoard);
    }

    private void playerVsComputerHard() throws FileNotFoundException {
        Board playerBoard = new Board();
        playerBoard.setBoardDimensions("hard");
        playerBoard.populateBoard();



        Board oppBoard = new Board();
        oppBoard.setBoardDimensions("hard");
        oppBoard.populateBoard();
        oppBoard.autoPlaceShip();

        inGameChoice(playerBoard, oppBoard);
    }

    private void playerVsPlayerEasy() throws FileNotFoundException {
        Board player1Board = new Board();
        Board player2Board = new Board();

        player1Board.setBoardDimensions("easy");
        player2Board.setBoardDimensions("easy");

        player1Board.populateBoard();
        player2Board.populateBoard();

        inGameChoicePVP(player1Board,player2Board);
    }

    private void playerVsPlayerHard() throws FileNotFoundException {
        Board player1Board = new Board();
        Board player2Board = new Board();

        player1Board.setBoardDimensions("hard");
        player2Board.setBoardDimensions("hard");

        player1Board.populateBoard();
        player2Board.populateBoard();

        inGameChoicePVP(player1Board,player2Board);
    }
}
