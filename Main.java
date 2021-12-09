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

    //number of search queries, which user can do
    private int numOfQueries;

    //enumeration of all people
    private static String[][] people;

    //people which was founded by data
    private static List<String> foundedPeople;

    public Main() {
        numOfPeople = 0;
        numOfQueries = 0;
        foundedPeople = new ArrayList<>();
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    //getters and setters

    //-----------------------------//

    public int getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(int numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public int getNumOfQueries() {
        return numOfQueries;
    }

    public void setNumOfQueries(int numOfQueries) {
        this.numOfQueries = numOfQueries;
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
        System.out.println("Enter the number of people:");

        String s = reader.readLine();
        setNumOfPeople(Integer.parseInt(s));


        people = new String[getNumOfPeople()][3];


        System.out.println("Enter all people:");

        for (int i = 0; i < getNumOfPeople(); i++) {
            String str = reader.readLine();
            words = str.split(" ");
            for (int j = 0; j < words.length; j++) {
                people[i][j] = words[j];
            }
        }

        // Dwight Joseph djo@gmail.com
        // Rene Webb webb@gmail.com
        // Katie Jacobs
        // Erick Harrington harrington@gmail.com
        // Myrtle Medina
        // Erick Burgess

        //ERICK
        //unknown
        //WEBB@gmail.com


        System.out.println("Enter the number of search queries:");
        s = reader.readLine();
        setNumOfQueries(Integer.parseInt(s));


        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < getNumOfQueries(); k++) {
            System.out.println("Enter data to search people:");
            setData(reader.readLine());

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
    }
}
