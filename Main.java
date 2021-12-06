package tictactoe;

import java.util.Random;
import java.util.Scanner;

//An instance of this class is used to call only
//one method - game(), all logic described in this method.
class Main {

    //contain our game field
    private char[][] charState;

    public char[][] getCharState() {
        return charState;
    }

    private char sign;

    private Scanner scanner;

    // for easyAI
    private Random random;

    //line e.g. "start easy user" splits on 3 parts
    private String command;
    private String firstPlayer;
    private String secondPlayer;

    private GFG gfg;

    public static void main(String[] args) {
        new Main().game();
    }

    //Constructor
    public Main() {
        charState = new char[3][3];

        scanner = new Scanner(System.in);
        random = new Random();

        command = "";

        gfg = new GFG();

    }

    //the main method
    public void game() {
        while (!command.equals("exit")) {
            initTable();
            askCommand();

            //костыль...
            if (command.equals("exit")) {
                return;
            }

            printTable(charState);

            //decides, whose turn now: true means firstPlayer,
            //false means secondPlayer
            boolean checkTurn = true;

            //while game is alive, this variable has to control its validness
            boolean isGameValid = true;

            //After the command input (askCommand)
            //this cycle starts the game
            //and controls it finished or not.
            //then program ask for a command again

            while (isGameValid) {
                turn(checkTurn);                            //make the move
                printTable(charState);                      //print game field
                isGameValid = checkGameValid(); //check the game for validness
                checkTurn = !checkTurn;                     //change turn to next player
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
            if (fullCommand.equals("exit")) {
                command = fullCommand;
                return;
            }
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
            return command.equals("exit") || (
                    command.equals("start") &&
                            (firstPlayer.equals("easy") || firstPlayer.equals("user")
                                    || firstPlayer.equals("medium") || firstPlayer.equals("hard")) &&
                            (secondPlayer.equals("easy") || secondPlayer.equals("user")
                                    || secondPlayer.equals("medium") || secondPlayer.equals("hard")));
        } catch (NullPointerException e) {
            return false;
        }
    }

    //define "isGameValid" variable, that controls game validness
    private boolean checkGameValid() {
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
            case ("medium"):
                turnMediumAI();
                break;
            case ("hard"):
                turnHardAI();
                break;
        }
    }

    //methods that choose, who will make the turn -
    //firstPlayer or secondPlayer
    private void turn(boolean checkTurn) {
        if (checkTurn) {
            turn(firstPlayer);
            return;
        }
        turn(secondPlayer);
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
        setSignInCharState(x, y);
    }

    //simply makes random move
    private void randomMove() {
        int x = 4, y = 4;
        do {
            x = random.nextInt(3);
            y = random.nextInt(3);
        } while (!isCellValidAI(x, y));
        setSignInCharState(x, y);
    }

    //easy AI turn, based on a random
    private void turnEasyAI() {
        System.out.println("Making move level \"easy\"");
        randomMove();
    }

    //medium AI turn. Description from the task:
    /*
    When the AI is playing at medium difficulty level, it makes moves using the following logic:

    1. If it already has two in a row and can win with one further move, it does so.
    2. If its opponent can win with one move, it plays the move necessary to block this.
    3. Otherwise, it makes a random move.
    */
    private void turnMediumAI() {
        System.out.println("Making move level \"medium\"");

        //check for victory in one turn
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isCellValidAI(i, j)) {
                    setSignInCharState(i, j);
                    if (checkWin(sign)) {
                        return;
                    }
                    charState[i][j] = '_';
                }
            }
        }

        //prevent opponent's victory in one turn
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isCellValidAI(i, j)) {
                    setOppositeSignInCharState(i, j);
                    if (checkWin(oppositeDefineSign())) {
                        setSignInCharState(i, j);
                        return;
                    }
                    charState[i][j] = '_';
                }
            }
        }

        randomMove();
    }

    //hard AI turn. Description from the task:
    /*
    The algorithm that implements this is called minimax.
    It's a brute force algorithm that maximizes the value of the AI's position
    and minimizes the worth of its opponent's. Minimax is not just for Tic-Tac-Toe.
    You can use it with any other game where two players
    make alternate moves, such as chess.
     */
    private void turnHardAI() {
        System.out.println("Making move level \"hard\"");
        int[] coordinates = new int[2];
        System.arraycopy(gfg.getCoordinates(), 0, coordinates, 0, 2);
        int x = coordinates[0];
        int y = coordinates[1];
        setSignInCharState(x, y);
    }

    //set actual sign for this turn
    //in game field (char array charState[][])
    private void setSignInCharState(int x, int y) {
        sign = defineSign();
        charState[x][y] = sign;
    }

    //sets opposite sign to the actual one
    //in game field (char array charState[][])
    private void setOppositeSignInCharState(int x, int y) {
        sign = oppositeDefineSign();
        charState[x][y] = sign;
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
    //'X' or 'O', this method decide, which is actual for this turn
    public char defineSign() {
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

    //returns opposite sign to the actual one
    public char oppositeDefineSign() {
        if (defineSign() == 'X') {
            return 'O';
        }
        return 'X';
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

    //this class is made for algorithm "minimax". Full copypast
    //from "geeksforgeeks.org".
    //please don't even try to understand, just accept.

}

// Java program to find the
// next optimal move for a player
class GFG
{
    static class Move
    {
        int row, col;
    };

    static char player = 'x', opponent = 'o';

    // This function returns true if there are moves
// remaining on the board. It returns false if
// there are no moves left to play.
    static Boolean isMovesLeft(char[][] board)
    {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == '_')
                    return true;
        return false;
    }

    // This is the evaluation function as discussed
// in the previous article ( http://goo.gl/sJgv68 )
    static int evaluate(char[][] b)
    {
        // Checking for Rows for X or O victory.
        for (int row = 0; row < 3; row++)
        {
            if (b[row][0] == b[row][1] &&
                    b[row][1] == b[row][2])
            {
                if (b[row][0] == player)
                    return +10;
                else if (b[row][0] == opponent)
                    return -10;
            }
        }

        // Checking for Columns for X or O victory.
        for (int col = 0; col < 3; col++)
        {
            if (b[0][col] == b[1][col] &&
                    b[1][col] == b[2][col])
            {
                if (b[0][col] == player)
                    return +10;

                else if (b[0][col] == opponent)
                    return -10;
            }
        }

        // Checking for Diagonals for X or O victory.
        if (b[0][0] == b[1][1] && b[1][1] == b[2][2])
        {
            if (b[0][0] == player)
                return +10;
            else if (b[0][0] == opponent)
                return -10;
        }

        if (b[0][2] == b[1][1] && b[1][1] == b[2][0])
        {
            if (b[0][2] == player)
                return +10;
            else if (b[0][2] == opponent)
                return -10;
        }

        // Else if none of them have won then return 0
        return 0;
    }

    // This is the minimax function. It considers all
// the possible ways the game can go and returns
// the value of the board
    static int minimax(char[][] board,
                       int depth, Boolean isMax)
    {
        int score = evaluate(board);

        // If Maximizer has won the game
        // return his/her evaluated score
        if (score == 10)
            return score;

        // If Minimizer has won the game
        // return his/her evaluated score
        if (score == -10)
            return score;

        // If there are no more moves and
        // no winner then it is a tie
        if (!isMovesLeft(board))
            return 0;

        // If this maximizer's move
        if (isMax)
        {
            int best = -1000;

            // Traverse all cells
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    // Check if cell is empty
                    if (board[i][j]=='_')
                    {
                        // Make the move
                        board[i][j] = player;

                        // Call minimax recursively and choose
                        // the maximum value
                        best = Math.max(best, minimax(board,
                                depth + 1, !isMax));

                        // Undo the move
                        board[i][j] = '_';
                    }
                }
            }
            return best;
        }

        // If this minimizer's move
        else
        {
            int best = 1000;

            // Traverse all cells
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    // Check if cell is empty
                    if (board[i][j] == '_')
                    {
                        // Make the move
                        board[i][j] = opponent;

                        // Call minimax recursively and choose
                        // the minimum value
                        best = Math.min(best, minimax(board,
                                depth + 1, !isMax));

                        // Undo the move
                        board[i][j] = '_';
                    }
                }
            }
            return best;
        }
    }

    // This will return the best possible
// move for the player
    static Move findBestMove(char[][] board)
    {
        int bestVal = -1000;
        Move bestMove = new Move();
        bestMove.row = -1;
        bestMove.col = -1;

        // Traverse all cells, evaluate minimax function
        // for all empty cells. And return the cell
        // with optimal value.
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                // Check if cell is empty
                if (board[i][j] == '_')
                {
                    // Make the move
                    board[i][j] = player;

                    // compute evaluation function for this
                    // move.
                    int moveVal = minimax(board, 0, false);

                    // Undo the move
                    board[i][j] = '_';

                    // If the value of the current move is
                    // more than the best value, then update
                    // best/
                    if (moveVal > bestVal)
                    {
                        bestMove.row = i;
                        bestMove.col = j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return bestMove;
    }

    // Driver code
    public int[] getCoordinates(char[][] charState)
    {
        char board[][] = {{ 'x', 'o', 'x' },
                { 'o', 'o', 'x' },
                { '_', '_', '_' }};

        Move bestMove = findBestMove(board);

        int[] move = {bestMove.row, bestMove.col};
        return move;
    }

}

// This code is contributed by PrinciRaj1992

