import model.Board;
import view.Menu;
import utils.FileReader;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

// the start of the program
        FileReader reader = new FileReader(); // creating custom file reader object
        reader.readFile(); // calls the readFile function to read the txt file with the game config in it

        Menu menu = new Menu(); // creating custom menu object
        menu.displayMenu(); // displays the menu to the user
        menu.userGameModeChoice(); // where the user choices which game mode they want to player




    }


}
