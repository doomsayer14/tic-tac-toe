package tictactoe;

import java.util.Random;
import java.util.Scanner;

//An instance of this class is used to call only
//one method - game(), all logic described in this method.

public class Main {

    //contain our game field
    private char[][] charState;

    private Scanner scanner;
    // for easyAI
    private Random random;

    //line e.g. "start easy user" splits on 3 parts
    private String command;
    private String firstPlayer;
    private String secondPlayer;

    //while game is alive, this variable has to control its validness
    private boolean isGameValid;

    public static void main(String[] args) {
        new Main().game();
    }

    //Constructor
    public Main() {
        charState = new char[3][3];
        scanner = new Scanner(System.in);
        random = new Random();
        isGameValid = true;
    }

    //the main method
    public void game() {
        while (command != "exit") {
            initTable();
            askCommand();
            printTable(charState);

            //After the command input (askCommand)
            //this cycle starts the game
            //and controls it finished or not.
            //then program ask for a command again
            while (isGameValid) {
                turn(firstPlayer);
                printTable(charState);
                isGameValid = checkGameValid(defineSign());

                turn(secondPlayer);
                printTable(charState);
                isGameValid = checkGameValid(defineSign());
            }
        }
    }

    //first we need to fill in our array "charState" with
    //sign '_' which means empty cell
    private void initTable() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                charState[i][j] = '_';
            }
        }
    }

    //then we split our command on 3 parts and check them on correctness
    //with additional methods
    private void askCommand() {
        System.out.print("Input command: ");
        scanCommand();
        while (!checkCommand()) {
            System.out.println("Bad parameters!");
            System.out.print("Input command: ");
            scanCommand();
        }
    }

    //this method literally takes full line (fullCommand) and splits it by
    //space on 3 parts. Try/catch is necessary to avoid not correct input.
    private void scanCommand() {
        try {
            String fullCommand;
            String[] commandList;
            fullCommand = scanner.nextLine();
            commandList = fullCommand.split(" ", 3);
            command = commandList[0];
            firstPlayer = commandList[1];
            secondPlayer = commandList[2];
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    //hardcoding all appropriate variants of player - AI and its levels
    //or human. Try/catch is necessary to avoid not correct input.
    private boolean checkCommand() {
        try {
            return command.equals("start") &&
                    (firstPlayer.equals("easy") || firstPlayer.equals("user")) &&
                    (secondPlayer.equals("easy") || secondPlayer.equals("user"));
        } catch (NullPointerException e) {
            return false;
        }
    }

    //define "isGameValid" variable, that controls game validness
    private boolean checkGameValid(char sign) {
        if (checkWin(sign)) {
            System.out.println(sign + " wins");
            return false;
        }
        if (isTableFull()) {
            System.out.println("Draw");
            return false;
        }
        return true;
    }

    //hardcoding of all combinations of win, why not?)))))))
    private boolean checkWin(char dot) {
        for (int i = 0; i < 3; i++)
            if ((charState[i][0] == dot && charState[i][1] == dot &&
                    charState[i][2] == dot) ||
                    (charState[0][i] == dot && charState[1][i] == dot &&
                            charState[2][i] == dot))
                return true;
        if ((charState[0][0] == dot && charState[1][1] == dot &&
                charState[2][2] == dot) ||
                (charState[2][0] == dot && charState[1][1] == dot &&
                        charState[0][2] == dot))
            return true;
        return false;
    }

    //hardcoding all cases, when game field is full
    private boolean isTableFull() {
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++)
                if (charState[row][col] == '_')
                    return false;
        return true;
    }

    //methods that choose, who will make the turn -
    //AI of any level or human
    private void turn(String player) {
        switch (player) {
            case ("user"):
                turnHuman();
                break;
            case ("easy"):
                turnEasyAI();
                break;
        }
    }

    //human turn, hardcoding all input mistakes,
    //which user can make. Methods ask coordinates
    //until user type right coordinates.
    private void turnHuman() {
        int x = 4, y = 4;
        do {
            System.out.print("Enter the coordinates: ");
            if (scanner.hasNextInt()) {
                x = scanner.nextInt() - 1;
                y = scanner.nextInt() - 1;
                if (x >= 3 || y >= 3) {
                    System.out.println("Coordinates should be from 1 to 3!");
                }
            } else {
                System.out.println("You should enter numbers!");
                x = 4;
                y = 4;
                scanner = new Scanner(System.in);
            }
        } while (!isCellValid(x, y));
        charState[x][y] = defineSign();
    }

    //easy AI turn, based on a random
    private void turnEasyAI() {
        int x = 4, y = 4;
        System.out.println("Making move level \"easy\"");
        do {
            x = random.nextInt(3);
            y = random.nextInt(3);
        } while (!isCellValidAI(x, y));
        charState[x][y] = defineSign();
    }


    //while human tries to type not occupied
    //and correct coordinates, this method always check player input
    private boolean isCellValid(int x, int y) {
        if (x < 0 || y < 0 || x > 2 || y > 2) {
            return false;
        }
        if (charState[x][y] == 'X' || charState[x][y] == 'O') {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }
        return charState[x][y] == '_';
    }

    //the same as previous but for AI of any level
    private boolean isCellValidAI(int x, int y) {
        if (charState[x][y] == 'X' || charState[x][y] == 'O') {
            return false;
        }
        return charState[x][y] == '_';
    }

    //when program needs to send as an argument in some methods
    //'X' or 'O', this method decide, which is right
    private char defineSign() {
        int x = 0;
        int y = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (charState[i][j] == 'X') {
                    x++;
                }
                if (charState[i][j] == 'O') {
                    y++;
                }
            }
        }
        if (y == x) {
            return 'X';
        }
        return 'O';
    }

    //simply prints game field. Additional methods added to
    //satisfy DRY principle.
    private static void printTable(char[][] charState) {
        printHorizontalBorder();
        printBody(charState);
        printHorizontalBorder();
    }

    private static void printHorizontalBorder() {
        System.out.println("---------");
    }

    private static void printBody(char[][] charState) {
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                if (charState[i][j] == '_') {
                    System.out.print("  ");
                } else {
                    System.out.print(charState[i][j] + " ");
                }
            }
            System.out.println("|");
        }
    }
}