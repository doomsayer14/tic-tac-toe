package search;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private final Scanner scanner;

    //line, that user type in console
    private String inputLine;

    //array of words in "inputLine", separated with space
    private String[] words;

    //word, that we have to search for
    private String specWord;

    //typed buy user data. With this data program search people
    private String data;

    //number of people, which user can type in
    private int numOfPeople;

    //number of search queries, which user can do
    private int numOfQueries;

    //enumeration of all people
    private String[][] people;

    //people which was founded by data
    private ArrayList<String> foundedPeople;

    public Main() {
        scanner = new Scanner(System.in);
        inputLine = "";
        specWord = "";
        numOfPeople = 0;
        numOfQueries = 0;
        foundedPeople = new ArrayList<>();
    }

    //getters and setters

    //-----------------------------//
    public String getInputLine() {
        return inputLine;
    }

    public void setInputLine(String inputLine) {
        this.inputLine = inputLine;
    }

    //впадлу обращаться к массиву через гетер
    //public String[] getWords() { return words; }

    public void setWords(String[] words) {
        this.words = words;
    }

    public String getSpecWord() {
        return specWord;
    }

    public void setSpecWord(String specWord) {
        this.specWord = specWord;
    }

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
    public static void main(String[] args) {
        new Main().process();
    }

    //main method
    private void process() {
        System.out.println("Enter the number of people:");
        setNumOfPeople(scanner.nextInt());

        people = new String[getNumOfPeople()][3];

        System.out.println("Enter all people:");
        for (int i = 0; i < getNumOfQueries(); i++) {
            setInputLine(scanner.nextLine());
            setWords(getInputLine().split(" "));
            for (int j = 0; j < words.length; j++) {
                people[i][j] = words[j];
            }
        }

        System.out.println("Enter the number of search queries:");
        setNumOfQueries(scanner.nextInt());

        for (int k = 0; k < getNumOfQueries(); k++) {
            System.out.println("Enter data to search people:");
            setData(scanner.nextLine());
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < words.length; j++) {
                    if (people[i][j].equals(getData())) {
                        foundedPeople.add(people[i][j]);
                    }
                }
            }
            if (foundedPeople.isEmpty()) {
                System.out.println("No matching people found.");
            }
        }
    }
}
