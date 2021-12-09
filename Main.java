package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


class Main {

    private final BufferedReader reader;

    //array of data about people, separated with space
    private String[] words;

    //typed by user data, by which program search people
    private String data;

    //number of people, which user can type in
    private int numOfPeople;

    //enumeration of all people
    private static String[][] people;

    //people which was founded by data
    private static List<String> foundedPeople;

    //number of command in user menu
    private static int command;

    public Main() {
        numOfPeople = 0;
        foundedPeople = new ArrayList<>();
        reader = new BufferedReader(new InputStreamReader(System.in));
        command = -1;
    }

    //getters and setters

    //-----------------------------//

    public int getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(int numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    //-----------------------------//

    //start of the program. Calls method "process()",
    //all logic described here
    public static void main(String[] args) throws IOException {
        new Main().process();
    }

    //main method
    private void process() throws IOException {

        //asks and save number of people, which user can type in
        System.out.println("Enter the number of people:");
        String s = reader.readLine();
        setNumOfPeople(Integer.parseInt(s));

        people = new String[getNumOfPeople()][3];

        //user types in all the people in array "people"
        System.out.println("Enter all people:");
        for (int i = 0; i < getNumOfPeople(); i++) {
            String str = reader.readLine();
            words = str.split(" ");
            for (int j = 0; j < words.length; j++) {
                people[i][j] = words[j];
            }
        }

        while (true) {
            showUserMenu();
            s = reader.readLine();
            command = Integer.parseInt(s);
            switch (command) {
                case 1:
                    System.out.println("Enter a name or email to search all suitable people.");
                    setData(reader.readLine());
                    searchInformation();
                    break;
                case 2:
                    showAllPeople();
                    break;
                case 0:
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Incorrect option! Try again.");
            }
        }

    }

    //displays all the people, that user typed in
    private void showAllPeople() {
        StringBuilder sb = new StringBuilder();

        System.out.println("=== List of people ===");
        for (int i = 0; i < getNumOfPeople(); i++) {
            for (int j = 0; j < 3; j++) {
                if (people[i][j] != null) {
                    sb.append(people[i][j]).append(" ");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            System.out.println(sb);
            sb.delete(0, sb.length());
        }
    }

    //search for specified data in array "people"
    private void searchInformation() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < getNumOfPeople(); i++) {
            for (int j = 0; j < 3; j++) {
                if (people[i][j] != null) {
                    if (people[i][j].contains(getData()) || people[i][j].equalsIgnoreCase(getData())) {
                        for (int l = 0; l < 3; l++) {
                            if (people[i][l] != null) {
                                sb.append(people[i][l]).append(" ");
                            }
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        foundedPeople.add(sb.toString());
                        sb.delete(0, sb.length());
                    }
                }
            }
        }

        if (foundedPeople.isEmpty()) {
            System.out.println("No matching people found.");

        } else {

            System.out.println("Found people:");
            for (String str : foundedPeople) {
                System.out.println(str);
            }

            foundedPeople.clear();
        }

    }

    //displays user menu
    private void showUserMenu() {
        System.out.println("=== Menu ===");
        System.out.println("1. Search information.");
        System.out.println("2. Print all data.");
        System.out.println("0. Exit.");
    }
}
