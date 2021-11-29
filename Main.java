package tictactoe;

import java.util.Scanner;

public class Main {

    private char[][] charState;
    private Scanner scanner;

    public static void main(String[] args) {
        new Main().game();
    }

    public Main() {
        charState = new char[3][3];
        scanner = new Scanner(System.in);
    }

    public void game() {
        initTable();
        printTable(charState);
        turn();
        printTable(charState);
        if (checkWin('X')) {
            System.out.println("X wins");
            return;
        }
        if (checkWin('O')) {
            System.out.println("O wins");
            return;
        }
        if (isTableFull()) {
            System.out.println("Draw");
            return;
        }
        System.out.println("Game not finished");
    }

    private void initTable() {
        int pos = 0;
        System.out.print("Enter the cells: ");
        String stringState = scanner.nextLine();
        char[] charState1D = stringState.toCharArray();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                charState[i][j] = charState1D[pos];
                pos++;
            }
        }
    }

    private void turn() {
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
        charState[x][y] = defineSign(charState);
    }

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

    private boolean isTableFull() {
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++)
                if (charState[row][col] == '_')
                    return false;
        return true;
    }

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

    private static char defineSign(char[][] charState) {
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
}
